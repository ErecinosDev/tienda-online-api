# Tienda Online API

Proyecto universitario desarrollado para la Universidad Mariano Gálvez de Guatemala.

## Descripción

API RESTful para la administración de productos, gestión de carritos de compra anónimos y procesamiento de compras mediante métodos de pago simulados.

## Tecnologías

- Java 17
- Spring Boot
- Spring Data JPA / Hibernate
- MySQL
- Lombok
- JUnit 5 y Mockito

## Patrones de diseño

- **Strategy:** procesamiento de diferentes métodos de pago.
- **Factory Method:** demostración de conexiones JDBC con distintos privilegios MySQL.

## Estado del proyecto

Actualmente en preparación para la implementación.

El análisis, diseño y esquema MySQL fueron definidos previamente. La implementación Java se realizará por funcionalidades utilizando Git, GitHub y Codex.

## Base de datos

Los scripts SQL de configuración se encuentran en la carpeta `database/`.

La aplicación utilizará el esquema `tienda_online`.