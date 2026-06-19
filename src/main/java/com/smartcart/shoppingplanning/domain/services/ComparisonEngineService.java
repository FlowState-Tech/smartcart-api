package com.smartcart.shoppingplanning.domain.services;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingList;
import com.smartcart.shoppingplanning.domain.model.valueobjects.Budget;
import com.smartcart.shoppingplanning.domain.model.valueobjects.PriceComparisonResult;
import com.smartcart.shoppingplanning.infrastructure.acl.StoreCatalogACL;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Motor de comparación de precios (PriceComparison aggregate logic).
 */
@Service
public class ComparisonEngineService {

    private final StoreCatalogACL catalogACL;

    public ComparisonEngineService(StoreCatalogACL catalogACL) {
        this.catalogACL = catalogACL;
    }

    public List<PriceComparisonResult> compare(ShoppingList list, List<Long> storeIds) {
        return compare(list, storeIds, null);
    }

    public List<PriceComparisonResult> compare(ShoppingList list, List<Long> storeIds, Budget budget) {
        var results = new ArrayList<PriceComparisonResult>();
        for (Long storeId : storeIds) {
            var total = BigDecimal.ZERO;
            int found = 0;
            int missing = 0;
            for (var item : list.getItems()) {
                var price = catalogACL.findPrice(storeId, item.getSku());
                if (price.isPresent()) {
                    total = total.add(price.get().multiply(item.getQuantity()));
                    found++;
                } else {
                    missing++;
                }
            }
            boolean withinBudget = budget == null || total.compareTo(budget.getAmount()) <= 0;
            var budgetLimit = budget != null ? budget.getAmount() : null;
            results.add(new PriceComparisonResult(
                    storeId, catalogACL.storeName(storeId), total, "PEN", found, missing,
                    BigDecimal.ZERO, BigDecimal.ZERO, withinBudget, budgetLimit));
        }
        return applySavings(results);
    }

    private List<PriceComparisonResult> applySavings(List<PriceComparisonResult> results) {
        if (results.isEmpty()) return results;
        var worst = results.stream().max(Comparator.comparing(PriceComparisonResult::totalCost)).orElseThrow();
        return results.stream()
                .sorted(Comparator.comparing(PriceComparisonResult::totalCost))
                .map(r -> {
                    var saved = worst.totalCost().subtract(r.totalCost());
                    var pct = worst.totalCost().compareTo(BigDecimal.ZERO) > 0
                            ? saved.multiply(BigDecimal.valueOf(100)).divide(worst.totalCost(), 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;
                    return new PriceComparisonResult(r.storeId(), r.storeName(), r.totalCost(), r.currency(),
                            r.itemsFound(), r.itemsMissing(), saved, pct, r.withinBudget(), r.budgetLimit());
                }).toList();
    }
}
