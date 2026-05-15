
### 2.6.3. Bounded Context: Store Management
El Bounded Context Store Management constituye la infraestructura operativa y el pilar de veracidad de datos dentro del ecosistema de SmartCart. Su misión estratégica es la gobernanza integral de los establecimientos comerciales y la sincronización precisa de sus inventarios, transformando la gestión manual en una ventaja competitiva digital. Este contexto opera bajo un modelo de negocio de Efficiency & Reliability, clasificándose como un dominio Core debido a que la exactitud de sus datos (precios y stock) es lo que garantiza el éxito del proceso de ahorro del usuario final.

La comunicación con otros contextos, como Shopping Journey o Experience, se realiza mediante un patrón Customer/Supplier, donde Store Management actúa como el proveedor de la "verdad de campo" (precios verificados y disponibilidad). Para proteger la integridad del dominio, se implementa una Anti-Corruption Layer (ACL) que filtra las actualizaciones masivas de inventario, asegurando que los modelos externos no contaminen la lógica de negocio interna.

#### 2.6.3.1. Domain Layer
La capa de dominio de Store Management concentra toda la lógica de negocio relacionada con la administración de locales y la gestión de catálogos. A continuación, se detallan los bloques de construcción tácticos identificados a partir del Impact Mapping y los requerimientos operativos del sistema:

La capa de dominio de **Store Management** concentra toda la lógica de negocio relacionada con la administración de locales y la gestión de catálogos. A continuación, se detallan los bloques de construcción tácticos identificados:

##### Aggregates

Un **Aggregate** es un clúster de objetos de dominio que se trata como una única unidad de consistencia.

| Aggregate Root | Descripción | Responsabilidad principal |
|---|---|---|
| `Store` | Representa la unidad física y legal del comercio en el sistema. | Garantizar la validez legal del comercio (RUC) y la precisión de sus datos operativos como ubicación y horarios de atención. |
| `Inventory` | Representa el conjunto dinámico de productos, precios y disponibilidad por local. | Mantener la consistencia atómica de precios y stock, especialmente durante procesos de actualización masiva mediante archivos externos. |

##### Entities

Las **Entities** son objetos con identidad propia que persisten a lo largo del tiempo y pueden cambiar de estado.

| Entidad | Aggregate al que pertenece | Atributos clave | Comportamientos |
|---|---|---|---|
| `Merchant` | `Store` | `merchantId`, `fullName: String`, `dni: String`, `email: String`, `lastLogin: LocalDateTime` | `updateProfile()`, `verifyIdentity()`, `trackActivity()` |
| `StoreBranch` | `Store` | `branchId`, `address: Address`, `openingHours: List<Hours>`, `isActive: boolean` | `openBranch()`, `closeBranch()`, `updateLocation()` |
| `Product` | `Inventory` | `productId`, `sku: Sku`, `name: String`, `brand: String`, `categoryId: Long` | `updateDetails()`, `assignCategory()`, `deactivate()` |
| `PriceItem` | `Inventory` | `priceId`, `amount: Money`, `isPromotional: boolean`, `expiryDate: LocalDate` | `applyClearance()`, `validateVigency()`, `markAsExpired()` |
| `StockPoint` | `Inventory` | `stockId`, `quantity: int`, `minThreshold: int`, `lastChecked: LocalDateTime` | `updateStock()`, `replenish()`, `checkLowStock()` |

##### Value Objects

Los **Value Objects** son objetos sin identidad propia que se definen únicamente por sus atributos. Son inmutables y encapsulan reglas de validación del dominio.

