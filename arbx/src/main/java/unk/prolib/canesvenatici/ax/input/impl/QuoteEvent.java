package unk.prolib.canesvenatici.ax.input.impl;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXQuoteType;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.input.AXQuoteEvent;
import unk.prolib.canesvenatici.ax.input.AXQuoteEventType;

@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
@Getter
public class QuoteEvent implements AXQuoteEvent {
    private final AXSymbol symbol;
    private final AXQuoteEventType eventType;
    private final AXQuoteType quoteType;
    private final BigDecimal price;
    private BigDecimal volume;

    public QuoteEvent(@NonNull AXSymbol symbol, @NonNull AXQuoteEventType eventType,
            @NonNull AXQuoteType quoteType, @NonNull BigDecimal price, BigDecimal volume)
    {
        this.symbol = symbol;
        this.eventType = eventType;
        this.quoteType = quoteType;
        this.price = price;
        this.volume = null;
        switch ( eventType ) {
        case UPDATE:
            Objects.requireNonNull(volume, "Volume must be defined");
            break;
        case DELETE:
            if ( ! Objects.isNull(volume) ) {
                throw new IllegalArgumentException("Volume must be undefined");
            }
            break;
        default:
            throw new IllegalStateException("Unsupported event type: " + eventType);
        }
    }

    public static class QuoteEventBuilder {
        
        public QuoteEventBuilder updateAsk(@NonNull BigDecimal price, @NonNull BigDecimal volume) {
            return eventType(AXQuoteEventType.UPDATE).quoteType(AXQuoteType.ASK).price(price).volume(volume);
        }

        public QuoteEventBuilder deleteAsk(@NonNull BigDecimal price) {
            return eventType(AXQuoteEventType.DELETE).quoteType(AXQuoteType.ASK).price(price);
        }

        public QuoteEventBuilder updateBid(@NonNull BigDecimal price, @NonNull BigDecimal volume) {
            return eventType(AXQuoteEventType.UPDATE).quoteType(AXQuoteType.BID).price(price).volume(volume);
        }

        public QuoteEventBuilder deleteBid(@NonNull BigDecimal price) {
            return eventType(AXQuoteEventType.DELETE).quoteType(AXQuoteType.BID).price(price);
        }

    }
    
}
