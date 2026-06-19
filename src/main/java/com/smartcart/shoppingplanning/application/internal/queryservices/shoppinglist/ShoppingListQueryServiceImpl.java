package com.smartcart.shoppingplanning.application.internal.queryservices.shoppinglist;

import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingList;
import com.smartcart.shoppingplanning.domain.model.queries.GetShoppingListsByBuyerQuery;
import com.smartcart.shoppingplanning.domain.model.queries.GetShoppingListQuery;
import com.smartcart.shoppingplanning.domain.services.shoppinglist.ShoppingListQueryService;
import com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories.ShoppingListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ShoppingListQueryServiceImpl implements ShoppingListQueryService {

    private final ShoppingListRepository listRepository;

    public ShoppingListQueryServiceImpl(ShoppingListRepository listRepository) {
        this.listRepository = listRepository;
    }

    @Override
    public Optional<ShoppingList> handle(GetShoppingListQuery query) {
        return listRepository.findById(query.listId());
    }

    @Override
    public List<ShoppingList> handle(GetShoppingListsByBuyerQuery query) {
        return listRepository.findByBuyerId(query.buyerId());
    }
}
