[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=soydz_pt-supermercado&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=soydz_pt-supermercado)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=soydz_pt-supermercado&metric=coverage)](https://sonarcloud.io/summary/new_code?id=soydz_pt-supermercado)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=soydz_pt-supermercado&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=soydz_pt-supermercado)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=soydz_pt-supermercado&metric=bugs)](https://sonarcloud.io/summary/new_code?id=soydz_pt-supermercado)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=soydz_pt-supermercado&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=soydz_pt-supermercado)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=soydz_pt-supermercado&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=soydz_pt-supermercado)

## API REST - Gestión de ventas de una cadena de supermercados

### 🚀 🛠️ Tecnologías utilizadas

![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Podman](https://img.shields.io/badge/-Podman-68217A?style=for-the-badge&logo=podman&logoColor=white)
![Google Java Format](https://img.shields.io/badge/Google%20Java%20Format-4285F4?style=for-the-badge&logo=google&logoColor=white)
![Licencia GPL-3.0](https://img.shields.io/badge/licencia-GPL%20v3-blue?style=for-the-badge&)

### 🎯 Objetivo

Desarrollar una API RESTful completa con Java + Spring Boot, que implementa operaciones CRUD con JPA, relaciones entre
entidades, control de errores y excepciones, uso de DTO, buenas prácticas y programación funcional (uso de lambdas y
streams).

### 📘 Descripción del caso

Una reconocida cadena de supermercados desea digitalizar su sistema de control de ventas. Para ello necesita una API que
permita (de forma básica):

- Registrar productos con sus respectivos precios.
- Gestionar las sucursales donde se venden los productos.
- Registrar ventas realizadas en una sucursal, especificando los productos vendidos y cantidades.

La empresa desea consultar luego las ventas por sucursal, totalizar ingresos, filtrar productos más vendidos, etc.

### Ejercicio tomado de : [TodoCode](https://www.youtube.com/watch?v=l-Bl45I6UEY)

---

## Solución

### Diagrama de clases

![Diagrama de clases](./doc/images/DClase-GestionVentasSupermercado.jpg)
