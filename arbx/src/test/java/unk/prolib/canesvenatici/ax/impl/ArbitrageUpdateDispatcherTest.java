package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.easymock.EasyMock.*;

import org.assertj.core.util.Sets;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXArbitrageUpdateHandler;
import unk.prolib.canesvenatici.ax.AXSymbol;

class ArbitrageUpdateDispatcherTest {
    private static AXSymbol
        symbol1 = Symbol.builder().baseAsset("A").quoteAsset("B").exchangeID("1").build(),
        symbol2 = Symbol.builder().baseAsset("A").quoteAsset("B").exchangeID("2").build(),
        symbol3 = Symbol.builder().baseAsset("A").quoteAsset("B").exchangeID("3").build();

    private IMocksControl control;
    private AXArbitrageUpdateHandler handlerMock;
    private Set<AXSymbol> candidates;
    private ArbitrageUpdateDispatcher service;

    @BeforeEach
    void setUp() throws Exception {
        control = createStrictControl();
        handlerMock = control.createMock(AXArbitrageUpdateHandler.class);
        candidates = new LinkedHashSet<>();
        service = new ArbitrageUpdateDispatcher(handlerMock, candidates);
    }

    @Test
    void testUpdateCandidate_ShouldSkipIfNoCandidates() {
        control.replay();
        
        service.updateCandidate(symbol1);
        
        control.verify();
        assertEquals(Sets.newLinkedHashSet(symbol1), candidates);
    }

    @Test
    void testUpdateCandidates_ShouldHandleAllPairs() {
        candidates.add(symbol1);
        candidates.add(symbol2);
        handlerMock.handle(symbol3.toAskSymbol(), symbol1.toBidSymbol());
        handlerMock.handle(symbol1.toAskSymbol(), symbol3.toBidSymbol());
        handlerMock.handle(symbol3.toAskSymbol(), symbol2.toBidSymbol());
        handlerMock.handle(symbol2.toAskSymbol(), symbol3.toBidSymbol());
        control.replay();
        
        service.updateCandidate(symbol3);
        
        control.verify();
        assertEquals(Sets.newLinkedHashSet(symbol1, symbol2, symbol3), candidates);
    }

    @Test
    void testRemoveCandidate_ShouldSkipIfNoCandidates() {
        control.replay();
        
        service.removeCandidate(symbol1);
        
        control.verify();
        assertEquals(Sets.newLinkedHashSet(), candidates);
    }

    @Test
    void testRemoveCandidates_ShouldHandleAllPairs() {
        candidates.add(symbol1);
        candidates.add(symbol2);
        candidates.add(symbol3);
        handlerMock.handleRemoved(symbol2.toAskSymbol(), symbol1.toBidSymbol());
        handlerMock.handleRemoved(symbol1.toAskSymbol(), symbol2.toBidSymbol());
        handlerMock.handleRemoved(symbol2.toAskSymbol(), symbol3.toBidSymbol());
        handlerMock.handleRemoved(symbol3.toAskSymbol(), symbol2.toBidSymbol());
        control.replay();
        
        service.removeCandidate(symbol2);
        
        control.verify();
        assertEquals(Sets.newLinkedHashSet(symbol1, symbol3), candidates);
    }
}
