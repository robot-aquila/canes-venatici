package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import static org.easymock.EasyMock.*;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXMarketDepthRegistry;
import unk.prolib.canesvenatici.ax.output.AXArbitrageEventListener;

class ArbitrageServiceFactoryTest {
    private IMocksControl control;
    private AXArbitrageEventListener listenerMock;
    private AXMarketDepthRegistry registryMock;
    private SpreadDetectorValidator.Validator vMock1, vMock2, vMock3;
    private ArbitrageServiceFactory service;

    @BeforeEach
    void setUp() throws Exception {
        control = createStrictControl();
        listenerMock = control.createMock(AXArbitrageEventListener.class);
        registryMock = control.createMock(AXMarketDepthRegistry.class);
        vMock1 = control.createMock(SpreadDetectorValidator.Validator.class);
        vMock2 = control.createMock(SpreadDetectorValidator.Validator.class);
        vMock3 = control.createMock(SpreadDetectorValidator.Validator.class);
        service = ArbitrageServiceFactory.getInstance();
    }

    @Test
    void testVariation1_Defaults() {
        var actual = service.variation1().withEventListener(listenerMock).build();
        
        var expectedRegistry = new MarketDepthRegistry();
        var expected = ArbitrageService.builder()
                .registry(expectedRegistry)
                .trackerFactory(new MarketDepthTrackerFactory())
                .dispatcherFactory(ArbitrageUpdateDispatcherFactory.builder()
                        .listener(listenerMock)
                        .openingSpreadDetector(SpreadDetectorValidator.builder()
                                .detector(SpreadDetectorVega.builder()
                                        .marketDepthMaxUpdateDelay(Duration.ofMinutes(5))
                                        .registry(expectedRegistry)
                                        .build())
                                .build())
                        .closingSpreadDetector(SpreadDetectorValidator.builder()
                                .detector(SpreadDetectorVega.builder()
                                        .marketDepthMaxUpdateDelay(Duration.ofMinutes(5))
                                        .registry(expectedRegistry)
                                        .build())
                                .build())
                        .build())
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void testVariation1_Customization() {
        var actual = service.variation1().withEventListener(listenerMock)
                .withMarketDepthMaxUpdateDelayMinutes(10)
                .addSpreadValidator(vMock1)
                .addSpreadValidator(vMock2)
                .addSpreadValidator(vMock3)
                .withMarketDepthRegistry(registryMock)
                .build();
        
        var expected = ArbitrageService.builder()
                .registry(registryMock)
                .trackerFactory(new MarketDepthTrackerFactory())
                .dispatcherFactory(ArbitrageUpdateDispatcherFactory.builder()
                        .listener(listenerMock)
                        .openingSpreadDetector(SpreadDetectorValidator.builder()
                                .addValidator(vMock1)
                                .addValidator(vMock2)
                                .addValidator(vMock3)
                                .detector(SpreadDetectorVega.builder()
                                        .registry(registryMock)
                                        .marketDepthMaxUpdateDelay(Duration.ofMinutes(10))
                                        .build())
                                .build())
                        .closingSpreadDetector(SpreadDetectorValidator.builder()
                                .addValidator(vMock1)
                                .addValidator(vMock2)
                                .addValidator(vMock3)
                                .detector(SpreadDetectorVega.builder()
                                        .registry(registryMock)
                                        .marketDepthMaxUpdateDelay(Duration.ofMinutes(10))
                                        .build())
                                .build())
                        .build())
                .build();
        assertEquals(expected, actual);
    }
}
