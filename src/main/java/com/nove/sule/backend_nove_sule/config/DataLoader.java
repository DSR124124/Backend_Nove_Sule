package com.nove.sule.backend_nove_sule.config;

import com.nove.sule.backend_nove_sule.entity.Usuario;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.Rol;
import com.nove.sule.backend_nove_sule.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Cargador de datos iniciales para la aplicación
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando carga de datos iniciales...");
        
        createDefaultUsers();
        
        log.info("Carga de datos iniciales completada.");
    }

    /**
     * Crea usuarios por defecto si no existen
     */
    private void createDefaultUsers() {
        // Usuario Administrador
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = Usuario.builder()
                .username("admin")
                .email("admin@novasule.com")
                .password(passwordEncoder.encode("admin123"))
                .rol(Rol.ADMIN)
                .estado(Estado.ACTIVO)
                .build();
            
            usuarioRepository.save(admin);
            log.info("Usuario administrador creado: {}", admin.getUsername());
        }

        // Usuario Gerente
        if (!usuarioRepository.existsByUsername("gerente")) {
            Usuario gerente = Usuario.builder()
                .username("gerente")
                .email("gerente@novasule.com")
                .password(passwordEncoder.encode("gerente123"))
                .rol(Rol.GERENTE)
                .estado(Estado.ACTIVO)
                .build();
            
            usuarioRepository.save(gerente);
            log.info("Usuario gerente creado: {}", gerente.getUsername());
        }

        // Usuario Vendedor
        if (!usuarioRepository.existsByUsername("vendedor")) {
            Usuario vendedor = Usuario.builder()
                .username("vendedor")
                .email("vendedor@novasule.com")
                .password(passwordEncoder.encode("vendedor123"))
                .rol(Rol.VENDEDOR)
                .estado(Estado.ACTIVO)
                .build();
            
            usuarioRepository.save(vendedor);
            log.info("Usuario vendedor creado: {}", vendedor.getUsername());
        }

        // Usuario Cajero
        if (!usuarioRepository.existsByUsername("cajero")) {
            Usuario cajero = Usuario.builder()
                .username("cajero")
                .email("cajero@novasule.com")
                .password(passwordEncoder.encode("cajero123"))
                .rol(Rol.CAJERO)
                .estado(Estado.ACTIVO)
                .build();
            
            usuarioRepository.save(cajero);
            log.info("Usuario cajero creado: {}", cajero.getUsername());
        }

        // Usuario Almacenero
        if (!usuarioRepository.existsByUsername("almacenero")) {
            Usuario almacenero = Usuario.builder()
                .username("almacenero")
                .email("almacenero@novasule.com")
                .password(passwordEncoder.encode("almacenero123"))
                .rol(Rol.ALMACENERO)
                .estado(Estado.ACTIVO)
                .build();
            
            usuarioRepository.save(almacenero);
            log.info("Usuario almacenero creado: {}", almacenero.getUsername());
        }

        log.info("Verificación de usuarios por defecto completada");
    }
}
