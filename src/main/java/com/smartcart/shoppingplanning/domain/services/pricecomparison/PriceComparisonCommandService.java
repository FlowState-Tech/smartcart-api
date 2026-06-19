package com.smartcart.shoppingplanning.domain.services.pricecomparison;

import com.smartcart.shoppingplanning.domain.model.commands.CompareBasketCommand;
import com.smartcart.shoppingplanning.domain.model.valueobjects.CompareBasketResult;

public interface PriceComparisonCommandService {
    CompareBasketResult handle(CompareBasketCommand command);
}
