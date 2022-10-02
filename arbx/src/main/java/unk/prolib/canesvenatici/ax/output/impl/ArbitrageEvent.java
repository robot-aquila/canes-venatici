package unk.prolib.canesvenatici.ax.output.impl;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.output.AXArbitrageEvent;
import unk.prolib.canesvenatici.ax.output.AXArbitrageSpread;

@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
@Getter
public class ArbitrageEvent implements AXArbitrageEvent {
    @NonNull private final AXArbitrageSpread spreadAtStart;
    @NonNull private final AXArbitrageSpread spreadAtMaximum;
    @NonNull private final AXArbitrageSpread spreadAtEnd;

}
