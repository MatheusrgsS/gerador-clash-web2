package web2_matheus.gerador_clash.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import web2_matheus.gerador_clash.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    Page<Usuario> findByNomeContainingIgnoreCaseOrUsernameContainingIgnoreCase(String nome, String username, Pageable pageable);
}
