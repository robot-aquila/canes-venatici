package unk.prolib.canesvenatici.ax.output.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXAskSymbol;
import unk.prolib.canesvenatici.ax.AXBidSymbol;
import unk.prolib.canesvenatici.ax.impl.Quote;
import unk.prolib.canesvenatici.ax.impl.Symbol;

class ArbitrageSpreadTest {
    private static final Instant ANY_TIME = Instant.parse("2022-09-25T00:00:00Z");
    private static final Symbol symbol1 = Symbol.builder().exchangeID("XXX").baseAsset("A").quoteAsset("B").build();
    private static final Symbol symbol2 = Symbol.builder().exchangeID("YYY").baseAsset("A").quoteAsset("B").build();

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
                .symbol(Symbol.builder()
                        .exchangeID("YYY")
                        .baseAsset("A")
                        .quoteAsset("B")
                        .build().toAskSymbol())
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
                .symbol(Symbol.builder()
                        .exchangeID("YYY")
                        .baseAsset("A")
                        .quoteAsset("B")
                        .build().toBidSymbol())
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

    @Test
    void testGetAbsoluteValue() {
        assertEquals(new BigDecimal("0.18"), ArbitrageSpread.builder()
                .time(ANY_TIME)
                .askQuote(Quote.ofAsk(symbol1, "26.95", "150"))
                .bidQuote(Quote.ofBid(symbol2, "27.13", "100"))
                .build()
                .getAbsoluteValue());
        assertEquals(new BigDecimal("-2.42"), ArbitrageSpread.builder()
                .time(ANY_TIME)
                .askQuote(Quote.ofAsk(symbol1, "28.56", "200"))
                .bidQuote(Quote.ofBid(symbol2, "26.14", "180"))
                .build()
                .getAbsoluteValue());
    }

    @Test
    void testGetBidToAskPriceRatioAndProfitAndLoss() {
        var spread = ArbitrageSpread.builder()
                .time(ANY_TIME)
                .askQuote(Quote.ofAsk(symbol1, "124.26", "800"))
                .bidQuote(Quote.ofBid(symbol2, "154.02", "200"))
                .build();
        // 154.02/124.26=1.239498
        assertEquals(1.239498d, spread.getBidToAskPriceRatio(), 0.0000001d);
        assertEquals(0.239498d, spread.getProfitAndLoss(), 0.0000001d);
        
        spread = ArbitrageSpread.builder()
                .time(ANY_TIME)
                .askQuote(Quote.ofAsk(symbol1, "86.72", "100"))
                .bidQuote(Quote.ofBid(symbol2, "75.12", "200"))
                .build();
        // 75.12/86.72=0.866236
        assertEquals(0.866236d, spread.getBidToAskPriceRatio(), 0.0000001d);
        assertEquals(-0.133764d, spread.getProfitAndLoss(), 0.0000001d);
    }
}
