# ✈️ TourPath

> Plataforma internacional de descubrimiento turístico. Selecciona tu ciudad,
> explora lugares únicos, reserva directamente y apoya a los negocios locales.

**Integrantes:** Freck Rivero · Jose Bettin · Cristian Cohen — 2026

---

## 🚀 Stack Tecnológico

| Capa | Tecnología |
|------|-----------|
| Backend | Spring Boot 3.2 + Java 17 |
| Seguridad | Spring Security 6 + JWT (JJWT 0.11.5) |
| Base de datos | MongoDB Atlas |
| Templates | Thymeleaf + Thymeleaf Security Extras |
| Frontend | HTML5 · CSS3 (variables) · JavaScript ES6 |
| Fuentes | Sora + Inter (Google Fonts) |
| Build | Maven 3 (sin Lombok) |
| Tests | JUnit 5 + Mockito |

---

## 📁 Estructura del Proyecto

```
tourpath/
├── src/main/java/com/tourpath/
│   ├── TourPathApplication.java          ← Punto de entrada
│   ├── config/
│   │   ├── SecurityConfig.java           ← Reglas de seguridad por rol
│   │   ├── JwtUtil.java                  ← Generación/validación JWT
│   │   ├── JwtRequestFilter.java         ← Filtro JWT en cada request
│   │   ├── DataInitializer.java          ← Carga 10 ciudades + lugares de ejemplo
│   │   ├── GlobalExceptionHandler.java   ← Manejo global de errores
│   │   └── GlobalControllerAdvice.java   ← Atributos globales (isAdmin, isOwner...)
│   ├── controller/
│   │   ├── HomeController.java           ← Inicio, explorar, buscar
│   │   ├── CityController.java           ← Página de ciudad con sus lugares
│   │   ├── PlaceController.java          ← Detalle de lugar + reseñas
│   │   ├── BusinessController.java       ← Formulario público de registro
│   │   ├── OwnerController.java          ← Panel exclusivo del dueño
│   │   ├── SuperAdminController.java     ← Panel super administrador
│   │   ├── AuthController.java           ← Login, registro, logout
│   │   └── UserController.java           ← Perfil de usuario
│   ├── model/
│   │   ├── User.java                     ← Roles: ROLE_USER, ROLE_OWNER, ROLE_SUPER_ADMIN
│   │   ├── City.java                     ← Ciudades del mundo
│   │   ├── Place.java                    ← Lugares con bookingUrl + bookingLabel
│   │   ├── BusinessRequest.java          ← Solicitud de registro (PENDING/APPROVED/REJECTED)
│   │   └── Review.java                   ← Reseñas de lugares
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── CityRepository.java
│   │   ├── PlaceRepository.java
│   │   ├── ReviewRepository.java
│   │   └── BusinessRequestRepository.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── CityService.java
│   │   ├── PlaceService.java
│   │   ├── ReviewService.java
│   │   ├── BusinessRequestService.java   ← Lógica de aprobación/rechazo
│   │   └── CustomUserDetailsService.java
│   └── dto/
│       ├── RegisterDTO.java
│       ├── UserProfileDTO.java
│       └── (sin Lombok — getters/setters manuales)
├── src/main/resources/
│   ├── application.properties            ← ⚠️ Configurar MongoDB aquí
│   ├── static/css/
│   │   ├── main.css                      ← Diseño TourPath completo
│   │   └── admin.css                     ← Panel admin/owner
│   ├── static/js/main.js                 ← Interactividad general
│   └── templates/
│       ├── home.html                     ← Hero + ciudades + lugares destacados
│       ├── explore.html                  ← Selector de ciudad con filtros
│       ├── search.html                   ← Búsqueda global
│       ├── nosotros.html
│       ├── contacto.html
│       ├── city/detail.html              ← Ciudad + sus lugares filtrados
│       ├── places/detail.html            ← Lugar + botón de reserva + reseñas
│       ├── business/register.html        ← Formulario público de solicitud
│       ├── business/status.html
│       ├── owner/dashboard.html          ← Panel del dueño
│       ├── owner/edit-place.html         ← Editar su establecimiento
│       ├── admin/dashboard.html          ← Panel super admin
│       ├── admin/requests.html           ← Cola de solicitudes
│       ├── admin/request-detail.html     ← Aprobar / Rechazar
│       ├── admin/cities.html             ← CRUD ciudades
│       ├── admin/city-form.html
│       ├── admin/places.html             ← CRUD lugares
│       ├── admin/place-form.html
│       ├── admin/users.html              ← Gestión de usuarios
│       ├── auth/login.html
│       ├── auth/register.html
│       ├── user/profile.html
│       ├── error/error.html              ← Página 403/404/500
│       └── fragments/layout.html        ← Navbar + Footer reutilizable
└── src/test/java/com/tourpath/
    ├── TourPathApplicationTest.java
    └── service/
        ├── CityServiceTest.java          ← 7 casos
        ├── PlaceServiceTest.java         ← 8 casos
        ├── BusinessRequestServiceTest.java ← 6 casos
        └── UserServiceTest.java          ← 6 casos
```

