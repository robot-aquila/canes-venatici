package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SymbolSubjectTest {
    private SymbolSubject service;

    @BeforeEach
    void setUp() throws Exception {
        service = SymbolSubject.builder()
                .baseAsset("USD")
                .quoteAsset("BNB")
                .build();
    }

    @Test
    void testGetters() {
        assertEquals("USD", service.getBaseAsset());
        assertEquals("BNB", service.getQuoteAsset());
    }

}
