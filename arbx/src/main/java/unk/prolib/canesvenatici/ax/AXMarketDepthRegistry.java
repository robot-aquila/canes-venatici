package unk.prolib.canesvenatici.ax;

import java.util.Optional;

public interface AXMarketDepthRegistry {

    Optional<AXMarketDepth> getMarketDepth(AXSymbol symbol);

}
