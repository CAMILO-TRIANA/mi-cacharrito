# Mi Cacharrito - Backend (Spring Boot)

Backend construido con Spring Boot 3, Spring Data JPA y MySQL, siguiendo la
misma estructura de tu proyecto de EPS (modelo, repositorio, controlador) y
**sin usar `@PathVariable`** en ningún endpoint (solo `@RequestParam` y
`@RequestBody`, igual que en tu ejemplo de Citas).

## Base de datos

No necesitas crear la base de datos a mano: Spring Boot + Hibernate la crean
y actualizan automáticamente gracias a:

```
spring.datasource.url=jdbc:mysql://localhost:3306/mi_cacharrito?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.jpa.hibernate.ddl-auto=update
```

Solo asegúrate de tener MySQL corriendo en `localhost:3306` con usuario
`root` y sin contraseña (o edita `application.properties` con tus propias
credenciales).

## Tablas generadas (según el modelo relacional que compartiste)

- `usuarios`
- `administradores`
- `vehiculos`
- `alquileres`
- `auditoria`

## Endpoints principales

### Usuarios (`/usuarios/u`)
- `GET /listarTodo`
- `POST /guardarUsuario` (registro)
- `POST /actualizarUsuario`
- `POST /eliminarUsuario`
- `POST /login?identificacion=...&password=...`
- `POST /buscarPorIdentificacion?identificacion=...`

### Administradores (`/administradores/a`)
- `GET /listarTodo`
- `POST /guardarAdministrador`
- `POST /eliminarAdministrador`
- `POST /login?usuario=...&password=...`

> Como no hay un formulario de registro de administradores en el enunciado,
> crea el primer administrador directamente en la tabla `administradores`
> de MySQL, o llama una vez al endpoint `guardarAdministrador` con Postman.

### Vehículos (`/vehiculos/v`)
- `GET /listarTodo`
- `POST /buscarPorTipo?tipo=automovil|camioneta|campero|microbus|motocicleta`
  (solo trae los que están `disponible`)
- `POST /buscarPorPlaca?placa=...`
- `POST /guardarVehiculo`
- `POST /actualizarVehiculo`
- `POST /eliminarVehiculo`

### Alquileres (`/alquileres/al`)
- `GET /listarTodo`
- `POST /guardarAlquiler` (el usuario solicita el alquiler; calcula el valor
  total según los días y marca el vehículo como `alquilado`)
- `POST /cancelarAlquiler?numeroAlquiler=...` (el usuario cancela en
  cualquier momento; el vehículo vuelve a `disponible`)
- `POST /buscarPorNumero?numeroAlquiler=...`
- `POST /listarPorUsuario?usuarioId=...`
- `GET /listarNoEntregados` (para el administrador)
- `POST /marcarEntregado?placa=...&adminId=...`
- `POST /marcarDisponible?numeroAlquiler=...&fechaEntregaReal=yyyy-MM-dd&adminId=...`
  (cobra los días adicionales si la entrega real es posterior a la pactada)
- `GET /generarPdf?numeroAlquiler=...` (devuelve el PDF del alquiler)

## Generación de PDF

Se usa la librería `openpdf` (declarada en el `pom.xml`), que es liviana y
funciona igual que iText clásico. La clase `cacharrito.util.GeneradorPdf`
arma el PDF con: número de alquiler, nombre del usuario, identificación,
fechas, tipo de vehículo, placa, color, valor del alquiler y estado.

## Datos de prueba (se crean solo la primera vez, si las tablas están vacías)

| Rol | Usuario | Contraseña |
|---|---|---|
| Administrador | `admin` | `admin123` |
| Cliente | `1000000001` (identificación) | `cliente123` |

También se cargan 12 vehículos de ejemplo (automóvil, camioneta, campero, microbús y motocicleta).
Las fotos que suba el administrador se guardan en la carpeta `uploads/` (junto al proyecto) y se sirven en `/uploads/**`.
