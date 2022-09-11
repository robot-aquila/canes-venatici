package unk.prolib.canesvenatici.ax.output.impl;

import java.util.Optional;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.output.AXArbitrageEvent;
import unk.prolib.canesvenatici.ax.output.AXSpread;

@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
@Getter
public class ArbitrageEvent implements AXArbitrageEvent {
    @NonNull private final AXSpread spreadAtStart;
    @NonNull private final AXSpread spreadAtMaximum;
    private final AXSpread spreadAtEnd;

    @Override
    public Optional<AXSpread> getSpreadAtEnd() {
        return Optional.ofNullable(spreadAtEnd);
    }

}
