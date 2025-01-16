import java.util.Map;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Converter converter = new Converter();

        System.out.println("Bienvenido al conversor de monedas");

        while (true) {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Convertir moneda");
            System.out.println("2. Mostrar monedas disponibles");
            System.out.println("3. Buscar moneda por país");
            System.out.println("4. Salir");

            int opcion = scanner.nextInt();

            if (opcion == 4) {
                System.out.println("Gracias por usar el conversor. ¡Adiós!");
                break;
            }

            if (opcion == 2) {
                try {
                    System.out.println("Monedas disponibles:");
                    Map<String, String> currencies = converter.getCurrencyCodes();
                    currencies.forEach((code, name) -> System.out.println(code + " - " + name));
                } catch (Exception e) {
                    System.out.println("Ocurrió un error al obtener las monedas: " + e.getMessage());
                }
            }

            if (opcion == 3) {
                System.out.println("Ingrese el nombre del país o parte del mismo:");
                scanner.nextLine(); // Limpiar buffer
                String country = scanner.nextLine();

                try {
                    Map<String, String> results = converter.searchCurrencyByCountry(country);

                    if (results.isEmpty()) {
                        System.out.println("No se encontraron monedas para el país especificado.");
                    } else {
                        System.out.println("Resultados:");
                        results.forEach((code, name) -> System.out.println(code + " - " + name));
                    }
                } catch (Exception e) {
                    System.out.println("Ocurrió un error al buscar la moneda: " + e.getMessage());
                }
            }

            if (opcion == 1) {
                System.out.println("Ingrese la moneda de origen (por ejemplo, USD):");
                String fromCurrency = scanner.next().toUpperCase();

                System.out.println("Ingrese la moneda de destino (por ejemplo, EUR):");
                String toCurrency = scanner.next().toUpperCase();

                System.out.println("Ingrese la cantidad a convertir:");
                double amount = scanner.nextDouble();

                try {
                    double result = converter.convert(fromCurrency, toCurrency, amount);
                    System.out.printf("%.2f %s equivalen a %.2f %s\n", amount, fromCurrency, result, toCurrency);
                } catch (Exception e) {
                    System.out.println("Ocurrió un error: " + e.getMessage());
                }
            }
        }

        scanner.close();
    }
}