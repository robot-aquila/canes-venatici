package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXMarketDepth;
import unk.prolib.canesvenatici.ax.AXSymbol;

class MarketDepthRegistryTest {
    private static Symbol SYMBOL1 = Symbol.builder()
            .baseAsset("foo")
            .quoteAsset("BTC")
            .exchangeID("XXX")
            .build();
    private static Symbol SYMBOL2 = Symbol.builder()
            .baseAsset("bar")
            .quoteAsset("BTC")
            .exchangeID("VVV")
            .build();
    private IMocksControl control;
    private AXMarketDepth mdMock1, mdMock2;
    private Map<AXSymbol, AXMarketDepth> data;
    private MarketDepthRegistry service;

    @BeforeEach
    void setUp() throws Exception {
        control = createStrictControl();
        mdMock1 = control.createMock(AXMarketDepth.class);
        mdMock2 = control.createMock(AXMarketDepth.class);
        data = new LinkedHashMap<>();
        service = new MarketDepthRegistry(data);
    }

    @Test
    void testGetMarketDepth() {
        data.put(SYMBOL1, mdMock1);
        control.replay();
        
        assertEquals(Optional.of(mdMock1), service.getMarketDepth(SYMBOL1));
        assertEquals(Optional.empty(), service.getMarketDepth(SYMBOL2));
        
        control.verify();
    }

    @Test
    void testUpdateMarketDepth() {
        expect(mdMock1.getSymbol()).andStubReturn(SYMBOL1);
        expect(mdMock2.getSymbol()).andStubReturn(SYMBOL2);
        control.replay();
        
        service.updateMarketDepth(mdMock1);
        service.updateMarketDepth(mdMock2);
        
        control.verify();
        var expectedData = new LinkedHashMap<>();
        expectedData.put(SYMBOL1, mdMock1);
        expectedData.put(SYMBOL2, mdMock2);
        assertEquals(expectedData, data);
    }
}
