package unk.prolib.canesvenatici.ax.input.impl;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXQuote;
import unk.prolib.canesvenatici.ax.AXQuoteType;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.impl.Quote;
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
    private final BigDecimal volume;

    public QuoteEvent(@NonNull AXSymbol symbol, @NonNull AXQuoteEventType eventType,
            @NonNull AXQuoteType quoteType, @NonNull BigDecimal price, BigDecimal volume)
    {
        this.symbol = symbol;
        this.eventType = eventType;
        this.quoteType = quoteType;
        this.price = price;
        this.volume = volume;
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

    public static QuoteEvent ofUpdateAsk(AXSymbol symbol, BigDecimal price, BigDecimal volume) {
        return QuoteEvent.builder().symbol(symbol).updateAsk(price, volume).build();
    }

    public static QuoteEvent ofUpdateAsk(AXSymbol symbol, String price, String volume) {
        return ofUpdateAsk(symbol, new BigDecimal(price), new BigDecimal(volume));
    }

    public static QuoteEvent ofUpdateBid(AXSymbol symbol, BigDecimal price, BigDecimal volume) {
        return QuoteEvent.builder().symbol(symbol).updateBid(price, volume).build();
    }

    public static QuoteEvent ofUpdateBid(AXSymbol symbol, String price, String volume) {
        return ofUpdateBid(symbol, new BigDecimal(price), new BigDecimal(volume));
    }

    public static QuoteEvent ofDeleteAsk(AXSymbol symbol, BigDecimal price) {
        return QuoteEvent.builder().symbol(symbol).deleteAsk(price).build();
    }

    public static QuoteEvent ofDeleteAsk(AXSymbol symbol, String price) {
        return ofDeleteAsk(symbol, new BigDecimal(price));
    }

    public static QuoteEvent ofDeleteBid(AXSymbol symbol, BigDecimal price) {
        return QuoteEvent.builder().symbol(symbol).deleteBid(price).build();
    }

    public static QuoteEvent ofDeleteBid(AXSymbol symbol, String price) {
        return ofDeleteBid(symbol, new BigDecimal(price));
    }
    
    @Override
    public AXQuote toQuote() {
        switch ( eventType ) {
        case UPDATE:
            return Quote.builder().symbol(symbol).quoteType(quoteType).price(price).volume(volume).build();
        default:
            throw new IllegalStateException("Unsupported event type: " + eventType);
        }
    }

}
