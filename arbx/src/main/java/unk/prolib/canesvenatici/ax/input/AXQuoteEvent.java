package unk.prolib.canesvenatici.ax.input;

import java.math.BigDecimal;

import unk.prolib.canesvenatici.ax.AXQuote;

public interface AXQuoteEvent extends AXQuote {
    
    AXQuoteEventType getEventType();
    
    /**
     * Get a quote volume if provided for this type of event.
     * <p>
     * @return - quote volume or null if volume not provided
     */
    @Override
    BigDecimal getVolume();
    
    AXQuote toQuote();
}
