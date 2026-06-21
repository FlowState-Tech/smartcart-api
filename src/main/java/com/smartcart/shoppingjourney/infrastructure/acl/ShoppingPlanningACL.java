package com.smartcart.shoppingjourney.infrastructure.acl;

import com.smartcart.shared.infrastructure.events.BasketComparedIntegrationEvent;
import com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories.ShoppingPreferencesRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * ACL — Customer/Supplier: Shopping Planning (U) → Shopping Journey (D).
 * Traduce Canasta Comparada y preferencias al lenguaje del dominio de rutas.
 * Reporte 2.5.2 §4 y 2.6.6.
 */
@Component
public class ShoppingPlanningACL {

    private final ShoppingPreferencesRepository preferencesRepository;

    public ShoppingPlanningACL(ShoppingPreferencesRepository preferencesRepository) {
        this.preferencesRepository = preferencesRepository;
    }

    public PlanningSnapshot translate(BasketComparedIntegrationEvent event) {
        var residence = findBuyerResidence(event.buyerId());
        return new PlanningSnapshot(
                event.buyerId(),
                event.listId(),
                event.bestStoreId(),
                event.bestTotalCost(),
                event.rankedStoreIds(),
                residence.map(r -> r[0]).orElse(null),
                residence.map(r -> r[1]).orElse(null));
    }

    public Optional<double[]> findBuyerResidence(Long buyerId) {
        return preferencesRepository.findByBuyerId(buyerId)
                .filter(p -> p.getResidenceLat() != null && p.getResidenceLng() != null)
                .map(p -> new double[]{p.getResidenceLat(), p.getResidenceLng()});
    }

    public record PlanningSnapshot(
            Long buyerId,
            Long listId,
            Long bestStoreId,
            java.math.BigDecimal bestTotalCost,
            java.util.List<Long> rankedStoreIds,
            Double residenceLat,
            Double residenceLng
    ) {
        public boolean hasResidence() {
            return residenceLat != null && residenceLng != null;
        }
    }
}
