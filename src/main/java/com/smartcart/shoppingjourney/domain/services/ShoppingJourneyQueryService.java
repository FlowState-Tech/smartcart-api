package com.smartcart.shoppingjourney.domain.services;

import com.smartcart.shoppingjourney.domain.model.aggregates.ShoppingRoute;
import com.smartcart.shoppingjourney.domain.model.queries.FindRoutesQuery;
import com.smartcart.shoppingjourney.domain.model.queries.GetRouteQuery;

import java.util.List;
import java.util.Optional;

public interface ShoppingJourneyQueryService {
    Optional<ShoppingRoute> handle(GetRouteQuery query);
    List<ShoppingRoute> handle(FindRoutesQuery query);
}