| Value Object | Atributos | Reglas de validación / invariantes |
|---|---|---|
| `Ruc` | `value: String` | Debe tener exactamente 11 dígitos numéricos. Lanza `InvalidRucException` si el formato legal de SUNAT no es válido. |
| `Money` | `amount: BigDecimal`, `currency: String` | El monto debe ser mayor o igual a cero. Soporta operaciones para el cálculo de ahorro neto. |
| `Address` | `street: String`, `latitude: double`, `longitude: double`, `district: String` | Las coordenadas GPS son obligatorias y deben estar dentro de rangos geográficos válidos. |
| `Sku` | `code: String` | Identificador alfanumérico único para sincronización. No puede ser nulo ni vacío. |
| `OpeningHours` | `openTime: LocalTime`, `closeTime: LocalTime`, `dayOfWeek: Day` | Invariante: La hora de cierre debe ser posterior a la de apertura. |
| `StockStatus` | `status: Enum{AVAILABLE, LOW_STOCK, OUT_OF_STOCK}` | Controla la visibilidad del producto en la vitrina digital según la cantidad actual. |

##### Domain Events

Los **Domain Events** son hechos relevantes que han ocurrido dentro del dominio y son publicados para que otros contextos puedan reaccionar.

| Domain Event | Aggregate origen | Atributos del payload | Significado de negocio |
|---|---|---|---|
| `StoreVerified` | `Store` | `storeId`, `ruc`, `merchantId`, `verifiedAt` | Un comercio ha superado las validaciones legales y puede empezar a publicar ofertas oficiales. |
| `PriceUpdated` | `Inventory` | `productId`, `storeId`, `newAmount`, `oldAmount`, `isPromotional` | Se ha detectado un cambio de precio que debe notificar a los usuarios interesados en el ahorro. |
| `InventoryBulkSyncCompleted` | `Inventory` | `inventoryId`, `storeId`, `totalItemsProcessed`, `timestamp` | Se ha completado con éxito la carga masiva de precios y stock desde un archivo externo. |
| `LowStockDetected` | `Inventory` | `productId`, `storeId`, `currentQuantity`, `sku` | La existencia de un producto ha caído por debajo del umbral mínimo configurado. |
| `ProductClearanceStarted` | `Inventory` | `productId`, `storeId`, `discountRate`, `expiryDate` | Se ha iniciado una liquidación de productos perecibles para evitar mermas en el local. |

##### Domain Services

Los **Domain Services** encapsulan lógica de negocio que no pertenece naturalmente a ningún Aggregate, ya que opera sobre múltiples objetos o requiere información de varias fuentes.

| Domain Service | Método principal | Descripción |
|---|---|---|
| `InventoryBulkProcessorService` | `process(StoreId id, DataStream source): BulkResult` | Orquesta la carga y validación de miles de registros de productos, asegurando la consistencia del `Inventory`. |
| `LegalComplianceService` | `validateRuc(Ruc ruc): VerificationStatus` | Coordina la validación del estado del comercio (Activo/Habido) interactuando con la ACL de servicios gubernamentales. |
| `PerformanceAnalyticsService` | `generateConversionMetrics(StoreId id): Report` | Analiza el impacto de los precios en las visitas reales para justificar ajustes estratégicos de mercado. |
| `StoreGeofencingService` | `isWithinOperationalRange(Address addr): boolean` | Valida si la ubicación de una sucursal se encuentra dentro de las zonas de cobertura logística permitidas. |

#### 2.6.3.2. Interface Layer

La capa de interfaz de **Store Management** expone las capacidades operativas y de gestión de comercios mediante una API REST. Esta es consumida por la **Web App** del Merchant para la administración de inventarios y por la **Mobile App** del Buyer para la consulta de productos y precios verificados. Todos los endpoints están prefijados con `/api/v1/store-management`.

##### StoreManagementController

