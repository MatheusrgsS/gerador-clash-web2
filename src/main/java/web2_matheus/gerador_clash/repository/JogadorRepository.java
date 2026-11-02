package web2_matheus.gerador_clash.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import web2_matheus.gerador_clash.model.Jogador;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {

    List<Jogador> findByEquipeId(Long equipeId);
}