package unk.prolib.canesvenatici.ax;

import java.math.BigDecimal;

public interface AXQuote extends Comparable<AXQuote> {
    AXSymbol getSymbol();
    AXQuoteType getQuoteType();
    BigDecimal getPrice();
    BigDecimal getVolume();
    
    default boolean isBid() {
        return getQuoteType() == AXQuoteType.BID;
    }
    
    default boolean isAsk() {
        return getQuoteType() == AXQuoteType.ASK;
    }
    
    @Override
    default int compareTo(AXQuote other) {
        if ( other == null ) {
            return 1;
        }
        switch ( getQuoteType() ) {
        case ASK:
        case BID:
            if ( getQuoteType() != other.getQuoteType() ) {
                throw new IllegalArgumentException("Unexpected quote type: " + other.getQuoteType());
            }
            return other.getPrice().compareTo(getPrice()) * (getQuoteType() == AXQuoteType.ASK ? -1 : 1);
        default:
            throw new UnsupportedOperationException();
        }
    }
}
