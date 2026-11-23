package web2_matheus.gerador_clash.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web2_matheus.gerador_clash.dto.UsuarioDTO;
import web2_matheus.gerador_clash.model.Papel;
import web2_matheus.gerador_clash.model.Usuario;
import web2_matheus.gerador_clash.repository.PapelRepository;
import web2_matheus.gerador_clash.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PapelRepository papelRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    @Transactional(readOnly = true)
    public Page<Usuario> listarPaginado(String busca, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
        if (busca != null && !busca.trim().isEmpty()) {
            return usuarioRepository.findByNomeContainingIgnoreCaseOrUsernameContainingIgnoreCase(busca, busca, pageable);
        }
        return usuarioRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Papel> listarTodosPapeis() {
        return papelRepository.findAll(Sort.by("nome").ascending());
    }

    @Transactional
    public Usuario salvar(UsuarioDTO dto) {
        Usuario usuario;

        if (dto.getId() == null) {
            // Novo cadastro
            if (usuarioRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("Já existe um usuário com o login '" + dto.getUsername() + "'");
            }
            if (dto.getSenha() == null || dto.getSenha().trim().length() < 4) {
                throw new IllegalArgumentException("A senha inicial deve ter pelo menos 4 caracteres");
            }
            usuario = new Usuario();
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        } else {
            // Edição
            usuario = buscarPorId(dto.getId());
            // Se mudou o username, checa unicidade
            if (!usuario.getUsername().equalsIgnoreCase(dto.getUsername()) && usuarioRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("Já existe um usuário com o login '" + dto.getUsername() + "'");
            }
            // Atualiza senha somente se foi informada
            if (dto.getSenha() != null && !dto.getSenha().trim().isEmpty()) {
                if (dto.getSenha().trim().length() < 4) {
                    throw new IllegalArgumentException("A nova senha deve ter pelo menos 4 caracteres");
                }
                usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
            }
        }

        usuario.setNome(dto.getNome());
        usuario.setUsername(dto.getUsername().trim().toLowerCase());
        usuario.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);

        // Associar papéis
        List<Papel> papeisSelecionados = papelRepository.findAllById(dto.getPapelIds());
        usuario.setPapeis(new HashSet<>(papeisSelecionados));

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void alternarStatus(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(!usuario.getAtivo());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}
