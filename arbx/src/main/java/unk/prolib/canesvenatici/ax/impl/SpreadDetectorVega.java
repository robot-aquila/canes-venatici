package unk.prolib.canesvenatici.ax.impl;

import java.time.Clock;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXMarketDepth;
import unk.prolib.canesvenatici.ax.AXMarketDepthRegistry;
import unk.prolib.canesvenatici.ax.AXSpreadDetector;
import unk.prolib.canesvenatici.ax.AXArbitragePair;
import unk.prolib.canesvenatici.ax.output.AXArbitrageSpread;
import unk.prolib.canesvenatici.ax.output.impl.ArbitrageSpread;

@ToString
@EqualsAndHashCode
@Builder
public class SpreadDetectorVega implements AXSpreadDetector {
    @NonNull private final AXMarketDepthRegistry registry;
    @NonNull private final Clock clock;
    /**
     * Maximum time between now and last update of market depth to make the latter considered actual (not outdated)
     */
    @NonNull private final Duration marketDepthMaxUpdateDelay;

    @Override
    public Optional<AXArbitrageSpread> detectSpread(AXArbitragePair pair) {
        AXMarketDepth buyers, sellers;
        var t = clock.instant();
        if (   ! Objects.isNull(buyers = registry.getMarketDepth(pair.getBidSymbol()).orElse(null))
            && ! Objects.isNull(sellers = registry.getMarketDepth(pair.getAskSymbol()).orElse(null))
            && Duration.between(buyers.getLastUpdateTime(), t).abs().compareTo(marketDepthMaxUpdateDelay) <= 0
            && Duration.between(sellers.getLastUpdateTime(), t).abs().compareTo(marketDepthMaxUpdateDelay) <= 0
            && buyers.hasBids() && sellers.hasAsks()
        ) {
            return Optional.of(ArbitrageSpread.builder()
                    .time(t)
                    .askQuote(sellers.getBestAsk())
                    .bidQuote(buyers.getBestBid())
                    .build());
        }
        return Optional.empty();
    }

    public static class SpreadDetectorVegaBuilder {
        private Clock clock = Clock.systemUTC();
    }
}
