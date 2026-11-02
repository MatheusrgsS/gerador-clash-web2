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

    // Construtor explícito: injeta as dependências e elimina 100% o bug do VS Code
    // os construtures do loombok estavam dando erro no VS Code, então optei por fazer o construtor manualmente
    public EquipeService(EquipeRepository equipeRepository, JogadorRepository jogadorRepository) {
        this.equipeRepository = equipeRepository;
        this.jogadorRepository = jogadorRepository;
    }

    // Listagem com paginação e busca por nome 
    @Transactional(readOnly = true)
    public Page<Equipe> listarPaginado(String busca, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("nome").ascending());
        if (busca != null && !busca.trim().isEmpty()) {
            return equipeRepository.findByNomeContainingIgnoreCase(busca.trim(), pageable);
        }
        return equipeRepository.findAll(pageable);
    }

    // Listagem simples (usada na seleção de participantes de torneios)
    @Transactional(readOnly = true)
    public List<Equipe> listarTodas() {
        return equipeRepository.findAll(Sort.by("nome").ascending());
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

    // Gerenciamento de jogadores vinculados à equipe
    @Transactional
    public Jogador adicionarJogador(Long equipeId, Jogador jogador) {
        Equipe equipe = buscarPorId(equipeId);
        equipe.adicionarJogador(jogador);
        return jogadorRepository.save(jogador);
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