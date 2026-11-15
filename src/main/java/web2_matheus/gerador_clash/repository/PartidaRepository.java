package web2_matheus.gerador_clash.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import web2_matheus.gerador_clash.model.Partida;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByCampeonatoIdOrderByRodadaAscIdAsc(Long campeonatoId);
}