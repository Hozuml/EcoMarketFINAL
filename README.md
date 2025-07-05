# EcoMarketFINAL
Descripción del Proyecto
EcoMarket es una aplicación de comercio electrónico construida con Spring Boot y Java. Permite gestionar productos, realizar pedidos y manejar usuarios a través de una interfaz web.

Estructura del Proyecto
El proyecto está organizado en las siguientes carpetas y archivos principales:

src/main/java/cl/duoc/caasoto/ecomarket: Contiene el código fuente de la aplicación.

controller: Los controladores que gestionan las solicitudes HTTP. Ejemplo: HomeController.java, que maneja la lógica de la página principal, y ProductController.java, que maneja la interacción con productos.

service: La lógica de negocio. Ejemplo: ProductService.java, que contiene métodos como getAllProducts() para recuperar productos de la base de datos.

model: Define las clases de modelo que representan los objetos de la aplicación, como Product.java.

repository: Interfaz que extiende de JpaRepository para realizar operaciones CRUD en la base de datos.

Funcionamiento
1. Spring Boot Application
El punto de entrada de la aplicación es la clase EcoMarketApplication.java, que está anotada con @SpringBootApplication para inicializar la aplicación Spring Boot.

2. Controladores
Los controladores gestionan las solicitudes HTTP. Por ejemplo:

HomeController.java: Maneja la solicitud GET a la página principal.

ProductController.java: Permite gestionar productos (mostrar, crear, editar).

3. Servicios
La lógica de negocio está encapsulada en los servicios. Por ejemplo:

ProductService.java: Proporciona métodos para interactuar con los productos, como obtener todos los productos (getAllProducts()).

4. Repositorio
El repositorio ProductRepository.java interactúa directamente con la base de datos para obtener y almacenar productos.
