package unk.prolib.canesvenatici.ax;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface AXMarketDepth {
    AXSymbol getSymbol();
    Instant getLastUpdateTime();
    List<AXQuote> getBids();
    List<AXQuote> getAsks();
    
    default AXQuote getBestBid() {
        return getBids().get(0);
    }
    
    default AXQuote getBestAsk() {
        return getAsks().get(0);
    }
    
    default BigDecimal getBestBidPrice() {
        return getBestBid().getPrice();
    }
    
    default BigDecimal getBestAskPrice() {
        return getBestAsk().getPrice();
    }
    
    default BigDecimal getBestBidVolume() {
        return getBestBid().getVolume();
    }
    
    default BigDecimal getBestAskVolume() {
        return getBestAsk().getVolume();
    }
    
    default int getAskCount() {
        return getAsks().size();
    }
    
    default int getBidCount() {
        return getBids().size();
    }
    
    default boolean hasAsks() {
        return getAskCount() > 0;
    }
    
    default boolean hasBids() {
        return getBidCount() > 0;
    }
}
