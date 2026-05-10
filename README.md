# Investment Tracking — Backend

## Resumen
Backend en Java Spring Boot para el seguimiento de carteras de inversión.
Proporciona una API REST para gestionar usuarios, categorías, ítems y transacciones.
Persistencia con JPA y MariaDB (en tests se usa H2 en memoria).
Seguridad basada en JWT con Spring Security.

---

## Requisitos
- JDK 17 o superior
- Maven (se recomienda usar el wrapper `./mvnw` incluido)
- MariaDB en `localhost:3306` (solo para ejecutar la aplicación; los tests usan H2)

Comprobar Java:
```
java -version
```

---

## Estructura del proyecto

- `src/main/java/.../model` — entidades JPA (`User`, `Category`, `Item`, `Transaction`)
- `src/main/java/.../repository` — interfaces `JpaRepository`
- `src/main/java/.../service` — lógica de negocio (`UserService`, `ItemService`, `TransactionService`, `CategoryService`, `PriceService`)
- `src/main/java/.../controller` — controladores REST + `GlobalExceptionHandler`
- `src/main/java/.../dto` — objetos de transferencia (`UserDTO`, `ItemDTO`, `TransactionDTO`, `CategoryDTO`, `RegisterRequest`)
- `src/main/java/.../security` — JWT, filtros y configuración de seguridad
- `src/main/resources` — `application.properties`, `schema.sql`
- `src/test/java` — pruebas unitarias, de integración y E2E

---

## Base de datos — Tablas

### users
| Columna | Tipo | Restricciones |
|---|---:|---|
| id | BIGINT | PK, auto-increment |
| username | VARCHAR(255) | NOT NULL |
| password | VARCHAR(255) | NOT NULL (hash BCrypt) |
| email | VARCHAR(255) | NOT NULL, UNIQUE |

### categories
| Columna | Tipo | Restricciones |
|---|---:|---|
| id | BIGINT | PK, auto-increment |
| name | VARCHAR(255) | NOT NULL |

### items
| Columna | Tipo | Restricciones |
|---|---:|---|
| id | BIGINT | PK, auto-increment |
| name | VARCHAR(255) | NOT NULL |
| user_id | BIGINT | FK → users.id |
| category_id | BIGINT | FK → categories.id |

### transactions
| Columna | Tipo | Restricciones |
|---|---:|---|
| id | BIGINT | PK, auto-increment |
| item_id | BIGINT | FK → items.id |
| stocks | DOUBLE | NOT NULL |
| purchase_price | DOUBLE | NOT NULL |
| inv_eur | DOUBLE | NOT NULL (se calcula automáticamente si no se envía) |
| purchase_date | DATETIME | NOT NULL (se asigna automáticamente si no se envía) |

> El DDL completo está en `src/main/resources/schema.sql`.

---

## Entidades Java (modelos)

- `model/User.java` — `id`, `username`, `password`, `email`
- `model/Category.java` — `id`, `name`
- `model/Item.java` — `id`, `name`, `user` (ManyToOne), `category` (ManyToOne), `transactions` (OneToMany, cascade delete)
- `model/Transaction.java` — `id`, `stocks`, `purchasePrice`, `invEur`, `purchaseDate`, `item` (ManyToOne)

Todas las entidades usan `@Entity`, `@Table`, Lombok `@Data`. `Item` y `Transaction` tienen `@JsonManagedReference` / `@JsonBackReference` para evitar bucles en la serialización JSON.

---

## Configuración y variables de entorno

Archivo principal: `src/main/resources/application.properties`

| Variable de entorno | Propiedad | Valor por defecto |
|---|---|---|
| `DB_USERNAME` | `spring.datasource.username` | `root` |
| `DB_PASSWORD` | `spring.datasource.password` | `root` |
| `JWT_SECRET` | `jwt.secret` | clave de desarrollo (≥ 32 caracteres requeridos) |
| `FINNHUB_API_KEY` | `finnhub.api.key` | vacío |
| `TWELVE_API_KEY` | `twelve.api.key` | vacío |

Para desarrollo local, crea `src/main/resources/application-local.properties` y arranca con:
```
./mvnw spring-boot:run -Dspring.profiles.active=local
```

La base de datos `invest_db` se crea automáticamente si no existe. Hibernate DDL mode: `update`.

---

## Seguridad

