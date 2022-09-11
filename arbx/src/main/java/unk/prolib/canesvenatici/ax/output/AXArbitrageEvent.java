package unk.prolib.canesvenatici.ax.output;

import java.util.Optional;

public interface AXArbitrageEvent {
    AXArbitrageSpread getSpreadAtStart();
    AXArbitrageSpread getSpreadAtMaximum();
    Optional<AXArbitrageSpread> getSpreadAtEnd();
}
