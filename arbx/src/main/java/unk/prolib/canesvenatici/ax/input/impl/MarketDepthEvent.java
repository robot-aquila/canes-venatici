package unk.prolib.canesvenatici.ax.input.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
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
        private Set<AXQuoteEvent> quoteEvents = new HashSet<>();
        
        public MarketDepthEventBuilder addUpdateAskEvent(BigDecimal price, BigDecimal volume) {
            quoteEvents.add(QuoteEvent.builder().symbol(symbol).updateAsk(price, volume).build());
            return this;
        }
        
        public MarketDepthEventBuilder addUpdateAskEvent(String price, String volume) {
            return addUpdateAskEvent(new BigDecimal(price), new BigDecimal(volume));
        }
        
        public MarketDepthEventBuilder addUpdateBidEvent(BigDecimal price, BigDecimal volume) {
            quoteEvents.add(QuoteEvent.builder().symbol(symbol).updateBid(price, volume).build());
            return this;
        }
        
        public MarketDepthEventBuilder addUpdateBidEvent(String price, String volume) {
            return addUpdateBidEvent(new BigDecimal(price), new BigDecimal(volume));
        }
        
        public MarketDepthEventBuilder addDeleteAskEvent(BigDecimal price) {
            quoteEvents.add(QuoteEvent.builder().symbol(symbol).deleteAsk(price).build());
            return this;
        }
        
        public MarketDepthEventBuilder addDeleteAskEvent(String price) {
            return addDeleteAskEvent(new BigDecimal(price));
        }
        
        public MarketDepthEventBuilder addDeleteBidEvent(BigDecimal price) {
            quoteEvents.add(QuoteEvent.builder().symbol(symbol).deleteBid(price).build());
            return this;
        }
        
        public MarketDepthEventBuilder addDeleteBidEvent(String price) {
            return addDeleteBidEvent(new BigDecimal(price));
        }
    }
}
