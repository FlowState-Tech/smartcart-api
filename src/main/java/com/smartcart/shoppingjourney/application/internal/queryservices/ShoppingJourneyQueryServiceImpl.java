package com.smartcart.shoppingjourney.application.internal.queryservices;

import com.smartcart.shoppingjourney.domain.model.aggregates.ShoppingRoute;
import com.smartcart.shoppingjourney.domain.model.queries.FindRoutesQuery;
import com.smartcart.shoppingjourney.domain.model.queries.GetRouteQuery;
import com.smartcart.shoppingjourney.domain.services.ShoppingJourneyQueryService;
import com.smartcart.shoppingjourney.infrastructure.persistence.jpa.repositories.ShoppingRouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ShoppingJourneyQueryServiceImpl implements ShoppingJourneyQueryService {

    private final ShoppingRouteRepository routeRepository;

    public ShoppingJourneyQueryServiceImpl(ShoppingRouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public Optional<ShoppingRoute> handle(GetRouteQuery query) {
        return routeRepository.findById(query.routeId());
    }

    @Override
    public List<ShoppingRoute> handle(FindRoutesQuery query) {
        if (query.buyerId() != null && query.listId() != null) {
            return routeRepository.findByBuyerIdAndListIdOrderByIdDesc(query.buyerId(), query.listId());
        }
        if (query.buyerId() != null) {
            return routeRepository.findByBuyerIdOrderByIdDesc(query.buyerId());
        }
        return List.of();
    }
}
