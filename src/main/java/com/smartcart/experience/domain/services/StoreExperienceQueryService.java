package com.smartcart.experience.domain.services;

import com.smartcart.experience.domain.model.entities.PriceError;
import com.smartcart.experience.domain.model.entities.Rating;
import com.smartcart.experience.domain.model.entities.Review;
import com.smartcart.experience.domain.model.entities.Savings;
import com.smartcart.experience.domain.model.entities.StoreExperience;
import com.smartcart.experience.domain.model.queries.*;
import com.smartcart.experience.infrastructure.persistence.jpa.repositories.StoreExperienceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StoreExperienceQueryService {

    private final StoreExperienceRepository repository;

    public StoreExperienceQueryService(StoreExperienceRepository repository) {
        this.repository = repository;
    }

    public List<Review> handle(GetPublishedReviewsQuery query) {
        return repository.findByStoreId(query.storeId()).stream()
                .flatMap(e -> e.getReviews().stream())
                .filter(r -> "PUBLICADA".equals(r.getEstadoPublicacion()))
                .sorted(Comparator.comparing(Review::getFechaCreacion).reversed())
                .skip((long) query.page() * query.size())
                .limit(query.size())
                .toList();
    }

    public Optional<Savings> handle(GetSavingsByJourneyQuery query) {
        return repository.findByRecorridoId(query.recorridoId()).map(StoreExperience::getSavings);
    }

    public Optional<TrustProfile> handle(GetTrustProfileQuery query) {
        var experiences = repository.findByStoreId(query.storeId());
        if (experiences.isEmpty()) {
            return Optional.empty();
        }
        var ratings = experiences.stream().flatMap(e -> e.getRatings().stream()).toList();
        var confirmedErrors = experiences.stream()
                .flatMap(e -> e.getPriceErrors().stream())
                .filter(pe -> "CONFIRMADO".equals(pe.getEstadoError()))
                .count();
        double avg = ratings.stream().mapToInt(Rating::getPuntuacion).average().orElse(0);
        var badges = new ArrayList<String>();
        if (avg >= 4.5) badges.add("TOP_RATED");
        if (confirmedErrors == 0 && !ratings.isEmpty()) badges.add("PRICE_ACCURATE");
        return Optional.of(new TrustProfile(query.storeId(), avg, ratings.size(),
                (int) confirmedErrors, badges, LocalDateTime.now()));
    }

    public List<Rating> handle(GetAllRatingsQuery query) {
        return repository.findByStoreId(query.storeId()).stream()
                .flatMap(e -> e.getRatings().stream())
                .sorted(Comparator.comparing(Rating::getFechaRegistro).reversed())
                .toList();
    }

    public List<Review> handle(GetAllReviewsQuery query) {
        return repository.findByStoreId(query.storeId()).stream()
                .flatMap(e -> e.getReviews().stream())
                .sorted(Comparator.comparing(Review::getFechaCreacion).reversed())
                .skip((long) query.page() * query.size())
                .limit(query.size())
                .toList();
    }

    public List<PriceError> handle(GetPriceErrorsByStoreQuery query) {
        return repository.findByStoreId(query.storeId()).stream()
                .flatMap(e -> e.getPriceErrors().stream())
                .sorted(Comparator.comparing(PriceError::getFechaReporte).reversed())
                .toList();
    }

    public record TrustProfile(
            String storeId,
            Double trustScore,
            Integer totalCalificaciones,
            Integer erroresDePrecioConfirmados,
            List<String> insignias,
            LocalDateTime ultimaActualizacion
    ) {}
}