---

## ⚙️ Configuración Inicial

### 1. Configurar MongoDB Atlas

Edita `src/main/resources/application.properties`:

```properties
spring.data.mongodb.uri=mongodb+srv://<USERNAME>:<PASSWORD>@<CLUSTER>.mongodb.net/tourpath?retryWrites=true&w=majority
```

### 2. Ejecutar

```bash
# Terminal de VS Code
mvn spring-boot:run
```

Abre: **http://localhost:8080**

---

## 🔐 Credenciales por defecto

| Rol | Usuario | Contraseña |
|-----|---------|-----------|
| Super Admin | `superadmin` | `TourPath2026@` |

---

## 🌍 Ciudades cargadas al inicio

Al arrancar por primera vez se crean automáticamente:

| Ciudad | País | Continente |
|--------|------|-----------|
| Cartagena | Colombia 🇨🇴 | América del Sur |
| París | Francia 🇫🇷 | Europa |
| Tokio | Japón 🇯🇵 | Asia |
| Nueva York | EE.UU. 🇺🇸 | América del Norte |
| Barcelona | España 🇪🇸 | Europa |
| Ciudad de México | México 🇲🇽 | América del Norte |
| Buenos Aires | Argentina 🇦🇷 | América del Sur |
| Dubái | EAU 🇦🇪 | Asia |
| Roma | Italia 🇮🇹 | Europa |
| Bogotá | Colombia 🇨🇴 | América del Sur |

---

## 👥 Roles y Flujo de Permisos

```
ROLE_SUPER_ADMIN
  ├── Gestionar ciudades (CRUD)
  ├── Gestionar lugares (CRUD)
  ├── Gestionar usuarios
  ├── Ver y procesar solicitudes de negocios
  └── Aprobar → crea Place + cuenta OWNER automáticamente

ROLE_OWNER
  ├── Ver su dashboard con estadísticas
  ├── Editar SOLO su propio establecimiento
  ├── Ver reseñas de su negocio
  └── Actualizar su link de reservas (bookingUrl)

ROLE_USER
  ├── Explorar ciudades y lugares
  ├── Ver detalle de lugar + botón "Reservar"
  ├── Dejar reseñas
  └── Editar su perfil
```

---

## 📅 Flujo de registro de negocio

```
1. Dueño llena formulario en /business/register (público)
2. Solicitud queda en estado PENDING
3. Super Admin ve la cola en /superadmin/requests
4. Super Admin revisa el detalle completo
5a. APROBAR → Se crea el Place + cuenta OWNER automáticamente
5b. RECHAZAR → Se guarda el motivo, dueño es notificado
6. Owner inicia sesión → ve /owner/dashboard con su establecimiento
7. Owner puede editar su info y actualizar el link de reservas
```

---

## 🔗 Rutas Principales

| Ruta | Acceso | Descripción |
|------|--------|-------------|
| `/` | Público | Página principal |
| `/explore` | Público | Selector de ciudades |
| `/city/{id}` | Público | Ciudad con sus lugares |
| `/places/{id}` | Público | Detalle + botón reserva |
| `/search` | Público | Búsqueda global |
| `/business/register` | Público | Registrar negocio |
| `/auth/login` | Público | Login |
| `/auth/register` | Público | Registro |
| `/owner/dashboard` | OWNER | Panel del dueño |
| `/owner/place/edit` | OWNER | Editar su negocio |
| `/superadmin/dashboard` | SUPER_ADMIN | Panel administrador |
| `/superadmin/requests` | SUPER_ADMIN | Cola de solicitudes |
| `/superadmin/cities` | SUPER_ADMIN | Gestión de ciudades |
| `/superadmin/places` | SUPER_ADMIN | Gestión de lugares |
| `/superadmin/users` | SUPER_ADMIN | Gestión de usuarios |

---

## 🧪 Ejecutar Tests

```bash
mvn test
```

27 casos de prueba cubriendo servicios principales.

---

© 2026 TourPath · Freak Rivero · Jose Bettin · Cristian Cohen
