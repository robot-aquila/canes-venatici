package unk.prolib.canesvenatici.ax.output;

public interface AXArbitrageEvent {
    AXArbitrageSpread getSpreadAtStart();
    AXArbitrageSpread getSpreadAtMaximum();
    AXArbitrageSpread getSpreadAtEnd();
}
