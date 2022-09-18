package unk.prolib.canesvenatici.ax.impl;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXAskSymbol;
import unk.prolib.canesvenatici.ax.AXBidSymbol;
import unk.prolib.canesvenatici.ax.AXQuote;
import unk.prolib.canesvenatici.ax.AXQuoteType;
import unk.prolib.canesvenatici.ax.AXSymbol;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
public class Quote<T extends AXSymbol> implements AXQuote<T> {
    @NonNull private final T symbol;
    @NonNull private final AXQuoteType quoteType;
    @NonNull private final BigDecimal price;
    @NonNull private final BigDecimal volume;
    
    public static class QuoteBuilder<T extends AXSymbol> {
        
        public QuoteBuilder<T> ask() {
            return quoteType(AXQuoteType.ASK);
        }
        
        public QuoteBuilder<T> bid() {
            return quoteType(AXQuoteType.BID);
        }
        
        public QuoteBuilder<T> price(BigDecimal price) {
            this.price = price;
            return this;
        }
        
        public QuoteBuilder<T> price(String price) {
            return price(new BigDecimal(price));
        }
        
        public QuoteBuilder<T> volume(BigDecimal volume) {
            this.volume = volume;
            return this;
        }
        
        public QuoteBuilder<T> volume(String volume) {
            return volume(new BigDecimal(volume));
        }
    }
    
    public static Quote<AXAskSymbol> ofAsk(AXSymbol symbol, BigDecimal price, BigDecimal volume) {
        return Quote.<AXAskSymbol>builder().ask().symbol(AskSymbol.of(symbol)).price(price).volume(volume).build();
    }
    
    public static Quote<AXAskSymbol> ofAsk(AXSymbol symbol, String price, String volume) {
        return Quote.<AXAskSymbol>builder().ask().symbol(AskSymbol.of(symbol)).price(price).volume(volume).build();
    }
    
    public static Quote<AXBidSymbol> ofBid(AXSymbol symbol, BigDecimal price, BigDecimal volume) {
        return Quote.<AXBidSymbol>builder().bid().symbol(BidSymbol.of(symbol)).price(price).volume(volume).build();
    }
    
    public static Quote<AXBidSymbol> ofBid(AXSymbol symbol, String price, String volume) {
        return Quote.<AXBidSymbol>builder().bid().symbol(BidSymbol.of(symbol)).price(price).volume(volume).build();
    }

}
