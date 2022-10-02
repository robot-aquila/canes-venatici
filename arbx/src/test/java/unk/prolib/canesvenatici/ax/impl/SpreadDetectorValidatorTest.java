package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import static org.easymock.EasyMock.*;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXArbitragePair;
import unk.prolib.canesvenatici.ax.AXSpreadDetector;
import unk.prolib.canesvenatici.ax.impl.SpreadDetectorValidator.Validator;
import unk.prolib.canesvenatici.ax.output.AXArbitrageSpread;

class SpreadDetectorValidatorTest {
    private IMocksControl control;
    private AXSpreadDetector detectorMock;
    private AXArbitragePair pairMock;
    private Validator vMock1, vMock2, vMock3;
    private AXArbitrageSpread spreadMock;
    private SpreadDetectorValidator service;

    @BeforeEach
    void setUp() throws Exception {
        control = createStrictControl();
        detectorMock = control.createMock(AXSpreadDetector.class);
        pairMock = control.createMock(AXArbitragePair.class);
        vMock1 = control.createMock(Validator.class);
        vMock2 = control.createMock(Validator.class);
        vMock3 = control.createMock(Validator.class);
        spreadMock = control.createMock(AXArbitrageSpread.class);
        service = SpreadDetectorValidator.builder()
                .detector(detectorMock)
                .addValidator(vMock1)
                .addValidator(vMock2)
                .addValidator(vMock3)
                .build();
    }

    @Test
    void testDetectSpread_SpreadWasNotDetected() {
        expect(detectorMock.detectSpread(pairMock)).andReturn(Optional.empty());
        control.replay();
        
        var actual = service.detectSpread(pairMock);
        
        control.verify();
        assertEquals(Optional.empty(), actual);
    }

    @Test
    void testDetectSpread_SpreadRejected() {
        expect(detectorMock.detectSpread(pairMock)).andReturn(Optional.of(spreadMock));
        expect(vMock1.isValid(spreadMock)).andReturn(true);
        expect(vMock2.isValid(spreadMock)).andReturn(false);
        control.replay();
        
        assertEquals(Optional.empty(), service.detectSpread(pairMock));
        
        control.verify();
    }

    @Test
    void testDetectSpread_SpreadAccepted() {
        expect(detectorMock.detectSpread(pairMock)).andReturn(Optional.of(spreadMock));
        expect(vMock1.isValid(spreadMock)).andReturn(true);
        expect(vMock2.isValid(spreadMock)).andReturn(true);
        expect(vMock3.isValid(spreadMock)).andReturn(true);
        control.replay();
        
        assertEquals(Optional.of(spreadMock), service.detectSpread(pairMock));
        
        control.verify();
    }

    @Test
    void testValidator_MinProfitAndLossValidator() {
        var service = new SpreadDetectorValidator.MinProfitAndLoss(0.04d);
        expect(spreadMock.getProfitAndLoss()).andReturn(0.15d).andReturn(0.02d);
        control.replay();
        
        assertTrue(service.isValid(spreadMock));
        assertFalse(service.isValid(spreadMock));
        
        control.verify();
    }
}
