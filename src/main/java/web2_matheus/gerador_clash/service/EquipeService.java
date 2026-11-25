package web2_matheus.gerador_clash.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web2_matheus.gerador_clash.model.Equipe;
import web2_matheus.gerador_clash.model.Jogador;
import web2_matheus.gerador_clash.repository.EquipeRepository;
import web2_matheus.gerador_clash.repository.JogadorRepository;

@Service
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final JogadorRepository jogadorRepository;

    public EquipeService(EquipeRepository equipeRepository, JogadorRepository jogadorRepository) {
        this.equipeRepository = equipeRepository;
        this.jogadorRepository = jogadorRepository;
    }

    @Transactional(readOnly = true)
    public Page<Equipe> listarPaginado(String busca, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Direction.ASC, "nome"));
        if (busca != null && !busca.trim().isEmpty()) {
            return equipeRepository.findByNomeContainingIgnoreCase(busca.trim(), pageable);
        }
        return equipeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Equipe> listarTodas() {
        return equipeRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    @Transactional(readOnly = true)
    public Equipe buscarPorId(Long id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada com ID: " + id));
    }

    @Transactional
    public Equipe salvar(Equipe equipe) {
        return equipeRepository.save(equipe);
    }

    @Transactional
    public void excluir(Long id) {
        if (!equipeRepository.existsById(id)) {
            throw new IllegalArgumentException("Equipe não encontrada para exclusão.");
        }
        equipeRepository.deleteById(id);
    }

    @Transactional
    public Jogador adicionarJogador(Long equipeId, Jogador jogador) {
        Equipe equipe = buscarPorId(equipeId);
        jogador.setId(null);
        jogador.setEquipe(equipe);
        Jogador salvo = jogadorRepository.save(jogador);
        equipe.getJogadores().add(salvo);
        return salvo;
    }

    @Transactional
    public void removerJogador(Long equipeId, Long jogadorId) {
        Equipe equipe = buscarPorId(equipeId);
        Jogador jogador = jogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new IllegalArgumentException("Jogador não encontrado."));

        equipe.removerJogador(jogador);
        jogadorRepository.delete(jogador);
    }
}