package com.linox.sistemaventas.config;
import com.linox.sistemaventas.repositories.UsuarioRolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.linox.sistemaventas.models.Rol;
import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.models.UsuarioRol;
import com.linox.sistemaventas.repositories.RolRepository;
import com.linox.sistemaventas.repositories.UsuarioRepository;

@Configuration
public class DataInitializer {

    private final UsuarioRolRepository usuarioRolRepository;

    DataInitializer(UsuarioRolRepository usuarioRolRepository) {
        this.usuarioRolRepository = usuarioRolRepository;
    }

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Crear rol si no existe
            Rol rolAdmin = rolRepository.findByNombreRol("ADMIN").orElse(null);
            if (rolAdmin == null) {
                rolAdmin = new Rol();
                rolAdmin.setNombreRol("ADMIN");
                rolAdmin.setDescripcionRol("Administrador del sistema");
                rolAdmin.setIdEstado(1); // Activo
                rolRepository.save(rolAdmin);
                System.out.println("✅ Rol ROLE_ADMIN creado.");
            }

            // Crear usuario si no existe
            if (usuarioRepository.findByUsuario("admin").isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setUsuario("admin");
                usuario.setCorreo("admin@example.com");
                usuario.setContrasenaEnc(passwordEncoder.encode("admin123")); // Encriptamos la contraseña
                usuario.setIdEstado(1); // Activo
                usuarioRepository.save(usuario);
                System.out.println("✅ Usuario admin creado con contraseña encriptada.");

                // Asociar el rol al usuario
                UsuarioRol usuarioRol = new UsuarioRol(usuario, rolAdmin);
                usuarioRol.setIdEstado(1); // Activo
                usuarioRolRepository.save(usuarioRol);
                System.out.println("✅ Rol ADMIN asignado al usuario admin.");
            }
        };
    }
}
