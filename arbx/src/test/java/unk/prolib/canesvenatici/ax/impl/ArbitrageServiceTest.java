package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXArbitrageUpdateDispatcher;
import unk.prolib.canesvenatici.ax.AXArbitrageUpdateDispatcherFactory;
import unk.prolib.canesvenatici.ax.AXMarketDepth;
import unk.prolib.canesvenatici.ax.AXMarketDepthRegistry;
import unk.prolib.canesvenatici.ax.AXMarketDepthTracker;
import unk.prolib.canesvenatici.ax.AXMarketDepthTrackerFactory;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.AXSymbolSubject;
import unk.prolib.canesvenatici.ax.input.AXMarketDepthEvent;

class ArbitrageServiceTest {
    private static AXSymbol
            symbol1 = Symbol.builder().baseAsset("A").quoteAsset("B").exchangeID("1").build(),
            symbol2 = Symbol.builder().baseAsset("A").quoteAsset("C").exchangeID("2").build(),
            symbol3 = Symbol.builder().baseAsset("A").quoteAsset("D").exchangeID("3").build();
    private IMocksControl control;
    private AXArbitrageUpdateDispatcherFactory dispatcherFactoryMock;
    private AXMarketDepthTrackerFactory trackerFactoryMock;
    private AXMarketDepthRegistry registryMock;
    private AXArbitrageUpdateDispatcher dispatcherMock;
    private AXMarketDepthTracker trackerMock;
    private AXMarketDepth marketDepthMock;
    private AXMarketDepthEvent eventMock;
    private Map<AXSymbol, AXMarketDepthTracker> trackers;
    private Map<AXSymbolSubject, AXArbitrageUpdateDispatcher> dispatchers;
    private ArbitrageService service;

    @BeforeEach
    void setUp() throws Exception {
        control = createStrictControl();
        dispatcherFactoryMock = control.createMock(AXArbitrageUpdateDispatcherFactory.class);
        trackerFactoryMock = control.createMock(AXMarketDepthTrackerFactory.class);
        registryMock = control.createMock(AXMarketDepthRegistry.class);
        dispatcherMock = control.createMock(AXArbitrageUpdateDispatcher.class);
        trackerMock = control.createMock(AXMarketDepthTracker.class);
        marketDepthMock = control.createMock(AXMarketDepth.class);
        eventMock = control.createMock(AXMarketDepthEvent.class);
        trackers = new LinkedHashMap<>();
        dispatchers = new LinkedHashMap<>();
        service = new ArbitrageService(dispatcherFactoryMock, trackerFactoryMock, registryMock, trackers, dispatchers);
    }

    @Test
    void testAccept_NewSymbol_NewSubject() {
        expect(eventMock.getSymbol()).andReturn(symbol1);
        expect(trackerFactoryMock.produce(symbol1)).andReturn(trackerMock);
        expect(dispatcherFactoryMock.produce(symbol1)).andReturn(dispatcherMock);
        expect(trackerMock.consume(eventMock)).andReturn(marketDepthMock);
        registryMock.updateMarketDepth(marketDepthMock);
        dispatcherMock.updateCandidate(symbol1);
        control.replay();
        
        service.accept(eventMock);
        
        control.verify();
        var expectedTrackers = new HashMap<>();
        expectedTrackers.put(symbol1, trackerMock);
        assertEquals(expectedTrackers, trackers);
        var expectedDispatchers = new HashMap<>();
        expectedDispatchers.put(symbol1.toSubject(), dispatcherMock);
        assertEquals(expectedDispatchers, dispatchers);
    }

    @Test
    void testAccept_ExistingSymbol() {
        trackers.put(symbol1, trackerMock);
        trackers.put(symbol2, control.createMock(AXMarketDepthTracker.class));
        trackers.put(symbol3, control.createMock(AXMarketDepthTracker.class));
        var expectedTrackers = new LinkedHashMap<>(trackers);
        dispatchers.put(symbol1.toSubject(), dispatcherMock);
        dispatchers.put(symbol2.toSubject(), control.createMock(AXArbitrageUpdateDispatcher.class));
        dispatchers.put(symbol3.toSubject(), control.createMock(AXArbitrageUpdateDispatcher.class));
        var expectedDispatchers = new LinkedHashMap<>(dispatchers);
        expect(eventMock.getSymbol()).andReturn(symbol1);
        expect(trackerMock.consume(eventMock)).andReturn(marketDepthMock);
        registryMock.updateMarketDepth(marketDepthMock);
        dispatcherMock.updateCandidate(symbol1);
        control.replay();
        
        service.accept(eventMock);
        
        control.verify();
        assertEquals(expectedTrackers, trackers);
        assertEquals(expectedDispatchers, dispatchers);
    }

}
