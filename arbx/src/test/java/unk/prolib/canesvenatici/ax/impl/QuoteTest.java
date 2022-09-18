package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXAskSymbol;
import unk.prolib.canesvenatici.ax.AXBidSymbol;
import unk.prolib.canesvenatici.ax.AXQuote;
import unk.prolib.canesvenatici.ax.AXQuoteType;
import unk.prolib.canesvenatici.ax.AXSymbol;

class QuoteTest {
    private AXSymbol symbol = Symbol.builder().exchangeID("XXL").baseAsset("LTC").quoteAsset("BTC").build();

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void testOfAskSSS() {
        var actual = Quote.ofAsk(symbol, "115.28", "1000");
        
        var expected = Quote.builder()
                .symbol(symbol)
                .ask()
                .price("115.28")
                .volume("1000")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void testOfAskSDD() {
        var actual = Quote.ofAsk(symbol, new BigDecimal("67.01"), new BigDecimal("10"));
        
        var expected = Quote.builder()
                .symbol(symbol)
                .ask()
                .price("67.01")
                .volume("10")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void testOfBidSSS() {
        var actual = Quote.ofBid(symbol, "30.91", "50");
        
        var expected = Quote.builder()
                .symbol(symbol)
                .bid()
                .price("30.91")
                .volume("50")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void testOfBidSDD() {
        var actual = Quote.ofBid(symbol, new BigDecimal("708"), new BigDecimal("512"));
        
        var expected = Quote.builder()
                .symbol(symbol)
                .bid()
                .price("708")
                .volume("512")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void testGetters() {
        var actual = Quote.builder()
                .symbol(symbol)
                .ask()
                .price("215.48")
                .volume("115")
                .build();
        
        assertEquals(symbol, actual.getSymbol());
        assertEquals(AXQuoteType.ASK, actual.getQuoteType());
        assertEquals(new BigDecimal("215.48"), actual.getPrice());
        assertEquals(new BigDecimal("115"), actual.getVolume());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    void testCompareTo_AskVsBidShouldThrow() {
        AXQuote ask = Quote.ofAsk(symbol, "20.15", "100");
        AXQuote bid = Quote.ofBid(symbol, "20.15", "100");
        assertThrows(IllegalArgumentException.class, () -> ask.compareTo(bid));
        assertThrows(IllegalArgumentException.class, () -> bid.compareTo(ask));
    }

    @Test
    void testCompareTo_Ask() {
        var quote3 = Quote.ofAsk(symbol, "20.50",  "5");
        var quote2 = Quote.ofAsk(symbol, "20.45",  "7");
        var quote1 = Quote.ofAsk(symbol, "20.35", "10");
        
        assertEquals( 1, quote2.compareTo(null));
        assertEquals( 0, quote2.compareTo(quote2));
        assertEquals( 1, quote2.compareTo(quote1));
        assertEquals(-1, quote2.compareTo(quote3));
        
        List<AXQuote<AXAskSymbol>> sorted = new ArrayList<>();
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
        var quote1 = Quote.ofBid(symbol, "20.15", "10");
        var quote2 = Quote.ofBid(symbol, "20.12",  "1");
        var quote3 = Quote.ofBid(symbol, "20.11",  "5");
        
        assertEquals( 1, quote2.compareTo(null));
        assertEquals( 0, quote2.compareTo(quote2));
        assertEquals( 1, quote2.compareTo(quote1));
        assertEquals(-1, quote2.compareTo(quote3));
        
        List<AXQuote<AXBidSymbol>> sorted = new ArrayList<>();
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
