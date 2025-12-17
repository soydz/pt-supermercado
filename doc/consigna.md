# Prueba Técnica Spring Boot

### Tomada de : [TodoCode](https://www.youtube.com/watch?v=l-Bl45I6UEY)

## APIREST para la gestión de ventas en una cadena de supermercados

## 🎯 Objetivo
El objetivo de esta prueba es evaluar conocimientos en Java + Spring Boot, incluyendo el desarrollo de una API RESTful completa que implemente operaciones CRUD con JPA, relaciones entre entidades, control de errores y excepciones, uso de DTOs, buenas prácticas REST y programación funcional (uso de lambdas y streams) donde aplique.

## 📘 Descripción del caso

Una reconocida cadena de supermercados desea digitalizar su sistema de control de ventas. Para ello necesita una API que permita (de forma básica):

- Registrar productos con sus respectivos precios.
- Gestionar las sucursales donde se venden los productos.
- Registrar ventas realizadas en una sucursal, especificando los productos vendidos y cantidades.

La empresa desea consultar luego las ventas por sucursal, totalizar ingresos, filtrar productos más vendidos, etc.

## 📚 Entidades principales

- **Sucursal**: Representa una ubicación física del supermercado (una por cada ubicación).
- **Producto**: Representa un artículo que puede venderse (ejemplo: arroz, botella de agua, etc).
- **Venta**: Contiene una o más líneas de productos, asociadas a una sucursal.

## Relaciones

- Una **Sucursal** puede tener muchas **Ventas**.
- Una **Venta** tiene muchos **Productos** asociados.
- Un mismo **Producto** puede estar en **muchas ventas**.

## ✅ Requisitos técnicos

- Utilizar **Spring Boot** con **JPA** para manejo de bases de datos.
- Base de datos **relacional** (por ejemplo: H2 o MySQL).
- Exponer **endpoints RESTful** para realizar **CRUDs** (GET, POST, PUT, DELETE o los métodos que se consideren necesarios).
- Utilizar **DTOs** para separar modelo de dominio y representación externa.
- Manejo adecuado de **errores** con **ResponseEntity**, códigos HTTP correctos (status code) y mensajes claros.
- Uso de **lambdas** o **streams** en al menos una operación del backend.
- Organización **modular** del proyecto (service, repository, controller).

## 📑 Historias de usuario (Requerimientos funcionales)

### HU-01-Productos

1. **Obtener listado de productos**
   - **Método**: GET
   - **Path**: /api/productos
   - **Descripción**: Listar todos los productos registrados.

2. **Registrar nuevo producto**
   - **Método**: POST
   - **Path**: /api/productos
   - **Descripción**: Crear un nuevo producto con nombre, precio y categoría.

3. **Actualizar producto existente**
   - **Método**: PUT
   - **Path**: /api/productos/{id}
   - **Descripción**: Modificar los datos de un producto específico.

4. **Eliminar un producto**
   - **Método**: DELETE
   - **Path**: /api/productos/{id}
   - **Descripción**: Eliminar un producto específico.

### HU-02-Sucursales

1. **Obtener listado de sucursales**
   - **Método**: GET
   - **Path**: /api/sucursales
   - **Descripción**: Listar todas las sucursales del sistema.

2. **Registrar nueva sucursal**
   - **Método**: POST
   - **Path**: /api/sucursales
   - **Descripción**: Crear una nueva sucursal con dirección, nombre, etc.

3. **Actualizar sucursal existente**
   - **Método**: PUT
   - **Path**: /api/sucursales/{id}
   - **Descripción**: Modificar los datos de una sucursal existente.

4. **Eliminar una sucursal**
   - **Método**: DELETE
   - **Path**: /api/sucursales/{id}
   - **Descripción**: Eliminar una sucursal del sistema.

### HU-03-Ventas

1. **Registrar nueva venta**
   - **Método**: POST
   - **Path**: /api/ventas
   - **Payload**:
     ```json
     {
       "sucursalId": 1,
       "detalle": [
         { "productoId": 10, "cantidad": 2 },
         { "productoId": 5, "cantidad": 1 }
       ]
     }
     ```
   - **Descripción**: Crear una nueva venta para una sucursal con productos y cantidades.

2. **Obtener ventas por sucursal y fecha**
   - **Método**: GET
   - **Path**: /api/ventas?sucursalId=1&fecha=2025-06-01
   - **Descripción**: Listar ventas realizadas en una fecha específica para una sucursal.

3. **Eliminar/Anular una venta**
   - **Método**: DELETE
   - **Path**: /api/ventas/{id}
   - **Descripción**: Eliminar una venta registrada.
   - **Nota**: Se valorará uso de borrado lógico.

**Nota adicional**: Las ventas **NO SE PUEDEN MODIFICAR** sin permisos de superusuario (no es necesario implementar esto).

## HU-04- EXTRA - Estadísticas (opcional)

1. **Obtener producto más vendido**
   - **Método**: GET
   - **Path**: /api/estadisticas/producto-mas-vendido
   - **Descripción**: Calcular el producto más vendido utilizando **Java Streams**.


