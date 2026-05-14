package com.smartcart.iam.interfaces.rest.transform;

import com.smartcart.iam.domain.model.aggregates.User;
import com.smartcart.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(user.getId(), user.getUsername(), token);
    }
}
