package unk.prolib.canesvenatici.ax.impl;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXArbitrageUpdateDispatcher;
import unk.prolib.canesvenatici.ax.AXArbitrageUpdateDispatcherFactory;
import unk.prolib.canesvenatici.ax.AXSpreadDetector;
import unk.prolib.canesvenatici.ax.AXSymbol;
import unk.prolib.canesvenatici.ax.output.AXArbitrageEventListener;

@Builder
@ToString
@EqualsAndHashCode
public class ArbitrageUpdateDispatcherFactory implements AXArbitrageUpdateDispatcherFactory {
    @NonNull private final AXSpreadDetector openingSpreadDetector;
    @NonNull private final AXSpreadDetector closingSpreadDetector;
    @NonNull private final AXArbitrageEventListener listener;

    @Override
    public AXArbitrageUpdateDispatcher produce(AXSymbol symbol) {
        return new ArbitrageUpdateDispatcher(ArbitrageUpdateHandler.builder()
                .openingSpreadDetector(openingSpreadDetector)
                .closingSpreadDetector(closingSpreadDetector)
                .listener(listener)
                .build());
    }

}
