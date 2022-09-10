package unk.prolib.canesvenatici.ax.impl;

import java.time.Instant;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXMarketDepth;
import unk.prolib.canesvenatici.ax.AXQuote;
import unk.prolib.canesvenatici.ax.AXSymbol;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
public class MarketDepth implements AXMarketDepth {
    @NonNull private final AXSymbol symbol;
    @NonNull private final Instant lastUpdateTime;
    @NonNull private final List<AXQuote> bids;
    @NonNull private final List<AXQuote> asks;
}
