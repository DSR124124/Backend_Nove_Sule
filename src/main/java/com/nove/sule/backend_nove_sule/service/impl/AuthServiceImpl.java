package com.nove.sule.backend_nove_sule.service.impl;

import com.nove.sule.backend_nove_sule.dto.auth.LoginRequestDTO;
import com.nove.sule.backend_nove_sule.dto.auth.LoginResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Usuario;
import com.nove.sule.backend_nove_sule.repository.UsuarioRepository;
import com.nove.sule.backend_nove_sule.security.JwtTokenUtil;
import com.nove.sule.backend_nove_sule.service.AuthService;
import com.nove.sule.backend_nove_sule.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de autenticación
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // Obtener usuario autenticado
            Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(Constants.ERROR_USUARIO_NO_ENCONTRADO));

            // Generar token
            String token = jwtTokenUtil.generateToken(usuario);

            // Construir respuesta
            return LoginResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .nombreCompleto(usuario.getEmpleado() != null ? 
                    usuario.getEmpleado().getNombreCompleto() : usuario.getUsername())
                .build();

        } catch (AuthenticationException e) {
            log.error("Error en autenticación para usuario: {}", loginRequest.getUsername());
            throw new RuntimeException(Constants.ERROR_CREDENCIALES_INVALIDAS);
        }
    }

    @Override
    public String generateToken(Usuario usuario) {
        return jwtTokenUtil.generateToken(usuario);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            // Para validación simple sin UserDetails
            String username = jwtTokenUtil.getUsernameFromToken(token);
            return username != null && !jwtTokenUtil.isTokenExpired(token);
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        return jwtTokenUtil.getUsernameFromToken(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return jwtTokenUtil.isTokenExpired(token);
    }
}
