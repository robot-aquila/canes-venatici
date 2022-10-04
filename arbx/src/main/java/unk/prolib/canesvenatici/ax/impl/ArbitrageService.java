package unk.prolib.canesvenatici.ax.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXArbitrageService;
import unk.prolib.canesvenatici.ax.AXArbitrageUpdateDispatcher;
import unk.prolib.canesvenatici.ax.AXArbitrageUpdateDispatcherFactory;
import unk.prolib.canesvenatici.ax.AXMarketDepthRegistry;
import unk.prolib.canesvenatici.ax.AXMarketDepthTracker;
import unk.prolib.canesvenatici.ax.AXMarketDepthTrackerFactory;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.AXSymbolSubject;
import unk.prolib.canesvenatici.ax.input.AXMarketDepthEvent;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ArbitrageService implements AXArbitrageService {
    @NonNull private final AXArbitrageUpdateDispatcherFactory dispatcherFactory;
    @NonNull private final AXMarketDepthTrackerFactory trackerFactory;
    @NonNull private final AXMarketDepthRegistry registry;
    @NonNull private final Map<AXSymbol, AXMarketDepthTracker> trackers;
    @NonNull private final Map<AXSymbolSubject, AXArbitrageUpdateDispatcher> dispatchers;

    @Builder
    public ArbitrageService(AXArbitrageUpdateDispatcherFactory dispatcherFactory,
            AXMarketDepthTrackerFactory trackerFactory,
            AXMarketDepthRegistry registry)
    {
        this(dispatcherFactory, trackerFactory, registry, new HashMap<>(), new HashMap<>());
    }

    @Override
    public void accept(AXMarketDepthEvent event) {
        var symbol = Symbol.of(event.getSymbol());
        var subject = symbol.toSubject();
        var tracker = trackers.get(symbol);
        if ( tracker == null ) {
            trackers.put(symbol, tracker = trackerFactory.produce(symbol));
        }
        var dispatcher = dispatchers.get(subject);
        if ( dispatcher == null ) {
            dispatchers.put(subject, dispatcher = dispatcherFactory.produce(symbol));
        }
        registry.updateMarketDepth(tracker.consume(event));
        dispatcher.updateCandidate(symbol);
    }

}
