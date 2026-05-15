package com.smartcart.storemanagement.interfaces.rest.transform;

import com.smartcart.storemanagement.domain.model.entities.PriceItem;
import com.smartcart.storemanagement.domain.model.entities.Product;
import com.smartcart.storemanagement.domain.model.entities.StockPoint;
import com.smartcart.storemanagement.interfaces.rest.resources.ProductStockResponse;

public class ProductStockResponseFromEntityAssembler {

    public static ProductStockResponse toResource(Product product, PriceItem priceItem, StockPoint stockPoint) {
        var amount = priceItem != null ? priceItem.getAmount() : null;
        var currency = amount != null ? amount.getCurrency() : null;
        var price = amount != null ? amount.getAmount() : null;
        var promotional = priceItem != null && priceItem.isPromotional();
        var expiryDate = priceItem != null ? priceItem.getExpiryDate() : null;
        var quantity = stockPoint != null ? stockPoint.getQuantity() : 0;
        var minThreshold = stockPoint != null ? stockPoint.getMinThreshold() : 0;
        return new ProductStockResponse(
                product.getSku().getCode(),
                product.getName(),
                product.getBrand(),
                product.getCategoryId(),
                product.isActive(),
                price,
                currency,
                promotional,
                expiryDate,
                quantity,
                minThreshold
        );
    }
}

