package com.linox.sistemaventas.services;
@Service
public class EmailService {
    public void enviarCorreo(String mensaje) {
        System.out.println("Enviando correo: " + mensaje);
    }
    
}
