package com.smartcart.shoppingjourney.domain.services;

import com.smartcart.storemanagement.domain.model.aggregates.Store;
import com.smartcart.storemanagement.infrastructure.persistence.jpa.repositories.StoreRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Optional;

@Service
public class StoreHoursService {

    private final StoreRepository storeRepository;

    public StoreHoursService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public boolean isStoreOpen(Long storeId) {
        return storeRepository.findById(storeId)
                .map(this::isOpenNow)
                .orElse(false);
    }

    public Optional<Long> findNearestOpen24hAlternative(Long closedStoreId) {
        var closed = storeRepository.findById(closedStoreId).orElse(null);
        if (closed == null || closed.getBranches().isEmpty()) return Optional.empty();
        var ref = closed.getBranches().getFirst().getAddress();
        return storeRepository.findAll().stream()
                .filter(s -> !s.getId().equals(closedStoreId))
                .filter(this::isOpen24h)
                .min(Comparator.comparingDouble(s -> distance(ref.getLatitude(), ref.getLongitude(),
                        s.getBranches().getFirst().getAddress().getLatitude(),
                        s.getBranches().getFirst().getAddress().getLongitude())))
                .map(Store::getId);
    }

    private boolean isOpenNow(Store store) {
        if (store.getBranches().isEmpty()) return false;
        var branch = store.getBranches().getFirst();
        if (!branch.isActive()) return false;
        if (isOpen24h(store)) return true;
        var now = LocalTime.now();
        var today = DayOfWeek.from(java.time.LocalDate.now());
        return branch.getOpeningHours().stream()
                .anyMatch(h -> h.getDayOfWeek() == today
                        && !now.isBefore(h.getOpenTime())
                        && now.isBefore(h.getCloseTime()));
    }

    private boolean isOpen24h(Store store) {
        if (store.getBranches().isEmpty()) return false;
        return store.getBranches().getFirst().getOpeningHours().stream()
                .anyMatch(h -> h.getOpenTime().equals(LocalTime.MIDNIGHT)
                        && h.getCloseTime().equals(LocalTime.of(23, 59)));
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        return GeofencingService.haversineMeters(lat1, lon1, lat2, lon2);
    }
}
