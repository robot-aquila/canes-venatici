package unk.prolib.canesvenatici.ax.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXArbitrageService;
import unk.prolib.canesvenatici.ax.AXArbitrageUpdateDispatcher;
import unk.prolib.canesvenatici.ax.AXArbitrageUpdateDispatcherFactory;
import unk.prolib.canesvenatici.ax.AXMarketDepthRegistry;
import unk.prolib.canesvenatici.ax.AXMarketDepthTracker;
import unk.prolib.canesvenatici.ax.AXMarketDepthTrackerFactory;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.input.AXMarketDepthEvent;

@AllArgsConstructor
public class ArbitrageService implements AXArbitrageService {
    
    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    static class Entry {
        @NonNull private final AXMarketDepthTracker tracker;
        @NonNull private final AXArbitrageUpdateDispatcher dispatcher;
    }
    
    @NonNull private final AXArbitrageUpdateDispatcherFactory dispatcherFactory;
    @NonNull private final AXMarketDepthTrackerFactory trackerFactory;
    @NonNull private final AXMarketDepthRegistry registry;
    @NonNull private final Map<AXSymbol, Entry> entries;

    @Builder
    public ArbitrageService(AXArbitrageUpdateDispatcherFactory dispatcherFactory,
            AXMarketDepthTrackerFactory trackerFactory,
            AXMarketDepthRegistry registry)
    {
        this(dispatcherFactory, trackerFactory, registry, new HashMap<>());
    }

    @Override
    public void accept(AXMarketDepthEvent event) {
        var symbol = Symbol.of(event.getSymbol());
        var entry = entries.get(symbol);
        if ( entry == null ) {
            entry = Entry.builder()
                    .dispatcher(dispatcherFactory.produce(symbol))
                    .tracker(trackerFactory.produce(symbol))
                    .build();
            entries.put(symbol, entry);
        }
        registry.updateMarketDepth(entry.getTracker().consume(event));
        entry.getDispatcher().updateCandidate(symbol);
    }

}
