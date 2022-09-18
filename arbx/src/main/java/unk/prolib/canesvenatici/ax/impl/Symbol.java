package unk.prolib.canesvenatici.ax.impl;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXSymbol;

@Getter
@ToString
@Builder(toBuilder = true)
public class Symbol implements AXSymbol {
    @NonNull private final String exchangeID;
    @NonNull private final String baseAsset;
    @NonNull private final String quoteAsset;
    
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
    
    @Override
    public boolean equals(Object other) {
        return equals(this, other);
    }
    
    @Override
    public int hashCode() {
        return hashCode(this);
    }
}
