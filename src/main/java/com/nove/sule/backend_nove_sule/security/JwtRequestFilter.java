package com.nove.sule.backend_nove_sule.security;

import com.nove.sule.backend_nove_sule.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro para validar tokens JWT en cada request
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        // Skip JWT processing for public endpoints
        if (isPublicEndpoint(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = request.getHeader(Constants.JWT_HEADER_NAME);

        String username = null;
        String jwtToken = null;

        // JWT Token está en la forma "Bearer token". Remover Bearer y obtener solo el token
        if (requestTokenHeader != null && requestTokenHeader.startsWith(Constants.JWT_TOKEN_PREFIX)) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                log.error("No se pudo obtener el username del token JWT: {}", e.getMessage());
            }
        } else {
            log.debug("JWT Token no comienza con Bearer String");
        }

        // Una vez obtenido el token, validar
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Si el token es válido, configurar Spring Security para establecer la autenticación
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Después de establecer la autenticación en el contexto, especificamos
                // que el usuario actual está autenticado. Pasa las configuraciones de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Verifica si el endpoint es público y no requiere autenticación
     */
    private boolean isPublicEndpoint(String requestPath) {
        String[] publicPaths = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/swagger-ui",
            "/api-docs",
            "/v3/api-docs"
        };

        for (String publicPath : publicPaths) {
            if (requestPath.startsWith(publicPath)) {
                return true;
            }
        }
        
        return false;
    }
}
