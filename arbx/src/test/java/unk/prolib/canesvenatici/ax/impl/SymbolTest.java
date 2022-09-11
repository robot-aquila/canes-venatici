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
    void testIsSimilarTo() {
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
    void testIsDifferentExchanges() {
        assertTrue(service.isDifferentExchanges(Symbol.builder()
                .exchangeID("KAPPA")
                .baseAsset("LOL")
                .quoteAsset("GAP")
                .build()));
        assertFalse(service.isDifferentExchanges(Symbol.builder()
                .exchangeID("TUBORG")
                .baseAsset("LOL")
                .quoteAsset("GAP")
                .build()));
    }

}
