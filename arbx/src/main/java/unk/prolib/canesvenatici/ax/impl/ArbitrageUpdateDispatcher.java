package unk.prolib.canesvenatici.ax.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXArbitrageUpdateDispatcher;
import unk.prolib.canesvenatici.ax.AXArbitrageUpdateHandler;
import unk.prolib.canesvenatici.ax.AXSymbol;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ArbitrageUpdateDispatcher implements AXArbitrageUpdateDispatcher {
    @NonNull private final AXArbitrageUpdateHandler handler;
    
    /**
     * Complete set of candidates.
     */
    @NonNull private final Set<AXSymbol> symbols;
    
    public ArbitrageUpdateDispatcher(AXArbitrageUpdateHandler handler) {
        this(handler, new HashSet<>());
    }
    
    private Collection<AXSymbol> getAllCandidatesExcept(AXSymbol exception) {
        return symbols.stream().filter(x -> !exception.equals(x)).collect(Collectors.toList());
    }
    
    @Override
    public void updateCandidate(AXSymbol symbol) {
        this.symbols.add(symbol);
        for ( AXSymbol other : getAllCandidatesExcept(symbol) ) {
            handler.handle(symbol.toAskSymbol(), other.toBidSymbol());
            handler.handle(other.toAskSymbol(), symbol.toBidSymbol());
        }
    }

    @Override
    public void removeCandidate(AXSymbol symbol) {
        this.symbols.remove(symbol);
        for ( AXSymbol other : getAllCandidatesExcept(symbol) ) {
            handler.handleRemoved(symbol.toAskSymbol(), other.toBidSymbol());
            handler.handleRemoved(other.toAskSymbol(), symbol.toBidSymbol());
        }
    }

}