| Método HTTP | Endpoint | Descripción | Request DTO | Response DTO |
|---|---|---|---|---|
| POST | `/stores` | Registra una nueva tienda y su Merchant administrador. | `RegisterStoreRequest` | `StoreResponse` |
| GET | `/stores/{storeId}` | Obtiene la información administrativa y estado de verificación legal (RUC). | — | `StoreProfileResponse` |
| POST | `/stores/{storeId}/inventory/bulk` | Carga masiva de productos y precios mediante archivos CSV/Excel. | `MultipartFile` | `BulkUploadResponse` |
| POST | `/stores/{storeId}/inventory/clearance` | Registra productos en liquidación por fecha de vencimiento próxima. | `CreateClearanceRequest` | `ClearanceResponse` |
| GET | `/stores/{storeId}/inventory` | Obtiene el catálogo de productos con stock y precios vigentes. | — (query params: `category`, `sku`) | `Page<ProductStockResponse>` |
| GET | `/stores/{storeId}/analytics` | Provee métricas de conversión y carritos abandonados para el Merchant. | — | `StoreAnalyticsResponse` |

##### DTOs de Request y Response

**RegisterStoreRequest**
```json
{
  "merchantId": "uuid",
  "name": "Bodega Don Carlos",
  "ruc": "20123456789",
  "address": {
    "street": "Av. Petit Thouars 123",
    "district": "Lince",
    "latitude": -12.084,
    "longitude": -77.035
  },
  "operatingHours": [
    { "day": "MONDAY", "open": "08:00", "close": "22:00" }
  ]
}
```
**BulkUploadResponse**
```json
{
  "jobId": "uuid",
  "status": "COMPLETED",
  "totalItemsProcessed": 1250,
  "errorsCount": 0,
  "timestamp": "2026-04-23T15:30:00"
}
```
**CreateClearanceRequest**
```json
{
  "productId": "uuid",
  "discountPercentage": 30,
  "expiryDate": "2026-04-30",
  "reason": "FECHA_PROXIMA_VENCIMIENTO"
}
```
**StoreAnalyticsResponse**
```json
{
  "storeId": "uuid",
  "metrics": {
    "totalViews": 1200,
    "abandonedCarts": 150,
    "conversionRate": 0.12,
    "topProducts": ["SKU-9921", "SKU-1022"]
  }
}
```

#### 2.6.3.3. Application Layer

La capa de aplicación orquesta los flujos de negocio coordinando los objetos del dominio, los repositorios y los servicios de infraestructura. Su responsabilidad es dirigir el flujo de trabajo sin contener lógica de decisión de negocio (orquestación pura).

##### Application Services

| Application Service | Responsabilidad |
|---|---|
| `StoreApplicationService` | Orquesta los procesos de registro, actualización de sucursales y validación legal ante entes gubernamentales (SUNAT). |
| `InventoryApplicationService` | Gestiona la lógica de sincronización de inventarios, procesando cargas masivas y gestionando el ciclo de vida de los precios y stock. |

##### Command Handlers

Los **Command Handlers** reciben un Command Object y ejecutan la operación de escritura correspondiente sobre el dominio.

| Command | Command Handler | Flujo de ejecución |
|---|---|---|
| `RegisterStoreCommand` | `RegisterStoreCommandHandler` | 1) Valida RUC único en el repositorio. 2) Invoca `LegalComplianceService`. 3) Crea el Aggregate `Store`. 4) Persiste en base de datos. 5) Publica el evento `StoreVerified`. |
| `ProcessBulkInventoryCommand` | `ProcessBulkInventoryCommandHandler` | 1) Recibe el archivo del Merchant. 2) Invoca `InventoryBulkProcessorService`. 3) Actualiza el Aggregate `Inventory`. 4) Publica `BulkInventoryUpdated`. |
| `ApplyProductClearanceCommand` | `ApplyProductClearanceCommandHandler` | 1) Identifica ítems próximos a vencer. 2) Llama a `priceItem.applyClearance()`. 3) Actualiza el estado promocional. 4) Publica `ClearanceSaleStarted`. |
| `UpdateStockLevelCommand` | `UpdateStockLevelCommandHandler` | 1) Recupera el `StockPoint`. 2) Ejecuta `updateQuantity()`. 3) Si la cantidad es menor al umbral, publica `LowStockDetected`. 4) Persiste los cambios. |

##### Query Handlers

