package unk.prolib.canesvenatici.ax.input.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXQuote;
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

    @Test
    void testCompareTo_AskVsBidShouldThrow() {
        assertThrows(IllegalArgumentException.class,
            () -> QuoteEvent.ofUpdateAsk(symbol, "20.15", "100").compareTo(QuoteEvent.ofUpdateBid(symbol, "20.15", "100")));
        assertThrows(IllegalArgumentException.class,
            () -> QuoteEvent.ofUpdateBid(symbol, "20.15", "100").compareTo(QuoteEvent.ofUpdateAsk(symbol, "20.15", "100")));
        assertThrows(IllegalArgumentException.class,
            () -> QuoteEvent.ofDeleteAsk(symbol, "20.15").compareTo(QuoteEvent.ofDeleteBid(symbol, "20.15")));
        assertThrows(IllegalArgumentException.class,
            () -> QuoteEvent.ofDeleteBid(symbol, "20.15").compareTo(QuoteEvent.ofDeleteAsk(symbol, "20.15")));
    }

    @Test
    void testCompareTo_Ask() {
        var quote3 = QuoteEvent.ofUpdateAsk(symbol, "20.50",  "5");
        var quote2 = QuoteEvent.ofUpdateAsk(symbol, "20.45",  "7");
        var quote1 = QuoteEvent.ofUpdateAsk(symbol, "20.35", "10");
        
        assertEquals( 1, quote2.compareTo(null));
        assertEquals( 0, quote2.compareTo(quote2));
        assertEquals( 1, quote2.compareTo(quote1));
        assertEquals(-1, quote2.compareTo(quote3));
        
        List<AXQuote<AXSymbol>> sorted = new ArrayList<>();
        sorted.add(quote1);
        sorted.add(quote2);
        sorted.add(quote3);
        Collections.sort(sorted);
        assertEquals(
            sorted.stream().map(x -> x.getPrice().toString()).collect(Collectors.toList()),
            List.of("20.35", "20.45", "20.50")
        );
    }

    @Test
    void testCompareTo_Bid() {
        var quote1 = QuoteEvent.ofUpdateBid(symbol, "20.15", "10");
        var quote2 = QuoteEvent.ofUpdateBid(symbol, "20.12",  "1");
        var quote3 = QuoteEvent.ofUpdateBid(symbol, "20.11",  "5");
        
        assertEquals( 1, quote2.compareTo(null));
        assertEquals( 0, quote2.compareTo(quote2));
        assertEquals( 1, quote2.compareTo(quote1));
        assertEquals(-1, quote2.compareTo(quote3));
        
        List<AXQuote<AXSymbol>> sorted = new ArrayList<>();
        sorted.add(quote1);
        sorted.add(quote2);
        sorted.add(quote3);
        Collections.sort(sorted);
        assertEquals(
            sorted.stream().map(x -> x.getPrice().toString()).collect(Collectors.toList()),
            List.of("20.15", "20.12", "20.11")
        );
    }
}
