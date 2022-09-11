package unk.prolib.canesvenatici.ax;

import java.math.BigDecimal;

public interface AXQuote {
    AXSymbol getSymbol();
    AXQuoteType getQuoteType();
    BigDecimal getPrice();
    BigDecimal getVolume();
}