Los **Query Handlers** se encargan exclusivamente de las operaciones de lectura, optimizando la respuesta hacia la interfaz (CQRS).

| Query | Query Handler | Descripción |
|---|---|---|
| `GetStoreProfileQuery` | `GetStoreProfileQueryHandler` | Recupera el perfil administrativo y legal de una tienda específica mediante un Read Model. |
| `GetInventoryByStoreQuery` | `GetInventoryByStoreQueryHandler` | Obtiene el catálogo de productos y precios optimizado para paginación y búsqueda rápida. |
| `GetStoreMetricsQuery` | `GetStoreMetricsQueryHandler` | Consulta las proyecciones de analítica para mostrar conversiones y carritos abandonados al Merchant. |

##### Integración con procesos de Inventario

El flujo de actualización de inventario permite que el Merchant mantenga su oferta competitiva de forma ágil mediante una arquitectura dirigida por eventos:

```text
[Merchant Web App] --sube archivo--> BulkInventory (REST)
        |
        v
[InventoryApplicationService]
        ├──> [InventoryBulkProcessorService] (Domain Service)
        ├──> [Inventory Aggregate] .updatePriceAndStock()
        └──> publica PriceChanged (Event) --capturado por--> [Shopping Journey]

```
**Al finalizar una carga masiva exitosa:**

* El **`InventoryApplicationService`** confirma la persistencia de los nuevos precios en el repositorio de datos.
* Se emite el evento **`PriceChanged`**, el cual es capturado asíncronamente por el contexto de **Shopping Journey** para actualizar el cálculo de ahorro en las rutas activas de los usuarios.
* Se recalcula el **`StockStatus`**, notificando mediante un servicio de mensajería a los usuarios que tengan el producto en su lista de favoritos si este vuelve a estar disponible.

#### 2.6.3.4. Infrastructure Layer
#### 2.6.3.4. Infrastructure Layer

La capa de infraestructura provee las implementaciones concretas de las interfaces definidas por el dominio (repositorios, mensajería, persistencia) y la capa anticorrupción (ACL) que aísla a **Store Management** de los modelos externos y servicios gubernamentales.

##### Repositories (Implementación)

Las interfaces de repositorio se definen en la capa de dominio y se implementan en infraestructura siguiendo el principio de inversión de dependencias.

| Interfaz (Dominio) | Implementación (Infraestructura) | Tecnología |
|---|---|---|
| `StoreRepository` | `StoreJpaRepository` | Spring Data JPA + PostgreSQL |
| `InventoryRepository` | `InventoryJpaRepository` | Spring Data JPA + PostgreSQL |
| `MerchantReadRepository` | `MerchantMongoReadRepository` | Spring Data MongoDB (Read Model optimizado para analítica) |

##### Mapeo a Base de Datos (Persistencia)

La persistencia utiliza una estrategia híbrida: **PostgreSQL** para los aggregates transaccionales (seguridad ACID para stock y precios) y **MongoDB** para los read models de alta frecuencia de lectura y analítica de conversión.

**Tabla `stores` (PostgreSQL)**
| Columna | Tipo | Descripción |
|---|---|---|
| `id` | UUID (PK) | Identificador único del aggregate `Store`. |
| `ruc` | VARCHAR(11) | Registro Único de Contribuyente (Unique). |
| `legal_status` | VARCHAR(20) | Estado: PENDING, VERIFIED, REJECTED. |
| `latitude` | DECIMAL(10, 8) | Coordenada para geolocalización de rutas. |
| `longitude` | DECIMAL(11, 8) | Coordenada para geolocalización de rutas. |
| `created_at` | TIMESTAMP | Fecha de registro en la plataforma. |

