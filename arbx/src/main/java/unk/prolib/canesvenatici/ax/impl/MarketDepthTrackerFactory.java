package unk.prolib.canesvenatici.ax.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXMarketDepthTracker;
import unk.prolib.canesvenatici.ax.AXMarketDepthTrackerFactory;
import unk.prolib.canesvenatici.ax.AXSymbol;

@ToString
@EqualsAndHashCode
public class MarketDepthTrackerFactory implements AXMarketDepthTrackerFactory {

    @Override
    public AXMarketDepthTracker produce(AXSymbol symbol) {
        return new MarketDepthTracker(symbol);
    }

}
