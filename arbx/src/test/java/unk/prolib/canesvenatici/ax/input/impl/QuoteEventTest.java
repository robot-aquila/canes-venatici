package unk.prolib.canesvenatici.ax.input.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXQuoteType;
import unk.prolib.canesvenatici.ax.input.AXQuoteEventType;

class QuoteEventTest {

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void testBuild() {
        assertEquals(
                new QuoteEvent(AXQuoteEventType.UPDATE, AXQuoteType.ASK, new BigDecimal("10.05"), new BigDecimal("1")),
                QuoteEvent.buildUpdateAsk(new BigDecimal("10.05"), new BigDecimal("1"))
            );
        assertEquals(
                new QuoteEvent(AXQuoteEventType.UPDATE, AXQuoteType.BID, new BigDecimal("85.13"), new BigDecimal("9")),
                QuoteEvent.buildUpdateBid(new BigDecimal("85.13"), new BigDecimal("9"))
            );
        assertEquals(
                new QuoteEvent(AXQuoteEventType.DELETE, AXQuoteType.ASK, new BigDecimal("102.50"), null),
                QuoteEvent.buildDeleteAsk(new BigDecimal("102.50"))
            );
        assertEquals(
                new QuoteEvent(AXQuoteEventType.DELETE, AXQuoteType.BID, new BigDecimal("725.08"), null),
                QuoteEvent.buildDeleteBid(new BigDecimal("725.08"))
            );
    }

}
