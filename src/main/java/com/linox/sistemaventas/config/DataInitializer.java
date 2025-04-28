package com.linox.sistemaventas.config;

import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByUsuario("admin").isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setUsuario("admin");
                usuario.setCorreo("admin@example.com");
                usuario.setContrasenaEnc(passwordEncoder.encode("admin123")); // <<<< Encriptamos la contraseña
                usuario.setIdEstado(1); // Activo
                usuarioRepository.save(usuario);
                System.out.println("✅ Usuario admin creado con contraseña encriptada.");
            }
        };
    }
}