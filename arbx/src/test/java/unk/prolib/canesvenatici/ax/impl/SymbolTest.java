package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SymbolTest {
    private Symbol service;

    @BeforeEach
    void setUp() throws Exception {
        service = Symbol.builder()
                .exchangeID("TUBORG")
                .baseAsset("USD")
                .quoteAsset("RUB")
                .build();
    }

    @Test
    void testGetters() {
        assertEquals("TUBORG", service.getExchangeID());
        assertEquals("USD", service.getBaseAsset());
        assertEquals("RUB", service.getQuoteAsset());
    }

    @Test
    void testIsSimilarTo_Symbol() {
        assertTrue(service.isSimilarTo(Symbol.builder()
                .exchangeID("TUBORG")
                .baseAsset("USD")
                .quoteAsset("RUB")
                .build()));
        assertTrue(service.isSimilarTo(Symbol.builder()
                .exchangeID("ANY_EXCHANGE_ID")
                .baseAsset("USD")
                .quoteAsset("RUB")
                .build()));
        assertFalse(service.isSimilarTo(Symbol.builder()
                .exchangeID("ANY_EXCHANGE_ID")
                .baseAsset("CAD")
                .quoteAsset("RUB")
                .build()));
        assertFalse(service.isSimilarTo(Symbol.builder()
                .exchangeID("ANY_EXCHANGE_ID")
                .baseAsset("USD")
                .quoteAsset("CAD")
                .build()));
    }

    @Test
    void testIsSimilarTo_SymbolSubject() {
        assertTrue(service.isSimilarTo(SymbolSubject.builder()
                .baseAsset("USD")
                .quoteAsset("RUB")
                .build()));
        assertFalse(service.isSimilarTo(SymbolSubject.builder()
                .baseAsset("CAD")
                .quoteAsset("RUB")
                .build()));
        assertFalse(service.isSimilarTo(SymbolSubject.builder()
                .baseAsset("USD")
                .quoteAsset("GBP")
                .build()));
    }

    @Test
    void testIsDiffExchanges() {
        assertTrue(service.isDiffExchanges(Symbol.builder()
                .exchangeID("KAPPA")
                .baseAsset("LOL")
                .quoteAsset("GAP")
                .build()));
        assertFalse(service.isDiffExchanges(Symbol.builder()
                .exchangeID("TUBORG")
                .baseAsset("LOL")
                .quoteAsset("GAP")
                .build()));
    }

    @Test
    void testToSubject() {
        var actual = service.toSubject();
        
        var expected = SymbolSubject.builder()
                .baseAsset("USD")
                .quoteAsset("RUB")
                .build();
        assertEquals(expected, actual);
    }
}
