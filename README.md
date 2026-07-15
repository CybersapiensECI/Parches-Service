# Parches-Service

## Microservicio de Parches (DOSW)

*"Conecta, organiza, comparte — tu próximo parche está a un clic"*

---

### Stack Tecnológico

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-336791?style=for-the-badge&logo=postgresql&logoColor=white)

### Infraestructura y Calidad

![Azure](https://img.shields.io/badge/Azure-Pending-0078D4?style=for-the-badge&logo=microsoft-azure&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Container-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

### Arquitectura

![Hexagonal](https://img.shields.io/badge/Architecture-Hexagonal-blueviolet?style=for-the-badge)
![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-blue?style=for-the-badge)
![REST API](https://img.shields.io/badge/REST-API-009688?style=for-the-badge)

---

## Tabla de Contenidos

2. [Tecnologías Utilizadas](#2-tecnologías-utilizadas)
3. [Descripción del Módulo](#3-descripción-del-módulo)
4. [Cómo Funciona el Módulo](#4-cómo-funciona-el-módulo)
   - [4.1 Arquitectura Hexagonal](#41-arquitectura-hexagonal)
   - [4.2 Principios de Diseño](#42-principios-de-diseño)
   - [4.3 Patrones de Diseño Implementados](#43-patrones-de-diseño-implementados)
   - [4.4 Estrategia de Ramas (Git Flow)](#44-estrategia-de-ramas-git-flow)
   - [4.5 Despliegue e Infraestructura](#45-despliegue-e-infraestructura)
5. [Diagramas](#5-diagramas)
   - [5.1 Diagrama de Base de Datos](#51-diagrama-de-base-de-datos)
   - [5.2 Diagrama de Clases del Dominio](#52-diagrama-de-clases-del-dominio)
   - [5.3 Diagrama de Componentes](#53-diagrama-de-componentes)
6. [Funcionalidades — Endpoints](#6-funcionalidades--endpoints)
   - [6.1 Manejo de Errores (Global Exception Handler)](#61-manejo-de-errores-global-exception-handler)
   - [6.2 Crear Parche](#62-crear-parche)
   - [6.3 Consultar Parches](#63-consultar-parches)
   - [6.4 Consultar Parche por ID](#64-consultar-parche-por-id)
   - [6.5 Actualizar Parche](#65-actualizar-parche)
   - [6.6 Archivar Parche](#66-archivar-parche)
   - [6.7 Unirse a un Parche Público](#67-unirse-a-un-parche-público)
   - [6.8 Salir de un Parche](#68-salir-de-un-parche)
   - [6.9 Enviar Invitación](#69-enviar-invitación)
   - [6.10 Responder Invitación](#610-responder-invitación)
7. [Mensajería Asíncrona (RabbitMQ)](#7-mensajería-asíncrona-rabbitmq)
8. [Pruebas](#8-pruebas)
9. [Ejecución del Proyecto](#9-ejecución-del-proyecto)
10. [Evidencia del Despliegue CI/CD](#10-evidencia-del-despliegue-cicd)
11. [Link Azure / Swagger](#11-link-azure--swagger)
12. [Código Organizado en Carpetas](#12-código-organizado-en-carpetas)
13. [Conexiones con Servicios Externos](#13-conexiones-con-servicios-externos)
14. [Pipelines (Desarrollo + PROD)](#14-pipelines-desarrollo--prod)

---



## 2. Tecnologías Utilizadas

| **Tecnología / Herramienta** | **Uso principal en el proyecto** |
|------------------------------|----------------------------------|
| **Java 21 (OpenJDK)** | Lenguaje de programación base del microservicio backend, con soporte a records, switch expressions y mejoras modernas. |
| **Spring Boot 4.0.6** | Framework principal para construir el microservicio, exponiendo APIs REST y gestionando configuración e inyección de dependencias. |
| **Spring Web** | Exposición de endpoints REST (controladores HTTP) dentro de la arquitectura hexagonal. |
| **Spring Security** | Configuración de seguridad del microservicio; permite proteger endpoints y controlar el acceso mediante encabezados de identidad. |
| **Spring Data JPA** | Integración del microservicio con la base de datos PostgreSQL usando el patrón Repository y puertos/adaptadores. |
| **PostgreSQL 18** | Base de datos relacional principal, con tablas para `parches`, `members` e `invitations`. |
| **Flyway** | Gestión y versionado del esquema de base de datos mediante migraciones SQL controladas. |
| **Apache Maven** | Gestión de dependencias, empaquetado del microservicio y automatización de builds en los pipelines CI/CD. |
| **Lombok** | Reducción de código repetitivo con anotaciones como `@Getter`, `@Builder`, `@Data` y `@RequiredArgsConstructor`. |
| **MapStruct** | Generación automática de mappers entre entidades de dominio, entidades de persistencia y DTOs. |
| **JUnit 5** | Framework de pruebas unitarias para validar la lógica de dominio y casos de uso en el microservicio. |
| **Mockito** | Simulación de dependencias (puertos, repositorios) en pruebas unitarias sin acceder a infraestructura real. |
| **JaCoCo** | Generación de reportes de cobertura de código para evaluar la efectividad de las pruebas. |
| **Swagger (OpenAPI 3 / springdoc)** | Generación automática de documentación y prueba interactiva de los endpoints REST. |
| **Postman** | Validación manual de peticiones y respuestas JSON de los endpoints (`POST`, `GET`, `PATCH`, `DELETE`). |
| **Docker** | Contenerización del microservicio con build multi-stage para despliegues aislados y consistentes. |
| **Docker Compose** | Orquestación local de la aplicación y PostgreSQL para desarrollo y pruebas de integración. |
| **Azure** | Plataforma cloud donde se desplegará el contenedor Docker del microservicio (pendiente Resource Group). |
| **GitHub Actions** | Pipeline de integración y despliegue continuo (CI/CD) para compilar, probar, analizar y desplegar el microservicio. |

> Stack tecnológico seleccionado para asegurar escalabilidad, modularidad, seguridad, trazabilidad y mantenibilidad, aplicando buenas prácticas de ingeniería de software.

---

## 3. Descripción del Módulo

El microservicio de **Parches** tiene como objetivo gestionar los encuentros sociales y académicos — llamados *parches* (jerga colombiana para reunión/salida grupal) — entre estudiantes dentro de la plataforma DOSW. Este servicio se encarga de crear y administrar parches públicos y privados, controlar la membresía de sus participantes, y gestionar el sistema de invitaciones para parches de acceso restringido. Además, implementa reglas de negocio como cupo máximo por parche, límite de parches activos por estudiante (máx. 5), y archivo automático de parches vencidos, garantizando una experiencia organizada y confiable para todos los usuarios.

---

## 4. Cómo Funciona el Módulo

El microservicio de Parches se integra con otros módulos del ecosistema DOSW  y validaciones síncronas (identidad de usuarios). Publica eventos cuando ocurren acciones relevantes (nuevo miembro, invitación enviada/aceptada) que son consumidos por **Gamification** (para recompensas) y **Notification** (para notificaciones push). Consume validación de identidad del **User Service** para verificar que los `X-User-Id` correspondan a estudiantes activos.

A continuación se detalla la arquitectura, principios, patrones, estrategia de ramas y despliegue del módulo.

### 4.1 Arquitectura Hexagonal

El microservicio de **Parches** sigue una **arquitectura hexagonal (puertos y adaptadores)** que separa las responsabilidades en capas bien definidas, promoviendo la escalabilidad, testabilidad y mantenibilidad del código.

**Flujo principal:**

- **ParcheController / MemberController / InvitationController**
  - Reciben solicitudes HTTP y las delegan a los puertos de entrada correspondientes.

**Lógica de Negocio (Dominio):**

- **Casos de Uso (Application Layer)**
  - `CreateParcheUseCase`, `GetParcheUseCase`, `UpdateParcheUseCase`, `CloseParcheUseCase`
  - `JoinParcheUseCase`, `LeaveParcheUseCase`
  - `InvitationUseCase`, `RespondInvitationUseCase`
  - Cada caso de uso implementa un puerto de entrada y orquesta la lógica mediante puertos de salida.

- **ParcheArchiveScheduler**
  - Tarea programada que se ejecuta periódicamente para archivar parches vencidos (>24h transcurridas).

**Integración y Adaptadores:**

- **Persistencia:**
  - Adaptadores `ParcheRepositoryAdapter`, `MemberRepositoryAdapter`, `InvitationRepositoryAdapter` implementan los puertos de salida.
  - Mappers (MapStruct) traducen entre entidades de dominio y entidades JPA.
  - Persiste en PostgreSQL con queries Spring Data JPA.

- **Mensajería Asíncrona:**
  - `RabbitMQEventPublisher` publica eventos de dominio (`NuevoMiembroEvent`, `InvitationSentEvent`, `InvitationAcceptedEvent`) en el exchange `hangout.events`.

- **Notificaciones:**
  - `NotificacionEventListener` escucha eventos de dominio y los reenvía vía `NotificacionAdapter`.

- **Manejo de Errores:**
  - `GlobalExceptionHandler` centraliza el manejo de excepciones de dominio.

<div align="center">

| **Capa** | **Responsabilidad** | **Dependencias** |
|:-----------|:----------------------|:-------------------|
| **Domain** | Lógica de negocio pura, entidades (`Parche`, `Member`, `Invitation`), enums, eventos y puertos (interfaces) | Ninguna (independiente) |
| **Application** | Casos de uso, DTOs, mappers y validaciones | Solo `Domain` |
| **Entrypoints** | Controladores REST y manejador global de excepciones | `Domain` + `Application` |
| **Infrastructure** | Adaptadores JPA, scheduler, notificaciones, mensajería y configuración | `Domain` + `Application` |

</div>

**Flujo de dependencias:** `Entrypoints / Infrastructure → Application → Domain`

### 4.2 Principios de Diseño

<div align="center">

| **Principio** | **Implementación** |
|:----------------|:---------------------|
| **Separación de responsabilidades** | Cada capa tiene un propósito único y bien definido |
| **Inversión de dependencias** | Las capas externas dependen de interfaces (puertos) definidas en el dominio |
| **Independencia del framework** | La lógica de negocio no depende de Spring ni de JPA |
| **Patrón Ports & Adapters** | Los casos de uso consumen puertos; la infraestructura los implementa |
| **Testabilidad** | Fácil crear pruebas unitarias mockeando puertos; integración con BD real |
| **Mantenibilidad** | Cambios en una capa no afectan a las demás |

</div>

### 4.3 Patrones de Diseño Implementados

<div align="center">

| **Patrón** | **Implementación** | **Propósito** |
|:--------------|:----------------------|:-----------------|
| **Builder** | `@Builder` de Lombok en `Parche`, `Member`, `Invitation`, DTOs y entidades JPA | Construir objetos complejos con múltiples campos opcionales de forma legible e inmutable |
| **Repository** | `Spring Data JPA` repositories + adaptadores que implementan puertos de salida del dominio | Abstraer el acceso a datos y permitir cambiar la tecnología de persistencia sin afectar el dominio |
| **Adapter (Ports & Adapters)** | `ParcheRepositoryAdapter`, `MemberRepositoryAdapter`, `InvitationRepositoryAdapter`, `NotificacionAdapter`, `RabbitMQEventPublisher` | Implementar los puertos de salida definidos en el dominio para conectar con infraestructura externa |
| **Use Case / Interactor** | Casos de uso en `application/usecase/` que implementan interfaces `InputPort` | Orquestar flujos de negocio completos manteniendo el dominio puro y sin dependencias externas |
| **DTO (Data Transfer Object)** | `CreateParcheRequest`, `UpdateParcheRequest`, `RespondInvitationRequest`, `ParcheResponse`, `MemberResponse`, `InvitationResponse` | Separar la representación de los datos de entrada/salida de las entidades del dominio |
| **Observer (Eventos de dominio)** | `ApplicationEventPublisher` + `@EventListener` asíncrono para `NuevoMiembroEvent`, `InvitationSentEvent`, `InvitationAcceptedEvent` | Notificar cambios a otros componentes del sistema sin acoplamiento directo |
| **Mapper (MapStruct)** | `ParcheMapper`, `ParcheEntityMapper`, `MemberEntityMapper`, `InvitationEntityMapper` | Transformar objetos entre capas (dominio ↔ persistencia, dominio ↔ DTO) de forma segura y eficiente |
| **Scheduler** | `@Scheduled` en `ParcheArchiveScheduler` | Ejecutar tareas programadas (archivar parches vencidos) sin intervención manual |
| **Global Exception Handler** | `@ControllerAdvice` + `@ExceptionHandler` en `GlobalExceptionHandler` | Centralizar el manejo de errores HTTP con respuestas JSON estandarizadas |
| **Aggregate Root** | `Parche` como raíz de agregado | Frontera de consistencia del dominio: única entidad que se comunica directamente con el exterior de la capa de dominio |
| **State** | `Invitation` (contexto) + `InvitationState` (interfaz) + `PendingState`, `AcceptedState`, `RejectedState` | Delegar el comportamiento de la invitación al estado activo, mutando dinámicamente sin condicionales complejos |

</div>

Además de los patrones descritos en la tabla anterior, el núcleo del dominio incorpora dos patrones estructurales adicionales que merecen una explicación detallada por su papel central en la integridad y consistencia del modelo:

**Aggregate Root.** Este patrón se localiza centralmente en la clase `Parche`. Este patrón funciona como una frontera de consistencia donde `Parche` es la única entidad que se comunica directamente con el exterior de la capa de dominio. Las demás entidades, como Miembro, Invitación, Publicación y Comentario, se ubican colgadas internamente de esta raíz. Su utilidad radica en que ninguna de estas clases secundarias puede ser modificada o creada de forma aislada desde afuera; cualquier operación de negocio debe invocar obligatoriamente a un método de `Parche`, garantizando por ejemplo que no existan publicaciones que no estén asociadas a un parche.

**State.** Este patrón de diseño se encuentra implementado explícitamente en el núcleo del microservicio de parches, específicamente dentro de la entidad `Invitation`, que actúa como la clase de contexto, y la interfaz del dominio `InvitationState`. En lugar de manejar el flujo con enumeraciones estáticas, la invitación delega sus acciones de negocio a tres clases de estado concretas llamadas `PendingState`, `AcceptedState` y `RejectedState`. De esta manera, cuando el agregado `Parche` solicita aceptar o rechazar una solicitud, la invitación transfiere la operación a la instancia del estado activo en ese momento, logrando que el comportamiento de la entidad mute dinámicamente y que las transiciones de estado se ejecuten de forma segura y centralizada en objetos independientes.

El uso de esta estructura ayuda a obtener un código con alta cohesión y un sólido encapsulamiento, puesto que las reglas y consecuencias de cada transición quedan aisladas en su respectiva clase, facilitando además la mantenibilidad futura en caso de requerir nuevos estados en la aplicación. Gracias a esto, se evita por completo la acumulación de bloques condicionales complejos y estructuras densas de control dentro de la entidad principal. Asimismo, se impide la aparición de inconsistencias lógicas en la memoria del sistema, ya que si una solicitud ya se encuentra aprobada o denegada y se intenta volver a aceptar o rechazar, la clase concreta se encarga de disparar una excepción de dominio de forma inmediata, impidiendo corrupciones en el flujo de datos.

![Diagrama de clases ParchesAlphaECI.drawio (1).png](Images/Uml/Diagrama%20de%20clases%20ParchesAlphaECI.drawio%20%281%29.png)

### 4.4 Estrategia de Ramas (Git Flow)

El proyecto maneja **GitFlow**, el modelo de ramificación para el control de versiones de Git.

#### Ramas y propósito

##### `main`
- **Propósito:** rama **estable** con la versión final (lista para demo/producción).
- **Reglas:**
  - Solo recibe merges desde `release/*` y `hotfix/*`.
  - Cada merge a `main` debe crear un **tag** SemVer (`vX.Y.Z`).
  - Rama **protegida**: PR obligatorio, 1–2 aprobaciones, checks de CI en verde.

##### `develop`
- **Propósito:** integración continua de trabajo; base de nuevas funcionalidades.
- **Reglas:**
  - Recibe merges desde `feature/*` y también desde `release/*` al finalizar un release.
  - Rama **protegida** similar a `main`.

##### `feature/*`
- **Propósito:** desarrollo de una funcionalidad, refactor o spike.
- **Base:** `develop`.
- **Cierre:** se fusiona a `develop` mediante PR.

##### `release/*`
- **Propósito:** congelar cambios para estabilizar pruebas, textos y versiones previas al deploy.
- **Base:** `develop`.
- **Cierre:** merge a `main` (crear **tag** `vX.Y.Z`) **y** merge de vuelta a `develop`.
- **Ejemplo de nombre:** `release/1.3.0`

##### `hotfix/*`
- **Propósito:** corregir un bug **crítico** detectado en `main`.
- **Base:** `main`.
- **Cierre:** merge a `main` (crear **tag** de **PATCH**) **y** merge a `develop` para mantener paridad.
- **Ejemplos de nombre:** `hotfix/fix-blank-screen`, `hotfix/css-broken-header`

#### Convenciones para crear ramas

**`feature/*`**
- Formato: `feature/[nombre-funcionalidad]`
- Ejemplos: `feature/gestionParches`, `feature/sistemaInvitaciones`
- Usar **PascalCase**, máximo 50 caracteres, descripción clara.

**`release/*`**
- Formato: `release/[version]`
- Ejemplo: `release/1.0.0`

**`hotfix/*`**
- Formato: `hotfix/[descripcion-breve-del-fix]`
- Ejemplos: `hotfix/corregirArchivoAutomatico`, `hotfix/fixValidacionCupo`

#### Convenciones para crear commits

Formato: `[tipo]: [descripción específica de la acción]`

| Tipo | Uso |
|------|-----|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de errores |
| `docs` | Cambios en documentación |

### 4.5 Despliegue e Infraestructura

El microservicio de **Parches** se ejecutará como un contenedor Docker en **Azure Web App**, respaldado por una arquitectura robusta de CI/CD.

- **Ejecución:** Contenedor Docker en Azure Web App (imagen construida con Dockerfile multi-stage).
- **Base de datos:** **PostgreSQL 18** provisionada por Azure con variables de entorno inyectadas automáticamente (`PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`).
- **CI/CD (GitHub Actions):**
  - Pruebas unitarias (JUnit 5) y cobertura (JaCoCo).
  - Tests de integración contra PostgreSQL 18 en servicio de GitHub Actions.
  - Despliegue a Azure vía publish profile cuando esté configurado.
- **Construcción:** Dockerfile multi-stage (Maven Build → JRE 21 Alpine Runtime).
- **Configuración:** Variables de entorno gestionadas desde Azure App Service.

<div align="center">

| **Componente** | **Descripción** |
|------------------|-------------------|
| Azure App Service | Hosting del contenedor Docker del microservicio |
| Azure Database for PostgreSQL | Base de datos relacional gestionada |
| GitHub Actions | Automatización de CI/CD y calidad de código |
| RabbitMQ (CloudAMQP / Azure) | Mensajería asíncrona entre microservicios |
| Swagger UI | Documentación interactiva en `/swagger-ui/index.html` |

</div>

---

## 5. Diagramas

### 5.1 Diagrama de Base de Datos

El diagrama de base de datos relacional modela de forma consistente la persistencia en PostgreSQL para el microservicio de Parches, mapeando con precisión los tipos de datos y las restricciones del modelo de dominio. La estructura se fundamenta en la tabla principal `parches` y se desacopla mediante relaciones de uno a muchos (1:N) hacia las entidades satélites `members`, `invitations` y `posts`, empleando identificadores únicos globales (UUID) como llaves primarias para garantizar la consistencia en entornos distribuidos.

![DiagramaDATOSParchesAlphaECI.drawio (1).png](Images/Uml/DiagramaDATOSParchesAlphaECI.drawio%20%281%29.png)

El microservicio de Parches utiliza **PostgreSQL 18** como motor de base de datos relacional. Contiene tres tablas principales: `parches`, `members` e `invitations`.

#### Tabla: `parches`

<div align="center">

| Campo | Tipo | Descripción | Restricciones |
|:---|:---|:---|:---|
| **id** | `UUID` | Identificador único del parche | Primary Key, `gen_random_uuid()` |
| **name** | `VARCHAR(100)` | Nombre del parche | NOT NULL |
| **description** | `VARCHAR(500)` | Descripción del parche | Opcional |
| **place** | `VARCHAR(200)` | Lugar de encuentro | NOT NULL |
| **category** | `VARCHAR(20)` | Categoría (MUSIC, SOCCER, etc.) | Opcional |
| **type** | `VARCHAR(20)` | Tipo: PUBLIC o PRIVATE | NOT NULL, CHECK |
| **date** | `DATE` | Fecha de realización | NOT NULL |
| **hour** | `TIME` | Hora de inicio | NOT NULL |
| **maximum_quota** | `INTEGER` | Cupo máximo de participantes | NOT NULL, CHECK (2–30) |
| **date_realization** | `TIMESTAMP` | Fecha y hora combinadas | NOT NULL |
| **status** | `VARCHAR(20)` | Estado: ACTIVE o FILED | NOT NULL, DEFAULT 'ACTIVE' |
| **captain_id** | `UUID` | ID del capitán del parche | NOT NULL |
| **creation_date** | `TIMESTAMP` | Fecha de creación | NOT NULL, DEFAULT now() |
| **event_id** | `UUID` | ID de evento externo asociado | Opcional |

</div>

#### Tabla: `members`

<div align="center">

| Campo | Tipo | Descripción | Restricciones |
|:---|:---|:---|:---|
| **id** | `UUID` | Identificador único de la membresía | Primary Key |
| **parche_id** | `UUID` | Parche al que pertenece | FK → parches(id) |
| **student_id** | `UUID` | ID del estudiante miembro | NOT NULL |
| **union_date** | `TIMESTAMP` | Fecha de ingreso al parche | NOT NULL, DEFAULT now() |
| **member_role** | `VARCHAR(20)` | Rol: CAPTAIN o STUDENT | NOT NULL, CHECK |

</div>

#### Tabla: `invitations`

<div align="center">

| Campo | Tipo | Descripción | Restricciones |
|:---|:---|:---|:---|
| **id** | `UUID` | Identificador único de la invitación | Primary Key |
| **parche_id** | `UUID` | Parche al que se invita | FK → parches(id) |
| **captain_id** | `UUID` | ID del capitán que envió la invitación | NOT NULL |
| **invited_student_id** | `UUID` | ID del estudiante invitado | NOT NULL |
| **status** | `VARCHAR(20)` | Estado: PENDING, ACCEPTED o REJECTED | NOT NULL, DEFAULT 'PENDING' |
| **sent_at** | `TIMESTAMP` | Fecha de envío de la invitación | NOT NULL |
| **responded_at** | `TIMESTAMP` | Fecha de respuesta | Opcional |

</div>

### 5.2 Diagrama de Clases del Dominio

![Diagrama de clases ParchesAlphaECI.drawio (1).png](Images/Uml/Diagrama%20de%20clases%20ParchesAlphaECI.drawio%20%281%29.png)

**Resumen del diseño de dominio:**

La arquitectura de dominio se centra en las entidades **Parche**, **Member** e **Invitation**.

- **Entidad de Dominio:** `Parche` contiene identificadores, datos del encuentro y lista de miembros. El campo `status` gestiona su ciclo de vida (ACTIVE → FILED).
- **Membresía:** `Member` vincula un estudiante con un parche y almacena su rol (CAPTAIN/STUDENT) y fecha de ingreso.
- **Invitación:** `Invitation` controla el flujo de incorporación a parches privados con su propio ciclo de vida (PENDING → ACCEPTED/REJECTED), delegado a las clases de estado descritas en la sección 4.3.
- **Enumeraciones:** `ParcheType`, `ParcheStatus`, `ParcheCategory`, `MemberRole` e `InvitationStatus` garantizan valores controlados en todo el dominio.

> Este diseño asegura la integridad de los parches y permite extender las funcionalidades sin afectar las reglas de negocio centrales.

#### DTOs Principales

**Request DTOs**

| DTO | Atributos Principales | Descripción |
|---|---|---|
| **CreateParcheRequest** | name, description, place, category, date, hour, maximumQuota, type, eventId | Solicitud para crear un nuevo parche. Valida fecha futura y cupo entre 2 y 30. |
| **UpdateParcheRequest** | name, description, place, category, date, hour, maximumQuota, type, eventId | Actualización parcial de datos del parche. Todos los campos son opcionales. |
| **RespondInvitationRequest** | answer (ACCEPTED / REJECTED) | Respuesta del estudiante invitado a una invitación pendiente. |

**Response DTOs**

| DTO | Atributos Principales | Descripción |
|---|---|---|
| **ParcheResponse** | id, name, description, place, category, type, status, maximumQuota, actualMembers, captainId, dateRealization | Respuesta completa con los datos del parche, incluyendo conteo de miembros. |
| **MemberResponse** | id, parcheId, studentId, memberRole, unionDate | Confirmación de membresía con rol y fecha de ingreso. |
| **InvitationResponse** | id, parcheId, invitedStudentId, status, sentAt, respondedAt | Estado actual de una invitación con sus timestamps. |
| **ErrorResponse** | status, message | Estructura estandarizada para el retorno de excepciones. |

**Enums del Dominio**

| Enum | Valores | Descripción |
|---|---|---|
| **ParcheType** | PUBLIC, PRIVATE | Tipo de acceso al parche (libre o por invitación). |
| **ParcheStatus** | ACTIVE, FILED | Estado del ciclo de vida del parche. |
| **ParcheCategory** | MUSIC, PROGRAMMING, PHOTOGRAPHY, DESIGN, SOCCER, HIKING, READING, GAMING, GASTRONOMY, YOGA, ENTREPRENEURSHIP, ART, CINEMA, DANCE, VOLUNTEERING, COOKING | Categoría temática del parche. |
| **MemberRole** | CAPTAIN, STUDENT | Rol del miembro dentro del parche. |
| **InvitationStatus** | PENDING, ACCEPTED, REJECTED | Estado de la invitación en su ciclo de vida. |

### 5.3 Diagrama de Componentes



#### Vista Específica

Este diagrama detalla la arquitectura del microservicio estructurada bajo los principios de la Arquitectura Hexagonal y la segregación de responsabilidades. En la periferia izquierda, los adaptadores de entrada (`ParcheRestController` e `InvitationRestController`) capturan las solicitudes HTTP y las delegan hacia la capa de casos de uso (Ports / In), aislando cada acción del sistema en interfaces independientes y planas.

En el núcleo central se orquesta la lógica de negocio a través de los servicios de aplicación (`ParcheCommandService`, `ParcheQueryService` e `InvitationApplicationService`), los cuales coordinan las reglas con las entidades del dominio (`Parche`, `Member`, `Invitation`). Finalmente, mediante la inversión de dependencias, el flujo se conecta con los puertos de salida (Ports / Out / SPI) donde los adaptadores de infraestructura (`ParcheDatabaseAdapter` e `InvitationDatabaseAdapter`) canalizan el almacenamiento definitivo de forma modular hacia una única base de datos física PostgreSQL.

![DiagramaEspecificoParchesAlphaECI-Página-4.drawio.png](Images/Uml/DiagramaEspecificoParchesAlphaECI-P%C3%A1gina-4.drawio.png)

> El microservicio de Parches gestiona todo el ciclo de vida de los encuentros estudiantiles, integrándose con otros servicios del ecosistema DOSW a través de eventos.

---

## 6. Funcionalidades — Endpoints

### 6.1 Manejo de Errores (Global Exception Handler)

El microservicio de **Parches** implementa un **mecanismo centralizado de manejo de errores** que garantiza uniformidad, claridad y seguridad en todas las respuestas enviadas al cliente cuando ocurre un fallo.

#### Estrategia general

El sistema utiliza una **clase global** `GlobalExceptionHandler` con la anotación `@ControllerAdvice` que intercepta todas las excepciones lanzadas desde los controladores REST. Cada excepción de dominio se transforma en una respuesta **JSON estandarizada** con el código HTTP apropiado.

**Características principales:**

- Centraliza la captura de excepciones desde todos los controladores.
- Retorna mensajes JSON consistentes con el mismo formato estructurado.
- Asigna códigos HTTP según la naturaleza del error (400, 403, 404, 409, 500).
- Define mensajes descriptivos que ayudan tanto al desarrollador como al usuario.
- Mantiene la aplicación limpia, eliminando bloques try-catch redundantes.
- Mejora la trazabilidad y facilita la depuración en entornos de prueba y producción.

#### Excepciones de dominio manejadas

<div align="center">

| Excepción | HTTP | Escenario |
|:----------------|:----------:|:----------------|
| `ParcheNotFoundException` | 404 | El parche solicitado no existe en la base de datos |
| `AccessDeniedException` | 403 | El usuario no tiene permisos para la operación (ej. no es el capitán) |
| `MaximumCapacityReachedException` | 409 | El parche ya alcanzó su cupo máximo de participantes |
| `MaxHangoutsReachedException` | 409 | El estudiante ya tiene 5 parches activos simultáneos |
| `StudentAlreadyMemberException` | 409 | El estudiante ya es miembro del parche |
| `DuplicateInvitationException` | 409 | Ya existe una invitación pendiente para ese estudiante en ese parche |
| `InvitationAlreadyRespondedException` | 409 | La invitación ya fue aceptada o rechazada previamente |
| `ConstraintViolationException` | 400 | Violación de restricción de base de datos (ej. datos inconsistentes) |
| `MethodArgumentNotValidException` | 400 | Validación de campos del DTO fallida (`@NotBlank`, `@Min`, etc.) |
| `IllegalArgumentException` | 400 | Argumento inválido en la lógica de negocio |
| `RuntimeException` | 500 | Error inesperado del servidor |

</div>

---

### 6.2 Crear Parche

Permite crear un nuevo parche indicando nombre, lugar, categoría, tipo (público/privado), cupo máximo y fecha. El estudiante que lo crea se convierte automáticamente en su capitán.

**Endpoint principal:** `POST /api/v1/parches`

#### Estructura de la Solicitud (Request)

<div align="center">

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| name | String | Obligatorio, no vacío | Nombre del parche. |
| description | String | Opcional | Descripción breve del parche. |
| place | String | Obligatorio, no vacío | Lugar de encuentro. |
| category | Enum | Obligatorio | Categoría del parche (MUSIC, PROGRAMMING, SOCCER, etc.). |
| date | LocalDate | Obligatorio, hoy o futuro | Fecha de realización (yyyy-MM-dd). |
| hour | LocalTime | Obligatorio | Hora de inicio (HH:mm:ss). |
| maximumQuota | Integer | 2–30 | Cupo máximo de participantes. |
| type | Enum | Obligatorio | Tipo: PUBLIC o PRIVATE. |
| eventId | UUID | Opcional | ID del evento externo asociado. |

</div>

#### Estructura de la Respuesta (Response)

<div align="center">

| Campo | Tipo | Descripción |
|---|---|---|
| id | UUID | Identificador único del parche creado. |
| name | String | Nombre del parche. |
| description | String | Descripción del parche. |
| place | String | Lugar de encuentro. |
| category | Enum | Categoría del parche. |
| type | Enum | Tipo (PUBLIC / PRIVATE). |
| status | Enum | Estado actual (ACTIVE / FILED). |
| maximumQuota | Integer | Cupo máximo. |
| actualMembers | Integer | Número actual de miembros. |
| captainId | UUID | ID del estudiante capitán. |
| dateRealization | LocalDateTime | Fecha y hora combinadas de realización. |

</div>

#### Happy Path (Ejemplo de Uso Exitoso)

1. El capitán envía un POST con los datos del parche e incluye su ID en el header `X-User-Id`.
2. El sistema valida que la fecha sea presente o futura y que el cupo esté entre 2 y 30.
3. Se verifica que el estudiante no haya alcanzado el límite de 5 parches activos.
4. Se crea el parche en estado `ACTIVE` y el capitán queda registrado como primer miembro.
5. Se retorna `201 CREATED` con los datos del parche.

**Request (Solicitud):**
```json
POST /api/v1/parches
Headers: X-User-Id: 550e8400-e29b-41d4-a716-446655440001

{
  "name": "Parche de estudio",
  "description": "Repaso grupal de matemáticas",
  "place": "Café del edificio Bernardo",
  "category": "PROGRAMMING",
  "date": "2026-06-15",
  "hour": "14:00:00",
  "maximumQuota": 10,
  "type": "PUBLIC"
}
```

**Response (Respuesta):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440000",
  "name": "Parche de estudio",
  "description": "Repaso grupal de matemáticas",
  "place": "Café del edificio Bernardo",
  "category": "PROGRAMMING",
  "type": "PUBLIC",
  "status": "ACTIVE",
  "maximumQuota": 10,
  "actualMembers": 1,
  "captainId": "550e8400-e29b-41d4-a716-446655440001",
  "dateRealization": "2026-06-15T14:00:00"
}
```

#### Diagrama de Secuencia

![Diagrama de Secuencia Crear Parche](docs/uml/CrearParche.png)

<details>
<summary><strong>Explicación del Flujo</strong></summary>

El proceso inicia cuando el capitán envía un POST al `ParcheController`. El `CreateParcheUseCase` valida las restricciones de negocio (fecha futura, cupo válido, límite de parches activos). Se crea el parche con estado `ACTIVE`, se persiste en PostgreSQL vía el adaptador de repositorio, y el capitán queda automáticamente inscrito como primer miembro. Se retorna la respuesta con el parche creado.

</details>

#### Tipos de errores manejados

<div align="center">

| Código HTTP | Escenario | Mensaje de Error |
|:------------------:|:----------------|:------------------------|
| 400 Bad Request | Datos inválidos | `"Name cannot be blank"` |
| 400 Bad Request | Fecha pasada | `"Date must be today or in the future"` |
| 409 Conflict | Límite de parches | `"Student has reached the maximum number of active hangouts"` |

</div>

---

### 6.3 Consultar Parches

Permite listar todos los parches existentes con filtros opcionales por tipo y estado.

**Endpoint principal:** `GET /api/v1/parches`

#### Estructura de la Solicitud (Request)

<div align="center">

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| tipo | Enum | Opcional (query param) | Filtrar por PUBLIC o PRIVATE. |
| estado | Enum | Opcional (query param) | Filtrar por ACTIVE o FILED. |

</div>

#### Estructura de la Respuesta (Response)

<div align="center">

| Campo | Tipo | Descripción |
|---|---|---|
| (lista) | List\<ParcheResponse\> | Lista de parches que cumplen los filtros. |

</div>

#### Happy Path (Ejemplo de Uso Exitoso)

1. El cliente consulta los parches enviando filtros opcionales.
2. El sistema ejecuta la búsqueda en la base de datos aplicando los filtros.
3. Se retorna la lista de parches encontrados.

**Request (Solicitud):**
```
GET /api/v1/parches?tipo=PUBLIC&estado=ACTIVE
```

**Response (Respuesta):**
```json
[
  {
    "id": "770e8400-e29b-41d4-a716-446655440000",
    "name": "Parche de estudio",
    "type": "PUBLIC",
    "status": "ACTIVE",
    "actualMembers": 3,
    "maximumQuota": 10,
    "dateRealization": "2026-06-15T14:00:00"
  }
]
```

#### Diagrama de Secuencia

![Diagrama de Secuencia Consultar Parches](docs/uml/ConsultarParches.png)

<details>
<summary><strong>Explicación del Flujo</strong></summary>

El `ParcheController` recibe la petición con los filtros opcionales y los delega al `GetParcheUseCase`. Este invoca al repositorio para recuperar los parches que coinciden con los criterios. Los resultados se transforman a `ParcheResponse` y se retorna la lista al cliente.

</details>

#### Tipos de errores manejados

<div align="center">

| Código HTTP | Escenario | Mensaje de Error |
|:------------------:|:----------------|:------------------------|
| 200 OK | Sin resultados | Lista vacía `[]` |
| 500 Internal Server Error | Error interno | `"Unexpected error"` |

</div>

---

### 6.4 Consultar Parche por ID

Permite recuperar la información detallada de un parche específico.

**Endpoint principal:** `GET /api/v1/parches/{id}`

#### Estructura de la Solicitud (Request)

<div align="center">

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | UUID | Obligatorio (path) | Identificador único del parche. |

</div>

#### Happy Path (Ejemplo de Uso Exitoso)

1. El cliente envía el `id` del parche en el path.
2. El sistema busca el parche en la base de datos.
3. Si existe, retorna la información completa.

**Request (Solicitud):**
```
GET /api/v1/parches/770e8400-e29b-41d4-a716-446655440000
```

#### Diagrama de Secuencia

<details>
<summary><strong>Explicación del Flujo</strong></summary>

El `ParcheController` recibe el UUID del parche. El `GetParcheUseCase` busca el parche por ID en el repositorio. Si no se encuentra, lanza `ParcheNotFoundException`. Si existe, convierte la entidad a `ParcheResponse` y retorna la respuesta con HTTP 200.

</details>

#### Tipos de errores manejados

<div align="center">

| Código HTTP | Escenario | Mensaje de Error |
|:------------------:|:----------------|:------------------------|
| 404 Not Found | Parche no existe | `"Parche not found"` |

</div>

---

### 6.5 Actualizar Parche

Permite al capitán modificar los datos de un parche existente.

**Endpoint principal:** `PATCH /api/v1/parches/{id}`

#### Estructura de la Solicitud (Request)

<div align="center">

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| name | String | Opcional | Nuevo nombre del parche. |
| description | String | Opcional | Nueva descripción. |
| place | String | Opcional | Nuevo lugar de encuentro. |
| category | Enum | Opcional | Nueva categoría. |
| date | LocalDate | Opcional | Nueva fecha (yyyy-MM-dd). |
| hour | LocalTime | Opcional | Nueva hora (HH:mm). |
| maximumQuota | Integer | Opcional, 2–50 | Nuevo cupo máximo. |
| type | Enum | Opcional | Nuevo tipo (PUBLIC / PRIVATE). |
| eventId | UUID | Opcional | Nuevo ID de evento externo. |

</div>

#### Happy Path (Ejemplo de Uso Exitoso)

1. El capitán envía un PATCH con los campos a actualizar e incluye su ID en `X-User-Id`.
2. El sistema verifica que el solicitante sea el capitán del parche.
3. Se actualizan únicamente los campos enviados.
4. Se retorna `200 OK` con el parche actualizado.

**Request (Solicitud):**
```json
PATCH /api/v1/parches/770e8400-e29b-41d4-a716-446655440000
Headers: X-User-Id: 550e8400-e29b-41d4-a716-446655440001

{
  "place": "Biblioteca central",
  "maximumQuota": 15
}
```

#### Diagrama de Secuencia

Diagrama de secuencia de actualizar parche 
<details>
<summary><strong>Explicación del Flujo</strong></summary>

El `ParcheController` delega al `UpdateParcheUseCase`. Este verifica que el solicitante sea el capitán del parche; si no lo es, lanza `AccessDeniedException`. Si la validación pasa, aplica los cambios sobre los campos enviados y persiste la entidad actualizada.

</details>

#### Tipos de errores manejados

<div align="center">

| Código HTTP | Escenario | Mensaje de Error |
|:------------------:|:----------------|:------------------------|
| 403 Forbidden | No es el capitán | `"Only the captain can update this parche"` |
| 404 Not Found | Parche no existe | `"Parche not found"` |

</div>

---

### 6.6 Archivar Parche

Permite al capitán archivar un parche, cambiando su estado de `ACTIVE` a `FILED`.

**Endpoint principal:** `DELETE /api/v1/parches/{id}`

#### Estructura de la Solicitud (Request)

<div align="center">

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | UUID | Obligatorio (path) | Identificador del parche a archivar. |
| X-User-Id | UUID | Obligatorio (header) | ID del estudiante que solicita el archivo. |

</div>

#### Happy Path (Ejemplo de Uso Exitoso)

1. El capitán envía un DELETE con su ID en el header.
2. El sistema verifica que sea el capitán.
3. El parche pasa a estado `FILED` (no se elimina físicamente).
4. Se retorna `204 No Content`.

**Request (Solicitud):**
```
DELETE /api/v1/parches/770e8400-e29b-41d4-a716-446655440000
Headers: X-User-Id: 550e8400-e29b-41d4-a716-446655440001
```

#### Diagrama de Secuencia


<details>
<summary><strong>Explicación del Flujo</strong></summary>

El `CloseParcheUseCase` valida que el solicitante es el capitán del parche. Si la verificación es exitosa, cambia el estado del parche a `FILED` y lo persiste. La operación es un soft delete: el registro permanece en la base de datos pero deja de aparecer en listados activos.

</details>

#### Tipos de errores manejados

<div align="center">

| Código HTTP | Escenario | Mensaje de Error |
|:------------------:|:----------------|:------------------------|
| 403 Forbidden | No es el capitán | `"Only the captain can archive this parche"` |
| 404 Not Found | Parche no existe | `"Parche not found"` |

</div>

---

### 6.7 Unirse a un Parche Público

Permite a un estudiante unirse directamente a un parche público activo sin necesidad de invitación.

**Endpoint principal:** `POST /api/v1/parches/{parcheId}/miembros`

#### Estructura de la Solicitud (Request)

<div align="center">

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| parcheId | UUID | Obligatorio (path) | ID del parche al que desea unirse. |
| X-User-Id | UUID | Obligatorio (header) | ID del estudiante. |

</div>

#### Estructura de la Respuesta (Response)

<div align="center">

| Campo | Tipo | Descripción |
|---|---|---|
| id | UUID | Identificador único de la membresía. |
| parcheId | UUID | ID del parche. |
| studentId | UUID | ID del estudiante. |
| memberRole | Enum | Rol del miembro (STUDENT / CAPTAIN). |
| unionDate | LocalDateTime | Fecha y hora de ingreso. |

</div>

#### Happy Path (Ejemplo de Uso Exitoso)

1. El estudiante envía un POST con el `parcheId` en el path y su ID en el header.
2. El sistema verifica que el parche esté activo y no archivado.
3. Se valida que el estudiante no sea ya miembro y que haya cupo disponible.
4. Se verifica que el estudiante no tenga 5 parches activos.
5. Se crea la membresía y se retorna `201 CREATED`.

**Request (Solicitud):**
```
POST /api/v1/parches/770e8400-e29b-41d4-a716-446655440000/miembros
Headers: X-User-Id: 880e8400-e29b-41d4-a716-446655440002
```

**Response (Respuesta):**
```json
{
  "id": "990e8400-e29b-41d4-a716-446655440003",
  "parcheId": "770e8400-e29b-41d4-a716-446655440000",
  "studentId": "880e8400-e29b-41d4-a716-446655440002",
  "memberRole": "STUDENT",
  "unionDate": "2026-05-06T10:30:00"
}
```

#### Diagrama de Secuencia

Uniser a parche
<details>
<summary><strong>Explicación del Flujo</strong></summary>

El `MemberController` delega al `JoinParcheUseCase`. Este verifica que el parche exista y esté activo, que no haya superado su cupo máximo, que el estudiante no sea miembro ya, y que el estudiante no tenga más de 5 parches activos. Si todo es válido, se persiste la membresía y se publica un `NuevoMiembroEvent`.

</details>

#### Tipos de errores manejados

<div align="center">

| Código HTTP | Escenario | Mensaje de Error |
|:------------------:|:----------------|:------------------------|
| 400 Bad Request | Parche archivado | `"Cannot join a filed parche"` |
| 404 Not Found | Parche no existe | `"Parche not found"` |
| 409 Conflict | Ya es miembro | `"Student is already a member of this parche"` |
| 409 Conflict | Cupo lleno | `"Maximum capacity reached"` |
| 409 Conflict | Límite de parches | `"Student has reached the maximum number of active hangouts"` |

</div>

---

### 6.8 Salir de un Parche

Permite a un estudiante abandonar voluntariamente un parche del que es miembro.

**Endpoint principal:** `DELETE /api/v1/parches/{parcheId}/miembros`

#### Estructura de la Solicitud (Request)

<div align="center">

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| parcheId | UUID | Obligatorio (path) | ID del parche. |
| X-User-Id | UUID | Obligatorio (header) | ID del estudiante que desea salir. |

</div>

#### Happy Path (Ejemplo de Uso Exitoso)

1. El estudiante envía un DELETE con su ID en el header.
2. El sistema verifica que sea miembro del parche y que no sea el capitán.
3. Se elimina la membresía y se retorna `204 No Content`.

**Request (Solicitud):**
```
DELETE /api/v1/parches/770e8400-e29b-41d4-a716-446655440000/miembros
Headers: X-User-Id: 880e8400-e29b-41d4-a716-446655440002
```

#### Diagrama de Secuencia

Diagrama de secuencia de slair parche
<details>
<summary><strong>Explicación del Flujo</strong></summary>

El `LeaveParcheUseCase` verifica que el parche esté activo y que el estudiante sea miembro. Si el estudiante es el capitán, se lanza una excepción indicando que debe transferir el liderazgo primero. Si la validación pasa, se elimina la membresía de la base de datos.

</details>

#### Tipos de errores manejados

<div align="center">

| Código HTTP | Escenario | Mensaje de Error |
|:------------------:|:----------------|:------------------------|
| 400 Bad Request | Es el capitán | `"Captain must transfer leadership before leaving"` |
| 400 Bad Request | Parche archivado | `"Cannot leave a filed parche"` |
| 404 Not Found | No es miembro | `"Member not found in this parche"` |

</div>

---

### 6.9 Enviar Invitación

Permite al capitán de un parche privado invitar a un estudiante específico.

**Endpoint principal:** `POST /api/v1/parches/{parcheId}/invitaciones/{studentId}`

#### Estructura de la Solicitud (Request)

<div align="center">

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| parcheId | UUID | Obligatorio (path) | ID del parche privado. |
| studentId | UUID | Obligatorio (path) | ID del estudiante a invitar. |
| X-User-Id | UUID | Obligatorio (header) | ID del capitán que envía la invitación. |

</div>

#### Estructura de la Respuesta (Response)

<div align="center">

| Campo | Tipo | Descripción |
|---|---|---|
| id | UUID | Identificador único de la invitación. |
| parcheId | UUID | ID del parche al que se invita. |
| invitedStudentId | UUID | ID del estudiante invitado. |
| status | Enum | Estado de la invitación (PENDING). |
| sentAt | LocalDateTime | Fecha y hora de envío. |
| respondedAt | LocalDateTime | Fecha y hora de respuesta (null si pendiente). |

</div>

#### Happy Path (Ejemplo de Uso Exitoso)

1. El capitán envía un POST con el ID del parche y el ID del estudiante a invitar.
2. El sistema verifica que el solicitante sea el capitán del parche.
3. Se valida que el estudiante no sea ya miembro y no tenga una invitación pendiente.
4. Se crea la invitación con estado `PENDING` y se retorna `201 CREATED`.

**Request (Solicitud):**
```
POST /api/v1/parches/770e8400-e29b-41d4-a716-446655440000/invitaciones/880e8400-e29b-41d4-a716-446655440002
Headers: X-User-Id: 550e8400-e29b-41d4-a716-446655440001
```

**Response (Respuesta):**
```json
{
  "id": "aa0e8400-e29b-41d4-a716-446655440004",
  "parcheId": "770e8400-e29b-41d4-a716-446655440000",
  "invitedStudentId": "880e8400-e29b-41d4-a716-446655440002",
  "status": "PENDING",
  "sentAt": "2026-05-06T11:00:00",
  "respondedAt": null
}
```

#### Diagrama de Secuencia

Diagrama de secuencia de invitación de parche
<details>
<summary><strong>Explicación del Flujo</strong></summary>

El `InvitationUseCase` verifica que el remitente sea el capitán. Luego confirma que el estudiante no sea ya miembro del parche y que no tenga una invitación activa pendiente. Si todo es válido, crea la invitación con estado `PENDING`, la persiste y publica un `InvitationSentEvent` para notificaciones.

</details>

#### Tipos de errores manejados

<div align="center">

| Código HTTP | Escenario | Mensaje de Error |
|:------------------:|:----------------|:------------------------|
| 403 Forbidden | No es el capitán | `"Only the captain can send invitations"` |
| 409 Conflict | Ya es miembro | `"Student is already a member"` |
| 409 Conflict | Invitación duplicada | `"Student already has a pending invitation"` |

</div>

---

### 6.10 Responder Invitación

Permite al estudiante invitado aceptar o rechazar una invitación pendiente. Internamente, esta operación es resuelta por el patrón State: la invitación delega la transición a la clase de estado activa (`PendingState`), la cual valida que aún no se haya respondido antes de mutar hacia `AcceptedState` o `RejectedState`.

**Endpoint principal:** `PATCH /api/v1/invitaciones/{invitationId}`

#### Estructura de la Solicitud (Request)

<div align="center">

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| invitationId | UUID | Obligatorio (path) | ID de la invitación a responder. |
| X-User-Id | UUID | Obligatorio (header) | ID del estudiante invitado. |
| answer | Enum | Obligatorio (body) | Respuesta: ACCEPTED o REJECTED. |

</div>

#### Happy Path (Ejemplo de Uso Exitoso)

1. El estudiante invitado envía PATCH con su respuesta y su ID en el header.
2. El sistema verifica que sea el destinatario de la invitación.
3. Verifica que la invitación esté en estado `PENDING`.
4. Si acepta: se crea automáticamente la membresía en el parche.
5. La invitación queda con estado `ACCEPTED` o `REJECTED`.

**Request (Solicitud):**
```json
PATCH /api/v1/invitaciones/aa0e8400-e29b-41d4-a716-446655440004
Headers: X-User-Id: 880e8400-e29b-41d4-a716-446655440002

{
  "answer": "ACCEPTED"
}
```

**Response (Respuesta):**
```json
{
  "id": "aa0e8400-e29b-41d4-a716-446655440004",
  "parcheId": "770e8400-e29b-41d4-a716-446655440000",
  "invitedStudentId": "880e8400-e29b-41d4-a716-446655440002",
  "status": "ACCEPTED",
  "sentAt": "2026-05-06T11:00:00",
  "respondedAt": "2026-05-06T11:30:00"
}
```

#### Diagrama de Secuencia

Responder invitación
<details>
<summary><strong>Explicación del Flujo</strong></summary>

El `RespondInvitationUseCase` verifica que el estudiante sea el destinatario de la invitación y que ésta esté en estado `PENDING`. Si acepta, se verifica que el parche tenga cupo disponible y se crea la membresía. La invitación se actualiza con el estado final y la fecha de respuesta. Se publica un `InvitationAcceptedEvent` en caso de aceptación. Si la invitación ya fue respondida previamente, la clase de estado concreta (`AcceptedState` o `RejectedState`) dispara de inmediato una excepción de dominio, evitando transiciones inválidas.

</details>

#### Tipos de errores manejados

<div align="center">

| Código HTTP | Escenario | Mensaje de Error |
|:------------------:|:----------------|:------------------------|
| 403 Forbidden | No es el invitado | `"You are not the invited student"` |
| 404 Not Found | Invitación no existe | `"Invitation not found"` |
| 409 Conflict | Ya respondida | `"Invitation has already been responded"` |
| 409 Conflict | Parche lleno | `"Maximum capacity reached"` |

</div>

---

## 7. Mensajería Asíncrona (RabbitMQ)

El microservicio utiliza **RabbitMQ** para publicar eventos de dominio que son consumidos asincrónicamente por otros microservicios del ecosistema DOSW. La topología se define en `RabbitMQConfig.java`.

### Topología

| Componente | Nombre | Tipo | Características |
|:-----------|:-------|:-----|:----------------|
| **Exchange** | `hangout.events` | Topic | Durable |

### Routing Keys y Colas

| Routing Key | Cola | Consumidor | Evento que lo dispara |
|:------------|:-----|:-----------|:---------------------|
| `invitation.accepted` | `gamification.invitation.accepted` | gamification-service | Invitación aceptada |
| `invitation.accepted` | `notification.invitation.accepted` | notification-service | Invitación aceptada |
| `invitation.sent` | `notification.invitation.sent` | notification-service | Invitación enviada |
| `member.joined` | `notification.member.joined` | notification-service | Nuevo miembro en parche |

### Payloads de Mensajes (JSON)

#### InvitationAcceptedMessage
Publicado cuando un estudiante acepta una invitación.

| Campo | Tipo | Descripción |
|:------|:-----|:------------|
| invitationId | String | UUID de la invitación |
| parcheId | String | UUID del parche |
| studentId | String | UUID del estudiante que aceptó |
| captainId | String | UUID del capitán del parche |
| occurredAt | LocalDateTime | Timestamp del evento |

#### InvitationSentMessage
Publicado cuando el capitán envía una invitación.

| Campo | Tipo | Descripción |
|:------|:-----|:------------|
| invitationId | String | UUID de la invitación |
| parcheId | String | UUID del parche |
| invitedStudentId | String | UUID del estudiante invitado |
| captainId | String | UUID del capitán que envía |
| occurredAt | LocalDateTime | Timestamp del evento |

#### MemberJoinedMessage
Publicado cuando un estudiante se une a un parche público.

| Campo | Tipo | Descripción |
|:------|:-----|:------------|
| parcheId | String | UUID del parche |
| parcheNombre | String | Nombre del parche |
| capitanId | String | UUID del capitán |
| estudianteId | String | UUID del nuevo miembro |
| occurredAt | LocalDateTime | Timestamp del evento |



## 8. Pruebas

El microservicio de **Parches** implementa una **estrategia integral de pruebas** que garantiza la calidad, funcionalidad y confiabilidad del código mediante pruebas unitarias y de integración.

### Tipos de pruebas implementadas

<div align="center">

| Tipo de Prueba | Descripción | Herramientas |
|:---------------------|:-------------------|:--------------------|
| **Pruebas Unitarias** | Validan el funcionamiento aislado de casos de uso, controladores y lógica de dominio con mocks | JUnit 5, Mockito |
| **Pruebas de Integración** | Verifican la interacción real entre capas contra una base de datos PostgreSQL | Spring Test |
| **Cobertura de Código** | Mide el porcentaje de código cubierto por las pruebas | JaCoCo |

</div>

### Cómo ejecutar las pruebas

#### 1. Ejecutar pruebas unitarias

```bash
mvn test
```

Este comando ejecuta solo las pruebas unitarias (excluye las de integración).

#### 2. Ejecutar pruebas de integración

Requiere una instancia de PostgreSQL corriendo (usa `docker compose up -d postgres`):

```bash
mvn failsafe:integration-test failsafe:verify
```

#### 3. Ejecutar todas las pruebas

```bash
mvn verify
```

#### 4. Generar reporte de cobertura con JaCoCo

```bash
mvn clean test jacoco:report
```

El reporte HTML se generará en:
```
target/site/jacoco/index.html
```

#### 5. Ejecutar pruebas desde IntelliJ IDEA

1. Click derecho sobre la carpeta `src/test/java`.
2. Seleccionar "Run 'Tests in...'".
3. Ver resultados en el panel inferior.

#### 6. Ejecutar una prueba específica

```bash
mvn test -Dtest=RespondInvitationUseCaseTest
```

### Ejemplo de prueba de integración

A continuación se muestra un ejemplo real de una prueba de integración para el controlador de invitaciones, donde se valida el flujo completo de aceptación contra una base de datos real.

```java
@Test
void accept_successFlow_createsMembership() throws Exception {
    RespondInvitationRequest request = new RespondInvitationRequest();
    request.setAnswer(InvitationStatus.ACCEPTED);

    mockMvc.perform(patch("/api/v1/invitaciones/{id}", invitationId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-User-Id", studentId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("ACCEPTED"));

    assertTrue(memberRepository.existsByParcheIdAndStudentId(parcheId, studentId));
}
```


### Criterios de aceptación de pruebas

Para considerar el sistema correctamente probado, se debe cumplir:

- Todas las pruebas en estado PASSED (sin fallos).
- Cero errores de compilación en el código de pruebas.
- Pruebas de casos felices y casos de error implementadas.
- Pruebas de integración que verifican flujos completos contra base de datos real.
- Cobertura de las transiciones de estado de `Invitation` (`PendingState`, `AcceptedState`, `RejectedState`), incluyendo los intentos de transición inválida.
- Cobertura de las reglas de consistencia del agregado `Parche` (cupo, límite de parches activos, validación de capitán).

### Integración con CI/CD

Las pruebas se ejecutan automáticamente en cada push o pull request mediante GitHub Actions:

```yaml
- name: Tests unitarios
  run: mvn test

- name: Tests de integración
  run: mvn failsafe:integration-test failsafe:verify
```

Esto garantiza que ningún cambio roto llegue a producción.

---

## 9. Ejecución del Proyecto

### Prerrequisitos
- Java 21
- Maven 3.9+
- Docker y Docker Compose (para ejecución containerizada)
- PostgreSQL 18 (si se ejecuta localmente sin Docker)

### Opción 1: Ejecución Local (Maven)

```bash
# 1. Clonar repositorio
git clone https://github.com/<org>/parches-service.git

# 2. Levantar base de datos local
docker compose up -d postgres

# 3. Ejecutar aplicación
mvn spring-boot:run
```
**URL Local:** `http://localhost:8080`
**Documentación API:** `http://localhost:8080/swagger-ui/index.html`

### Opción 2: Ejecución con Docker Compose

```bash
# Levantar toda la stack (app + postgres)
docker compose up --build
```

Esto levanta:
- `dosw-postgres`: PostgreSQL 18 en el puerto 5432
- `dosw-hangout-service`: La aplicación en el puerto 8080

### Variables de Entorno

| Variable | Valor por defecto | Descripción |
|:---------|:-----------------|:------------|
| `PGHOST` | `localhost` | Host de PostgreSQL |
| `PGPORT` | `5432` | Puerto de PostgreSQL |
| `PGDATABASE` | `hangoutdb` | Nombre de la base de datos |
| `PGUSER` | `postgres` | Usuario de PostgreSQL |
| `PGPASSWORD` | `postgres` | Contraseña de PostgreSQL |
| `PORT` | `8080` | Puerto del servidor |

> **Swagger UI:** Cuando el proyecto esté desplegado en Azure, la documentación interactiva estará disponible en `https://<app-name>.azurewebsites.net/swagger-ui/index.html`

---

## 10. Evidencia del Despliegue CI/CD

![Captura](docs/uml/CICD.png)

---

## 11. Link Azure / Swagger

| Recurso | Estado / URL |
|:-----------|:----------------|
| **Swagger UI** | Pendiente — se habilitará cuando se configure el Resource Group en Azure |
| **Azure App Service** | Pendiente — URL se asignará al completar el despliegue |

---

## 12. Código Organizado en Carpetas

El microservicio de **Parches** sigue una **arquitectura hexagonal (puertos y adaptadores)** que separa las responsabilidades en capas bien definidas. Todo el código fuente cuenta con documentación Javadoc completa en cada clase, método y campo.

### Estructura general del proyecto (Scaffolding)

```
parches-service/
│
├── src/
│   ├── main/
│   │   ├── java/com/charizard/compiled/hangout_service/
│   │   │   │
│   │   │   ├── application/                              (CAPA DE APLICACION)
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/   (CreateParcheRequest, UpdateParcheRequest, RespondInvitationRequest)
│   │   │   │   │   └── response/  (ParcheResponse, MemberResponse, InvitationResponse, ErrorResponse)
│   │   │   │   ├── mapper/        (ParcheMapper)
│   │   │   │   └── usecase/       (CreateParche, GetParche, UpdateParche, CloseParche, JoinParche,
│   │   │   │                       LeaveParche, InvitationUseCase, RespondInvitationUseCase, ArchiveParche)
│   │   │   │
│   │   │   ├── domain/                                   (CAPA DE DOMINIO)
│   │   │   │   ├── events/        (NuevoMiembroEvent, InvitationSentEvent, InvitationAcceptedEvent)
│   │   │   │   ├── exceptions/    (ParcheNotFoundException, AccessDeniedException, ...)
│   │   │   │   ├── model/         (Parche, Member, Invitation)
│   │   │   │   │   ├── enums/     (ParcheType, ParcheStatus, ParcheCategory, MemberRole, InvitationStatus)
│   │   │   │   │   └── state/     (InvitationState, PendingState, AcceptedState, RejectedState)
│   │   │   │   └── ports/
│   │   │   │       ├── in/        (Input ports / interfaces de casos de uso)
│   │   │   │       └── out/       (Output ports / interfaces de repositorios y notificaciones)
│   │   │   │
│   │   │   ├── entrypoints/                              (ENTRADA / DRIVING ADAPTERS)
│   │   │   │   ├── advice/        (GlobalExceptionHandler)
│   │   │   │   └── rest/controller/ (ParcheController, MemberController, InvitationController)
│   │   │   │
│   │   │   └── infrastructure/                           (INFRAESTRUCTURA / DRIVEN ADAPTERS)
│   │   │       ├── adapters/
│   │   │       │   ├── messaging/   (RabbitMQEventPublisher, InvitationAcceptedMessage,
│   │   │       │   │                 InvitationSentMessage, MemberJoinedMessage)
│   │   │       │   ├── notification/ (NotificacionAdapter, NotificacionEventListener)
│   │   │       │   ├── persistence/
│   │   │       │   │   ├── adapter/   (ParcheRepositoryAdapter, MemberRepositoryAdapter, InvitationRepositoryAdapter)
│   │   │       │   │   ├── entity/    (ParcheEntity, MemberEntity, InvitationEntity)
│   │   │       │   │   ├── mapper/    (ParcheEntityMapper, MemberEntityMapper, InvitationEntityMapper)
│   │   │       │   │   └── repository/ (ParcheRepository, MemberRepository, InvitationRepository)
│   │   │       │   └── scheduler/     (ParcheArchiveScheduler)
│   │   │       └── config/            (SecurityConfig, SwaggerConfig, AsyncConfig, FlywayConfig,
│   │   │                                RabbitMQConfig)
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   │           ├── V1__create_parches_tables.sql
│   │           └── V2__create_invitaciones_table.sql
│   │
│   └── test/                                             (PRUEBAS)
│       ├── java/.../
│       │   ├── application/usecase/   (RespondInvitationUseCaseTest, NotificacionEventListenerTest)
│       │   ├── entrypoints/rest/controller/ (InvitationControllerTest, InvitationIntegrationTest, MiembroIntegrationTest)
│       │   └── infrastructure/
│       │       ├── scheduler/         (ParcheArchivoSchedulerTest)
│       │       └── persistence/entity/ (ParcheEntityTest, InvitationEntityTest)
│       └── resources/
│           └── application.properties
│
├── .github/workflows/
│   └── ci-cd.yml
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

> El código fuente está organizado siguiendo estrictamente la arquitectura hexagonal para garantizar la separación de responsabilidades y facilitar el mantenimiento y la extensión del sistema. Toda la implementación en Java cuenta con Javadoc completo.

---

## 13. Conexiones con Servicios Externos

El microservicio se integra con otros sistemas del ecosistema DOSW a través de eventos asíncronos y validaciones síncronas.

<div align="center">

| Microservicio | Operación | Propósito |
|:---------------|:----------------|:-----------------------|
| **Gamification** | Nuevo miembro / Invitación aceptada | Disparar recompensas o puntos al unirse a un parche |
| **Notification** | Invitación enviada / Nuevo miembro | Enviar notificaciones push a los estudiantes |
| **User Service** | Validación de identidad | Verificar que el `X-User-Id` corresponde a un estudiante activo |

</div>

**Mecanismo de integración:**

- **Mensajería asíncrona:** Los eventos de dominio (`invitation.accepted`, `invitation.sent`, `member.joined`) se publican en el exchange `hangout.events` de RabbitMQ y son consumidos por Gamification y Notification.
- **Validación síncrona:** El servicio consume la identidad del usuario vía User Service para validar los headers `X-User-Id` en las peticiones.

---

## 14. Pipelines (Desarrollo + PROD)

El proyecto implementa un pipeline automatizado con GitHub Actions para garantizar la calidad del código. El flujo está definido en `.github/workflows/ci-cd.yml` y se adapta según el evento y la rama.

### Estructura del Pipeline

```
                ┌──────────────┐
                │    Build     │
                │ (mvn package)│
                └──────┬───────┘
                       │
         ┌─────────────┼─────────────┐
         ▼             ▼             ▼
  ┌────────────┐ ┌────────────┐ ┌────────────┐
  │Unit Tests  │ │Integration │ │  Quality   │
  │ (mvn test) │ │  Tests     │ │(JaCoCo check)│
  └────────────┘ │(failsafe)  │ └────────────┘
                 └────────────┘
                       │
                       ▼
                ┌──────────────┐
                │   Package    │
                │(Docker build)│
                └──────┬───────┘
                       │
                       ▼
                ┌──────────────┐
                │   Deploy     │
                │ (Azure - pendiente) │
                └──────────────┘
```

### Jobs del Pipeline

| Job | Descripción | Depende de |
|-----|-------------|------------|
| **build** | Compila y empaqueta el JAR con Maven. Sube el artifact del JAR. | — |
| **unit-tests** | Ejecuta pruebas unitarias (surefire) y publica reporte JUnit | build |
| **integration-tests** | Ejecuta pruebas de integración (failsafe) con PostgreSQL 18 en servicio del runner | build |
| **quality** | Verifica cobertura de código con JaCoCo | build |
| **package** | Construye imagen Docker del microservicio y la guarda como artifact | unit-tests, integration-tests, quality |
| **deploy** | Despliegue a Azure Web App (pendiente de configurar Resource Group) | package |

### Comportamiento por entorno

#### Pipeline de Desarrollo (`develop`)

Se ejecuta en cada push a `develop`:
- `build` → `unit-tests`, `integration-tests`, `quality` (corren en paralelo)
- Si `unit-tests` falla, `integration-tests` continúa igualmente (continue on failure)

#### Pipeline de Producción (`main`)

Se ejecuta en PR a `main`:
- `build` → `unit-tests`, `integration-tests`, `quality` → `package` (validar construcción Docker)

Se ejecuta en push a `main`:
- `build` → `unit-tests`, `integration-tests`, `quality` → `package` → `deploy` (a Azure)

| Evento | Jobs que se ejecutan |
|--------|----------------------|
| **Push a `develop`** | `build` → `unit-tests`, `integration-tests`, `quality` |
| **PR a `main`** | `build` → `unit-tests`, `integration-tests`, `quality` → `package` |
| **Push a `main`** | `build` → `unit-tests`, `integration-tests`, `quality` → `package` → `deploy` |

> Los jobs `unit-tests`, `integration-tests` y `quality` corren en paralelo después de `build`.

---

<div align="center">




> DOSW Parches-Service es un proyecto académico, pero su arquitectura y calidad están pensadas para ser escalables y adaptables a escenarios reales en instituciones educativas.

**Escuela Colombiana de Ingeniería Julio Garavito**

</div>
