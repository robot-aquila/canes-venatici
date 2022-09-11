package unk.prolib.canesvenatici.ax.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXMarketDepth;
import unk.prolib.canesvenatici.ax.AXMarketDepthTracker;
import unk.prolib.canesvenatici.ax.AXQuote;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.input.AXMarketDepthEvent;
import unk.prolib.canesvenatici.ax.input.AXQuoteEvent;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class MarketDepthTracker implements AXMarketDepthTracker {
    @NonNull private final AXSymbol symbol;
    private final Map<BigDecimal, AXQuote> askQuotes = new HashMap<>();
    private final Map<BigDecimal, AXQuote> bidQuotes = new HashMap<>();
    private Instant lastUpdateTime;
    
    @Override
    public AXMarketDepth consume(AXMarketDepthEvent event) {
        if ( ! event.getSymbol().equals(symbol) ) {
            throw new IllegalArgumentException("Unexpected symbol");
        }
        if ( lastUpdateTime == null || event.getUpdateTime().compareTo(lastUpdateTime) > 0 ) {
            lastUpdateTime = event.getUpdateTime();
        }
        for ( AXQuoteEvent quoteEvent : event.getQuoteEvents() ) {
            Map<BigDecimal, AXQuote> target = null;
            switch ( quoteEvent.getQuoteType() ) {
            case ASK:
                target = askQuotes;
                break;
            case BID:
                target = bidQuotes;
                break;
            default:
                throw new UnsupportedOperationException();
            }
            switch ( quoteEvent.getEventType() ) {
            case DELETE:
                target.remove(quoteEvent.getPrice());
                break;
            case UPDATE:
                target.put(quoteEvent.getPrice(), quoteEvent.toQuote());
                break;
            default:
                throw new UnsupportedOperationException();
            }
        }
        return MarketDepth.builder()
                .symbol(symbol)
                .lastUpdateTime(lastUpdateTime)
                .asks(askQuotes.values().stream().sorted().collect(Collectors.toList()))
                .bids(bidQuotes.values().stream().sorted().collect(Collectors.toList()))
                .build();
    }

}
