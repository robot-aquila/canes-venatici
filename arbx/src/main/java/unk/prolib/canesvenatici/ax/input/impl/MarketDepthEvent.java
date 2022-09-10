package unk.prolib.canesvenatici.ax.input.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.input.AXMarketDepthEvent;
import unk.prolib.canesvenatici.ax.input.AXQuoteEvent;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class MarketDepthEvent implements AXMarketDepthEvent {
    private final Instant updateTime;
    private final AXSymbol symbol;
    private final Set<AXQuoteEvent> quoteEvents;
    
    private MarketDepthEvent(@NonNull Instant updateTime, @NonNull AXSymbol symbol, @NonNull Set<AXQuoteEvent> events) {
        this.updateTime = updateTime;
        this.symbol = symbol;
        this.quoteEvents = Collections.unmodifiableSet(new HashSet<>(events));
    }
    
    public static class MarketDepthEventBuilder {
        private Set<AXQuoteEvent> quoteEvents = new LinkedHashSet<>();
        
        public MarketDepthEventBuilder addUpdateAskEvent(BigDecimal price, BigDecimal volume) {
            quoteEvents.add(QuoteEvent.buildUpdateAsk(price, volume));
            return this;
        }
        
        public MarketDepthEventBuilder addUpdateBidEvent(BigDecimal price, BigDecimal volume) {
            quoteEvents.add(QuoteEvent.buildUpdateBid(price, volume));
            return this;
        }
        
        public MarketDepthEventBuilder addDeleteAskEvent(BigDecimal price) {
            quoteEvents.add(QuoteEvent.buildDeleteAsk(price));
            return this;
        }
        
        public MarketDepthEventBuilder addDeleteBidEvent(BigDecimal price) {
            quoteEvents.add(QuoteEvent.buildDeleteBid(price));
            return this;
        }
    }
}
