package com.smartcart.iam.domain.model.entities;

import com.smartcart.iam.domain.model.valueobjects.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import java.util.List;

/**
 * Role Entity
 * Represents the role of a user in the system for access control (RBAC).
 * Linked to the 'roles' table in the single database.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    private Roles name;

    /**
     * Constructor to initialize a role by its name from the Enum.
     */
    public Role(Roles name) {
        this.name = name;
    }

    /**
     * Returns the role name as a string.
     * Useful for Spring Security integration.
     */
    public String getStringName() {
        return name.name();
    }

    /**
     * Defines the default role of the system.
     * Assigns ROLE_CUSTOMER for security to avoid accidental administrative access.
     */
    public static Role getDefaultRole() {
        return new Role(Roles.ROLE_CUSTOMER);
    }

    /**
     * Converts a String to a Role instance.
     * @param name Role name (e.g., "ROLE_MERCHANT")
     */
    public static Role toRoleFromName(String name) {
        return new Role(Roles.valueOf(name));
    }

    /**
     * Validates a list of roles. If the list is null or empty,
     * automatically assigns the default role (Customer).
     */
    public static List<Role> validateRoleSet(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of(getDefaultRole());
        }
        return roles;
    }
}