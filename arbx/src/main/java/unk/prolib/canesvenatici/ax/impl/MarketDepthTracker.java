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
import unk.prolib.canesvenatici.ax.AXAskSymbol;
import unk.prolib.canesvenatici.ax.AXBidSymbol;
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
    private final Map<BigDecimal, AXQuote<AXAskSymbol>> askQuotes = new HashMap<>();
    private final Map<BigDecimal, AXQuote<AXBidSymbol>> bidQuotes = new HashMap<>();
    private Instant lastUpdateTime;
    
    @Override
    public AXMarketDepth consume(AXMarketDepthEvent event) {
        if ( ! event.getSymbol().equals(symbol) ) {
            throw new IllegalArgumentException("Unexpected symbol");
        }
        if ( lastUpdateTime == null || event.getUpdateTime().compareTo(lastUpdateTime) > 0 ) {
            lastUpdateTime = event.getUpdateTime();
        }
        if ( event.isSnapshot() ) {
            askQuotes.clear();
            bidQuotes.clear();
        }
        for ( AXQuoteEvent quoteEvent : event.getQuoteEvents() ) {
            switch ( quoteEvent.getEventType() ) {
            case DELETE:
                switch ( quoteEvent.getQuoteType() ) {
                case ASK:
                    askQuotes.remove(quoteEvent.getPrice());
                    break;
                case BID:
                    bidQuotes.remove(quoteEvent.getPrice());
                    break;
                default:
                    throw new UnsupportedOperationException();
                }
                break;
            case UPDATE:
                switch ( quoteEvent.getQuoteType() ) {
                case ASK:
                    askQuotes.put(quoteEvent.getPrice(), quoteEvent.toAskQuote());
                    break;
                case BID:
                    bidQuotes.put(quoteEvent.getPrice(), quoteEvent.toBidQuote());
                    break;
                default:
                    throw new UnsupportedOperationException();
                }
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
