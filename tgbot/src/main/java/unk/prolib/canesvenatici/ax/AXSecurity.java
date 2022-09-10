package unk.prolib.canesvenatici.ax;

public interface AXSecurity {
    String getTicker();
    String getBaseSymbol();
    String getQuoteSymbol();
    AXMarketDepth getMarketDepth();
}
