package unk.prolib.canesvenatici.ax;

import java.util.Optional;

import unk.prolib.canesvenatici.ax.output.AXArbitrageSpread;

/**
 * Detector of spread between two exchanges when it is possible to be technically determined.
 */
public interface AXSpreadDetector {
    
    /**
     * Detect spread between two source exchanges.
     * <p>
     * @param askSymbol - security we may want to buy
     * @param bidSymbol - security we want to sell
     * @return a spread if can be determined or empty value if no spread
     */
    Optional<AXArbitrageSpread> detectSpread(AXAskSymbol askSymbol, AXBidSymbol bidSymbol);
}
