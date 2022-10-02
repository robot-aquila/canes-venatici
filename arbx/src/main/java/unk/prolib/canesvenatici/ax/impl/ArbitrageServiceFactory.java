package unk.prolib.canesvenatici.ax.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import unk.prolib.canesvenatici.ax.AXMarketDepthRegistry;
import unk.prolib.canesvenatici.ax.AXMarketDepthTrackerFactory;
import unk.prolib.canesvenatici.ax.output.AXArbitrageEventListener;

public class ArbitrageServiceFactory {

    public static ArbitrageServiceFactory getInstance() {
        return new ArbitrageServiceFactory();
    }

    /**
     * Build a variation 1 of service.
     * <p>
     * This variation based on same spread detector for opening and closing operations.
     * Configurable max update period manages when the data must considered actual or outdated.
     * Custom amount of additional validators may be added to a spread validation chain.
     * Event listener is mandatory component to receive events.
     * It cannot be determined automatically and must be specified explicitly. 
     * <p>
     * @return builder
     */
    public ArbitrageServiceBuilderV1 variation1() {
        return new ArbitrageServiceBuilderV1();
    }

    @ToString
    @EqualsAndHashCode
    public static class ArbitrageServiceBuilderV1 {
        private AXArbitrageEventListener listener;
        private AXMarketDepthRegistry registry = new MarketDepthRegistry();
        private AXMarketDepthTrackerFactory trackerFactory = new MarketDepthTrackerFactory();
        private List<SpreadDetectorValidator.Validator> validators = new ArrayList<>();
        private Duration marketDepthMaxUpdateDelay = Duration.ofMinutes(5L);
        
        public ArbitrageServiceBuilderV1 withEventListener(AXArbitrageEventListener listener) {
            this.listener = listener;
            return this;
        }
        
        public ArbitrageServiceBuilderV1 withMarketDepthRegistry(AXMarketDepthRegistry registry) {
            this.registry = registry;
            return this;
        }
        
        public ArbitrageServiceBuilderV1 withMarketDepthTrackerFactory(AXMarketDepthTrackerFactory factory) {
            this.trackerFactory = factory;
            return this;
        }
        
        /**
         * Add custom spread validator.
         * <p>
         * @param validator - validator
         * @return this
         */
        public ArbitrageServiceBuilderV1 addSpreadValidator(@NonNull SpreadDetectorValidator.Validator validator) {
            this.validators.add(validator);
            return this;
        }
        
        /**
         * Add validator to check that spread is greater or equals to specified PL.
         * <p>
         * @param minProfitAndLoss - PL rate: 0.01 means 1%, 0.1 means 10%, 0.25 means 25% etc...
         * @return this
         */
        public ArbitrageServiceBuilderV1 addMinProfitSpreadValidator(double minProfitAndLoss) {
            return addSpreadValidator(new SpreadDetectorValidator.MinProfitAndLoss(minProfitAndLoss));
        }
        
        public ArbitrageServiceBuilderV1 withMarketDepthMaxUpdateDelay(Duration duration) {
            this.marketDepthMaxUpdateDelay = duration;
            return this;
        }
        
        public ArbitrageServiceBuilderV1 withMarketDepthMaxUpdateDelayMinutes(long minutes) {
            return this.withMarketDepthMaxUpdateDelay(Duration.ofMinutes(minutes));
        }
        
        public ArbitrageService build() {
            Objects.requireNonNull(listener, "Event listener was not specified");
            Objects.requireNonNull(registry, "MD registry was not specified");
            Objects.requireNonNull(trackerFactory, "MD tracker factory was not specified");
            Objects.requireNonNull(marketDepthMaxUpdateDelay, "MD max update delay was not specified");
            var spreadDetector = SpreadDetectorValidator.builder()
                    .detector(SpreadDetectorVega.builder()
                            .marketDepthMaxUpdateDelay(marketDepthMaxUpdateDelay)
                            .registry(registry)
                            .build())
                    .validators(validators)
                    .build();
            return ArbitrageService.builder()
                    .registry(registry)
                    .trackerFactory(trackerFactory)
                    .dispatcherFactory(ArbitrageUpdateDispatcherFactory.builder()
                            .listener(listener)
                            .openingSpreadDetector(spreadDetector)
                            .closingSpreadDetector(spreadDetector)
                            .build())
                    .build();
        }
    }

}
