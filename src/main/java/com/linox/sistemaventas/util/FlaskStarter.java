package com.linox.sistemaventas.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class FlaskStarter {

    private Process flaskProcess;

    @PostConstruct
    public void startFlaskApi() {
        try {
            System.out.println("🚀 Iniciando intento de ejecutar la API Flask...");

            String pythonPath = "python3";

            String scriptPath = "modeladoLinox/api_prediccion.py";

            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, scriptPath);
            processBuilder.redirectErrorStream(true);
            flaskProcess = processBuilder.start(); // ← Guardamos el proceso

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(flaskProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[FLASK] " + line);
                    }
                } catch (IOException e) {
                    System.err.println("❌ Error leyendo salida del script Flask: " + e.getMessage());
                }
            }).start();

            System.out.println("✅ Proceso de arranque de Flask iniciado.");
        } catch (IOException e) {
            System.out.println("❌ Error al iniciar la API Flask: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void detenerFlaskApi() {
        if (flaskProcess != null && flaskProcess.isAlive()) {
            System.out.println("🛑 Deteniendo proceso Flask...");
            flaskProcess.destroy();
            try {
                if (!flaskProcess.waitFor(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    System.out.println("⚠️ Flask no respondió a destroy(). Forzando cierre...");
                    flaskProcess.destroyForcibly();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
