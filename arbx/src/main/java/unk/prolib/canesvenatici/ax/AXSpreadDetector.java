package unk.prolib.canesvenatici.ax;

import java.util.Optional;

import unk.prolib.canesvenatici.ax.output.AXArbitrageSpread;

/**
 * This interface is used to detect when spread is open and when a spread is closed.
 * The different conditions are possible for those two cases but interface is still same.
 */
public interface AXSpreadDetector {
    
    /**
     * Try to detect there is arbitrage spread between two source exchanges.
     * <p>
     * @param bidSymbol - security we want to sell (it should be a bid cuz we seek for buyer)
     * @param askSymbol - security we may want to buy (it should be an offer cuz we seek for seller)
     * @return a defined spread if there is suitable conditions for arbitrage trade or none value if no suitable
     * condition for the pair of sources
     */
    Optional<AXArbitrageSpread> detectSpread(AXSymbol bidSymbol, AXSymbol askSymbol);
}
