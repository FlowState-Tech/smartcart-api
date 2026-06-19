package com.smartcart.experience.domain.services;

import com.smartcart.experience.domain.model.commands.*;
import com.smartcart.experience.domain.model.entities.*;
import com.smartcart.experience.domain.model.queries.*;
import com.smartcart.experience.infrastructure.persistence.jpa.repositories.StoreExperienceRepository;
import com.smartcart.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class StoreExperienceCommandService {

    private static final Set<String> BANNED_WORDS = Set.of("spam", "offensive");

    private final StoreExperienceRepository repository;

    public StoreExperienceCommandService(StoreExperienceRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<Rating> handle(RateStoreCommand command) {
        validateScore(command.puntuacion());
        var experience = findOrCreateExperience(command.storeId(), command.buyerId(), command.recorridoId());
        var rating = new Rating(UUID.randomUUID().toString(), command.storeId(), command.buyerId(), command.puntuacion());
        rating.setStoreExperience(experience);
        experience.getRatings().add(rating);
        repository.save(experience);
        return Optional.of(rating);
    }

    @Transactional
    public Optional<Review> handle(PostReviewCommand command) {
        validateReviewContent(command.comentario());
        var experience = findOrCreateExperience(command.storeId(), command.buyerId(), command.recorridoId());
        var review = new Review(UUID.randomUUID().toString(), command.storeId(), command.buyerId(), command.comentario());
        if (containsBannedWord(command.comentario())) {
            review.markForReview();
        } else {
            review.publish();
        }
        review.setStoreExperience(experience);
        experience.getReviews().add(review);
        repository.save(experience);
        return Optional.of(review);
    }

    @Transactional
    public Optional<PriceError> handle(ReportPriceErrorCommand command) {
        var experience = findOrCreateExperience(command.storeId(), command.buyerId(), command.recorridoId());
        var error = new PriceError(UUID.randomUUID().toString(), command.storeId(), command.productoId(),
                command.precioDigital(), command.precioFisico(), command.moneda());
        error.setStoreExperience(experience);
        experience.getPriceErrors().add(error);
        repository.save(experience);
        return Optional.of(error);
    }

    @Transactional
    public Optional<Savings> handle(CalculateSavingsCommand command) {
        var experience = repository.findByRecorridoId(command.recorridoId())
                .orElseGet(() -> findOrCreateExperience("unknown", command.buyerId(), command.recorridoId()));
        if (experience.getSavings() != null) {
            return Optional.of(experience.getSavings());
        }
        var savings = new Savings(UUID.randomUUID().toString(), command.recorridoId(), command.buyerId(),
                command.precioReferencia(), command.precioPagado(), command.moneda());
        savings.setStoreExperience(experience);
        experience.setSavings(savings);
        repository.save(experience);
        return Optional.of(savings);
    }

    @Transactional
    public Optional<Review> handle(ReplyReviewCommand command) {
        for (var experience : repository.findByStoreId(command.storeId())) {
            for (var review : experience.getReviews()) {
                if (review.getId().equals(command.reviewId())) {
                    review.addReply(command.respuesta(), command.merchantId());
                    repository.save(experience);
                    return Optional.of(review);
                }
            }
        }
        throw new ResourceNotFoundException("Review", command.reviewId());
    }

    @Transactional
    public Optional<PriceError> handle(ConfirmPriceErrorCommand command) {
        for (var experience : repository.findByStoreId(command.storeId())) {
            for (var error : experience.getPriceErrors()) {
                if (error.getId().equals(command.priceErrorId())) {
                    if ("CONFIRMADO".equalsIgnoreCase(command.estado())) {
                        error.confirm();
                    } else if ("RECHAZADO".equalsIgnoreCase(command.estado())) {
                        error.reject();
                    } else {
                        throw new IllegalArgumentException("Estado must be CONFIRMADO or RECHAZADO");
                    }
                    repository.save(experience);
                    return Optional.of(error);
                }
            }
        }
        throw new ResourceNotFoundException("PriceError", command.priceErrorId());
    }

    private StoreExperience findOrCreateExperience(String storeId, String buyerId, String recorridoId) {
        return repository.findByRecorridoId(recorridoId)
                .orElseGet(() -> repository.save(new StoreExperience(
                        UUID.randomUUID().toString(), storeId, buyerId, recorridoId)));
    }

    private void validateScore(Integer score) {
        if (score == null || score < 1 || score > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5");
        }
    }

    private void validateReviewContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Review content is required");
        }
        if (content.length() > 1000) {
            throw new IllegalArgumentException("Review content exceeds 1000 characters");
        }
    }

    private boolean containsBannedWord(String content) {
        var lower = content.toLowerCase(Locale.ROOT);
        return BANNED_WORDS.stream().anyMatch(lower::contains);
    }
}
