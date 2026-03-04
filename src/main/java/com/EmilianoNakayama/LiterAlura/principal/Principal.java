package com.EmilianoNakayama.LiterAlura.principal;

import com.EmilianoNakayama.LiterAlura.service.CatalogoService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Principal {
    private final CatalogoService catalogoService;
    private final Scanner teclado = new Scanner(System.in);

    public Principal(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public void muestraElMenu() {
        var opcion = -1;

        while (opcion != 0) {
            mostrarMenu();

            String entrada = teclado.nextLine().trim();

            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Ingresá un número del menú.");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> catalogoService.listarLibrosRegistrados();
                case 3 -> catalogoService.listarAutoresRegistrados();
                case 4 -> listarAutoresVivosEnAnio();
                case 5 -> listarLibrosPorIdioma();
                case 0 -> System.out.println("Cerrando LiterAlura...");
                default -> System.out.println("Opción no válida. Elegí una opción del 0 al 5.");
            }

            if (opcion != 0) {
                System.out.println("\nPresioná ENTER para continuar...");
                teclado.nextLine();
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("""
                
                =====================================
                         L I T E R A L U R A
                =====================================
                1 - Buscar libro por título (API + guardar)
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un año
                5 - Listar libros por idioma
                0 - Salir
                
                Elegí una opción:
                """);
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingresá el título del libro a buscar:");
        String titulo = teclado.nextLine().trim();

        if (titulo.isBlank()) {
            System.out.println("No ingresaste ningún título.");
            return;
        }

        catalogoService.buscarLibroPorTitulo(titulo);
    }

    private void listarAutoresVivosEnAnio() {
        System.out.println("Ingresá el año para buscar autores vivos:");
        String entradaAnio = teclado.nextLine().trim();

        try {
            int anio = Integer.parseInt(entradaAnio);
            catalogoService.listarAutoresVivosEnAnio(anio);
        } catch (NumberFormatException e) {
            System.out.println("Año inválido. Debe ser un número entero.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingresá el idioma:
                - es (Español)
                - en (Inglés)
                - fr (Francés)
                - pt (Portugués)
                """);

        String idioma = teclado.nextLine().trim();

        if (idioma.isBlank()) {
            System.out.println("No ingresaste ningún idioma.");
            return;
        }

        catalogoService.listarLibrosPorIdioma(idioma);
    }
}
