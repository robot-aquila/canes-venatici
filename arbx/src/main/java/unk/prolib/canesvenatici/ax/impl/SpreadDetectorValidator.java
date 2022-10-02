package unk.prolib.canesvenatici.ax.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXArbitragePair;
import unk.prolib.canesvenatici.ax.AXSpreadDetector;
import unk.prolib.canesvenatici.ax.output.AXArbitrageSpread;

/**
 * Additional spread validators.
 */
@Builder
@ToString
@EqualsAndHashCode
public class SpreadDetectorValidator implements AXSpreadDetector {
    
    public interface Validator {
        boolean isValid(AXArbitrageSpread spread);
    }
    
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class MinProfitAndLoss implements Validator {
        private final double minProfitAndLoss;

        @Override
        public boolean isValid(AXArbitrageSpread spread) {
            return spread.getProfitAndLoss() >= minProfitAndLoss;
        }
        
    }
    
    @NonNull private final AXSpreadDetector detector;
    @NonNull private final List<Validator> validators;

    @Override
    public Optional<AXArbitrageSpread> detectSpread(AXArbitragePair pair) {
        var spread = detector.detectSpread(pair);
        if ( spread.isPresent() ) {
            for ( Validator v : validators ) {
                if ( ! v.isValid(spread.get()) ) {
                    return Optional.empty();
                }
            }
        }
        return spread;
    }

    public static class SpreadDetectorValidatorBuilder {
        private List<Validator> validators = new ArrayList<>();
        
        public SpreadDetectorValidatorBuilder addValidator(@NonNull Validator validator) {
            this.validators.add(validator);
            return this;
        }
        
        public SpreadDetectorValidatorBuilder addMinProfitAndLoss(double minProfitAndLoss) {
            return addValidator(new MinProfitAndLoss(minProfitAndLoss));
        }
    }
}
