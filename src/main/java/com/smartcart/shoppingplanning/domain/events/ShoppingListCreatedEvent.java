package com.smartcart.shoppingplanning.domain.events;

import java.time.Instant;

public record ShoppingListCreatedEvent(Long listId, Long buyerId, Instant occurredAt) {}
