package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXSymbol;

class MarketDepthTest {
    private AXSymbol symbol = Symbol.builder().exchangeID("XXL").baseAsset("LTC").quoteAsset("BTC").build();

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void testBuilder() {
        var expected = new MarketDepth(symbol, Instant.parse("2022-09-11T18:20:00Z"),
            List.of(
                Quote.ofAsk(symbol, "15.34", "20"),
                Quote.ofAsk(symbol, "15.37", "12"),
                Quote.ofAsk(symbol, "15.52", "10")
            ),
            List.of(
                Quote.ofBid(symbol, "15.29", "15"),
                Quote.ofBid(symbol, "15.05", "13"),
                Quote.ofBid(symbol, "15.01", "11")
            )
        );
        
        assertEquals(expected, MarketDepth.builder()
                .symbol(symbol)
                .lastUpdateTime(Instant.parse("2022-09-11T18:20:00Z"))
                .addAsk("15.34", "20")
                .addAsk("15.37", "12")
                .addAsk("15.52", "10")
                
                .addBid("15.29", "15")
                .addBid("15.05", "13")
                .addBid("15.01", "11")
                .build());
    }

}
