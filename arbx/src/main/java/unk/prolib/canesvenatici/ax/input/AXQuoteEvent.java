package unk.prolib.canesvenatici.ax.input;

import java.math.BigDecimal;

import unk.prolib.canesvenatici.ax.AXAskSymbol;
import unk.prolib.canesvenatici.ax.AXBidSymbol;
import unk.prolib.canesvenatici.ax.AXQuote;
import unk.prolib.canesvenatici.ax.AXSymbol;

public interface AXQuoteEvent extends AXQuote<AXSymbol> {
    
    AXQuoteEventType getEventType();
    
    /**
     * Get a quote volume if provided for this type of event.
     * <p>
     * @return - quote volume or null if volume not provided
     */
    @Override
    BigDecimal getVolume();
    
    AXQuote<AXAskSymbol> toAskQuote();
    AXQuote<AXBidSymbol> toBidQuote();
}
