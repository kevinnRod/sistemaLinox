package com.linox.sistemaventas.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linox.sistemaventas.models.Rol;
import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.models.UsuarioRol;
import com.linox.sistemaventas.services.RolService;
import com.linox.sistemaventas.services.UsuarioRolService;
import com.linox.sistemaventas.services.UsuarioService;
import com.linox.sistemaventas.services.impl.UsuarioDetailsServiceImpl;

@Controller
@RequestMapping("/usuario") // Ruta base para las vistas de usuario
public class UsuarioController {

    @Autowired
    private RolService rolService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioDetailsServiceImpl userDetailsService;

    // Obtener todos los usuarios y redirigir a la vista
    @GetMapping
    public String getAll(Model model) {
        List<Usuario> usuarios = usuarioService.findAllByEstadoActivo();
        model.addAttribute("usuarios", usuarios);// Pasar la lista de usuarios a la vista
        model.addAttribute("active_page", "usuario");
        return "usuario/usuarios"; // Nombre de la vista Thymeleaf
    }

    // Mostrar el formulario de creación de un nuevo usuario
    @GetMapping("/create")
    public String crearUsuario(Model model) {
        model.addAttribute("active_page", "usuario");
        return "usuario/crearUsuario"; // Vuelve a la vista usuario/crear.html
    }

    // Crear un nuevo usuario
    @PostMapping("/save")
    public String saveUsuario(
            @RequestParam("usuario") String usuario,
            @RequestParam("correo") String correo,
            @RequestParam("contrasenaEnc") String contrasenaEnc,
            @RequestParam("idEstado") Integer idEstado,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            RedirectAttributes redirectAttributes) {

        try {
            Usuario user = new Usuario();
            user.setUsuario(usuario);
            user.setCorreo(correo);
            user.setContrasenaEnc(passwordEncoder.encode(contrasenaEnc));
            user.setIdEstado(idEstado);

            // Procesar la imagen si se envió
            if (foto != null && !foto.isEmpty()) {
                // Carpeta donde se guardan las fotos (relativa a la raíz del proyecto)
                String uploadDir = "uploads/usuarios/";
                String fileName = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                // Crear carpeta si no existe
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Guardar el archivo en disco
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Guardar la ruta relativa en la BD
                user.setUrlFoto("/uploads/usuarios/" + fileName);
            }

            // Guardar el usuario
            usuarioService.save(user);
            redirectAttributes.addFlashAttribute("success", "Usuario guardado correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }

        return "redirect:/usuario";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Integer id, Model model) {
        Optional<Usuario> existente = usuarioService.findById(id);
        if (!existente.isPresent()) {
            return "redirect:/usuario";
        }
        Usuario usuarioExistente = existente.get();
        List<UsuarioRol> roles = usuarioRolService.findAllByEstadoActivo(id);
        List<Rol> listaRoles = rolService.findAllByEstadoActivo();
        model.addAttribute("rolesA", roles);
        model.addAttribute("listaRoles", listaRoles);
        model.addAttribute("usuario", usuarioExistente);
        model.addAttribute("active_page", "usuario");
        return "usuario/editarUsuario"; // Vuelve a la vista usuario/crear.html
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes,
            @RequestParam("usuario") String usuario,
            @RequestParam("correo") String correo,
            @RequestParam("contrasena") String contrasena,
            @RequestParam("idEstado") Integer idEstado,
            @RequestParam(value = "foto", required = false) MultipartFile foto) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.findById(id);

            if (!usuarioOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El usuario no fue encontrado.");
                return "redirect:/usuario";
            }
            Usuario user = usuarioOpt.get();
            String nombreG = user.getUsuario();
            user.setUsuario(usuario);
            user.setCorreo(correo);
            if (contrasena != null && !contrasena.isBlank()) {
                user.setContrasenaEnc(passwordEncoder.encode(contrasena));
            }
            
            user.setIdEstado(idEstado);
            // Procesar la imagen si se envió
            if (foto != null && !foto.isEmpty()) {
                // Carpeta donde se guardan las fotos (relativa a la raíz del proyecto)
                String uploadDir = "uploads/usuarios/";
                String fileName = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                // Crear carpeta si no existe
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Guardar el archivo en disco
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Guardar la ruta relativa en la BD
                user.setUrlFoto("/uploads/usuarios/" + fileName);
            }
            usuarioService.save(user);
            // Actualiza el contexto de seguridad si el usuario actualizado es el
            // autenticado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = auth.getName();
            if (nombreG.equals(currentUsername)) {
                // Crea una nueva autenticación con los datos actualizados
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsuario());
                Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, auth.getCredentials(),
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuth);
                redirectAttributes.addFlashAttribute("success", "Usuario Actualizado correctamente.");
                return "redirect:/usuario/ver/" + user.getIdUsuario();
            }
            redirectAttributes.addFlashAttribute("success", "Usuario guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }
        return "redirect:/usuario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.findById(id);

            if (!usuarioOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El usuario no fue encontrado.");
                return "redirect:/usuario";
            }

            Usuario usuario = usuarioOpt.get();
            usuario.setIdEstado(0); // Cambia el estado a inactivo
            usuarioService.save(usuario);

            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }

        return "redirect:/usuario";
    }

    @GetMapping("/ver/{id}")
    public String verUsuario(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.findById(id);
        if (!usuarioOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/usuario";
        }
        // Obtener roles del usuario
        List<UsuarioRol> roles = usuarioRolService.findAllByEstadoActivo(id);
        List<Rol> listaRoles = rolService.findAllByEstadoActivo();
        model.addAttribute("active_page", "usuario");
        model.addAttribute("usuario", usuarioOpt.get());
        model.addAttribute("rolesA", roles);
        model.addAttribute("listaRoles", listaRoles);
        return "usuario/detalleUsuario";
    }

    @GetMapping("/roles-usuario")
    public ResponseEntity<?> getRolesUsuario(Authentication authentication) {
        try {
            // Obtener el usuario autenticado
            Usuario usuario = (Usuario) authentication.getPrincipal();
            List<UsuarioRol> roles = usuarioRolService.findAllByEstadoActivo(usuario.getIdUsuario());

            // Convertir los roles a un formato más simple si es necesario
            List<Map<String, Object>> rolesSimplificados = roles.stream()
                    .map(role -> {
                        Map<String, Object> roleData = new HashMap<>();
                        roleData.put("nombreRol", role.getRol().getNombreRol());
                        return roleData;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(rolesSimplificados);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener roles");
        }
    }

}
