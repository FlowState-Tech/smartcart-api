package com.smartcart.storemanagement.domain.model.commands;

import com.smartcart.storemanagement.domain.model.valueobjects.Address;
import com.smartcart.storemanagement.domain.model.valueobjects.OpeningHours;

import java.util.List;

public record RegisterStoreCommand(String merchantId,
                                   String name,
                                   String ruc,
                                   Address address,
                                   List<OpeningHours> openingHours) {
}

