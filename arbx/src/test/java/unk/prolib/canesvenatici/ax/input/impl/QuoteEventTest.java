package unk.prolib.canesvenatici.ax.input.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXQuoteType;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.impl.Symbol;
import unk.prolib.canesvenatici.ax.input.AXQuoteEventType;

class QuoteEventTest {
    AXSymbol symbol = Symbol.builder().exchangeID("XXL").baseAsset("ETH").quoteAsset("BTC").build();

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void testBuild() {
        assertEquals(
            new QuoteEvent(symbol, AXQuoteEventType.UPDATE, AXQuoteType.ASK, new BigDecimal("10.05"), new BigDecimal("1")),
            QuoteEvent.builder().symbol(symbol).updateAsk(new BigDecimal("10.05"), new BigDecimal("1")).build()
        );
        assertEquals(
            new QuoteEvent(symbol, AXQuoteEventType.UPDATE, AXQuoteType.BID, new BigDecimal("85.13"), new BigDecimal("9")),
            QuoteEvent.builder().symbol(symbol).updateBid(new BigDecimal("85.13"), new BigDecimal("9")).build()
        );
        assertEquals(
            new QuoteEvent(symbol, AXQuoteEventType.DELETE, AXQuoteType.ASK, new BigDecimal("102.50"), null),
            QuoteEvent.builder().symbol(symbol).deleteAsk(new BigDecimal("102.50")).build()
        );
        assertEquals(
            new QuoteEvent(symbol, AXQuoteEventType.DELETE, AXQuoteType.BID, new BigDecimal("725.08"), null),
            QuoteEvent.builder().symbol(symbol).deleteBid(new BigDecimal("725.08")).build()
        );
    }

}
