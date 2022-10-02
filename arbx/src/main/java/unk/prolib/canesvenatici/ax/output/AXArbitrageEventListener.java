package unk.prolib.canesvenatici.ax.output;

public interface AXArbitrageEventListener {
    void onSpreadOpened(AXArbitrageEvent event);
    void onSpreadUpdated(AXArbitrageEvent event);
    void onSpreadClosed(AXArbitrageEvent event);
}
