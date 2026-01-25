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

### Argocd - Despliegue GitOps

GitOps permite gestionar los despliegues de aplicaciones en Kubernetes directamente desde un repositorio Git, lo que
facilita el control de versiones y la automatización de las implementaciones.

ArgoCD es una herramienta de entrega continua declarativa y GitOps para Kubernetes.

#### Instalación

```bash
[soydz@dzvps ~]$ kubectl create namespace argocd

[soydz@dzvps ~]$ kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

Verificar que los pods están corriendo:

![](./doc/images/argocd_1.png)

Se crea un túnel temporal entre la máquina local y el clúster de Kubernetes,
redirigiendo el puerto local **8080** hacia el puerto **443** del servicio
`argocd-server` dentro del namespace `argocd`.

```bash
[soydz@dzvps ~]$ kubectl port-forward svc/argocd-server -n argocd 8080:443
```

Se establece una conexión SSH al servidor remoto (`usuario@IP_DEL_VPS`)
creando un **túnel de red seguro** que redirige el puerto **8080** de la máquina local
hacia el puerto **8080** del `localhost` del VPS.

```bash
[soydz@dzvps ~]$ ssh -L 8080:localhost:8080 usuario@IP_DEL_VPS
```

Gracias a este túnel, es posible acceder a la interfaz web de **ArgoCD**
desde el navegador en `https://localhost:8080` sin necesidad de exponer
el servicio a Internet ni abrir puertos en el servidor,
manteniendo así un acceso seguro.

Comprobamos si funciona:

![](doc/images/argocd_2.png)
![](doc/images/argocd_3.png)

### Variables de entorno en K8s

Creamos un secret de K8s para la base de datos:

```bash
[soydz@dzvps ~]$ kubectl create secret generic postgres-secret \
      -n database \
      --from-literal=POSTGRES_DB=appdb \
      --from-literal=POSTGRES_USER=appuser \
      --from-literal=POSTGRES_PASSWORD=supersecreta
```

Creamos otro secret para la api:

```bash
[soydz@dzvps ~]$ kubectl create secret generic api-secret \
    -n api \
    --from-literal=DB_USER=appuser \
    --from-literal=DB_PASSWORD=supersecreta \
    --from-literal=DB_URL=jdbc:postgresql://postgres.pt-supermercado-database.svc.cluster.local:5432/appdb
```

### Arquitectura del despliegue

```text
K3s Cluster
 ├─ Namespace: pt-supermercado-database
 │    └─ Postgres StatefulSet + Service
 ├─ Namespace: pt-supermercado
 │    └─ Spring Boot Deployment + Service
 └─ Argo CD
      └─ Sincroniza los manifests desde GitHub
```

### Argo CD - Aplicaciones

![](doc/images/argocd_4.png)
![](doc/images/argocd_5.png)
![](doc/images/argocd_6.png)

### Estado en Argo CD

Aplicaciones sincronizadas

![](doc/images/argocd_7.png)

### Página de inicio de la aplicación

La app está desplegada y accesible en la URL: http://pt-supermercado.soydz.com

![](doc/images/argocd_8.png)

## 📘 Documentación de la API (Scalar)

La API está documentada utilizando **OpenAPI + Scalar**.

![Products API - Scalar](doc/images/scalar_product_3.png)
