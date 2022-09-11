package unk.prolib.canesvenatici.ax.impl;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXQuote;
import unk.prolib.canesvenatici.ax.AXQuoteType;
import unk.prolib.canesvenatici.ax.AXSymbol;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
public class Quote implements AXQuote {
    @NonNull private final AXSymbol symbol;
    @NonNull private final AXQuoteType quoteType;
    @NonNull private final BigDecimal price;
    @NonNull private final BigDecimal volume;
    
    public static class QuoteBuilder {
        
        public QuoteBuilder ask() {
            return quoteType(AXQuoteType.ASK);
        }
        
        public QuoteBuilder bid() {
            return quoteType(AXQuoteType.BID);
        }
        
        public QuoteBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }
        
        public QuoteBuilder price(String price) {
            return price(new BigDecimal(price));
        }
        
        public QuoteBuilder volume(BigDecimal volume) {
            this.volume = volume;
            return this;
        }
        
        public QuoteBuilder volume(String volume) {
            return volume(new BigDecimal(volume));
        }
    }
    
    public static Quote ofAsk(AXSymbol symbol, BigDecimal price, BigDecimal volume) {
        return Quote.builder().ask().symbol(symbol).price(price).volume(volume).build();
    }
    
    public static Quote ofAsk(AXSymbol symbol, String price, String volume) {
        return Quote.builder().ask().symbol(symbol).price(price).volume(volume).build();
    }
    
    public static Quote ofBid(AXSymbol symbol, BigDecimal price, BigDecimal volume) {
        return Quote.builder().bid().symbol(symbol).price(price).volume(volume).build();
    }
    
    public static Quote ofBid(AXSymbol symbol, String price, String volume) {
        return Quote.builder().bid().symbol(symbol).price(price).volume(volume).build();
    }

}