**Tabla `inventory_items` (PostgreSQL)**
| Columna | Tipo | Descripción |
|---|---|---|
| `id` | UUID (PK) | Identificador único del ítem de inventario. |
| `store_id` | UUID (FK) | Vínculo con el aggregate `Store`. |
| `sku` | VARCHAR(50) | Código único de producto para el Merchant. |
| `price_amount` | DECIMAL(10, 2) | Precio actual del producto. |
| `is_clearance` | BOOLEAN | Indica si el producto está en liquidación. |
| `expiry_date` | DATE | Fecha de vencimiento para productos perecibles. |

**Colección `store_analytics_model` (MongoDB)**
```json
{
  "_id": "uuid",
  "storeId": "uuid",
  "totalViews": 1540,
  "abandonedCartsCount": 42,
  "conversionRate": 0.12,
  "topProductSkus": ["SKU-001", "SKU-099"],
  "lastSync": "2026-04-23T18:00:00"
}
```
##### Consumer (StoreManagementEventConsumer)

El consumidor de eventos se encarga de escuchar los mensajes provenientes de otros contextos o servicios externos (como la validación de SUNAT) y delegar la ejecución a la capa de aplicación.

```java
@Component
public class StoreManagementEventConsumer {

    private final StoreApplicationService storeApplicationService;
    private final LegalComplianceACL legalComplianceACL;

    @RabbitListener(queues = "store.ruc-validation")
    public void handleRucValidationResponse(SunatResponse message) {
        // La ACL traduce la respuesta externa a un DTO interno
        LegalStatusDTO statusDto = legalComplianceACL.traducirRegistro(message);
        
        // Se despacha la acción a la capa de aplicación
        storeApplicationService.actualizarEstadoLegal(statusDto);
    }
}
```
#### Anti-Corruption Layer (ACL) — Integración con External Legal API & Merchant Systems

La **Anti-Corruption Layer** es el componente más crítico de la infraestructura. Actúa como un traductor bidireccional que convierte los modelos externos (APIs de SUNAT o archivos de Merchants) al lenguaje ubicuo propio de SmartCart, evitando que conceptos ajenos contaminen el modelo interno.

##### Estructura de la ACL

```text
[External API / Merchant File] ────────────────────────┐
        │                                              │
        ▼                                              │
┌─────────────────────────────────────────────────────┐│
│          StoreManagementACL                         ││
│                                                     ││
│  SunatStatusResponse     →  LegalStatus (Enum)      ││
│  ExternalProductRow      →  Product (Entity)        ││
│  RawCoordinate           →  Address (ValueObject)   ││
│  MerchantCatalogItem     →  Sku (ValueObject)       ││
└─────────────────────────────────────────────────────┘│
        │                                              │
        ▼                                              │
[Store Management Domain Model] ◄──────────────────────┘
```
#### Contrato de traducción del ACL

El contrato define las reglas de transformación entre los esquemas externos y el modelo de dominio interno, asegurando que el **Ubiquitous Language** de Store Management se mantenga consistente.

| Concepto Externo (SUNAT / Merchant) | Traducción Interna (Store Management) | Notas |
|:---|:---|:---|
| `ddp_numruc` / `tax_id` | `Ruc` (Value Object) | Valida formato de 11 dígitos antes de crear el objeto. |
| `desc_estado` (ACTIVO/HABIDO) | `legalStatus` (Enum) | Mapea estados externos al lenguaje de verificación interno. |
| `item_sku_code` | `Sku` (Value Object) | Normaliza el código para asegurar unicidad en el inventario. |
| `raw_lat` / `raw_lng` | `Address` (Value Object) | Convierte coordenadas a dobles precisos para el motor de rutas. |

#### Implementación del ACL

La implementación técnica utiliza el patrón **Adapter** para transformar las respuestas de la API de SUNAT en objetos de transferencia de datos (DTOs) que la capa de aplicación pueda procesar.

