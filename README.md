API RESTful para la administración de productos, gestión de carritos de compra anónimos y procesamiento de compras mediante métodos de pago simulados.

 Tecnologías

- Java 17
- Spring Boot
- Spring Data JPA / Hibernate
- MySQL
- Lombok
- JUnit 5 y Mockito

Patrones de diseño

- **Strategy:** procesamiento de diferentes métodos de pago.
- **Factory Method:** demostración de conexiones JDBC con distintos privilegios MySQL.

 Base de datos

Los scripts SQL de configuración se encuentran en la carpeta `database/`.

La aplicación utilizará el esquema `tienda_online`.
 Endpoints

- `GET /api/productos`, `GET /api/productos/{id}`, `POST`, `PUT` y `DELETE /api/productos/{id}`
- `GET /api/carrito`
- `POST /api/carrito/productos`
- `PUT` y `DELETE /api/carrito/productos/{idProducto}`
- `POST /api/compras`

 Simulación de pagos

Los escenarios se controlan localmente con `pagos.tarjeta.escenario` y
`pagos.paypal.escenario`: `APROBADO`, `RECHAZADO` o `ERROR_TECNICO`.
