# 📚 LiterAlura - Catálogo de Libros (Java + Spring Boot + MySQL)

Aplicación de consola desarrollada en Java que consume una API de libros, procesa respuestas JSON, guarda libros y autores en una base de datos MySQL y permite consultar información mediante un menú interactivo.

## 🚀 Objetivo del proyecto

Desarrollar un catálogo de libros con interacción textual (vía consola), cumpliendo con los siguientes puntos:

- Consumo de API de libros
- Análisis de respuesta JSON
- Persistencia en base de datos
- Consultas y filtros
- Menú interactivo con 5 opciones funcionales

---

## 🛠️ Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Jackson (JSON)
- Maven
- Lombok

---

## 🌐 API utilizada

Se utiliza la API de **Gutendex** (Project Gutenberg):

- Búsqueda de libros por título
- Datos de autores
- Idiomas
- Cantidad de descargas

---

## ✅ Funcionalidades implementadas (MVP)

La aplicación ofrece un menú por consola con estas 5 opciones:

1. **Buscar libro por título** (consulta a la API y guarda en MySQL)
2. **Listar libros registrados**
3. **Listar autores registrados**
4. **Listar autores vivos en un año determinado**
5. **Listar libros por idioma**

---

## 🧠 Flujo de funcionamiento

1. El usuario ingresa un título en consola.
2. La app consulta la API de Gutendex.
3. Se procesa la respuesta JSON (Jackson).
4. Se mapean los datos a entidades (`Libro`, `Autor`).
5. Se guardan en MySQL evitando duplicados.
6. El usuario puede listar y filtrar datos ya persistidos.

---

## 🗂️ Estructura del proyecto (resumen)

```text
src/main/java/com/tuusuario/literalura
├── dto
│   ├── DatosRespuesta.java
│   ├── DatosLibro.java
│   └── DatosAutor.java
├── model
│   ├── Libro.java
│   └── Autor.java
├── repository
│   ├── LibroRepository.java
│   └── AutorRepository.java
├── service
│   ├── ConsumoApi.java
│   ├── ConvierteDatos.java
│   └── CatalogoService.java
├── principal
│   └── Principal.java
└── LiteraluraApplication.java
