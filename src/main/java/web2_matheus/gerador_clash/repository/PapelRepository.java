package web2_matheus.gerador_clash.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import web2_matheus.gerador_clash.model.Papel;

public interface PapelRepository extends JpaRepository<Papel, Long> {

    Optional<Papel> findByNome(String nome);
}
