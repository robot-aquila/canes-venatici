package unk.prolib.canesvenatici.ax;

import lombok.NonNull;

public interface AXSymbol {
    String getExchangeID();
    String getBaseAsset();
    String getQuoteAsset();
    
    default boolean isSimilarTo(@NonNull AXSymbol symbol) {
        return getBaseAsset().equals(symbol.getBaseAsset())
            && getQuoteAsset().equals(symbol.getQuoteAsset());
    }
    
    default boolean isDifferentExchanges(@NonNull AXSymbol symbol) {
        return ! getExchangeID().equals(symbol.getExchangeID());
    }
}
