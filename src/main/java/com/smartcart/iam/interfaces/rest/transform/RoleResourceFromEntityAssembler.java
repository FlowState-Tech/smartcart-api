package com.smartcart.iam.interfaces.rest.transform;

import com.smartcart.iam.domain.model.entities.Role;
import com.smartcart.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role role) {
        return new RoleResource(role.getId(), role.getStringName());
    }
}