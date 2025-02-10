El proyecto está pensado para ejecutarse con maven y hacer peticiones mediante postman.

Para arrancar:

mvn install

mvn spring-boot:run

El proyecto tiene tres endpoints principales

    http://localhost:8080/libros

    http://localhost:8080/usuarios

    http://localhost:8080/prestamos


Los métodos POST, PUT Y PATCH necesitan de un cuerpo en formato JSON para funcionar. Los métodos POST
y PUT asignan todos los valores menos el id que va automático y patch solo los campos que especifiquemos.

Ejemplo de un JSON para testear el método POST o PUT

    {
        "titulo": "titulo5",
        "autor": "autor5",
        "isbn": "isbn3",
        "fechaPublicacion": "2025-02-08"
    }



Al arrancar la aplicación se crean 3 libros, 3 usuarios y tres repositorios.
