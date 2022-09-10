package unk.prolib.canesvenatici.ax.impl;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXSymbol;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Symbol implements AXSymbol {
    @NonNull private final String exchangeID;
    @NonNull private final String baseAsset;
    @NonNull private final String quoteAsset;
}
