package com.smartcart.experience.interfaces.rest.transform;

import com.smartcart.experience.domain.services.StoreExperienceQueryService.TrustProfile;
import com.smartcart.experience.interfaces.rest.resources.TrustProfileResponse;

public class TrustProfileResponseFromEntityAssembler {

    public static TrustProfileResponse toResourceFromEntity(TrustProfile profile) {
        return new TrustProfileResponse(
                profile.storeId(),
                profile.trustScore(),
                profile.totalCalificaciones(),
                profile.erroresDePrecioConfirmados(),
                profile.insignias(),
                profile.ultimaActualizacion()
        );
    }
}
