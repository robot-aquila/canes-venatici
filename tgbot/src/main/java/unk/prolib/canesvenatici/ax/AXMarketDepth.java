package unk.prolib.canesvenatici.ax;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * @startuml
 * interface
 * @enduml
 */
public interface AXMarketDepth {
    Instant getLastUpdateTime();
    List<AXQuote> getBids();
    List<AXQuote> getAsks();
    AXQuote getBestBid();
    AXQuote getBestAsk();
    BigDecimal getBestBidPrice();
    BigDecimal getBestAskPrice();
}
