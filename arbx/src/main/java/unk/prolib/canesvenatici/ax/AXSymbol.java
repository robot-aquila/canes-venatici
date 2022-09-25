package unk.prolib.canesvenatici.ax;

import lombok.NonNull;

public interface AXSymbol { // Do not derive AXSymbolSubject!
    String getBaseAsset();
    String getQuoteAsset();
    String getExchangeID();
    AXSymbolSubject toSubject();
    
    default AXAskSymbol toAskSymbol() {
        throw new UnsupportedOperationException();
    }
    
    default AXBidSymbol toBidSymbol() {
        throw new UnsupportedOperationException();
    }
    
    default boolean isSimilarTo(@NonNull AXSymbolSubject subject) {
        return getBaseAsset().equals(subject.getBaseAsset())
            && getQuoteAsset().equals(subject.getQuoteAsset());
    }
    
    default boolean isSimilarTo(@NonNull AXSymbol symbol) {
        return getBaseAsset().equals(symbol.getBaseAsset())
            && getQuoteAsset().equals(symbol.getQuoteAsset());
    }
    
    default boolean isDiffExchanges(@NonNull AXSymbol symbol) {
        return ! getExchangeID().equals(symbol.getExchangeID());
    }
    
    default boolean isSameExchanges(@NonNull AXSymbol symbol) {
        return ! isDiffExchanges(symbol);
    }
}
