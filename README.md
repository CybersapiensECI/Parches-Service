# Parches-Service
## Patrones de diseño implementados 
El primer *patrón es el Aggregate root*, el cual se localiza centralmente en la clase Parche. Este patrón funciona como una frontera de consistencia donde Parche es la única entidad que se comunica directamente con el exterior de la capa de dominio. Las demás entidades, como Miembro, invitación, publicación y Comentario, se ubican colgadas internamente de esta raíz. Su utilidad radica en que ninguna de estas clases secundarias puede ser modificada o creada de forma aislada desde afuera; cualquier operación de negocio debe invocar obligatoriamente a un método de Parche, garantizando por ejemplo que no existan publicaciones que no estén asociadas a uno parche.

Además tenemos el patrón de diseño *state* se encuentra implementado explícitamente en el núcleo del microservicio de parches, específicamente dentro de la entidad Invitation que actua como la clase de contexto y la interfaz del dominio InvitationState. En lugar de manejar el flujo con enumeraciones estaticas, la invitacion delega sus acciones de negocio a tres clases de estado concretas llamadas PendingState, AcceptedState y RejectedState. De esta manera, cuando el agregado Parche solicita aceptar o rechazar una solicitud, la invitación transfiere la operación a la instancia del estado activo en ese momento, logrando que el comportamiento de la entidad mute dinámicamente y que las transiciones de estado se ejecuten de forma segura y centralizada en objetos independientes.
El uso de esta estructura nos ayuda a obtener un Código con alta cohesión y un solido encapsulamiento, puesto que las reglas y consecuencias de cada transición quedan aisladas en su respectiva clase, facilitando además la mantenibilidad futura en caso de requerir nuevos estados en la aplicación. Gracias a esto, evitamos por completo la acumulación de bloques condicionales complejos y estructuras densas de control dentro de la entidad principal. Asimismo, se impide la aparición de inconsistencias lógicas en la memoria del sistema, ya que si una solicitud ya se encuentra aprobada o denegada y se intenta volver a aceptar o rechazar, la clase concreta se encarga de disparar una excepción de dominio de forma inmediata, impidiendo corrupciones en el flujo de datos.

![Diagrama de clases ParchesAlphaECI.drawio (1).png](Images/Uml/Diagrama%20de%20clases%20ParchesAlphaECI.drawio%20%281%29.png)

## Diagrama de componentes especificos 
Este diagrama detalla la arquitectura del microservicio estructurada bajo los principios de la Arquitectura Hexagonal y la segregación de responsabilidades. En la periferia izquierda, los adaptadores de entrada (ParcheRestController e InvitationRestController) capturan las solicitudes HTTP y las delegan hacia la capa de casos de uso (Ports / In), aislando cada acción del sistema en interfaces independientes y planas.

En el núcleo central se orquesta la lógica de negocio a través de los servicios de aplicación (ParcheCommandService, ParcheQueryService e 
InvitationApplicationService), los cuales coordinan las reglas con las entidades del dominio (Parche, Member, Invitation). Finalmente, mediante la inversión de 
dependencias, el flujo se conecta con los puertos de salida (Ports / Out / SPI) donde los adaptadores de infraestructura (ParcheDatabaseAdapter e InvitationDatabaseAdapter) 
canalizan el almacenamiento definitivo de forma modular hacia una única base de datos física PostgreSQL.

![DiagramaEspecificoParchesAlphaECI-Página-4.drawio.png](Images/Uml/DiagramaEspecificoParchesAlphaECI-P%C3%A1gina-4.drawio.png)


## Diagrama de Base de datos Relacional 
El diagrama de base de datos relacional modela de forma consistente la persistencia en PostgreSQL para el microservicio de Parches, mapeando con precisión los tipos de datos y las restricciones del modelo de dominio. La estructura se fundamenta en la tabla principal PARCHES y se desacopla mediante relaciones de uno a muchos ($1:N$) hacia las entidades satélites MEMBERS, INVITATIONS y POSTS, empleando identificadores únicos globales (UUID) como llaves primarias para garantizar la consistencia en entornos distribuidos. 
