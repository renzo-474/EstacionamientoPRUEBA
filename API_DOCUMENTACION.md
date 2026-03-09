# Documentación de la API REST — Sistema de Estacionamiento

**Base URL:** `http://localhost:8080/api`  
**Versión:** 1.0.0  
**Autenticación:** Bearer Token (JWT)  
**Formato:** JSON  

---

## Tabla de Contenidos

- [Autenticación](#1-autenticación)
- [Vehículos](#2-vehículos)
- [Clientes](#3-clientes)
- [Empleados](#4-empleados)
- [Registro de Entrada](#5-registro-de-entrada)
- [Registro de Salida](#6-registro-de-salida)
- [Dashboard](#7-dashboard)
- [Reportes](#8-reportes)
- [Códigos de Respuesta](#códigos-de-respuesta-http)

---

> **Nota:** Todos los endpoints excepto los de autenticación requieren el header:
> ```
> Authorization: Bearer <token>
> ```

---

## 1. Autenticación

Base path: `/api/auth`  
No requiere autenticación previa.

---

### POST /api/auth/login

Autentica a un empleado y retorna un token JWT.

**Request Body:**

```json
{
  "idEmpleado": "EMP01",
  "password": "mi_contraseña"
}
```

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| idEmpleado | string | Si | ID del empleado (solo letras y números) |
| password | string | Si | Contraseña del empleado |

**Respuesta exitosa — 200 OK:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "idEmpleado": "EMP01",
  "nombre": "Renzo Laura",
  "rol": "EMPLEADO",
  "success": true
}
```

**Respuesta de error — 401 Unauthorized:**

```json
{
  "error": "Credenciales inválidas",
  "details": "Usuario o contraseña incorrectos",
  "success": "false"
}
```

**Respuesta de error — 400 Bad Request:**

```json
{
  "error": "Faltan credenciales",
  "details": "Se requiere ID de empleado y contraseña"
}
```

---

### POST /api/auth/register

Registra un nuevo empleado en el sistema.

**Request Body:**

```json
{
  "idEmpleado": "EMP02",
  "password": "contraseña123",
  "nombres": "Ana",
  "apellidos": "García López",
  "numDoc": "12345678",
  "telefono": 987654321,
  "correo": "ana.garcia@estacionamiento.pe",
  "direccion": "Av. Principal 123"
}
```

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| idEmpleado | string | Si | ID único del empleado (solo letras y números) |
| password | string | Si | Contraseña |
| nombres | string | Si | Nombres del empleado |
| apellidos | string | Si | Apellidos del empleado |
| correo | string | Si | Correo electrónico válido |
| numDoc | string | No | Número de documento de identidad |
| telefono | integer | No | Número de teléfono |
| direccion | string | No | Dirección |

**Respuesta exitosa — 201 Created:**

```json
{
  "success": true,
  "message": "Empleado registrado exitosamente",
  "idEmpleado": "EMP02",
  "nombre": "Ana García López"
}
```

**Respuesta de error — 400 Bad Request (ID duplicado):**

```json
{
  "error": "El ID de empleado ya existe",
  "message": "Ya existe un empleado con el ID: EMP02"
}
```

---

### POST /api/auth/verify

Verifica si un token JWT es válido.

**Headers:**

| Header | Valor |
|---|---|
| Authorization | Bearer `<token>` |

**Respuesta exitosa — 200 OK:**

```json
{
  "valid": true,
  "idEmpleado": "EMP01",
  "nombre": "Renzo Laura",
  "rol": "EMPLEADO"
}
```

**Respuesta de error — 401 Unauthorized:**

```json
{
  "valid": false,
  "message": "Token inválido o expirado"
}
```

---

### GET /api/auth/check

Verifica que el endpoint de autenticación esté operativo.

**Respuesta exitosa — 200 OK:**

```json
{
  "message": "Endpoint de autenticación funcionando",
  "status": "OK",
  "timestamp": "2025-03-09T10:30:00"
}
```

---

## 2. Vehículos

Base path: `/api/vehiculos`  
Requiere autenticación JWT.

---

### GET /api/vehiculos

Retorna la lista de todos los vehículos registrados.

**Respuesta exitosa — 200 OK:**

```json
[
  {
    "nroPlaca": "ABC-123",
    "codColor": 1,
    "color": "Rojo",
    "codTipoVhc": 1,
    "tipoVehiculo": "Automóvil",
    "codMarcaModelo": 3,
    "marcaModelo": "Toyota Corolla",
    "lunasPolarizadas": "SI"
  },
  {
    "nroPlaca": "XYZ-456",
    "codColor": 2,
    "color": "Blanco",
    "codTipoVhc": 2,
    "tipoVehiculo": "Motocicleta",
    "codMarcaModelo": 7,
    "marcaModelo": "Honda CB500",
    "lunasPolarizadas": "NO"
  }
]
```

---

### GET /api/vehiculos/{placa}

Retorna los datos de un vehículo por su número de placa.

**Parámetro de ruta:**

| Parámetro | Tipo | Descripción |
|---|---|---|
| placa | string | Número de placa del vehículo |

**Ejemplo:** `GET /api/vehiculos/ABC-123`

**Respuesta exitosa — 200 OK:**

```json
{
  "nroPlaca": "ABC-123",
  "codColor": 1,
  "color": "Rojo",
  "codTipoVhc": 1,
  "tipoVehiculo": "Automóvil",
  "codMarcaModelo": 3,
  "marcaModelo": "Toyota Corolla",
  "lunasPolarizadas": "SI"
}
```

---

### POST /api/vehiculos

Registra un nuevo vehículo.

**Request Body:**

```json
{
  "nroPlaca": "DEF-789",
  "codColor": 3,
  "codTipoVhc": 1,
  "codMarcaModelo": 5,
  "lunasPolarizadas": "NO"
}
```

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| nroPlaca | string | Si | Número de placa (máx. 10 caracteres) |
| codColor | integer | Si | Código del color |
| codTipoVhc | integer | Si | Código del tipo de vehículo |
| codMarcaModelo | integer | Si | Código de marca y modelo |
| lunasPolarizadas | string | Si | "SI" o "NO" |

**Respuesta exitosa — 201 Created:**

```json
{
  "nroPlaca": "DEF-789",
  "codColor": 3,
  "color": "Negro",
  "codTipoVhc": 1,
  "tipoVehiculo": "Automóvil",
  "codMarcaModelo": 5,
  "marcaModelo": "Hyundai Elantra",
  "lunasPolarizadas": "NO"
}
```

---

### PUT /api/vehiculos/{placa}

Actualiza los datos de un vehículo existente.

**Parámetro de ruta:**

| Parámetro | Tipo | Descripción |
|---|---|---|
| placa | string | Número de placa del vehículo a actualizar |

**Request Body:** mismo esquema que POST.

**Respuesta exitosa — 200 OK:** retorna el vehículo actualizado.

---

### DELETE /api/vehiculos/{placa}

Elimina un vehículo del sistema.

**Parámetro de ruta:**

| Parámetro | Tipo | Descripción |
|---|---|---|
| placa | string | Número de placa del vehículo a eliminar |

**Respuesta exitosa — 204 No Content:** sin cuerpo de respuesta.

---

## 3. Clientes

Base path: `/api/clientes`  
Requiere autenticación JWT.

Los endpoints siguen el mismo patrón CRUD que Vehículos.

**Esquema ClienteDTO:**

| Campo | Tipo | Descripción |
|---|---|---|
| idCliente | string | Identificador único del cliente |
| nombres | string | Nombres del cliente |
| apellidos | string | Apellidos del cliente |
| numDoc | string | Número de documento (único) |
| nroLicencia | integer | Número de licencia de conducir |
| telefono | integer | Teléfono de contacto |
| correo | string | Correo electrónico |
| direccion | string | Dirección |
| codNacio | integer | Código de nacionalidad |
| nacionalidad | string | Nombre de la nacionalidad |
| codSexo | integer | Código de sexo |
| sexo | string | Descripción del sexo |
| codDistrito | integer | Código del distrito |
| distrito | string | Nombre del distrito |

**Endpoints disponibles:**

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /api/clientes | Listar todos los clientes |
| GET | /api/clientes/{id} | Obtener cliente por ID |
| POST | /api/clientes | Crear nuevo cliente |
| PUT | /api/clientes/{id} | Actualizar cliente |
| DELETE | /api/clientes/{id} | Eliminar cliente |

---

## 4. Empleados

Base path: `/api/empleados`  
Requiere autenticación JWT.

**Esquema EmpleadoDTO:**

| Campo | Tipo | Descripción |
|---|---|---|
| idEmpleado | string | Identificador único del empleado |
| nombres | string | Nombres |
| apellidos | string | Apellidos |
| password | string | Contraseña (encriptada con BCrypt) |
| numDoc | string | Documento de identidad (único) |
| telefono | integer | Teléfono |
| correo | string | Correo electrónico |
| direccion | string | Dirección |
| codNacio | integer | Código de nacionalidad |
| codSexo | integer | Código de sexo |
| codDistrito | integer | Código de distrito |

**Endpoints disponibles:**

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /api/empleados | Listar todos los empleados |
| GET | /api/empleados/{id} | Obtener empleado por ID |
| POST | /api/empleados | Crear nuevo empleado |
| PUT | /api/empleados/{id} | Actualizar empleado |
| DELETE | /api/empleados/{id} | Eliminar empleado |

---

## 5. Registro de Entrada

Base path: `/api/registro-entradas`  
Requiere autenticación JWT.

---

### GET /api/registro-entradas

Retorna todos los registros de entrada.

**Respuesta exitosa — 200 OK:**

```json
[
  {
    "idEntrada": "E001",
    "idCliente": "C001",
    "nombreCliente": "Luis Pérez",
    "idEmpleado": "EMP01",
    "nombreEmpleado": "Renzo Laura",
    "codDocPaga": 1,
    "tipoDocPago": "Boleta",
    "nroPlaca": "ABC-123",
    "nroDocumento": 1001,
    "nivel": 1,
    "zona": "A",
    "fechaIngreso": "2025-03-09T08:30:00",
    "horaIngreso": "08:30:00",
    "tieneSalida": false
  }
]
```

---

### GET /api/registro-entradas/{id}

Retorna un registro de entrada por su ID.

**Parámetro de ruta:**

| Parámetro | Tipo | Descripción |
|---|---|---|
| id | string | ID del registro de entrada |

**Ejemplo:** `GET /api/registro-entradas/E001`

---

### POST /api/registro-entradas

Registra la entrada de un vehículo al estacionamiento.

**Request Body:**

```json
{
  "idEntrada": "E002",
  "idCliente": "C001",
  "idEmpleado": "EMP01",
  "codDocPaga": 1,
  "nroPlaca": "ABC-123",
  "nroDocumento": 1002,
  "nivel": 2,
  "zona": "B",
  "fechaIngreso": "2025-03-09T09:00:00",
  "horaIngreso": "09:00:00"
}
```

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| idEntrada | string | Si | ID único de la entrada |
| idCliente | string | Si | ID del cliente |
| idEmpleado | string | Si | ID del empleado que registra |
| codDocPaga | integer | Si | Tipo de documento de pago |
| nroPlaca | string | Si | Placa del vehículo |
| nivel | integer | No | Nivel del estacionamiento |
| zona | string | No | Zona asignada |
| nroDocumento | integer | No | Número de documento de pago |

**Respuesta exitosa — 201 Created:** retorna el RegistroEntradaDTO completo.

---

### PUT /api/registro-entradas/{id}

Actualiza un registro de entrada existente.

**Respuesta exitosa — 200 OK:** retorna el registro actualizado.

---

### DELETE /api/registro-entradas/{id}

Elimina un registro de entrada.

**Respuesta exitosa — 204 No Content.**

---

## 6. Registro de Salida

Base path: `/api/registro-salidas`  
Requiere autenticación JWT.

---

### GET /api/registro-salidas

Retorna todos los registros de salida.

**Respuesta exitosa — 200 OK:**

```json
[
  {
    "idSalida": "S001",
    "idEntrada": "E001",
    "idCliente": "C001",
    "nombreCliente": "Luis Pérez",
    "idEmpleado": "EMP01",
    "nombreEmpleado": "Renzo Laura",
    "codTipoVhc": 1,
    "fechaSalida": "2025-03-09T10:15:00",
    "horaSalida": "10:15:00",
    "montoTotal": 15.50
  }
]
```

---

### POST /api/registro-salidas

Registra la salida de un vehículo del estacionamiento.

**Request Body:**

```json
{
  "idSalida": "S002",
  "idEntrada": "E002",
  "idCliente": "C001",
  "idEmpleado": "EMP01",
  "codTipoVhc": 1,
  "fechaSalida": "2025-03-09T11:00:00",
  "horaSalida": "11:00:00"
}
```

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| idSalida | string | Si | ID único de la salida |
| idEntrada | string | Si | ID del registro de entrada asociado |
| idCliente | string | Si | ID del cliente |
| idEmpleado | string | Si | ID del empleado que registra |
| codTipoVhc | integer | Si | Tipo de vehículo |
| fechaSalida | datetime | Si | Fecha y hora de salida |
| horaSalida | time | Si | Hora de salida |

**Respuesta exitosa — 201 Created:** retorna el RegistroSalidaDTO completo.

---

## 7. Dashboard

Base path: `/api/dashboard`  
Requiere autenticación JWT.

---

### GET /api/dashboard

Retorna las estadísticas generales del estacionamiento.

**Respuesta exitosa — 200 OK:**

```json
{
  "totalEntradas": 120,
  "totalSalidas": 115,
  "vehiculosActivos": 5,
  "ingresosDia": 850.00,
  "entradasPorNivel": {
    "Nivel 1": 45,
    "Nivel 2": 38,
    "Nivel 3": 37
  },
  "ocupacionPorHora": [
    { "hora": "08:00", "cantidad": 12 },
    { "hora": "09:00", "cantidad": 18 },
    { "hora": "10:00", "cantidad": 25 }
  ]
}
```

| Campo | Tipo | Descripción |
|---|---|---|
| totalEntradas | long | Total de entradas registradas |
| totalSalidas | long | Total de salidas registradas |
| vehiculosActivos | long | Vehículos actualmente en el estacionamiento |
| ingresosDia | double | Ingresos del día actual |
| entradasPorNivel | map | Distribución de entradas por nivel |
| ocupacionPorHora | list | Ocupación por hora del día |

---

## 8. Reportes

Base path: `/api/reportes`  
Requiere autenticación JWT.

---

### GET /api/reportes/ingresos

Retorna un resumen de ingresos para un período determinado.

**Parámetros de consulta:**

| Parámetro | Tipo | Requerido | Descripción |
|---|---|---|---|
| desde | string | Si | Fecha de inicio (formato: YYYY-MM-DD) |
| hasta | string | Si | Fecha de fin (formato: YYYY-MM-DD) |

**Ejemplo:** `GET /api/reportes/ingresos?desde=2025-01-01&hasta=2025-03-09`

**Respuesta exitosa — 200 OK:**

```json
{
  "totalPeriodo": 15750.00,
  "diasOperacion": 30,
  "promedioDiario": 525.00,
  "mejorDia": {
    "fecha": "2025-01-15",
    "monto": 892.50
  },
  "peorDia": {
    "fecha": "2025-01-22",
    "monto": 125.00
  },
  "ingresosPorTipo": [
    { "tipo": "Automóvil",   "monto": 10500.00, "porcentaje": 66.7 },
    { "tipo": "Motocicleta", "monto": 2250.00,  "porcentaje": 14.3 },
    { "tipo": "Camioneta",   "monto": 3000.00,  "porcentaje": 19.0 }
  ]
}
```

---

### GET /api/reportes/exportar

Exporta un reporte en el formato solicitado (PDF o Excel).

**Parámetros de consulta:**

| Parámetro | Tipo | Requerido | Valores | Descripción |
|---|---|---|---|---|
| tipo | string | Si | `ingresos`, `entradas`, `salidas` | Tipo de reporte |
| formato | string | Si | `pdf`, `xlsx` | Formato de exportación |
| desde | string | Si | YYYY-MM-DD | Fecha de inicio |
| hasta | string | Si | YYYY-MM-DD | Fecha de fin |

**Ejemplo:** `GET /api/reportes/exportar?tipo=ingresos&formato=pdf&desde=2025-01-01&hasta=2025-03-09`

**Respuesta exitosa — 200 OK:**  
Descarga directa del archivo. Headers de respuesta:

```
Content-Type: application/pdf
Content-Disposition: attachment; filename=reporte_ingresos_2025-01-01_a_2025-03-09.pdf
```

---

## Códigos de Respuesta HTTP

| Código | Nombre | Descripción |
|---|---|---|
| 200 | OK | Solicitud exitosa |
| 201 | Created | Recurso creado correctamente |
| 204 | No Content | Operación exitosa sin cuerpo de respuesta |
| 400 | Bad Request | Datos de entrada inválidos o faltantes |
| 401 | Unauthorized | Token no proporcionado, inválido o expirado |
| 403 | Forbidden | Sin permisos para acceder al recurso |
| 404 | Not Found | Recurso no encontrado |
| 500 | Internal Server Error | Error interno del servidor |

---

## Estructura del Token JWT

El token retornado en `/api/auth/login` tiene una duración de **24 horas** (86400000 ms) y debe enviarse en el header de cada petición protegida:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJFTVAwMSIsImlhdCI6...
```

---
## Contacto

**Autor:** Renzo Alberto Laura Carhuamaca 
**Correo:** renzo.laura.c@uni.pe  
**Código UNI:** [20211378H]  
**GitHub:** [github.com/renzo-474](https://github.com/renzo-474)

> Documentación hecha para el curso de **Documentación de Software** — Universidad Nacional de Ingeniería (UNI)  

