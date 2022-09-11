package unk.prolib.canesvenatici.ax.output;

import java.time.Instant;

import unk.prolib.canesvenatici.ax.AXQuote;

public interface AXSpread {
    /**
     * Get time of spread detected.
     * <p>
     * @return time
     */
    Instant getTime();
    
    AXQuote getBidQuote();
    
    AXQuote getAskQuote();
}
