package unk.prolib.canesvenatici.ax;

import java.util.Optional;

import unk.prolib.canesvenatici.ax.output.AXArbitrageSpread;

/**
 * Detector of spread of same security between two exchanges when possible.
 */
public interface AXSpreadDetector {
    
    /**
     * Detect spread between two source exchanges.
     * <p>
     * @param pair - arbitrage pair
     * @return a spread if can be determined or empty value if no spread
     */
    Optional<AXArbitrageSpread> detectSpread(AXArbitragePair pair);
}
