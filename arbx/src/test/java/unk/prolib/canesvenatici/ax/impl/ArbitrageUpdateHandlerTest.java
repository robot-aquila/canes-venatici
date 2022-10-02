package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.easymock.EasyMock.*;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXArbitragePair;
import unk.prolib.canesvenatici.ax.AXSpreadDetector;
import unk.prolib.canesvenatici.ax.output.AXArbitrageEventListener;
import unk.prolib.canesvenatici.ax.output.AXArbitrageSpread;
import unk.prolib.canesvenatici.ax.output.impl.ArbitrageEvent;

class ArbitrageUpdateHandlerTest {
    private static final AXArbitragePair PAIR = ArbitragePair.builder()
            .askSymbol(Symbol.builder().baseAsset("A").quoteAsset("B").exchangeID("1").build().toAskSymbol())
            .bidSymbol(Symbol.builder().baseAsset("A").quoteAsset("B").exchangeID("2").build().toBidSymbol())
            .build();
    private IMocksControl control;
    private AXSpreadDetector openingSpreadDetectorMock;
    private AXSpreadDetector closingSpreadDetectorMock;
    private AXArbitrageEventListener listenerMock;
    private AXArbitrageSpread spreadMock1, spreadMock2, spreadMock3, spreadMock4;
    private ArbitrageEvent eventMock;
    private Map<AXArbitragePair, ArbitrageEvent> events;
    private ArbitrageUpdateHandler service;

    @BeforeEach
    void setUp() throws Exception {
        control = createStrictControl();
        openingSpreadDetectorMock = control.createMock(AXSpreadDetector.class);
        closingSpreadDetectorMock = control.createMock(AXSpreadDetector.class);
        listenerMock = control.createMock(AXArbitrageEventListener.class);
        spreadMock1 = control.createMock(AXArbitrageSpread.class);
        spreadMock2 = control.createMock(AXArbitrageSpread.class);
        spreadMock3 = control.createMock(AXArbitrageSpread.class);
        spreadMock4 = control.createMock(AXArbitrageSpread.class);
        eventMock = control.createMock(ArbitrageEvent.class);
        events = new LinkedHashMap<>();
        service = new ArbitrageUpdateHandler(openingSpreadDetectorMock, closingSpreadDetectorMock, listenerMock, events);
    }

    @Test
    void testHandleRemoved_HasEvent() {
        events.put(PAIR, eventMock);
        listenerMock.onSpreadClosed(eventMock);
        control.replay();
        
        service.handleRemoved(PAIR);
        
        control.verify();
        assertEquals(new LinkedHashMap<>(), events);
    }

    @Test
    void testHandleRemoved_NoEvent() {
        control.replay();
        
        service.handleRemoved(PAIR);
        
        control.verify();
    }

    @Test
    void testHandle_FirstTimeEvent_NoOpeningSpreadDetected() {
        expect(openingSpreadDetectorMock.detectSpread(PAIR)).andReturn(Optional.empty());
        control.replay();
        
        service.handle(PAIR);
        
        control.verify();
        assertEquals(new LinkedHashMap<>(), events);
    }

    @Test
    void testHandle_FirstTimeEvent_OpeningSpreadDetected() {
        var event = ArbitrageEvent.builder()
                .spreadAtStart(spreadMock1)
                .spreadAtMaximum(spreadMock1)
                .spreadAtEnd(spreadMock1)
                .build();
        expect(openingSpreadDetectorMock.detectSpread(PAIR)).andReturn(Optional.of(spreadMock1));
        listenerMock.onSpreadOpened(event);
        control.replay();
        
        service.handle(PAIR);
        
        control.verify();
        Map<AXArbitragePair, ArbitrageEvent> expectedEvents = new LinkedHashMap<>();
        expectedEvents.put(PAIR, event);
        assertEquals(expectedEvents, events);
        
    }

    @Test
    void testHandle_NextTimeEvent_NoCurrentSpreadDetected() {
        var event = ArbitrageEvent.builder()
                .spreadAtStart(spreadMock1)
                .spreadAtMaximum(spreadMock2)
                .spreadAtEnd(spreadMock3)
                .build();
        events.put(PAIR, event);
        expect(closingSpreadDetectorMock.detectSpread(PAIR)).andReturn(Optional.empty());
        listenerMock.onSpreadClosed(event);
        control.replay();
        
        service.handle(PAIR);
        
        control.verify();
        assertEquals(new LinkedHashMap<>(), events);
    }

    @Test
    void testHandle_NextTimeEvent_SpreadUpdated_MaxNotUpdated() {
        events.put(PAIR, ArbitrageEvent.builder()
                .spreadAtStart(spreadMock1)
                .spreadAtMaximum(spreadMock2)
                .spreadAtEnd(spreadMock3)
                .build());
        expect(closingSpreadDetectorMock.detectSpread(PAIR)).andReturn(Optional.of(spreadMock4));
        expect(spreadMock2.getAbsoluteValue()).andStubReturn(new BigDecimal("15.47"));
        expect(spreadMock4.getAbsoluteValue()).andStubReturn(new BigDecimal("13.04"));
        var event = ArbitrageEvent.builder()
                .spreadAtStart(spreadMock1)
                .spreadAtMaximum(spreadMock2)
                .spreadAtEnd(spreadMock4)
                .build();
        listenerMock.onSpreadUpdated(event);
        control.replay();
        
        service.handle(PAIR);
        
        control.verify();
        Map<AXArbitragePair, ArbitrageEvent> expectedEvents = new LinkedHashMap<>();
        expectedEvents.put(PAIR, event);
        assertEquals(expectedEvents, events);
    }

    @Test
    void testHandle_NextTimeEvent_SpreadUpdated_MaxUpdated() {
        events.put(PAIR, ArbitrageEvent.builder()
                .spreadAtStart(spreadMock1)
                .spreadAtMaximum(spreadMock2)
                .spreadAtEnd(spreadMock3)
                .build());
        expect(closingSpreadDetectorMock.detectSpread(PAIR)).andReturn(Optional.of(spreadMock4));
        expect(spreadMock2.getAbsoluteValue()).andStubReturn(new BigDecimal("15.47"));
        expect(spreadMock4.getAbsoluteValue()).andStubReturn(new BigDecimal("16.20"));
        var event = ArbitrageEvent.builder()
                .spreadAtStart(spreadMock1)
                .spreadAtMaximum(spreadMock4)
                .spreadAtEnd(spreadMock4)
                .build();
        listenerMock.onSpreadUpdated(event);
        control.replay();
        
        service.handle(PAIR);
        
        control.verify();
        Map<AXArbitragePair, ArbitrageEvent> expectedEvents = new LinkedHashMap<>();
        expectedEvents.put(PAIR, event);
        assertEquals(expectedEvents, events);
    }

}
