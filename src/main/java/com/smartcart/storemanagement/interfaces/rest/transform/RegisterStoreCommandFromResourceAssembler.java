package com.smartcart.storemanagement.interfaces.rest.transform;

import com.smartcart.storemanagement.domain.model.commands.RegisterStoreCommand;
import com.smartcart.storemanagement.domain.model.valueobjects.Address;
import com.smartcart.storemanagement.domain.model.valueobjects.OpeningHours;
import com.smartcart.storemanagement.interfaces.rest.resources.RegisterStoreRequest;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class RegisterStoreCommandFromResourceAssembler {

    public static RegisterStoreCommand toCommandFromResource(RegisterStoreRequest resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Register store request is required");
        }
        var address = new Address(
                resource.address().street(),
                resource.address().district(),
                resource.address().latitude(),
                resource.address().longitude()
        );
        List<OpeningHours> hours = resource.operatingHours().stream()
                .map(RegisterStoreCommandFromResourceAssembler::toOpeningHours)
                .toList();
        return new RegisterStoreCommand(
                resource.merchantId(),
                resource.name(),
                resource.ruc(),
                address,
                hours
        );
    }

    private static OpeningHours toOpeningHours(RegisterStoreRequest.OperatingHoursResource resource) {
        var day = DayOfWeek.valueOf(resource.day().trim().toUpperCase());
        var open = LocalTime.parse(resource.open());
        var close = LocalTime.parse(resource.close());
        return new OpeningHours(day, open, close);
    }
}

