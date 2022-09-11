package unk.prolib.canesvenatici.ax.output;

import java.time.Instant;

import unk.prolib.canesvenatici.ax.AXQuote;

public interface AXArbitrageSpread {
    /**
     * Get time of spread detected.
     * <p>
     * @return time
     */
    Instant getTime();
    
    /**
     * Get a quote of buyer.
     * <p>
     * Bid quote is a price and volume what can be used to make a sell operation.
     * In scope of arbitrage bid price must be great than an ask price to make it profitable.
     * <p>
     * @return bid quote
     */
    AXQuote getBidQuote();
    
    /**
     * Get a quote of seller.
     * <p>
     * Ask quote is a price and volume what can be used to make a buy operation.
     * In scope of arbitrage ask price must be less than a bid price to make it profitable.
     * <p>
     * @return ask quote
     */
    AXQuote getAskQuote();
}
