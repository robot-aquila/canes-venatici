package unk.prolib.canesvenatici.ax.input;

import java.math.BigDecimal;

import unk.prolib.canesvenatici.ax.AXQuoteType;

public interface AXQuoteEvent {
    
    AXQuoteEventType getEventType();
    
    AXQuoteType getQuoteType();
    
    BigDecimal getPrice();
    
    /**
     * Get a quote volume if provided for this type of event.
     * <p>
     * @return - quote volume or null if volume not provided
     */
    BigDecimal getVolume();
}
