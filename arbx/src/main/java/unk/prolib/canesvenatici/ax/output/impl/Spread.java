package unk.prolib.canesvenatici.ax.output.impl;

import java.time.Instant;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXQuote;
import unk.prolib.canesvenatici.ax.output.AXSpread;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
public class Spread implements AXSpread {
    @NonNull private final Instant time;
    @NonNull private final AXQuote bidQuote;
    @NonNull private final AXQuote askQuote;
}
