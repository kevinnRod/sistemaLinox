package com.linox.sistemaventas.api;

import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioGuardado;

    @BeforeEach
    public void setup() {
        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setUsuario("kevin");
        usuario.setCorreo("kevin@correo.com");
        usuario.setContrasenaEnc("1234");
        usuario.setIdEstado(1);

        usuarioGuardado = usuarioRepository.save(usuario);
    }

    @Test
    public void testGetUsuarioById() throws Exception {
        mockMvc.perform(get("/api/usuarios/" + usuarioGuardado.getIdUsuario()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario", is("kevin")))
                .andExpect(jsonPath("$.correo", is("kevin@correo.com")));
    }
}
