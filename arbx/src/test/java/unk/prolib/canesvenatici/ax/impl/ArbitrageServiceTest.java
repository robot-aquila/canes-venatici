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
import unk.prolib.canesvenatici.ax.impl.ArbitrageService.Entry;
import unk.prolib.canesvenatici.ax.input.AXMarketDepthEvent;

class ArbitrageServiceTest {
    private static AXSymbol symbol1 = Symbol.builder().baseAsset("A").quoteAsset("B").exchangeID("1").build(),
            symbol2 = Symbol.builder().baseAsset("A").quoteAsset("B").exchangeID("2").build(),
            symbol3 = Symbol.builder().baseAsset("A").quoteAsset("B").exchangeID("3").build();
    private IMocksControl control;
    private AXArbitrageUpdateDispatcherFactory dispatcherFactoryMock;
    private AXMarketDepthTrackerFactory trackerFactoryMock;
    private AXMarketDepthRegistry registryMock;
    private AXArbitrageUpdateDispatcher dispatcherMock;
    private AXMarketDepthTracker trackerMock;
    private AXMarketDepth marketDepthMock;
    private AXMarketDepthEvent eventMock;
    private Map<AXSymbol, Entry> entries;
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
        entries = new LinkedHashMap<>();
        service = new ArbitrageService(dispatcherFactoryMock, trackerFactoryMock, registryMock, entries);
    }

    @Test
    void testAccept_NewEntry() {
        expect(eventMock.getSymbol()).andReturn(symbol1);
        expect(dispatcherFactoryMock.produce(symbol1)).andReturn(dispatcherMock);
        expect(trackerFactoryMock.produce(symbol1)).andReturn(trackerMock);
        expect(trackerMock.consume(eventMock)).andReturn(marketDepthMock);
        registryMock.updateMarketDepth(marketDepthMock);
        dispatcherMock.updateCandidate(symbol1);
        control.replay();
        
        service.accept(eventMock);
        
        control.verify();
        var expectedEntries = new HashMap<>();
        expectedEntries.put(symbol1, Entry.builder().dispatcher(dispatcherMock).tracker(trackerMock).build());
        assertEquals(expectedEntries, entries);
    }

    @Test
    void testAccept_ExistingEntry() {
        Entry entryMock1, entryMock2;
        entries.put(symbol1, Entry.builder().dispatcher(dispatcherMock).tracker(trackerMock).build());
        entries.put(symbol2, entryMock1 = control.createMock(Entry.class));
        entries.put(symbol3, entryMock2 = control.createMock(Entry.class));
        expect(eventMock.getSymbol()).andReturn(symbol1);
        expect(trackerMock.consume(eventMock)).andReturn(marketDepthMock);
        registryMock.updateMarketDepth(marketDepthMock);
        dispatcherMock.updateCandidate(symbol1);
        control.replay();
        
        service.accept(eventMock);
        
        control.verify();
        var expectedEntries = new HashMap<>();
        expectedEntries.put(symbol1, Entry.builder().dispatcher(dispatcherMock).tracker(trackerMock).build());
        expectedEntries.put(symbol2, entryMock1);
        expectedEntries.put(symbol3, entryMock2);
        assertEquals(expectedEntries, entries);
    }

}
