package com.smartcart.storemanagement.domain.services;

import com.smartcart.storemanagement.domain.model.entities.PriceItem;
import com.smartcart.storemanagement.domain.model.entities.Product;
import com.smartcart.storemanagement.domain.model.entities.StockPoint;

public record InventoryItemResult(Product product,
                                  PriceItem priceItem,
                                  StockPoint stockPoint) {
}

