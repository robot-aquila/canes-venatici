package unk.prolib.canesvenatici.ax.output.impl;

import java.time.Instant;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXQuote;
import unk.prolib.canesvenatici.ax.output.AXArbitrageSpread;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
public class ArbitrageSpread implements AXArbitrageSpread {
    @NonNull private final Instant time;
    @NonNull private final AXQuote bidQuote;
    @NonNull private final AXQuote askQuote;
    
    public static class ArbitrageSpreadBuilder {
        
        private ArbitrageSpreadBuilder validateQuotes(AXQuote ask, AXQuote bid) {
            if ( ask == null || bid == null ) {
                return this;
            }
            if ( ! ask.getSymbol().isSimilarTo(bid.getSymbol()) ) {
                throw new IllegalArgumentException("Spread quotes must be of similar symbols");
            }
            if ( ask.getSymbol().isDifferentExchanges(bid.getSymbol()) ) {
                throw new IllegalArgumentException("Expected quotes of different exchanges");
            }
            if ( ! ask.isAsk() ) {
                throw new IllegalArgumentException("Unexpected ask quote type");
            }
            if ( ! bid.isBid() ) {
                throw new IllegalArgumentException("Unexpected bid quote type");
            }
            return this;
        }
        
        public ArbitrageSpreadBuilder askQuote(AXQuote askQuote) {
            validateQuotes(askQuote, bidQuote).askQuote = askQuote;
            return this;
        }
        
        public ArbitrageSpreadBuilder bidQuote(AXQuote bidQuote) {
            validateQuotes(askQuote, bidQuote).bidQuote = bidQuote;
            return this;
        }
    }
}