```java
@Component
public class StoreManagementACL {

    /**
     * Traduce la respuesta técnica de SUNAT al lenguaje del dominio de SmartCart.
     * @param msg Respuesta cruda de la API externa.
     * @return DTO con información normalizada.
     */
    public StoreProfileDTO traducirRegistro(SunatResponse msg) {
        return StoreProfileDTO.builder()
            .ruc(new Ruc(msg.getDdpNumruc()))
            .name(msg.getDdpNombre().trim())
            .status(mapLegalStatus(msg.getDescEstado(), msg.getDescCondicion()))
            .lastVerified(LocalDateTime.now(ZoneId.of("America/Lima")))
            .build();
    }

    /**
     * Lógica de mapeo para proteger el dominio de cambios en la API de SUNAT.
     */
    private LegalStatus mapLegalStatus(String estado, String condicion) {
        return ("ACTIVO".equalsIgnoreCase(estado) && "HABIDO".equalsIgnoreCase(condicion)) 
            ? LegalStatus.VERIFIED 
            : LegalStatus.REJECTED;
    }
}
``` 
> **Decisión de diseño: El ACL se implementa exclusivamente en la capa de infraestructura, garantizando que ninguna dependencia de modelos externos (como el esquema de la SUNAT) cruce la frontera hacia el dominio. Ante cualquier cambio del proveedor, solo el ACL requiere modificación, protegiendo la integridad del sistema ante la inestabilidad de servicios externos.


### 2.6.3.5. Bounded Context Software Architecture Component Level Diagrams

En esta sección se presentan los diagramas de nivel componente que ilustran la arquitectura de software del contexto de **Store Management**. Se detalla la interacción entre los controladores de API que atienden al Merchant, los servicios de aplicación encargados de orquestar la carga masiva de inventarios (Bulk Load) y los componentes de infraestructura que gestionan la persistencia transaccional. Asimismo, se muestra la integración con la **Anti-Corruption Layer (ACL)** para la validación de datos externos de la SUNAT y la comunicación asíncrona mediante el broker de mensajería para notificar cambios de precios al sistema de optimización de rutas.

### 2.6.3.6. Bounded Context Software Architecture Code Level Diagrams

En esta sección se presentan los diagramas de nivel código que detallan la estructura interna y la implementación técnica del contexto de **Store Management**. Estos diagramas reflejan la transición del modelo conceptual a la construcción del software, integrando los principios de **Domain-Driven Design (DDD)** para garantizar que la lógica de negocio, centrada en el cumplimiento legal del comercio y la integridad del stock, se mantenga aislada de las preocupaciones tecnológicas y de persistencia.
> ![Diagrama de Componentes - Store Management](assets/diagramas/Diagrama_Component_Store_Management.png)
#### 2.6.3.6.1. Bounded Context Domain Layer Class Diagrams

El diagrama de clases del **Domain Layer** del contexto de **Store Management** ilustra los agregados, entidades y objetos de valor que constituyen el núcleo del negocio. Se muestran las relaciones de composición dentro de los agregados de `Store` e `Inventory`, definiendo los límites de consistencia para el registro de comercios, la gestión de horarios de atención y la actualización de catálogos. Además, se detallan los métodos de negocio encargados de aplicar reglas críticas, como la normalización de SKUs, la validación de formatos de RUC y la lógica de liquidación para productos perecibles.

> ![Diagrama de Clases - Store Management](assets/diagramas/Diagrama_Clases_Store_Management.png)

#### 2.6.3.6.2. Bounded Context Database Design Diagram

El diagrama de diseño de base de datos del contexto de **Store Management** muestra la estructura de las tablas y sus restricciones de integridad en el motor relacional **PostgreSQL**. Se detallan las tablas principales que soportan el ciclo de vida del Merchant, las relaciones de clave foránea que vinculan los locales con sus respectivos ítems de inventario y la tabla de horarios de operación. Este diseño físico incluye índices espaciales para la ubicación geográfica de las tiendas y garantiza la consistencia ACID necesaria para manejar actualizaciones masivas de precios y disponibilidad de stock en tiempo real.

> ![Diagrama de Base de Datos - Store Management](assets/diagramas/Diagrama_Database_Store_Management.png)