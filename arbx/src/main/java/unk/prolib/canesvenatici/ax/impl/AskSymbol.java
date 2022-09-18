package unk.prolib.canesvenatici.ax.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.AXAskSymbol;

@Getter
@ToString
@Builder(toBuilder = true)
public class AskSymbol implements AXAskSymbol {
    @NonNull private final String exchangeID;
    @NonNull private final String baseAsset;
    @NonNull private final String quoteAsset;
    
    public static AXAskSymbol of(AXSymbol symbol) {
        return AskSymbol.builder()
                .exchangeID(symbol.getExchangeID())
                .baseAsset(symbol.getBaseAsset())
                .quoteAsset(symbol.getQuoteAsset())
                .build();
    }
    
    @Override
    public boolean equals(Object other) {
        return Symbol.equals(this, other);
    }
    
    @Override
    public int hashCode() {
        return Symbol.hashCode(this);
    }

}
