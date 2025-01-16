import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


class Converter {

    private static final String API_KEY = "api-key"; // Reemplaza con tu API Key
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    // Método para convertir monedas
    public double convert(String fromCurrency, String toCurrency, double amount) throws Exception {
        String urlStr = BASE_URL + API_KEY + "/pair/" + fromCurrency + "/" + toCurrency;

        // Realizar solicitud a la API
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Error en la conexión con la API: " + responseCode);
        }

        // Leer respuesta
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Procesar JSON usando Gson
        return parseRate(response.toString()) * amount;
    }

    // Método para obtener la tasa de conversión del JSON
    private double parseRate(String jsonResponse) throws Exception {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        if (!jsonObject.has("conversion_rate")) {
            throw new Exception("No se encontró la tasa de conversión en la respuesta.");
        }

        return jsonObject.get("conversion_rate").getAsDouble();
    }

    // Método para obtener la lista de monedas soportadas
    public Map<String, String> getCurrencyCodes() throws Exception {
        String urlStr = BASE_URL + API_KEY + "/codes";

        // Realizar solicitud a la API
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Error en la conexión con la API: " + responseCode);
        }

        // Leer respuesta
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Procesar JSON usando Gson
        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
        JsonArray supportedCodes = jsonObject.getAsJsonArray("supported_codes");

        Map<String, String> currencies = new HashMap<>();
        for (int i = 0; i < supportedCodes.size(); i++) {
            JsonArray currencyPair = supportedCodes.get(i).getAsJsonArray();
            String code = currencyPair.get(0).getAsString();
            String name = currencyPair.get(1).getAsString();
            currencies.put(code, name);
        }

        return currencies;
    }
    // Método para buscar monedas por nombre de país
    public Map<String, String> searchCurrencyByCountry(String country) throws Exception {
        Map<String, String> currencies = getCurrencyCodes();

        return currencies.entrySet().stream()
                .filter(entry -> entry.getValue().toLowerCase().contains(country.toLowerCase()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}