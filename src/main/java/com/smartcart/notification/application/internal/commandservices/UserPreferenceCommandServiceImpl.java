package com.smartcart.notification.application.internal.commandservices;

import com.smartcart.notification.domain.model.aggregates.UserPreference;
import com.smartcart.notification.domain.model.commands.UpdatePreferencesCommand;
import com.smartcart.notification.domain.model.entities.ChannelPreference;
import com.smartcart.notification.domain.services.UserPreferenceCommandService;
import com.smartcart.notification.infrastructure.persistence.jpa.repositories.UserPreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserPreferenceCommandServiceImpl implements UserPreferenceCommandService {

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreferenceCommandServiceImpl(UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
    }

    @Override
    @Transactional
    public Optional<UserPreference> handle(UpdatePreferencesCommand command) {
        UserPreference pref = userPreferenceRepository.findByUserId(command.userId())
                .orElseGet(() -> new UserPreference(command.userId()));
        pref.setUserId(command.userId());
        pref.setSilenceWindowStart(command.silenceWindowStart());
        pref.setSilenceWindowEnd(command.silenceWindowEnd());
        List<ChannelPreference> list = new ArrayList<>();
        for (UpdatePreferencesCommand.ChannelPreferenceItem item : command.channels()) {
            list.add(new ChannelPreference(item.tipo(), item.estaHabilitado(), item.tokenContacto()));
        }
        pref.replaceChannels(list);
        return Optional.of(userPreferenceRepository.save(pref));
    }
}
