package com.smartcart.storemanagement.interfaces.rest.transform;

import com.smartcart.storemanagement.infrastructure.persistence.jpa.readmodel.StoreAnalyticsReadModel;
import com.smartcart.storemanagement.interfaces.rest.resources.StoreAnalyticsResponse;

public class StoreAnalyticsResponseFromReadModelAssembler {

    public static StoreAnalyticsResponse toResource(StoreAnalyticsReadModel model) {
        var metrics = new StoreAnalyticsResponse.Metrics(
                model.getTotalViews(),
                model.getAbandonedCarts(),
                model.getConversionRate(),
                model.getTopProductSkus()
        );
        return new StoreAnalyticsResponse(model.getStoreId(), metrics);
    }
}

