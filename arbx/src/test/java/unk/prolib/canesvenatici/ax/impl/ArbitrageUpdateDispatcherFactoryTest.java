package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXSpreadDetector;
import unk.prolib.canesvenatici.ax.output.AXArbitrageEventListener;

class ArbitrageUpdateDispatcherFactoryTest {
    private IMocksControl control;
    private AXSpreadDetector openingSpreadDetectorMock;
    private AXSpreadDetector closingSpreadDetectorMock;
    private AXArbitrageEventListener listenerMock;
    private ArbitrageUpdateDispatcherFactory service;

    @BeforeEach
    void setUp() throws Exception {
        control = createStrictControl();
        openingSpreadDetectorMock = control.createMock(AXSpreadDetector.class);
        closingSpreadDetectorMock = control.createMock(AXSpreadDetector.class);
        listenerMock = control.createMock(AXArbitrageEventListener.class);
        service = ArbitrageUpdateDispatcherFactory.builder()
                .openingSpreadDetector(openingSpreadDetectorMock)
                .closingSpreadDetector(closingSpreadDetectorMock)
                .listener(listenerMock)
                .build();
    }

    @Test
    void testProduce() {
        control.replay();
        
        var actual = service.produce(null);
        
        control.verify();
        var expected = new ArbitrageUpdateDispatcher(ArbitrageUpdateHandler.builder()
                .openingSpreadDetector(openingSpreadDetectorMock)
                .closingSpreadDetector(closingSpreadDetectorMock)
                .listener(listenerMock)
                .build());
        assertEquals(expected, actual);
    }

}
