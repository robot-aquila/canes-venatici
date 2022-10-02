package unk.prolib.canesvenatici.ax.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXMarketDepth;
import unk.prolib.canesvenatici.ax.AXMarketDepthRegistry;
import unk.prolib.canesvenatici.ax.AXSymbol;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class MarketDepthRegistry implements AXMarketDepthRegistry {
    @NonNull private Map<AXSymbol, AXMarketDepth> data;

    public MarketDepthRegistry() {
        this(new HashMap<>());
    }

    @Override
    public Optional<AXMarketDepth> getMarketDepth(AXSymbol symbol) {
        return Optional.ofNullable(data.get(symbol));
    }

    @Override
    public void updateMarketDepth(AXMarketDepth marketDepth) {
        data.put(marketDepth.getSymbol(), marketDepth);
    }

}
