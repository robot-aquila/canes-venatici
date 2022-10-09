package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.input.impl.MarketDepthEvent;

class MarketDepthTrackerTest {
    private AXSymbol symbol1 = Symbol.builder().exchangeID("TUTUMBR").baseAsset("ETH").quoteAsset("BTC").build(),
            symbol2 = Symbol.builder().exchangeID("KAPPA").baseAsset("LTC").quoteAsset("BTC").build();
    private MarketDepthTracker service;

    @BeforeEach
    void setUp() throws Exception {
        service = new MarketDepthTracker(symbol1);
    }

    Instant T(String timeString) {
        return Instant.parse(timeString);
    }

    Instant anyT() {
        return T("2022-09-12T00:47:00Z");
    }

    @Test
    void testConsume_ShouldThrowIfBadSymbol() {
        var event = MarketDepthEvent.builder()
                .symbol(symbol2)
                .updateTime(anyT())
                .build();
        
        var e = assertThrows(IllegalArgumentException.class, () -> service.consume(event));
        
        assertEquals("Unexpected symbol", e.getMessage());
    }

    @Test
    void testConsume() {
        var actual = service.consume(MarketDepthEvent.builder()
                .symbol(symbol1)
                .updateTime(T("2022-09-12T00:50:00Z"))
                .addUpdateAskEvent("120.20", "100")
                .addUpdateAskEvent("119.50", "120")
                .addUpdateAskEvent("115.02", "150")
                .addUpdateBidEvent("110.84", "200")
                .addUpdateBidEvent("110.40", "180")
                .addUpdateBidEvent("105.23", "100")
                .build());
        assertEquals(MarketDepth.builder()
                .symbol(symbol1)
                .lastUpdateTime("2022-09-12T00:50:00Z")
                .addAsk("115.02", "150")
                .addAsk("119.50", "120")
                .addAsk("120.20", "100")
                .addBid("110.84", "200")
                .addBid("110.40", "180")
                .addBid("105.23", "100")
                .build(), actual);
        
        actual = service.consume(MarketDepthEvent.builder()
                .symbol(symbol1)
                .updateTime(T("2022-09-12T00:49:00Z")) // shouldn't be updated
                .addDeleteAskEvent("120.20")
                .addUpdateAskEvent("115.03", "100")
                .addUpdateAskEvent("115.02", "400")
                .addDeleteBidEvent("110.84")
                .addUpdateBidEvent("110.40", "200")
                .addDeleteBidEvent("105.23")
                .build());
        assertEquals(MarketDepth.builder()
                .symbol(symbol1)
                .lastUpdateTime("2022-09-12T00:50:00Z")
                .addAsk("115.02", "400")
                .addAsk("115.03", "100")
                .addAsk("119.50", "120")
                .addBid("110.40", "200")
                .build(), actual);
        
        actual = service.consume(MarketDepthEvent.builder()
                .symbol(symbol1)
                .updateTime(T("2022-09-12T01:53:00Z"))
                .addUpdateAskEvent("115.02", "290")
                .addDeleteAskEvent("115.03")
                .addDeleteAskEvent("119.50")
                .addDeleteBidEvent("110.40")
                .addUpdateBidEvent("110.39", "800")
                .build());
        assertEquals(MarketDepth.builder()
                .symbol(symbol1)
                .lastUpdateTime("2022-09-12T01:53:00Z")
                .addAsk("115.02", "290")
                .addBid("110.39", "800")
                .build(), actual);
    }

    @Test
    void testConsume_SnapshotUpdate() {
        service.consume(MarketDepthEvent.builder()
                .symbol(symbol1)
                .updateTime(T("2022-09-12T01:53:00Z"))
                .addUpdateAskEvent("115.02", "290")
                .addUpdateBidEvent("110.39", "800")
                .build());
        
        var actual = service.consume(MarketDepthEvent.builder()
                .symbol(symbol1)
                .updateTime(T("2022-09-12T01:53:00Z"))
                .snapshot(true)
                .addUpdateAskEvent("130.00", "300")
                .addUpdateBidEvent("120.00", "400")
                .build());
        assertEquals(MarketDepth.builder()
                .symbol(symbol1)
                .lastUpdateTime("2022-09-12T01:53:00Z")
                .addAsk("130.00", "300")
                .addBid("120.00", "400")
                .build(), actual);
    }
}
