package unk.prolib.canesvenatici.ax.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unk.prolib.canesvenatici.ax.AXAskSymbol;
import unk.prolib.canesvenatici.ax.AXBidSymbol;
import unk.prolib.canesvenatici.ax.AXMarketDepthRegistry;
import unk.prolib.canesvenatici.ax.output.impl.ArbitrageSpread;

class SpreadDetectorVegaTest {
    private static Duration MAX_UPDATE_DELAY = Duration.ofMinutes(5);
    private static AXAskSymbol ASK_SYMBOL = AskSymbol.of(Symbol.builder()
            .exchangeID("AAA").baseAsset("LTC").quoteAsset("BTC")
            .build());
    private static AXBidSymbol BID_SYMBOL = BidSymbol.of(Symbol.builder()
            .exchangeID("BBB").baseAsset("LTC").quoteAsset("BTC")
            .build());
    private static Instant
        T1 = T("2022-09-18T12:05:00Z"),
        T2 = T("2022-09-18T12:06:00Z"),
        T3 = T("2022-09-18T12:10:00Z"),
        T4 = T("2022-09-18T12:10:01Z");
    private IMocksControl control;
    private AXMarketDepthRegistry registryMock;
    private Clock clockMock;
    private SpreadDetectorVega service;

    static Instant T(String timeString) {
        return Instant.parse(timeString);
    }

    @BeforeEach
    void setUp() throws Exception {
        control = createStrictControl();
        registryMock = control.createMock(AXMarketDepthRegistry.class);
        clockMock = control.createMock(Clock.class);
        service = new SpreadDetectorVega(registryMock, clockMock, MAX_UPDATE_DELAY);
    }

    @Test
    void testDetectSpread_NoResultIfNoMarketDepthOfBidSymbol() {
        expect(clockMock.instant()).andReturn(T1);
        expect(registryMock.getMarketDepth(BID_SYMBOL)).andStubReturn(Optional.empty());
        expect(registryMock.getMarketDepth(ASK_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T1)
                .symbol(ASK_SYMBOL)
                .addAsk("117.25", "1000")
                .build()));
        control.replay();
        
        assertEquals(Optional.empty(), service.detectSpread(ASK_SYMBOL, BID_SYMBOL));
        
        control.verify();
    }

    @Test
    void testDetectSpread_NoResultIfNoMarketDepthOfAskSymbol() {
        expect(clockMock.instant()).andReturn(T1);
        expect(registryMock.getMarketDepth(BID_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T1)
                .symbol(BID_SYMBOL)
                .addBid("120.83", "110")
                .build()));
        expect(registryMock.getMarketDepth(ASK_SYMBOL)).andStubReturn(Optional.empty());
        control.replay();
        
        assertEquals(Optional.empty(), service.detectSpread(ASK_SYMBOL, BID_SYMBOL));
        
        control.verify();
    }

    @Test
    void testDetectSpread_NoResultIfMarketDepthOfBidSymbolIsOutdated() {
        expect(clockMock.instant()).andReturn(T4);
        expect(registryMock.getMarketDepth(BID_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T1)
                .symbol(BID_SYMBOL)
                .addBid("120.83", "110")
                .build()));
        expect(registryMock.getMarketDepth(ASK_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T4)
                .symbol(ASK_SYMBOL)
                .addAsk("117.25", "1000")
                .build()));
        control.replay();
        
        assertEquals(Optional.empty(), service.detectSpread(ASK_SYMBOL, BID_SYMBOL));
        
        control.verify();
    }

    @Test
    void testDetectSpread_NoResultIfMarketDepthOfAskSymbolIsOutdated() {
        expect(clockMock.instant()).andReturn(T4);
        expect(registryMock.getMarketDepth(BID_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T4)
                .symbol(BID_SYMBOL)
                .addBid("120.83", "110")
                .build()));
        expect(registryMock.getMarketDepth(ASK_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T1)
                .symbol(ASK_SYMBOL)
                .addAsk("117.25", "1000")
                .build()));
        control.replay();
        
        assertEquals(Optional.empty(), service.detectSpread(ASK_SYMBOL, BID_SYMBOL));
        
        control.verify();
    }

    @Test
    void testDetectSpread_NoResultIfMarketDepthOfBidSymbolHasNoBestBid() {
        expect(clockMock.instant()).andReturn(T4);
        expect(registryMock.getMarketDepth(BID_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T4)
                .symbol(BID_SYMBOL)
                .build()));
        expect(registryMock.getMarketDepth(ASK_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T4)
                .symbol(ASK_SYMBOL)
                .addAsk("117.25", "1000")
                .build()));
        control.replay();
        
        assertEquals(Optional.empty(), service.detectSpread(ASK_SYMBOL, BID_SYMBOL));
        
        control.verify();
    }

    @Test
    void testDetectSpread_NoResultIfMarketDepthOfAskSymbolHasNoBestAsk() {
        expect(clockMock.instant()).andReturn(T4);
        expect(registryMock.getMarketDepth(BID_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T4)
                .symbol(BID_SYMBOL)
                .addBid("120.83", "110")
                .build()));
        expect(registryMock.getMarketDepth(ASK_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T4)
                .symbol(ASK_SYMBOL)
                .build()));
        control.replay();
        
        assertEquals(Optional.empty(), service.detectSpread(ASK_SYMBOL, BID_SYMBOL));
        
        control.verify();
    }

    @Test
    void testDetectSpread_Success() {
        expect(clockMock.instant()).andReturn(T4);
        expect(registryMock.getMarketDepth(BID_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T3)
                .symbol(BID_SYMBOL)
                .addBid("121.02", "120")
                .build()));
        expect(registryMock.getMarketDepth(ASK_SYMBOL)).andStubReturn(Optional.of(MarketDepth.builder()
                .lastUpdateTime(T2)
                .symbol(ASK_SYMBOL)
                .addAsk("117.25", "1000")
                .build()));
        control.replay();
        
        var actual = service.detectSpread(ASK_SYMBOL, BID_SYMBOL).get();
        
        control.verify();
        var expected = ArbitrageSpread.builder()
                .time(T4)
                .askQuote(Quote.ofAsk(ASK_SYMBOL, "117.25", "1000"))
                .bidQuote(Quote.ofBid(BID_SYMBOL, "121.02", "120"))
                .build();
        assertEquals(expected, actual);
    }

}
