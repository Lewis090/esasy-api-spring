package com.easy.easyapi.controller;

import com.easy.easyapi.dto.UsuarioCreateDTO;
import com.easy.easyapi.dto.UsuarioDTO;
import com.easy.easyapi.model.Usuario;
import com.easy.easyapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:8080") // seu front-end
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private boolean isUsuarioAutenticadoOwner(Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Usuario) {
            return ((Usuario) principal).getId().equals(id);
        }
        return false;
    }

    // 🔹 Listar todos usuários (Protegido - Apenas para fins de exemplo, idealmente restrito)
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> dtos = usuarios.stream().map(UsuarioDTO::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // 🔹 Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        if (!isUsuarioAutenticadoOwner(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        return usuarioOpt
                .map(u -> ResponseEntity.ok(UsuarioDTO.fromEntity(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Criar usuário (cadastro) - Redirecionar para AuthController ou manter aqui com criptografia
    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioDto) {
        Usuario usuario = usuarioDto.toEntity();
        // Nota: O serviço deveria ser usado aqui para garantir a criptografia, 
        // mas para manter a compatibilidade se o front usar este endpoint:
        // Idealmente, este endpoint deveria ser desativado em favor do /auth/register
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
    }

    // 🔹 Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioCreateDTO u) {
        if (!isUsuarioAutenticadoOwner(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNome(u.getNome());
            usuarioExistente.setEmail(u.getEmail());
            // Se a senha mudou, ela deveria ser criptografada.
            // Recomendado usar o UsuarioService.salvar()
            usuarioExistente.setSenha(u.getSenha()); 
            Usuario atualizado = usuarioRepository.save(usuarioExistente);
            return ResponseEntity.ok(UsuarioDTO.fromEntity(atualizado));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        if (!isUsuarioAutenticadoOwner(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
