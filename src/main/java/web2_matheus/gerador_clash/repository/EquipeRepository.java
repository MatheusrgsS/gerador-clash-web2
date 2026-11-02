package web2_matheus.gerador_clash.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import web2_matheus.gerador_clash.model.Equipe;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {

    Page<Equipe> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}