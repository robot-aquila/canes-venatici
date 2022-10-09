package unk.prolib.canesvenatici.ax.impl;

import static org.easymock.EasyMock.createStrictControl;
import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXArbitrageService;
import unk.prolib.canesvenatici.ax.AXMarketDepthRegistry;
import unk.prolib.canesvenatici.ax.AXQuoteType;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.input.AXMarketDepthEvent;
import unk.prolib.canesvenatici.ax.input.AXQuoteEvent;
import unk.prolib.canesvenatici.ax.input.AXQuoteEventType;
import unk.prolib.canesvenatici.ax.input.impl.QuoteEvent;
import unk.prolib.canesvenatici.ax.output.AXArbitrageEvent;
import unk.prolib.canesvenatici.ax.output.AXArbitrageEventListener;
import unk.prolib.canesvenatici.ax.output.impl.ArbitrageEvent;
import unk.prolib.canesvenatici.ax.output.impl.ArbitrageSpread;

class ArbitrageServiceIT {
    
    enum EventType {
        OPENED,
        UPDATED,
        CLOSED
    }
    
    @Builder
    @ToString
    @EqualsAndHashCode
    static class Event {
        @NonNull private final EventType type;
        @NonNull private AXArbitrageEvent event;
    }
    
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    static class EventListener implements AXArbitrageEventListener {
        private final List<Event> events = new ArrayList<>();
        
        @Override
        public void onSpreadOpened(AXArbitrageEvent event) {
            events.add(new Event(EventType.OPENED, event));
        }

        @Override
        public void onSpreadUpdated(AXArbitrageEvent event) {
            events.add(new Event(EventType.UPDATED, event));
        }

        @Override
        public void onSpreadClosed(AXArbitrageEvent event) {
            events.add(new Event(EventType.CLOSED, event));
        }
        
    }
    
    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    static class InputEvent implements AXMarketDepthEvent {
        private AXSymbol symbol;
        private Instant updateTime;
        private Set<AXQuoteEvent> quoteEvents;
        private boolean snapshot;
        
        public void setSymbol(String symbol) {
            var chunks = symbol.split(":");
            switch ( chunks.length ) {
            case 3:
                this.symbol = Symbol.builder().baseAsset(chunks[0]).quoteAsset(chunks[1]).exchangeID(chunks[2]).build();
                break;
            default:
                throw new IllegalArgumentException("Unexpected symbol format: " + symbol);
            }
            
        }
        
        public void setTime(String time) {
            this.updateTime = "-".equals(time) ? DEFAULT_TIME : Instant.parse(time + "Z");
        }
        
        public void setEvents(List<String> events) {
            this.quoteEvents = new HashSet<>();
            for ( int i = 0; i < events.size(); i ++ ) {
                try {
                    this.quoteEvents.add(parseEvent(events.get(i)));
                } catch ( RuntimeException e ) {
                    throw new IllegalArgumentException("Unexpected event #" + i + " format: " + events.get(i), e);
                }
            }
        }
        
        public void setSnapshot(boolean snapshot) {
            this.snapshot = snapshot;
        }
        
        private AXQuoteEvent parseEvent(String e) {
            var chunks = e.split(":");
            var builder = QuoteEvent.builder().symbol(symbol);
            switch ( chunks.length ) {
            case 3: builder.eventType(AXQuoteEventType.UPDATE); break;
            case 2: builder.eventType(AXQuoteEventType.DELETE); break;
            default:
                throw new IllegalArgumentException("Unexpected number of chunks: " + chunks.length);
            }
            switch ( chunks[0] ) {
            case "A": builder.quoteType(AXQuoteType.ASK); break;
            case "B": builder.quoteType(AXQuoteType.BID); break;
            default:
                throw new IllegalArgumentException("Unexpected quote type: " + chunks[0]);
            }
            builder.price(new BigDecimal(chunks[1]));
            if ( chunks.length == 3 ) {
                builder.volume(new BigDecimal(chunks[2]));
            }
            return builder.build();
        }
    }
    
    private static AXSymbol SYMBOL1 = Symbol.builder().baseAsset("ETH").quoteAsset("BTC").exchangeID("XXX").build();
    private static AXSymbol SYMBOL2 = Symbol.builder().baseAsset("ETH").quoteAsset("BTC").exchangeID("YYY").build();
    private static Instant DEFAULT_TIME = Instant.parse("2022-01-01T00:00:00Z");
    private static ObjectMapper objectMapper = new ObjectMapper();
    private IMocksControl control;
    private Clock clockMock;
    private EventListener listener;
    @SuppressWarnings("unused")
    private AXMarketDepthRegistry registry;
    private AXArbitrageService service;
    
