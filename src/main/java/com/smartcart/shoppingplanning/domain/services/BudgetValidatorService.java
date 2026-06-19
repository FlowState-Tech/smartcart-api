package com.smartcart.shoppingplanning.domain.services;

import com.smartcart.shoppingplanning.domain.model.valueobjects.Budget;
import com.smartcart.shoppingplanning.domain.model.valueobjects.PriceComparisonResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BudgetValidatorService {

    public boolean verifyFeasibility(PriceComparisonResult projection, Budget budget) {
        if (budget == null) return true;
        return projection.totalCost().compareTo(budget.getAmount()) <= 0;
    }

    public boolean isWithinBudget(BigDecimal totalCost, Budget budget) {
        if (budget == null) return true;
        return totalCost.compareTo(budget.getAmount()) <= 0;
    }
}
