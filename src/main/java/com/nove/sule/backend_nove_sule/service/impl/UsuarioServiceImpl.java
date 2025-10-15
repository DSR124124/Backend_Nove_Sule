package com.nove.sule.backend_nove_sule.service.impl;

import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.dto.usuario.UsuarioDTO;
import com.nove.sule.backend_nove_sule.entity.Usuario;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.Rol;
import com.nove.sule.backend_nove_sule.mapper.UsuarioMapper;
import com.nove.sule.backend_nove_sule.repository.UsuarioRepository;
import com.nove.sule.backend_nove_sule.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Usuario
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioDTO crear(UsuarioDTO usuarioDTO) {
        log.info("Creando usuario: {}", usuarioDTO.getUsername());

        // Validar que no exista el username o email
        if (existeUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("Ya existe un usuario con ese username");
        }
        if (existeEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        
        // Encriptar password si viene en el DTO
        if (usuarioDTO.getEmail() != null) { // Usar un campo temporal para password
            usuario.setPassword(passwordEncoder.encode("123456")); // Password por defecto
        }

        usuario = usuarioRepository.save(usuario);
        
        log.info("Usuario creado exitosamente con ID: {}", usuario.getId());
        return usuarioMapper.toDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO) {
        log.info("Actualizando usuario con ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar username único (si cambió)
        if (!usuario.getUsername().equals(usuarioDTO.getUsername()) && 
            existeUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("Ya existe un usuario con ese username");
        }

        // Validar email único (si cambió)
        if (!usuario.getEmail().equals(usuarioDTO.getEmail()) && 
            existeEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        // Actualizar campos
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setRol(usuarioDTO.getRol());
        usuario.setEstado(usuarioDTO.getEstado());

        usuario = usuarioRepository.save(usuario);
        
        log.info("Usuario actualizado exitosamente");
        return usuarioMapper.toDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> buscarPorId(Long id) {
        return usuarioRepository.findByIdWithEmpleado(id)
            .map(usuarioMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<UsuarioDTO> listarTodos(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        
        List<UsuarioDTO> content = usuarios.getContent()
            .stream()
            .map(usuarioMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<UsuarioDTO>builder()
            .content(content)
            .page(usuarios.getNumber())
            .size(usuarios.getSize())
            .totalElements(usuarios.getTotalElements())
            .totalPages(usuarios.getTotalPages())
            .first(usuarios.isFirst())
            .last(usuarios.isLast())
            .empty(usuarios.isEmpty())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarPorEstado(Estado estado) {
        return usuarioRepository.findByEstado(estado)
            .stream()
            .map(usuarioMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol)
            .stream()
            .map(usuarioMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<UsuarioDTO> buscarConFiltros(String username, String email, 
                                                           Rol rol, Estado estado, Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findByFilters(username, email, rol, estado, pageable);
        
        List<UsuarioDTO> content = usuarios.getContent()
            .stream()
            .map(usuarioMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<UsuarioDTO>builder()
            .content(content)
            .page(usuarios.getNumber())
            .size(usuarios.getSize())
            .totalElements(usuarios.getTotalElements())
            .totalPages(usuarios.getTotalPages())
            .first(usuarios.isFirst())
            .last(usuarios.isLast())
            .empty(usuarios.isEmpty())
            .build();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEstado(Estado.INACTIVO);
        usuarioRepository.save(usuario);
        
        log.info("Usuario marcado como inactivo");
    }

    @Override
    @Transactional
    public UsuarioDTO cambiarEstado(Long id, Estado estado) {
        log.info("Cambiando estado del usuario {} a {}", id, estado);
        
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEstado(estado);
        usuario = usuarioRepository.save(usuario);
        
        return usuarioMapper.toDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void cambiarPassword(Long id, String nuevaPassword) {
        log.info("Cambiando contraseña para usuario ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
        
        log.info("Contraseña actualizada exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorTexto(String texto) {
        return usuarioRepository.findByTextoContaining(texto)
            .stream()
            .map(usuarioMapper::toDTO)
            .toList();
    }
}
