package unk.prolib.canesvenatici.ax.input.impl;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXQuoteType;
import unk.prolib.canesvenatici.ax.input.AXQuoteEvent;
import unk.prolib.canesvenatici.ax.input.AXQuoteEventType;

@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QuoteEvent implements AXQuoteEvent {
    @NonNull private final AXQuoteEventType eventType;
    @NonNull private final AXQuoteType quoteType;
    @NonNull private final BigDecimal price;
    private BigDecimal volume;

    public static QuoteEvent buildUpdateAsk(@NonNull BigDecimal price, @NonNull BigDecimal volume) {
        return new QuoteEvent(AXQuoteEventType.UPDATE, AXQuoteType.ASK, price, volume);
    }

    public static QuoteEvent buildDeleteAsk(@NonNull BigDecimal price) {
        return new QuoteEvent(AXQuoteEventType.DELETE, AXQuoteType.ASK, price, null);
    }

    public static QuoteEvent buildUpdateBid(@NonNull BigDecimal price, @NonNull BigDecimal volume) {
        return new QuoteEvent(AXQuoteEventType.UPDATE, AXQuoteType.BID, price, volume);
    }

    public static QuoteEvent buildDeleteBid(@NonNull BigDecimal price) {
        return new QuoteEvent(AXQuoteEventType.DELETE, AXQuoteType.BID, price, null);
    }

}
