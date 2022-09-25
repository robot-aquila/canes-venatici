package unk.prolib.canesvenatici.ax.impl;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXSymbolSubject;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class SymbolSubject implements AXSymbolSubject {
    @NonNull private final String baseAsset;
    @NonNull private final String quoteAsset;

}
