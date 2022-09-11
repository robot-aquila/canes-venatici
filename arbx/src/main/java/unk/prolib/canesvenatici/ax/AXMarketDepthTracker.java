package unk.prolib.canesvenatici.ax;

import unk.prolib.canesvenatici.ax.input.AXMarketDepthEvent;

public interface AXMarketDepthTracker {
    AXMarketDepth consume(AXMarketDepthEvent event);
}
