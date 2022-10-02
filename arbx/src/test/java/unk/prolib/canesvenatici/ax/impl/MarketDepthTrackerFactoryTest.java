package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketDepthTrackerFactoryTest {
    private static final Symbol SYMBOL = Symbol.builder()
            .baseAsset("ETH")
            .quoteAsset("BTC")
            .exchangeID("VXV")
            .build();
    private MarketDepthTrackerFactory service;

    @BeforeEach
    void setUp() throws Exception {
        service = new MarketDepthTrackerFactory();
    }

    @Test
    void testProduce() {
        var actual = service.produce(SYMBOL);
        
        var expected = new MarketDepthTracker(SYMBOL);
        assertEquals(expected, actual);
    }

}
