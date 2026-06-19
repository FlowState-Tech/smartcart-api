package com.smartcart.shoppingplanning.application.internal.commandservices.shoppinglist;

import com.smartcart.shared.domain.exceptions.ResourceNotFoundException;
import com.smartcart.shoppingplanning.domain.model.aggregates.ShoppingList;
import com.smartcart.shoppingplanning.domain.model.commands.AddItemToListCommand;
import com.smartcart.shoppingplanning.domain.model.commands.ApplyFamilyBasketCommand;
import com.smartcart.shoppingplanning.domain.events.ShoppingListCreatedEvent;
import com.smartcart.shoppingplanning.domain.events.ProductAddedToBasketEvent;
import com.smartcart.shoppingplanning.domain.model.commands.CreateShoppingListCommand;
import com.smartcart.shoppingplanning.domain.model.commands.RemoveItemFromListCommand;
import com.smartcart.shoppingplanning.domain.model.entities.ShoppingListItem;
import com.smartcart.shoppingplanning.domain.services.shoppinglist.ShoppingListCommandService;
import com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories.ShoppingListRepository;
import com.smartcart.shoppingplanning.infrastructure.persistence.jpa.repositories.ShoppingPreferencesRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ShoppingListCommandServiceImpl implements ShoppingListCommandService {

    private final ShoppingListRepository listRepository;
    private final ShoppingPreferencesRepository preferencesRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ShoppingListCommandServiceImpl(ShoppingListRepository listRepository,
                                          ShoppingPreferencesRepository preferencesRepository,
                                          ApplicationEventPublisher eventPublisher) {
        this.listRepository = listRepository;
        this.preferencesRepository = preferencesRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ShoppingList handle(CreateShoppingListCommand command) {
        var saved = listRepository.save(new ShoppingList(command.buyerId(), command.name()));
        eventPublisher.publishEvent(new ShoppingListCreatedEvent(
                saved.getId(), saved.getBuyerId(), java.time.Instant.now()));
        return saved;
    }

    @Override
    @Transactional
    public ShoppingList handle(AddItemToListCommand command) {
        var list = findList(command.listId());
        list.addItem(new ShoppingListItem(command.sku(), command.productName(), command.quantity(), command.unit()));
        var saved = listRepository.save(list);
        eventPublisher.publishEvent(new ProductAddedToBasketEvent(
                saved.getId(), command.sku(), command.quantity()));
        return saved;
    }

    @Override
    @Transactional
    public ShoppingList handle(RemoveItemFromListCommand command) {
        var list = findList(command.listId());
        list.removeItem(command.itemId());
        return listRepository.save(list);
    }

    @Override
    @Transactional
    public ShoppingList handle(ApplyFamilyBasketCommand command) {
        var list = findList(command.listId());
        if (!list.getBuyerId().equals(command.buyerId())) {
            throw new IllegalArgumentException("List does not belong to buyer");
        }
        var prefs = preferencesRepository.findByBuyerId(command.buyerId())
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingPreferences", command.buyerId()));
        var existingSkus = list.getItems().stream().map(ShoppingListItem::getSku)
                .collect(java.util.stream.Collectors.toSet());
        for (String sku : prefs.getFamilyBasketSkus()) {
            if (!existingSkus.contains(sku)) {
                list.addItem(new ShoppingListItem(sku, "Canasta familiar " + sku, BigDecimal.ONE, "und"));
            }
        }
        return listRepository.save(list);
    }

    private ShoppingList findList(Long listId) {
        return listRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingList", listId));
    }
}