    @BeforeEach
    void setUp() throws Exception {
        control = createStrictControl();
        clockMock = control.createMock(Clock.class);
        service = ArbitrageServiceFactory.getInstance().variation1()
                .withMarketDepthRegistry(registry = new MarketDepthRegistry())
                .addMinProfitSpreadValidator(0.05d)
                .withMarketDepthMaxUpdateDelayMinutes(10)
                .withClock(clockMock)
                .withEventListener(listener = new EventListener())
                .build();
    }
    
    @SneakyThrows
    private List<InputEvent> loadInputEvents(String path) {
        return objectMapper.readValue(new ClassPathResource(path).getInputStream(), new TypeReference<List<InputEvent>>(){});
    }
    
    @Test
    void testSimpleSequenceIgnoringTime() {
        expect(clockMock.instant()).andStubReturn(DEFAULT_TIME);
        control.replay();
        loadInputEvents("input-events1.json").forEach(service);
        
        var expected = List.of(
                Event.builder()
                    .type(EventType.OPENED)
                    .event(ArbitrageEvent.builder()
                            .spreadAtStart(ArbitrageSpread.builder()
                                    .time(DEFAULT_TIME)
                                    .bidQuote(Quote.ofBid(SYMBOL2, "127.15", "100"))
                                    .askQuote(Quote.ofAsk(SYMBOL1, "120.54", "100"))
                                    .build())
                            .spreadAtMaximum(ArbitrageSpread.builder()
                                    .time(DEFAULT_TIME)
                                    .bidQuote(Quote.ofBid(SYMBOL2, "127.15", "100"))
                                    .askQuote(Quote.ofAsk(SYMBOL1, "120.54", "100"))
                                    .build())
                            .spreadAtEnd(ArbitrageSpread.builder()
                                    .time(DEFAULT_TIME)
                                    .bidQuote(Quote.ofBid(SYMBOL2, "127.15", "100"))
                                    .askQuote(Quote.ofAsk(SYMBOL1, "120.54", "100"))
                                    .build())
                            .build())
                    .build(),
                Event.builder()
                    .type(EventType.UPDATED)
                    .event(ArbitrageEvent.builder()
                            .spreadAtStart(ArbitrageSpread.builder()
                                    .time(DEFAULT_TIME)
                                    .bidQuote(Quote.ofBid(SYMBOL2, "127.15", "100"))
                                    .askQuote(Quote.ofAsk(SYMBOL1, "120.54", "100"))
                                    .build())
                            .spreadAtMaximum(ArbitrageSpread.builder()
                                    .time(DEFAULT_TIME)
                                    .bidQuote(Quote.ofBid(SYMBOL2, "130.15", "150"))
                                    .askQuote(Quote.ofAsk(SYMBOL1, "120.54", "100"))
                                    .build())
                            .spreadAtEnd(ArbitrageSpread.builder()
                                    .time(DEFAULT_TIME)
                                    .bidQuote(Quote.ofBid(SYMBOL2, "130.15", "150"))
                                    .askQuote(Quote.ofAsk(SYMBOL1, "120.54", "100"))
                                    .build())
                            .build())
                    .build(),
                Event.builder()
                    .type(EventType.CLOSED)
                    .event(ArbitrageEvent.builder()
                            .spreadAtStart(ArbitrageSpread.builder()
                                    .time(DEFAULT_TIME)
                                    .bidQuote(Quote.ofBid(SYMBOL2, "127.15", "100"))
                                    .askQuote(Quote.ofAsk(SYMBOL1, "120.54", "100"))
                                    .build())
                            .spreadAtMaximum(ArbitrageSpread.builder()
                                    .time(DEFAULT_TIME)
                                    .bidQuote(Quote.ofBid(SYMBOL2, "130.15", "150"))
                                    .askQuote(Quote.ofAsk(SYMBOL1, "120.54", "100"))
                                    .build())
                            .spreadAtEnd(ArbitrageSpread.builder()
                                    .time(DEFAULT_TIME)
                                    .bidQuote(Quote.ofBid(SYMBOL2, "130.15", "150"))
                                    .askQuote(Quote.ofAsk(SYMBOL1, "120.54", "100"))
                                    .build())
                            .build())
                    .build()
            );
        assertEquals(expected, listener.events);
    }
    
    @Test
    void testMarketDepthSnapshotUpdate() {
        expect(clockMock.instant()).andStubReturn(DEFAULT_TIME);
        control.replay();
        loadInputEvents("input-events2.json").forEach(service);
        
        var actual = registry.getMarketDepth(SYMBOL1).get();
        
        var expected = MarketDepth.builder()
                .symbol(SYMBOL1)
                .lastUpdateTime("2022-01-01T00:02:00Z")
                .addBid("141.00", "200")
                .addBid("140.00", "150")
                .addAsk("150.00", "250")
                .addAsk("151.00", "300")
                .build();
        assertEquals(expected, actual);
    }

}
