package unk.prolib.canesvenatici.ax.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXAskSymbol;
import unk.prolib.canesvenatici.ax.AXBidSymbol;
import unk.prolib.canesvenatici.ax.AXMarketDepth;
import unk.prolib.canesvenatici.ax.AXQuote;
import unk.prolib.canesvenatici.ax.AXSymbol;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
public class MarketDepth implements AXMarketDepth {
    @NonNull private final AXSymbol symbol;
    @NonNull private final Instant lastUpdateTime;
    @NonNull private final List<AXQuote<AXAskSymbol>> asks;
    @NonNull private final List<AXQuote<AXBidSymbol>> bids;
    
    public static class MarketDepthBuilder {
        private List<AXQuote<AXAskSymbol>> asks = new ArrayList<>();
        private List<AXQuote<AXBidSymbol>> bids = new ArrayList<>();
        
        private MarketDepthBuilder expectSymbol() {
            if ( symbol == null ) {
                throw new IllegalStateException("Symbol must be defined");
            }
            return this;
        }
        
        public MarketDepthBuilder lastUpdateTime(Instant time) {
            this.lastUpdateTime = time;
            return this;
        }
        
        public MarketDepthBuilder lastUpdateTime(String timeString) {
            return lastUpdateTime(Instant.parse(timeString));
        }
        
        public MarketDepthBuilder addAsk(@NonNull BigDecimal price, @NonNull BigDecimal volume) {
            expectSymbol();
            asks.add(Quote.ofAsk(symbol, price, volume));
            return this;
        }
        
        public MarketDepthBuilder addAsk(@NonNull String price, @NonNull String volume) {
            return addAsk(new BigDecimal(price), new BigDecimal(volume));
        }
        
        public MarketDepthBuilder addBid(@NonNull BigDecimal price, @NonNull BigDecimal volume) {
            expectSymbol();
            bids.add(Quote.ofBid(symbol, price, volume));
            return this;
        }
        
        public MarketDepthBuilder addBid(@NonNull String price, @NonNull String volume) {
            return addBid(new BigDecimal(price), new BigDecimal(volume));
        }
    }
}
