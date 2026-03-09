# Documentación Técnica — Sistema de Gestión de Estacionamiento

**Proyecto:** estacionamiento-backend  
**Versión:** 1.0.0  
**Autor:** Renzo Alberto Laura Carhuamaca  
**Código UNI:** 20211378H  
**Curso:** Documentación de Software — Universidad Nacional de Ingeniería (UNI)  
**Fecha:** Marzo 2025  

---

## Tabla de Contenidos

1. [Modelo C4 de Arquitectura](#1-modelo-c4-de-arquitectura)
   - [Nivel 1 — Contexto](#nivel-1--contexto)
   - [Nivel 2 — Contenedores](#nivel-2--contenedores)
   - [Nivel 3 — Componentes](#nivel-3--componentes)
   - [Nivel 4 — Código](#nivel-4--código)
2. [Architecture Decision Records (ADR)](#2-architecture-decision-records-adr)
3. [Documentación de Despliegue](#3-documentación-de-despliegue)
4. [Diccionario de Datos](#4-diccionario-de-datos)

---

## 1. Modelo C4 de Arquitectura

El modelo C4 describe la arquitectura del sistema en cuatro niveles de detalle progresivo: desde la visión general hasta el código fuente.

---

### Nivel 1 — Contexto

Este nivel muestra cómo el sistema encaja en el mundo real: quiénes lo usan y con qué sistemas externos interactúa.

```
┌─────────────────────────────────────────────────────────────────────┐
│                        CONTEXTO DEL SISTEMA                         │
└─────────────────────────────────────────────────────────────────────┘

        [Empleado de Estacionamiento]
               │  Registra entradas,
               │  salidas y consulta
               │  reportes
               ▼
    ┌─────────────────────┐
    │                     │
    │  Sistema de Gestión │◄──── Usuario principal del sistema
    │  de Estacionamiento │
    │                     │
    └──────────┬──────────┘
               │
               │ Persiste datos
               ▼
    ┌─────────────────────┐
    │   SQL Server 2019   │
    │   (Base de datos)   │
    └─────────────────────┘
```

**Actores:**

| Actor | Tipo | Descripción |
|---|---|---|
| Empleado de Estacionamiento | Usuario interno | Opera el sistema para registrar entradas/salidas de vehículos y generar reportes |

**Sistemas externos:**

| Sistema | Tipo | Descripción |
|---|---|---|
| Microsoft SQL Server 2019 | Base de datos | Almacena toda la información del negocio: clientes, vehículos, registros y tablas maestras |

---

### Nivel 2 — Contenedores

Este nivel desglosa el sistema en sus partes ejecutables e identificables.

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           SISTEMA DE ESTACIONAMIENTO                    │
│                                                                         │
│   [Empleado]                                                            │
│       │                                                                 │
│       │ HTTP/HTTPS                                                      │
│       │ puerto 5173                                                     │
│       ▼                                                                 │
│  ┌────────────────┐    REST API      ┌──────────────────────────┐       │
│  │                │   JSON/HTTP      │                          │       │
│  │   Frontend     │ ◄──────────────► │   Backend               │       │
│  │   (React/Vite) │   puerto 8080    │   Spring Boot 3.5.0     │       │
│  │                │                  │   Java 17               │       │
│  └────────────────┘                  │   JWT Authentication    │       │
│                                      │                          │       │
│                                      └────────────┬─────────────┘       │
│                                                   │                     │
│                                                   │ JPA/Hibernate       │
│                                                   │ puerto 1433         │
│                                                   ▼                     │
│                                      ┌──────────────────────────┐       │
│                                      │   SQL Server 2019        │       │
│                                      │   EstacionamientoPRUEBA  │       │
│                                      │   _respaldo              │       │
│                                      └──────────────────────────┘       │
└─────────────────────────────────────────────────────────────────────────┘
```

**Descripción de contenedores:**

| Contenedor | Tecnología | Puerto | Descripción |
|---|---|---|---|
| Frontend | React + Vite | 5173 | Interfaz de usuario para los empleados |
| Backend | Spring Boot 3.5.0 / Java 17 | 8080 | API REST con lógica de negocio y autenticación JWT |
| Base de datos | SQL Server 2019 | 1433 | Almacenamiento persistente de datos |

**Comunicación entre contenedores:**

| Origen | Destino | Protocolo | Descripción |
|---|---|---|---|
| Frontend | Backend | HTTP REST / JSON | Llamadas a la API para todas las operaciones |
| Backend | Base de datos | JDBC / JPA | Consultas y persistencia de datos mediante Hibernate |

---

### Nivel 3 — Componentes

Este nivel muestra los componentes internos del contenedor Backend (Spring Boot).

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                          BACKEND — Spring Boot 3.5.0                         │
│                                                                              │
│  ┌────────────────────────────────────────────────────────────────────────┐  │
│  │                        CAPA DE SEGURIDAD                               │  │
│  │   JwtAuthFilter ──► JwtUtil ──► CustomUserDetailsService              │  │
│  │   SecurityConfig (Spring Security)                                     │  │
│  └────────────────────────────────────────────────────────────────────────┘  │
│                                    │                                         │
│                                    ▼                                         │
│  ┌────────────────────────────────────────────────────────────────────────┐  │
│  │                       CAPA DE CONTROLADORES                            │  │
│  │  AuthController      /api/auth                                         │  │
│  │  ClienteController   /api/clientes                                     │  │
│  │  EmpleadoController  /api/empleados                                    │  │
│  │  VehiculoController  /api/vehiculos                                    │  │
│  │  RegistroEntradaController  /api/registro-entradas                     │  │
│  │  RegistroSalidaController   /api/registro-salidas                      │  │
│  │  DashboardController        /api/dashboard                             │  │
│  │  ReporteController          /api/reportes                              │  │
│  └────────────────────────────────────────────────────────────────────────┘  │
│                                    │                                         │
│                                    ▼                                         │
│  ┌────────────────────────────────────────────────────────────────────────┐  │
│  │                         CAPA DE SERVICIOS                              │  │
│  │  VehiculoService  │  ClienteService  │  EmpleadoService               │  │
│  │  RegistroEntradaService  │  RegistroSalidaService                     │  │
│  │  DashboardService  │  ReporteService                                  │  │
│  └────────────────────────────────────────────────────────────────────────┘  │
│                                    │                                         │
│                                    ▼                                         │
│  ┌────────────────────────────────────────────────────────────────────────┐  │
│  │                       CAPA DE REPOSITORIOS                             │  │
│  │  VehiculoRepository  │  ClienteRepository  │  EmpleadoRepository      │  │
│  │  RegistroEntradaRepository  │  RegistroSalidaRepository               │  │
│  │  NacionalidadRepository  │  SexoRepository  │  DistritoRepository     │  │
│  └────────────────────────────────────────────────────────────────────────┘  │
│                                    │                                         │
│                                    ▼                                         │
│  ┌────────────────────────────────────────────────────────────────────────┐  │
│  │                         CAPA DE ENTIDADES                              │  │
│  │  Empleado  │  Cliente  │  Vehiculo  │  RegistroEntrada                │  │
│  │  RegistroSalida  │  Color  │  TipoVehiculo  │  MarcaModelo            │  │
│  │  Nacionalidad  │  Sexo  │  Distrito  │  TipoDocumentoPago             │  │
│  └────────────────────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────────────────┘
```

**Descripción de componentes clave:**

| Componente | Paquete | Responsabilidad |
|---|---|---|
| JwtAuthFilter | security | Intercepta cada petición HTTP y valida el token JWT |
| JwtUtil | util | Genera y valida tokens JWT con firma HMAC-SHA |
| SecurityConfig | config | Define rutas públicas y protegidas con Spring Security |
| CustomUserDetailsService | service | Carga el usuario desde la BD para la autenticación |
| AuthController | controller | Endpoints de login, registro y verificación de token |
| VehiculoController | controller | CRUD de vehículos |
| RegistroEntradaController | controller | Registro de ingreso de vehículos |
| RegistroSalidaController | controller | Registro de salida de vehículos |
| ReporteController | controller | Generación y exportación de reportes |

---

### Nivel 4 — Código

Este nivel muestra la estructura de clases del componente más representativo del sistema: el flujo de autenticación JWT.

```
┌──────────────────────────────────────────────────────────────────┐
│               FLUJO DE AUTENTICACIÓN JWT                         │
│                                                                  │
│  Cliente HTTP                                                    │
│      │                                                           │
│      │ POST /api/auth/login                                      │
│      │ { idEmpleado, password }                                  │
│      ▼                                                           │
│  AuthController.login()                                          │
│      │                                                           │
│      ├──► EmpleadoRepository.findByIdEmpleado()                  │
│      │         │ Empleado encontrado                             │
│      │         ▼                                                 │
│      ├──► AuthenticationManager.authenticate()                   │
│      │         │ Credenciales válidas                            │
│      │         ▼                                                 │
│      ├──► CustomUserDetailsService.loadUserByUsername()          │
│      │         │ UserDetails                                     │
│      │         ▼                                                 │
│      └──► JwtUtil.generateToken(userDetails)                     │
│               │                                                  │
│               │ Token JWT (HS256, 24h)                           │
│               ▼                                                  │
│  Response: { token, idEmpleado, nombre, rol, success: true }     │
│                                                                  │
│  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─   │
│                                                                  │
│  Petición posterior con JWT:                                     │
│      │                                                           │
│      │ Authorization: Bearer <token>                             │
│      ▼                                                           │
│  JwtAuthFilter.doFilterInternal()                                │
│      ├──► JwtUtil.extractUsername(token)                         │
│      ├──► JwtUtil.validateToken(token)                           │
│      └──► SecurityContextHolder.setAuthentication()             │
│               │ Petición autorizada                              │
│               ▼                                                  │
│          Controller correspondiente                              │
└──────────────────────────────────────────────────────────────────┘
```

**Relación entre clases principales:**

```
Empleado (entity)
    │ 1
    │ usa
    ▼ N
EmpleadoRepository ◄── CustomUserDetailsService ◄── AuthController
                                                         │
                                                    JwtUtil
                                                    JwtAuthFilter
```

---

## 2. Architecture Decision Records (ADR)

Los ADR documentan las decisiones de arquitectura tomadas durante el desarrollo, incluyendo el contexto, las alternativas consideradas y la justificación de la elección final.

---

### ADR-001 — Framework de Backend: Spring Boot

| Campo | Detalle |
|---|---|
| **ID** | ADR-001 |
| **Fecha** | Junio 2025 |
| **Estado** | Aceptado |

**Contexto:**  
El sistema requiere exponer una API REST que sea consumida por un frontend separado. Se necesita un framework en Java que soporte autenticación, persistencia y estructura en capas.

**Decisión:**  
Se eligió **Spring Boot 3.5.0** con Java 17.

**Alternativas consideradas:**

| Alternativa | Motivo de descarte |
|---|---|
| Jakarta EE (Payara/Wildfly) | Mayor complejidad de configuración y despliegue |
| Quarkus | Curva de aprendizaje mayor, menos documentación en español |
| Spring Boot (elegido) | — |

**Consecuencias:**
- Configuración automática con `application.properties`
- Integración nativa con Spring Security y Spring Data JPA
- Amplia comunidad y documentación disponible

---

### ADR-002 — Autenticación: JWT con Spring Security

| Campo | Detalle |
|---|---|
| **ID** | ADR-002 |
| **Fecha** | Junio 2025 |
| **Estado** | Aceptado |

**Contexto:**  
El sistema necesita proteger los endpoints de la API y permitir que el frontend mantenga sesión sin estado en el servidor (stateless).

**Decisión:**  
Se implementó autenticación basada en **JWT (JSON Web Tokens)** usando la librería `jjwt 0.11.5` integrada con Spring Security.

**Alternativas consideradas:**

| Alternativa | Motivo de descarte |
|---|---|
| Sesiones HTTP (HttpSession) | Requiere estado en el servidor, incompatible con arquitectura desacoplada |
| OAuth2 | Complejidad innecesaria para un sistema de un solo rol |
| JWT (elegido) | — |

**Consecuencias:**
- Los tokens expiran en 24 horas (86,400,000 ms)
- El servidor no almacena sesiones
- Cada petición protegida requiere el header `Authorization: Bearer <token>`
- La clave de firma está definida en `application.properties`

---

### ADR-003 — Base de Datos: Microsoft SQL Server

| Campo | Detalle |
|---|---|
| **ID** | ADR-003 |
| **Fecha** | Junio 2025 |
| **Estado** | Aceptado |

**Contexto:**  
El sistema necesita una base de datos relacional para almacenar registros de entradas, salidas, clientes y vehículos con integridad referencial entre tablas.

**Decisión:**  
Se eligió **Microsoft SQL Server 2019** con collation `Modern_Spanish_CI_AS`.

**Alternativas consideradas:**

| Alternativa | Motivo de descarte |
|---|---|
| MySQL / MariaDB | Menor soporte nativo en entornos Windows corporativos |
| PostgreSQL | No era familiar para el equipo de desarrollo |
| SQL Server (elegido) | — |

**Consecuencias:**
- Acceso mediante driver JDBC `mssql-jdbc`
- Conexión configurada en `application.properties` con usuario `estacionamiento_user`
- Collation español soporta caracteres especiales (ñ, tildes)

---

### ADR-004 — ORM: Hibernate / Spring Data JPA

| Campo | Detalle |
|---|---|
| **ID** | ADR-004 |
| **Fecha** | Junio 2025 |
| **Estado** | Aceptado |

**Contexto:**  
Se requiere una capa de acceso a datos que evite escribir SQL manual y facilite el mantenimiento del código.

**Decisión:**  
Se utilizó **Spring Data JPA** con Hibernate como proveedor ORM.

**Alternativas consideradas:**

| Alternativa | Motivo de descarte |
|---|---|
| JDBC puro | Código repetitivo, mayor probabilidad de errores |
| MyBatis | Requiere mapeo XML manual |
| Spring Data JPA (elegido) | — |

**Consecuencias:**
- Los repositorios extienden `JpaRepository` y heredan operaciones CRUD
- Las entidades se mapean a tablas con anotaciones `@Entity`, `@Table`, `@Column`
- Las relaciones entre tablas se representan con `@ManyToOne`, `@OneToMany`

---

### ADR-005 — Patrón de Transferencia de Datos: DTO

| Campo | Detalle |
|---|---|
| **ID** | ADR-005 |
| **Fecha** | Junio 2025 |
| **Estado** | Aceptado |

**Contexto:**  
Las entidades JPA contienen relaciones y campos internos que no deben exponerse directamente en la API (contraseñas, claves foráneas internas, etc.).

**Decisión:**  
Se implementó el patrón **DTO (Data Transfer Object)** para separar la capa de persistencia de la capa de presentación.

**Consecuencias:**
- Los controllers reciben y retornan DTOs, nunca entidades directamente
- Las contraseñas no se exponen en las respuestas
- Se utilizó Lombok `@Data` para reducir código boilerplate

---

## 3. Documentación de Despliegue

Esta sección describe cómo poner en funcionamiento el sistema en un entorno local o de producción.

---

### 3.1 Requisitos del Entorno

| Componente | Versión mínima | Notas |
|---|---|---|
| Java JDK | 17 | OpenJDK o Oracle JDK |
| Maven | 3.8+ | Para compilar y empaquetar |
| SQL Server | 2019 | Con TCP/IP habilitado en el puerto 1433 |
| SQL Server Management Studio | 19+ | Opcional, para administrar la BD |
| Git | 2.x | Para clonar el repositorio |

---

### 3.2 Arquitectura de Despliegue

```
┌─────────────────────────────────────────────────┐
│               MÁQUINA LOCAL / SERVIDOR           │
│                                                 │
│  ┌──────────────────┐    ┌───────────────────┐  │
│  │  Frontend        │    │  Backend          │  │
│  │  React + Vite    │───►│  Spring Boot JAR  │  │
│  │  puerto: 5173    │    │  puerto: 8080     │  │
│  └──────────────────┘    └────────┬──────────┘  │
│                                   │             │
│                                   ▼             │
│                        ┌───────────────────┐    │
│                        │  SQL Server 2019  │    │
│                        │  puerto: 1433     │    │
│                        │  BD: Estaciona-   │    │
│                        │  mientoPRUEBA     │    │
│                        │  _respaldo        │    │
│                        └───────────────────┘    │
└─────────────────────────────────────────────────┘
```

---

### 3.3 Pasos de Despliegue

**Paso 1 — Clonar el repositorio**

```bash
git clone https://github.com/renzo-474/EstacionamientoPRUEBA.git
cd EstacionamientoPRUEBA
```

**Paso 2 — Configurar la base de datos**

Restaurar el backup de la base de datos en SQL Server Management Studio:
- Base de datos: `EstacionamientoPRUEBA_respaldo`
- Usuario: `estacionamiento_user`
- Collation: `Modern_Spanish_CI_AS`

Verificar que TCP/IP esté habilitado en SQL Server Configuration Manager (puerto 1433).

**Paso 3 — Configurar `application.properties`**

Editar el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=EstacionamientoPRUEBA_respaldo;encrypt=false
spring.datasource.username=estacionamiento_user
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

jwt.secret=estacionamiento2024SecretKeyMuySeguraParaProduccion
jwt.expiration=86400000

server.port=8080
```

**Paso 4 — Compilar el proyecto**

```bash
mvn clean install -DskipTests
```

**Paso 5 — Ejecutar el backend**

```bash
mvn spring-boot:run
```

O ejecutar el JAR generado:

```bash
java -jar target/estacionamiento-backend-0.0.1-SNAPSHOT.jar
```

**Paso 6 — Verificar que el servidor esté activo**

```bash
curl http://localhost:8080/api/auth/check
```

Respuesta esperada:
```json
{
  "message": "Endpoint de autenticación funcionando",
  "status": "OK"
}
```

---

### 3.4 Variables de Configuración

| Variable | Valor por defecto | Descripción |
|---|---|---|
| `server.port` | 8080 | Puerto del servidor backend |
| `spring.datasource.url` | localhost:1433 | URL de conexión a SQL Server |
| `spring.datasource.username` | estacionamiento_user | Usuario de la base de datos |
| `jwt.secret` | (ver application.properties) | Clave de firma del token JWT |
| `jwt.expiration` | 86400000 | Duración del token en milisegundos (24h) |

---

### 3.5 Puertos Utilizados

| Servicio | Puerto | Protocolo |
|---|---|---|
| Backend Spring Boot | 8080 | HTTP |
| Frontend React/Vite | 5173 | HTTP |
| SQL Server | 1433 | TCP |

---

### 3.6 CORS Configurado

El backend acepta peticiones de los siguientes orígenes:

```
http://localhost:5173
http://localhost:8080
```

Para agregar un nuevo origen, modificar la anotación `@CrossOrigin` en los controllers o la configuración centralizada en `SecurityConfig.java`.

---

### 3.7 Errores Comunes en el Despliegue

| Error | Causa probable | Solución |
|---|---|---|
| `Connection refused port 1433` | SQL Server no está corriendo o TCP/IP deshabilitado | Iniciar el servicio y habilitar TCP/IP en SQL Server Configuration Manager |
| `Login failed for user` | Credenciales incorrectas en `application.properties` | Verificar usuario y contraseña de la BD |
| `Port 8080 already in use` | Otro proceso usa el puerto 8080 | Cambiar `server.port` o detener el proceso conflictivo |
| `JWT signature does not match` | Token generado con una clave diferente | Limpiar la sesión del frontend y volver a hacer login |
| `401 Unauthorized` en todos los endpoints | Token expirado o no enviado | Hacer login nuevamente y enviar el token en el header `Authorization` |

---

## 4. Diccionario de Datos

El diccionario de datos documenta cada tabla y sus campos en la base de datos `EstacionamientoPRUEBA_respaldo`.

**Convenciones:**

| Símbolo | Significado |
|---|---|
| PK | Clave primaria |
| FK | Clave foránea |
| UK | Valor único |
| SI | Campo obligatorio |
| NO | Campo opcional |

---

### Tabla: Registro_Entrada

Almacena cada ingreso de un vehículo al estacionamiento.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| IdEntrada | varchar(10) | NO | PK | Identificador único del registro de entrada |
| IdCliente | varchar(10) | NO | FK → Cliente | Cliente propietario del vehículo |
| IdEmpleado | varchar(10) | NO | FK → Empleado | Empleado que registró el ingreso |
| NroPlaca | varchar(10) | NO | FK → Vehiculos | Placa del vehículo que ingresa |
| CodDocPaga | int | NO | FK → TipoDocumentoPago | Tipo de documento de pago (boleta/factura) |
| NroDocumento | int | SI | — | Número del documento de pago generado |
| Nivel | int | SI | — | Nivel del estacionamiento donde se ubicó |
| Zona | varchar(20) | SI | — | Zona dentro del nivel asignado |
| FechaIngreso | datetime | NO | — | Fecha y hora del ingreso del vehículo |
| HoraIngreso | time(4) | NO | — | Hora exacta del ingreso |

---

### Tabla: Registro_Salida

Almacena cada salida de un vehículo del estacionamiento.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| IdSalida | varchar(10) | NO | PK | Identificador único del registro de salida |
| IdEntrada | varchar(10) | NO | FK → Registro_Entrada | Entrada a la que corresponde esta salida |
| IdCliente2 | varchar(10) | NO | FK → Cliente | Cliente que retira el vehículo |
| IdEmpleado | varchar(10) | NO | FK → Empleado | Empleado que registró la salida |
| CodTipoVhc | int | NO | FK → Tipo_Vehiculo | Tipo de vehículo que sale |
| FechaSalida | datetime | NO | — | Fecha y hora de la salida |
| HoraSalida | time(4) | NO | — | Hora exacta de la salida |

---

### Tabla: Vehiculos

Catálogo de vehículos registrados en el sistema.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| NroPlaca | varchar(10) | NO | PK | Número de placa del vehículo (identificador único) |
| CodColor | int | NO | FK → Color | Color del vehículo |
| CodTipoVhc | int | NO | FK → Tipo_Vehiculo | Categoría del vehículo |
| CodMarcaModelo | int | NO | FK → Marca_Modelo | Marca y modelo del vehículo |
| Lunas_Polarizadas | varchar(10) | SI | — | Indica si el vehículo tiene lunas polarizadas (SI/NO) |

---

### Tabla: Cliente

Información de los clientes del estacionamiento.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| IdCliente | varchar(10) | NO | PK | Identificador único del cliente |
| Nombres | varchar(15) | NO | — | Nombres del cliente |
| Apellidos | varchar(15) | NO | — | Apellidos del cliente |
| NumDoc | varchar(15) | NO | UK | Número de documento de identidad (único) |
| NroLicencia | int | SI | — | Número de licencia de conducir |
| Telefono | int | SI | — | Número de teléfono de contacto |
| Correo | varchar(20) | SI | — | Correo electrónico |
| Direccion | varchar(20) | SI | — | Dirección del cliente |
| CodNacio | int | NO | FK → Nacionalidad | Nacionalidad del cliente |
| CodSexo | int | NO | FK → Sexo | Sexo del cliente |
| CodDistrito | int | NO | FK → Distrito | Distrito de residencia |

---

### Tabla: Empleado

Información del personal que opera el sistema.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| IdEmpleado | varchar(10) | NO | PK | Identificador único del empleado |
| Nombres | varchar(15) | NO | — | Nombres del empleado |
| Apellidos | varchar(50) | NO | — | Apellidos del empleado |
| Password | varchar(15) | NO | — | Contraseña encriptada con BCrypt |
| NumDoc | varchar(15) | NO | UK | Número de documento de identidad (único) |
| Telefono | int | SI | — | Teléfono de contacto |
| Correo | varchar(50) | SI | — | Correo electrónico |
| Direccion | varchar(100) | SI | — | Dirección de residencia |
| CodNacio | int | NO | FK → Nacionalidad | Nacionalidad del empleado |
| CodSexo | int | NO | FK → Sexo | Sexo del empleado |
| CodDistrito | int | NO | FK → Distrito | Distrito de residencia |

---

### Tabla: Color

Tabla maestra de colores de vehículos.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| CodColor | int | NO | PK | Identificador del color |
| Descripcion | varchar | NO | — | Nombre del color (ej: Rojo, Blanco, Negro) |

---

### Tabla: Marca_Modelo

Tabla maestra de marcas y modelos de vehículos.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| CodMarcaModelo | int | NO | PK | Identificador de marca y modelo |
| Descripcion | varchar | NO | — | Nombre del modelo (ej: Toyota Corolla, Honda Civic) |

---

### Tabla: Tipo_Vehiculo

Tabla maestra de categorías de vehículos.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| CodTipoVhc | int | NO | PK | Identificador del tipo de vehículo |
| Descripcion | varchar | NO | — | Nombre del tipo (ej: Automóvil, Motocicleta, Camioneta) |

---

### Tabla: TipoDocumentoPago

Tabla maestra de tipos de documento de pago.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| CodDocPaga | int | NO | PK | Identificador del tipo de documento |
| Descripcion | varchar | NO | — | Nombre del documento (ej: Boleta, Factura) |

---

### Tabla: Nacionalidad

Tabla maestra de nacionalidades.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| CodNacio | int | NO | PK | Identificador de la nacionalidad |
| Descripcion | varchar | NO | — | Nombre de la nacionalidad (ej: Peruana, Colombiana) |

---

### Tabla: Sexo

Tabla maestra de sexo biológico.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| CodSexo | int | NO | PK | Identificador del sexo |
| Descripcion | varchar | NO | — | Descripción (Masculino / Femenino) |

---

### Tabla: Distrito

Tabla maestra de distritos.

| Campo | Tipo | Nulo | Restricción | Descripción |
|---|---|---|---|---|
| CodDistrito | int | NO | PK | Identificador del distrito |
| Descripcion | varchar | NO | — | Nombre del distrito (ej: Rímac, San Juan de Lurigancho) |

---

### Diagrama de Relaciones entre Tablas

```
Nacionalidad ◄──┐        ┌──► Color
Sexo ◄──────────┼── Cliente ◄── Registro_Entrada ──► Vehiculos ──► Marca_Modelo
Distrito ◄──────┘        │         │                          └──► Tipo_Vehiculo
                         │         └──► Empleado ◄─── Registro_Salida
                         └──── TipoDocumentoPago      │
                                                       └──► Tipo_Vehiculo
```

---

### Resumen de Tablas

| Tabla | Tipo | Registros esperados | Descripción |
|---|---|---|---|
| Registro_Entrada | Transaccional | Alto volumen | Registro de cada vehículo que ingresa |
| Registro_Salida | Transaccional | Alto volumen | Registro de cada vehículo que sale |
| Vehiculos | Catálogo | Medio | Vehículos conocidos del sistema |
| Cliente | Catálogo | Medio | Clientes registrados |
| Empleado | Operativa | Bajo | Personal que opera el sistema |
| Color | Maestra | Fijo (~15) | Colores disponibles |
| Marca_Modelo | Maestra | Fijo (~50) | Marcas y modelos de vehículos |
| Tipo_Vehiculo | Maestra | Fijo (~5) | Categorías de vehículos |
| TipoDocumentoPago | Maestra | Fijo (~3) | Tipos de documento de pago |
| Nacionalidad | Maestra | Fijo (~20) | Países/nacionalidades |
| Sexo | Maestra | Fijo (2) | Masculino / Femenino |
| Distrito | Maestra | Fijo (~50) | Distritos del Perú |

---

> Documentación técnica hecha para el curso de **Documentación de Software** — Universidad Nacional de Ingeniería (UNI)  
> Autor: Renzo Alberto Laura Carhuamaca | Código: 20211378H | GitHub: [renzo-474](https://github.com/renzo-474)
