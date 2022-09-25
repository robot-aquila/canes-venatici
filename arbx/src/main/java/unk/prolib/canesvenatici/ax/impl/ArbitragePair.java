package unk.prolib.canesvenatici.ax.impl;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXArbitragePair;
import unk.prolib.canesvenatici.ax.AXAskSymbol;
import unk.prolib.canesvenatici.ax.AXBidSymbol;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class ArbitragePair implements AXArbitragePair {
    @NonNull private final AXAskSymbol askSymbol;
    @NonNull private final AXBidSymbol bidSymbol;
    
    public static ArbitragePair of(AXAskSymbol askSymbol, AXBidSymbol bidSymbol) {
        return ArbitragePair.builder()
                .askSymbol(askSymbol)
                .bidSymbol(bidSymbol)
                .build();
    }
}
