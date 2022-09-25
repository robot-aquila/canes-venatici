package unk.prolib.canesvenatici.ax;

public interface AXArbitrageUpdateDispatcher {
    void updateCandidate(AXSymbol symbol);
    void removeCandidate(AXSymbol symbol);
}
