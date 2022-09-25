package unk.prolib.canesvenatici.ax;

import java.util.Optional;

import unk.prolib.canesvenatici.ax.output.AXArbitrageSpread;

public interface AXSpreadRegistry {
    void updateSpread(AXArbitragePair pair, Optional<AXArbitrageSpread> spread);
    boolean isSpreadOpen(AXArbitragePair pair);
}
