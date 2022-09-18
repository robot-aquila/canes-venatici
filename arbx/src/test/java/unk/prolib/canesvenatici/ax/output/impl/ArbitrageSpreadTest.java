package unk.prolib.canesvenatici.ax.output.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXAskSymbol;
import unk.prolib.canesvenatici.ax.AXBidSymbol;
import unk.prolib.canesvenatici.ax.impl.AskSymbol;
import unk.prolib.canesvenatici.ax.impl.BidSymbol;
import unk.prolib.canesvenatici.ax.impl.Quote;
import unk.prolib.canesvenatici.ax.impl.Symbol;

class ArbitrageSpreadTest {

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void testBuilder_AndAndBidQuotesShouldBeOfSameSymbol() {
        var e = assertThrows(IllegalArgumentException.class, () -> ArbitrageSpread.builder()
                .askQuote(Quote.ofAsk(Symbol.builder()
                        .exchangeID("YYY")
                        .baseAsset("A")
                        .quoteAsset("B")
                        .build(), "26.95", "150"))
                .bidQuote(Quote.ofBid(Symbol.builder()
                        .exchangeID("XXX")
                        .baseAsset("C")
                        .quoteAsset("D")
                        .build(), "27.13", "100"))
                .build());
        
        assertEquals("Spread quotes must be of similar symbols", e.getMessage());
    }

    @Test
    void testBuilder_AskAndBidQuotesShouldBeOfDifferentExchanges() {
        var e = assertThrows(IllegalArgumentException.class, () -> ArbitrageSpread.builder()
                .askQuote(Quote.ofAsk(Symbol.builder()
                        .exchangeID("XXX")
                        .baseAsset("A")
                        .quoteAsset("B")
                        .build(), "26.95", "150"))
                .bidQuote(Quote.ofBid(Symbol.builder()
                        .exchangeID("XXX")
                        .baseAsset("A")
                        .quoteAsset("B")
                        .build(), "27.13", "100"))
                .build());
        
        assertEquals("Expected quotes of different exchanges", e.getMessage());
    }

    @Test
    void testBuilder_AskQuoteShouldBeAskType() {
        var quote = Quote.<AXAskSymbol>builder()
                .bid()
                .symbol(AskSymbol.of(Symbol.builder()
                        .exchangeID("YYY")
                        .baseAsset("A")
                        .quoteAsset("B")
                        .build()))
                .price("26.95")
                .volume("150")
                .build();
        var e = assertThrows(IllegalArgumentException.class, () -> ArbitrageSpread.builder()
                .askQuote(quote)
                .bidQuote(Quote.ofBid(Symbol.builder()
                        .exchangeID("XXX")
                        .baseAsset("A")
                        .quoteAsset("B")
                        .build(), "27.13", "100"))
                .build());
        
        assertEquals("Unexpected ask quote type", e.getMessage());
    }

    @Test
    void testBuilder_BidQuoteShouldBeBidType() {
        var quote = Quote.<AXBidSymbol>builder()
                .ask()
                .symbol(BidSymbol.of(Symbol.builder()
                        .exchangeID("YYY")
                        .baseAsset("A")
                        .quoteAsset("B")
                        .build()))
                .price("26.95")
                .volume("150")
                .build();
        var e = assertThrows(IllegalArgumentException.class, () -> ArbitrageSpread.builder()
                .askQuote(Quote.ofAsk(Symbol.builder()
                        .exchangeID("XXX")
                        .baseAsset("A")
                        .quoteAsset("B")
                        .build(), "26.95", "150"))
                .bidQuote(quote)
                .build());
        
        assertEquals("Unexpected bid quote type", e.getMessage());
    }
}
