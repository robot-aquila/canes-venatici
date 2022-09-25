package unk.prolib.canesvenatici.ax;

import unk.prolib.canesvenatici.ax.impl.ArbitragePair;

public interface AXArbitrageUpdateHandler {
    void handle(AXArbitragePair pair);
    void handleRemoved(AXArbitragePair pair);
    
    default void handle(AXAskSymbol askSymbol, AXBidSymbol bidSymbol) {
        handle(ArbitragePair.of(askSymbol, bidSymbol));
    }
    
    default void handleRemoved(AXAskSymbol askSymbol, AXBidSymbol bidSymbol) {
        handleRemoved(ArbitragePair.of(askSymbol, bidSymbol));
    }
}
