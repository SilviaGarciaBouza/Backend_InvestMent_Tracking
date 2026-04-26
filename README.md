# Investment Tracking — Backend (Guía completa)

## Resumen
Proyecto backend en Java Spring Boot para seguimiento de inversiones. 
Proporciona una API REST para gestionar usuarios, categorías, ítems y transacciones. 
Persistencia con JPA y MariaDB (en tests H2). 
Seguridad con JWT y Spring Security.

---

## Requisitos
- JDK 17
- Maven (se recomienda usar el wrapper `./mvnw` incluido)
- MariaDB (opcional en desarrollo; los tests usan H2 por defecto)
- Git (opcional)

Comprobar Java:
```
java -version
```

---

## Estructura del proyecto (resumen)

- `src/main/java/.../model` — entidades JPA (User, Category, Item, Transaction)
- `src/main/java/.../repository` — interfaces `JpaRepository`
- `src/main/java/.../service` — lógica de negocio
- `src/main/java/.../controller` — controladores REST
- `src/main/java/.../security` — JWT, filtros y configuración de seguridad
- `src/main/resources` — `application.properties`, `schema.sql`
- `src/test/java` — pruebas unitarias, integración y E2E

---

## Base de datos — Tablas 
A continuación se describen las tablas que usa la aplicación (nombres y columnas principales).

### users
| Columna | Tipo | Restricciones |
|---|---:|---|
| id | BIGINT | PK, auto-increment |
| username | VARCHAR | UNIQUE, NOT NULL |
| password | VARCHAR | NOT NULL (almacenado en hash BCrypt) |
| email | VARCHAR | NULLABLE |

### categories
| Columna | Tipo | Restricciones |
|---|---:|---|
| id | BIGINT | PK, auto-increment |
| name | VARCHAR | NOT NULL |

### items
| Columna | Tipo | Restricciones |
|---|---:|---|
| id | BIGINT | PK, auto-increment |
| name | VARCHAR | NOT NULL |
| user_id | BIGINT | FK -> users.id |
| category_id | BIGINT | FK -> categories.id |

### transactions
| Columna | Tipo | Restricciones |
|---|---:|---|
| id | BIGINT | PK, auto-increment |
| item_id | BIGINT | FK -> items.id |
| amount | DECIMAL/DOUBLE | NOT NULL |
| type | VARCHAR | Ej: BUY / SELL (según modelo) |
| timestamp | TIMESTAMP/DATETIME | NOT NULL |

> Nota: el DDL exacto puede verse en `src/main/resources/schema.sql` o deducirse de las clases en `model`.

---

## Entidades Java (modelos) — archivos y campos clave
Lista de clases principales y su ubicación (ruta relativa a `src/main/java`):

- `model/User.java` — `id`, `username`, `password`, `email`
- `model/Category.java` — `id`, `name`
- `model/Item.java` — `id`, `name`, `user` (ManyToOne), `category` (ManyToOne), `transactions` (OneToMany)
- `model/Transaction.java` — `id`, `item` (ManyToOne), `amount`, `type`, `timestamp`

Estas clases están anotadas con JPA (`@Entity`, `@Table`, `@Id`, `@ManyToOne`, `@OneToMany`) y usan Lombok (`@Data`) para getters/setters.

---

## Paquetes importantes y responsabilidades

- `controller` — Exponen los endpoints REST. Ejemplos:
  - `UserController` — `/api/users` (registro, login, health)
  - `CategoryController` — `/api/categories` (listar)
  - `ItemController`, `TransactionController` — CRUD sobre ítems y transacciones

- `service` — Lógica de negocio, validaciones y mapeo entidad ↔ DTO
  - `UserService`, `CategoryService`, `ItemService`, `TransactionService`

- `repository` — Acceso a datos con JPA
  - `UserRepository`, `CategoryRepository`, `ItemRepository`, `TransactionRepository`

- `security` — JWT y filtros
  - `JwtService`, `JwtAuthenticationFilter`, `SecurityConfig`

