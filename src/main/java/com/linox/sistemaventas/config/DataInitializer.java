package com.linox.sistemaventas.config;
import com.linox.sistemaventas.repositories.UsuarioRolRepository;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.linox.sistemaventas.models.Rol;
import com.linox.sistemaventas.models.TipoMovimiento;
import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.models.UsuarioRol;
import com.linox.sistemaventas.repositories.RolRepository;
import com.linox.sistemaventas.repositories.UsuarioRepository;
import com.linox.sistemaventas.repositories.TipoMovimientoRepository; // <-- Importa tu repo

@Configuration
public class DataInitializer {

    private final UsuarioRolRepository usuarioRolRepository;
    private final TipoMovimientoRepository tipoMovimientoRepository; // <-- Nuevo

    // Modifica el constructor para recibir el nuevo repo
    public DataInitializer(UsuarioRolRepository usuarioRolRepository, TipoMovimientoRepository tipoMovimientoRepository) {
        this.usuarioRolRepository = usuarioRolRepository;
        this.tipoMovimientoRepository = tipoMovimientoRepository;
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
            if (tipoMovimientoRepository.findByCodigo("ENT").isEmpty()) {
                TipoMovimiento entrada = new TipoMovimiento();
                entrada.setCodigo("ENT");
                entrada.setNombre("Entrada");
                entrada.setIdEstado(1); // Activo
                entrada.setCreatedAt(LocalDateTime.of(2025, 6, 7, 16, 14));
                entrada.setUpdatedAt(LocalDateTime.of(2025, 6, 7, 16, 28));
                tipoMovimientoRepository.save(entrada);
                System.out.println("✅ TipoMovimiento 'Entrada' creado.");
            }

            if (tipoMovimientoRepository.findByCodigo("SAL").isEmpty()) {
                TipoMovimiento salida = new TipoMovimiento();
                salida.setCodigo("SAL");
                salida.setNombre("Salida");
                salida.setIdEstado(1); // Activo
                salida.setCreatedAt(LocalDateTime.of(2025, 6, 7, 16, 52));
                salida.setUpdatedAt(LocalDateTime.of(2025, 6, 7, 16, 52));
                tipoMovimientoRepository.save(salida);
                System.out.println("✅ TipoMovimiento 'Salida' creado.");
            }
        };
    }
}
