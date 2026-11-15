package web2_matheus.gerador_clash.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import web2_matheus.gerador_clash.model.Campeonato;

public interface CampeonatoRepository extends JpaRepository<Campeonato, Long> {
    Page<Campeonato> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}