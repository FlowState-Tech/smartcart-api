package com.smartcart.notification.domain.model.entities;

import com.smartcart.notification.domain.model.aggregates.UserPreference;
import com.smartcart.notification.domain.model.valueobjects.ChannelType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Per-channel opt-in and contact token for {@link UserPreference}.
 */
@Getter
@Setter
@Entity
@Table(name = "notification_channel_preferences",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_preference_id", "channel_type"}))
public class ChannelPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_preference_id", nullable = false)
    private UserPreference userPreference;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "channel_type", nullable = false, length = 20)
    private ChannelType channelType;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "contact_token", length = 2048)
    private String contactToken;

    public ChannelPreference() {
    }

    public ChannelPreference(ChannelType channelType, boolean enabled, String contactToken) {
        this.channelType = channelType;
        this.enabled = enabled;
        this.contactToken = contactToken;
    }
}
