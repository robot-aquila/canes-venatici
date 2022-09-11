package unk.prolib.canesvenatici.ax.output;

import java.util.Optional;

public interface AXArbitrageEvent {
    AXSpread getSpreadAtStart();
    AXSpread getSpreadAtMaximum();
    Optional<AXSpread> getSpreadAtEnd();
}
