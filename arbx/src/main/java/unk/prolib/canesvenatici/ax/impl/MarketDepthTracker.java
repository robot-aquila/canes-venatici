package unk.prolib.canesvenatici.ax.impl;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXSymbol;

@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class MarketDepthTracker {
    @Getter @NonNull private final AXSymbol symbol;
}
