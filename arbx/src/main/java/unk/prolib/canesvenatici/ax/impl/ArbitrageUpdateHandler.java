package unk.prolib.canesvenatici.ax.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import unk.prolib.canesvenatici.ax.AXArbitragePair;
import unk.prolib.canesvenatici.ax.AXArbitrageUpdateHandler;
import unk.prolib.canesvenatici.ax.AXSpreadDetector;
import unk.prolib.canesvenatici.ax.output.AXArbitrageEventListener;
import unk.prolib.canesvenatici.ax.output.impl.ArbitrageEvent;

@AllArgsConstructor
public class ArbitrageUpdateHandler implements AXArbitrageUpdateHandler {
    @NonNull private final AXSpreadDetector openingSpreadDetector;
    @NonNull private final AXSpreadDetector closingSpreadDetector;
    @NonNull private final AXArbitrageEventListener listener;
    @NonNull private final Map<AXArbitragePair, ArbitrageEvent> events;

    @Builder
    public ArbitrageUpdateHandler(@NonNull AXSpreadDetector openingSpreadDetector,
            @NonNull AXSpreadDetector closingSpreadDetector,
            @NonNull AXArbitrageEventListener listener)
    {
        this(openingSpreadDetector, closingSpreadDetector, listener, new HashMap<>());
    }

    @Override
    public void handle(AXArbitragePair pair) {
        var oldEvent = events.get(pair);
        if ( oldEvent == null ) {
            var spread = openingSpreadDetector.detectSpread(pair).orElse(null);
            if ( spread != null ) {
                var newEvent = ArbitrageEvent.builder()
                        .spreadAtStart(spread)
                        .spreadAtMaximum(spread)
                        .spreadAtEnd(spread)
                        .build();
                events.put(pair, newEvent);
                listener.onSpreadOpened(newEvent);
            }
        } else {
            var spread = closingSpreadDetector.detectSpread(pair).orElse(null);
            if ( spread != null ) {
                var newEventBuilder = oldEvent.toBuilder().spreadAtEnd(spread);
                if ( spread.getAbsoluteValue().compareTo(oldEvent.getSpreadAtMaximum().getAbsoluteValue()) > 0 ) {
                    newEventBuilder.spreadAtMaximum(spread);
                }
                var newEvent = newEventBuilder.build();
                events.put(pair, newEvent);
                listener.onSpreadUpdated(newEvent);
            } else {
                handleRemoved(pair);
            }
        }
    }

    @Override
    public void handleRemoved(AXArbitragePair pair) {
        var event = events.remove(pair);
        if ( event != null ) {
            listener.onSpreadClosed(event);
        }
    }

}