- `dto` — Objetos de transferencia (CategoryDTO, UserDTO, ItemDTO, TransactionDTO)

---

## Configuración importante
Archivo: `src/main/resources/application.properties`

Propiedades relevantes (ejemplos):
```
spring.datasource.url=jdbc:mariadb://localhost:3306/invest_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

- Guardar secretos (JWT secret, contraseñas) fuera del VCS. Usar variables de entorno en producción.

---

## Compilar y ejecutar (desarrollo)

1. Construir (wrapper):
```
./mvnw -DskipTests clean package
```

2. Ejecutar en desarrollo (hot reload con devtools si está activo):
```
./mvnw -DskipTests spring-boot:run
```

3. Ejecutar JAR:
```
java -jar target/investment-tracking-0.0.1-SNAPSHOT.jar
```

4. Cambiar puerto temporalmente:
```
./mvnw -DskipTests spring-boot:run -Dserver.port=8081
```

---

## Testing

- Ejecutar todos los tests:
```
./mvnw test
```

Tipos de pruebas incluidas:
- Unitarios
- Integración ascendente (bottom‑up) — `@DataJpaTest` + servicios
- Integración descendente (top‑down) — `@WebMvcTest` + mocks
- E2E / Flujos — `@SpringBootTest` + `RANDOM_PORT` + `TestRestTemplate`

Ejecutar un test concreto:
```
./mvnw -Dtest=CategoryBottomUpIntegrationTest test
```

---

## Endpoints y ejemplos (curl)

1) Registrar usuario
```
curl -X POST http://localhost:8080/api/users/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"user1","password":"Password123","email":"user1@example.com"}'
```

2) Login (obtener token)
```
curl -X POST http://localhost:8080/api/users/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"user1","password":"Password123"}'
```
Respuesta: JSON con campo `token` y `user`.

3) Llamada a endpoint protegido (ejemplo listar categorías)
```
curl -X GET http://localhost:8080/api/categories \
  -H 'Authorization: Bearer <TOKEN>'
```

> Ajusta las rutas si cambias `server.port` o el contexto.

---

## Despliegue (Docker + docker-compose)

**Dockerfile (sugerencia)**
```dockerfile
FROM eclipse-temurin:17-jre-focal
COPY target/investment-tracking-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

**docker-compose.yml (sugerencia)**
```yaml
version: '3.8'
services:
  db:
    image: mariadb:11
    environment:
      MYSQL_DATABASE: invest_db
      MYSQL_ROOT_PASSWORD: root
    ports:
      - "3306:3306"
    volumes:
      - db_data:/var/lib/mysql
  app:
    build: .
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:mariadb://db:3306/invest_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    ports:
      - "8080:8080"
volumes:
  db_data:
```

---

## Problemas comunes y soluciones rápidas
- Tomcat no arranca: `Address already in use` → puerto ocupado. Solución: identificar y matar proceso (`sudo lsof -nP -iTCP:8080 -sTCP:LISTEN`) o usar `-Dserver.port=8081`.
- Error de versión de Java: asegúrate de usar JDK 17 y que `JAVA_HOME` apunte correctamente.
- Propiedades marcadas como "Unused" en IDE: advertencia estática; Spring las lee en tiempo de ejecución.
- Tests E2E con JWT fallan: verifica que `register` y `login` funcionan y que `JwtService` usa la misma configuración en tests.

---

## Apéndice — archivos clave
- `src/main/java/com/silviagarcia/investtracking/investment_tracking/model` — entidades JPA
- `src/main/java/com/silviagarcia/investtracking/investment_tracking/repository` — repositorios JPA
- `src/main/java/com/silviagarcia/investtracking/investment_tracking/service` — servicios
- `src/main/java/com/silviagarcia/investtracking/investment_tracking/controller` — controladores REST
- `src/main/java/com/silviagarcia/investtracking/investment_tracking/security` — seguridad JWT
- `src/main/resources/application.properties` — configuración
- `src/main/resources/schema.sql` — DDL inicial (si existe)

---