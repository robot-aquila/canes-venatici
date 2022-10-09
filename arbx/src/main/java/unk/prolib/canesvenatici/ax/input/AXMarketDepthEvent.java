package unk.prolib.canesvenatici.ax.input;

import java.time.Instant;
import java.util.Set;

import unk.prolib.canesvenatici.ax.AXSymbol;

public interface AXMarketDepthEvent {
    Instant getUpdateTime();
    AXSymbol getSymbol();
    Set<AXQuoteEvent> getQuoteEvents();
    boolean isSnapshot();
}
