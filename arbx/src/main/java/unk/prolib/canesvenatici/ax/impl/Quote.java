package unk.prolib.canesvenatici.ax.impl;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXQuote;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
public class Quote implements AXQuote {
    @NonNull private final BigDecimal price;
    @NonNull private final BigDecimal volume;
}