- Rutas públicas: `POST /api/users/login`, `POST /api/users/register`, `GET /api/users/health`
- El resto de endpoints requieren `Authorization: Bearer <token>`
- JWT con algoritmo HS256, expiración de 10 horas
- Contraseñas almacenadas con BCrypt
- Sesiones sin estado (`STATELESS`)

---

## Precios en tiempo real (PriceService)

`PriceService` obtiene precios actuales de APIs externas según el símbolo del ítem:

| Tipo de activo | Detección | API |
|---|---|---|
| Forex | Símbolo contiene `/` (ej. `USD/EUR`) | ER-API |
| Cripto | Símbolo termina en `USDT` (ej. `BTCUSDT`) | Binance |
| Acciones | Cualquier otro (ej. `AAPL`) | Finnhub |

El precio actual se devuelve en `ItemDTO.currentPrice` y no se persiste en base de datos.

---

## Compilar y ejecutar

Construir:
```
./mvnw -DskipTests clean package
```

Ejecutar en desarrollo:
```
./mvnw spring-boot:run
```

Ejecutar el JAR:
```
java -jar target/investment-tracking-0.0.1-SNAPSHOT.jar
```

Cambiar puerto:
```
./mvnw spring-boot:run -Dserver.port=8081
```

---

## Tests

Ejecutar todos los tests:
```
./mvnw test
```

Ejecutar un test concreto:
```
./mvnw -Dtest=ItemServiceTest test
```

Tipos de pruebas incluidas:

| Anotación | Alcance |
|---|---|
| `@ExtendWith(MockitoExtension)` | Unitario — lógica de servicio pura |
| `@DataJpaTest` | Integración — consultas JPA contra H2 |
| `@WebMvcTest` | Componente — capa de controlador con servicios mockeados |
| `@SpringBootTest` | E2E — contexto completo, H2, HTTP real |

> `ApplicationIntegrationTest` requiere una conexión activa a MariaDB y fallará sin ella.

---

## Endpoints y ejemplos (curl)

### Registrar usuario
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"user1","password":"Password123","email":"user1@example.com"}'
```
Campos obligatorios: `username`, `password`, `email` (formato válido de email).

### Login — obtener token
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"user1@example.com","password":"Password123"}'
```
Respuesta: `{"token": "...", "user": {...}}`

### Listar categorías
```bash
curl -X GET http://localhost:8080/api/categories \
  -H 'Authorization: Bearer <TOKEN>'
```

### Crear ítem
```bash
curl -X POST http://localhost:8080/api/items \
  -H 'Authorization: Bearer <TOKEN>' \
  -H 'Content-Type: application/json' \
  -d '{"name":"BTCUSDT","userId":1,"categoryId":1}'
```
Campos obligatorios: `name`, `userId`, `categoryId`. Solo el propietario puede crear ítems para su propio `userId`.

### Listar ítems de un usuario
```bash
curl -X GET http://localhost:8080/api/items/user/1 \
  -H 'Authorization: Bearer <TOKEN>'
```

### Crear transacción
```bash
curl -X POST http://localhost:8080/api/transactions/item/1 \
  -H 'Authorization: Bearer <TOKEN>' \
  -H 'Content-Type: application/json' \
  -d '{"stocks":2.5,"purchasePrice":60000.0}'
```
`invEur` se calcula automáticamente (`stocks × purchasePrice`) si no se proporciona. `purchaseDate` se asigna automáticamente si no se envía.

### Eliminar transacción
```bash
curl -X DELETE http://localhost:8080/api/transactions/1 \
  -H 'Authorization: Bearer <TOKEN>'
```

---



## Problemas comunes

- **Puerto ocupado**: `Address already in use` → identificar el proceso con `sudo lsof -nP -iTCP:8080 -sTCP:LISTEN` y terminarlo, o arrancar en otro puerto con `-Dserver.port=8081`.
- **Error de versión Java**: asegúrate de que `java -version` muestra JDK 17 o superior y que `JAVA_HOME` apunta correctamente.
- **Tests E2E fallan**: los tests `@SpringBootTest` sin `@AutoConfigureTestDatabase` requieren MariaDB activo; el resto usan H2 y no necesitan base de datos externa.
- **Endpoints protegidos devuelven 401 sin token**: incluir siempre `Authorization: Bearer <TOKEN>` en la cabecera.