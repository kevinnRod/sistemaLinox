package com.linox.sistemaventas.controllers;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linox.sistemaventas.repositories.VentaRepository;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private VentaRepository ventaRepository;

    @PostMapping("/gpt")
    public ResponseEntity<Map<String, String>> responder(@RequestBody Map<String, String> payload) {
        String mensaje = payload.get("mensaje");

        String respuesta = enviarAOpenAI(mensaje);

        Map<String, String> result = new HashMap<>();
        result.put("respuesta", respuesta);
        return ResponseEntity.ok(result);
    }

    private String enviarAOpenAI(String mensajeUsuario) {
        String apiKey = "4fkL0NuMwI2yRtV9cvKfArDeSKKVsEBtsU6NY7IlZgkL19oDtNT8JQQJ99BGACHYHv6XJ3w3AAAAACOGQQuv";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Object[]> x = ventaRepository.obtenerVentasUltimoMes();
        List<Object[]> y = ventaRepository.obtenerMontoProductosVend();
        List<Object[]> z = ventaRepository.obtenerProdStockBajo();
        StringBuilder contexto = new StringBuilder(
                "Eres un asistente util. Aqui tienes los datos actuales del sistema ERP:");

        // Ventas por cliente
        contexto.append("1. Ventas por cliente del ultimo mes:");
        for (Object[] fila : x) {
            String codCliente = fila[0].toString();
            String total = fila[1].toString();
            contexto.append("- Cliente ").append(codCliente).append(": S/ ").append(total).append(" ");
        }

        // Monto por producto vendido
        contexto.append("2. Monto de productos vendidos: ");
        for (Object[] fila : y) {
            String producto = fila[0].toString();
            String monto = fila[1].toString();
            contexto.append("- ").append(producto).append(": S/ ").append(monto).append(" ");
        }

        // Productos con stock bajo
        contexto.append("3. Productos con stock bajo:");
        for (Object[] fila : z) {
            String producto = fila[0].toString();
            String stock = fila[1].toString();
            contexto.append("- ").append(producto).append(" (stock: ").append(stock).append(" )");
        }
        String text = limpiarTexto(contexto.toString());
        String system = text
                + " Responde unicamente en funcion de la información proporcionada del ERP de ventas y almacen. Estás diseñado para asistir a los usuarios resolviendo dudas relacionadas con el sistema, como:" + //
                                        "- Consultar ventas recientes, clientes o productos." +
                                        "- Explicar el stock, montos vendidos, o funciones del sistema ERP" +
                                        "- Dar la bienvenida y responder saludos." + //
                                        "Si el usuario pregunta por tus funcionalidades dentro del ERP, describe brevemente tu rol: eres un asistente que responde preguntas sobre los datos actuales del sistema y ayuda a los usuarios a entender como funciona." + //
                                        "Si el usuario hace preguntas que no están relacionadas con este ERP, como cálculos matemeticos arbitrarios, preguntas generales sobre inteligencia artificial o temas no vinculados al sistema, responde con:" + //
                                        "“Lo siento, esa pregunta no se encuentra dentro del alcance de este asistente, que está orientado exclusivamente a consultas sobre el sistema ERP de ventas y almacen.";
        // JSON igual al de C#
        ObjectMapper mapper1 = new ObjectMapper();
        ObjectNode root1 = mapper1.createObjectNode();

        ArrayNode messages = mapper1.createArrayNode();
        messages.add(mapper1.createObjectNode()
                .put("role", "system")
                .put("content", system));
        messages.add(mapper1.createObjectNode()
                .put("role", "user")
                .put("content", mensajeUsuario));

        root1.set("messages", messages);
        String jsonBody;
        try {
            jsonBody = mapper1.writeValueAsString(root1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar el JSON del request", e);
        }
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        String endpoint = "https://kevin-md2c07xm-eastus2.cognitiveservices.azure.com/openai/deployments/o3-mini/chat/completions?api-version=2025-01-01-preview";

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(endpoint, request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("ERROR: " + ex.getStatusCode());
            System.out.println("CUERPO: " + ex.getResponseBodyAsString());
            return "Error desde Azure OpenAI: " + ex.getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error inesperado al contactar Azure.";
        }
    }

    private String limpiarTexto(String texto) {
        if (texto == null)
            return "";

        // 1. Normaliza y elimina tildes
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // 2. Escapa comillas y backslashes
        sinTildes = sinTildes
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");

        // 3. Elimina caracteres de control o corruptos
        sinTildes = sinTildes
                .replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "")
                .replaceAll("[^\\x20-\\x7EñÑ\\n\\r\\t]", "")
                .trim();

        return sinTildes;
    }
}
