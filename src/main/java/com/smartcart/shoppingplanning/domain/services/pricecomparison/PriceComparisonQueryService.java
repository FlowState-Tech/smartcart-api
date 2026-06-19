package com.smartcart.shoppingplanning.domain.services.pricecomparison;

import com.smartcart.shoppingplanning.domain.model.queries.GetPriceProjectionsQuery;
import com.smartcart.shoppingplanning.domain.model.queries.GetSubstitutesQuery;
import com.smartcart.shoppingplanning.domain.model.queries.GetTotalCostQuery;
import com.smartcart.shoppingplanning.domain.model.queries.LookupBarcodeQuery;
import com.smartcart.shoppingplanning.domain.model.queries.VerifyStockQuery;
import com.smartcart.shoppingplanning.domain.model.valueobjects.BarcodeLookupResult;
import com.smartcart.shoppingplanning.domain.model.valueobjects.PriceComparisonResult;
import com.smartcart.shoppingplanning.domain.model.valueobjects.SubstituteSuggestion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PriceComparisonQueryService {
    List<PriceComparisonResult> handle(GetPriceProjectionsQuery query);
    Optional<BigDecimal> handle(GetTotalCostQuery query);
    List<String> handle(VerifyStockQuery query);
    Optional<SubstituteSuggestion> handle(GetSubstitutesQuery query);
    List<BarcodeLookupResult> handle(LookupBarcodeQuery query);
    List<SubstituteSuggestion> handleAllSubstitutes(VerifyStockQuery query);
}
