package com.smartcart.experience.infrastructure.acl;

import com.smartcart.shared.infrastructure.events.RecorridoFinalizadoIntegrationEvent;
import org.springframework.stereotype.Component;

/**
 * ACL — Customer/Supplier: Shopping Journey (U) → Experience (D).
 * Traduce el evento de recorrido finalizado al lenguaje del dominio de experiencia.
 * Reporte 2.5.2 §5 y 2.6.7.3.
 */
@Component
public class ShoppingJourneyACL {

    public RecorridoFinalizadoSnapshot translate(RecorridoFinalizadoIntegrationEvent event) {
        return new RecorridoFinalizadoSnapshot(
                event.recorridoId(),
                event.buyerId(),
                event.storeId() != null ? String.valueOf(event.storeId()) : null,
                event.listId());
    }

    public record RecorridoFinalizadoSnapshot(
            String recorridoId,
            String buyerId,
            String storeId,
            Long listId
    ) {
        public boolean hasStore() {
            return storeId != null && !storeId.isBlank();
        }
    }
}
