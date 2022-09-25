package unk.prolib.canesvenatici.ax.impl;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXAskSymbol;
import unk.prolib.canesvenatici.ax.AXBidSymbol;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.AXSymbolSubject;

@Getter
@ToString
@Builder(toBuilder = true)
public class Symbol implements AXSymbol {
    @NonNull private final String exchangeID;
    @NonNull private final String baseAsset;
    @NonNull private final String quoteAsset;
    
    @AllArgsConstructor
    class AskSymbol implements AXAskSymbol {
        @NonNull private final Symbol delegate;
        
        @Override
        public boolean equals(Object other) {
            return Symbol.equals(this, other);
        }
        
        @Override
        public int hashCode() {
            return Symbol.hashCode(this);
        }

        @Override
        public AXSymbolSubject toSubject() {
            return Symbol.toSubject(this);
        }

        @Override
        public String getBaseAsset() {
            return delegate.getBaseAsset();
        }

        @Override
        public String getQuoteAsset() {
            return delegate.getQuoteAsset();
        }

        @Override
        public String getExchangeID() {
            return delegate.getExchangeID();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("exchangeID", getExchangeID())
                    .append("baseAsset", getBaseAsset())
                    .append("quoteAsset", getQuoteAsset())
                    .build();
        }
    }
    
    @AllArgsConstructor
    class BidSymbol implements AXBidSymbol {
        @NonNull private final Symbol delegate;
        
        @Override
        public boolean equals(Object other) {
            return Symbol.equals(this, other);
        }
        
        @Override
        public int hashCode() {
            return Symbol.hashCode(this);
        }

        @Override
        public AXSymbolSubject toSubject() {
            return Symbol.toSubject(this);
        }

        @Override
        public String getBaseAsset() {
            return delegate.getBaseAsset();
        }

        @Override
        public String getQuoteAsset() {
            return delegate.getQuoteAsset();
        }

        @Override
        public String getExchangeID() {
            return delegate.getExchangeID();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("exchangeID", getExchangeID())
                    .append("baseAsset", getBaseAsset())
                    .append("quoteAsset", getQuoteAsset())
                    .build();
        }
    }
    
    public static boolean equals(@NonNull AXSymbol a, Object other) {
        if ( a == other ) {
            return true;
        }
        if ( other == null || !(other instanceof AXSymbol) ) {
            return false;
        }
        AXSymbol o = (AXSymbol) other;
        return new EqualsBuilder()
                .append(a.getExchangeID(), o.getExchangeID())
                .append(a.getBaseAsset(),  o.getBaseAsset())
                .append(a.getQuoteAsset(), o.getQuoteAsset())
                .build();
    }
    
    public static int hashCode(@NonNull AXSymbol a) {
        return new HashCodeBuilder()
                .append(a.getExchangeID())
                .append(a.getBaseAsset())
                .append(a.getQuoteAsset())
                .build();
    }
    
    public static AXSymbolSubject toSubject(@NonNull AXSymbol a) {
        return new SymbolSubject(a.getBaseAsset(), a.getQuoteAsset());
    }
    
    @Override
    public boolean equals(Object other) {
        return equals(this, other);
    }
    
    @Override
    public int hashCode() {
        return hashCode(this);
    }

    @Override
    public AXSymbolSubject toSubject() {
        return toSubject(this);
    }
    
    @Override
    public AXAskSymbol toAskSymbol() {
        return new AskSymbol(this);
    }
    
    @Override
    public AXBidSymbol toBidSymbol() {
        return new BidSymbol(this);
    }
    
    public static AXSymbol of(AXSymbol source) {
        return Symbol.builder()
                .baseAsset(source.getBaseAsset())
                .quoteAsset(source.getQuoteAsset())
                .exchangeID(source.getExchangeID())
                .build();
    }
}
