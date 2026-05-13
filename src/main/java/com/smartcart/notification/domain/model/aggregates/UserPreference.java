package com.smartcart.notification.domain.model.aggregates;

import com.smartcart.notification.domain.model.entities.ChannelPreference;
import com.smartcart.notification.domain.model.valueobjects.ChannelType;
import com.smartcart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Aggregate root: communication preferences for a user (channels + silence window).
 */
@Getter
@Setter
@Entity
@Table(name = "user_preferences", uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
public class UserPreference extends AuditableAbstractAggregateRoot<UserPreference> {

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "silence_window_start")
    private LocalTime silenceWindowStart;

    @Column(name = "silence_window_end")
    private LocalTime silenceWindowEnd;

    @OneToMany(mappedBy = "userPreference", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelPreference> channels = new ArrayList<>();

    public UserPreference() {
    }

    public UserPreference(Long userId) {
        this.userId = userId;
    }

    public void replaceChannels(List<ChannelPreference> newChannels) {
        channels.clear();
        for (ChannelPreference c : newChannels) {
            c.setUserPreference(this);
            channels.add(c);
        }
    }

    public Optional<ChannelPreference> findChannel(ChannelType type) {
        return channels.stream().filter(c -> c.getChannelType() == type).findFirst();
    }

    public boolean isChannelEnabled(ChannelType type) {
        return findChannel(type).map(ChannelPreference::isEnabled).orElse(false);
    }
}
