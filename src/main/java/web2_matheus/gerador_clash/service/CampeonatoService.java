package web2_matheus.gerador_clash.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web2_matheus.gerador_clash.model.Arena;
import web2_matheus.gerador_clash.model.Campeonato;
import web2_matheus.gerador_clash.model.Equipe;
import web2_matheus.gerador_clash.model.Partida;
import web2_matheus.gerador_clash.model.StatusCampeonato;
import web2_matheus.gerador_clash.model.StatusPartida;
import web2_matheus.gerador_clash.repository.ArenaRepository;
import web2_matheus.gerador_clash.repository.CampeonatoRepository;
import web2_matheus.gerador_clash.repository.EquipeRepository;
import web2_matheus.gerador_clash.repository.PartidaRepository;

@Service
public class CampeonatoService {

    private final CampeonatoRepository campeonatoRepository;
    private final PartidaRepository partidaRepository;
    private final EquipeRepository equipeRepository;
    private final ArenaRepository arenaRepository;

    public CampeonatoService(CampeonatoRepository campeonatoRepository,
            PartidaRepository partidaRepository,
            EquipeRepository equipeRepository,
            ArenaRepository arenaRepository) {
        this.campeonatoRepository = campeonatoRepository;
        this.partidaRepository = partidaRepository;
        this.equipeRepository = equipeRepository;
        this.arenaRepository = arenaRepository;
    }

    public Page<Campeonato> listarPaginado(String busca, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (busca != null && !busca.isBlank()) {
            return campeonatoRepository.findByNomeContainingIgnoreCase(busca.trim(), pageable);
        }
        return campeonatoRepository.findAll(pageable);
    }

    public Campeonato buscarPorId(Long id) {
        return campeonatoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campeonato não encontrado com ID: " + id));
    }

    public List<Partida> listarPartidasDoCampeonato(Long campeonatoId) {
        return partidaRepository.findByCampeonatoIdOrderByRodadaAscIdAsc(campeonatoId);
    }

    @Transactional
    public Campeonato criarCampeonato(String nome, List<Long> equipeIds) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do campeonato é obrigatório.");
        }

        if (equipeIds == null || equipeIds.isEmpty()) {
            throw new IllegalArgumentException("Selecione as equipes participantes.");
        }

        int total = equipeIds.size();
        if (total != 4 && total != 8 && total != 16) {
            throw new IllegalArgumentException(
                    "O campeonato mata-mata exige exatamente 4, 8 ou 16 equipes (selecionadas: " + total + ").");
        }

        List<Equipe> equipes = equipeRepository.findAllById(equipeIds);
        if (equipes.size() != total) {
            throw new IllegalArgumentException("Uma ou mais equipes selecionadas não foram encontradas no banco.");
        }

        Campeonato campeonato = new Campeonato();
        campeonato.setNome(nome.trim());
        campeonato.setDataInicio(LocalDate.now());
        campeonato.setStatus(StatusCampeonato.EM_ANDAMENTO);
        campeonato.setEquipes(new ArrayList<>(equipes));
        campeonato = campeonatoRepository.save(campeonato);

        List<Equipe> equipesEmbaralhadas = new ArrayList<>(equipes);
        Collections.shuffle(equipesEmbaralhadas);

        List<Arena> arenas = arenaRepository.findAll();
        Random random = new Random();

        List<Partida> partidasRodada1 = new ArrayList<>();
        LocalDateTime dataHoraBase = LocalDateTime.now().plusDays(1).withHour(16).withMinute(0).withSecond(0);

        for (int i = 0; i < equipesEmbaralhadas.size(); i += 2) {
            Partida partida = new Partida();
            partida.setCampeonato(campeonato);
            partida.setRodada(1);
            partida.setStatus(StatusPartida.AGENDADA);
            partida.setEquipeMandante(equipesEmbaralhadas.get(i));
            partida.setEquipeVisitante(equipesEmbaralhadas.get(i + 1));
            partida.setDataHora(dataHoraBase.plusHours(i));

            if (!arenas.isEmpty()) {
                partida.setArena(arenas.get(random.nextInt(arenas.size())));
            }

            partidasRodada1.add(partida);
        }

        partidaRepository.saveAll(partidasRodada1);
        return campeonato;
    }

    @Transactional
    public void registrarPlacar(Long campeonatoId, Long partidaId, Integer golsMandante, Integer golsVisitante) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada com ID: " + partidaId));

        if (!partida.getCampeonato().getId().equals(campeonatoId)) {
            throw new IllegalArgumentException("A partida não pertence a este campeonato.");
        }

        if (golsMandante == null || golsVisitante == null || golsMandante < 0 || golsVisitante < 0) {
            throw new IllegalArgumentException("Informe um placar válido para ambas as equipes.");
        }

        if (golsMandante.equals(golsVisitante)) {
            throw new IllegalArgumentException(
                    "Em partidas eliminatórias mata-mata não pode haver empate! Defina o vencedor.");
        }

        partida.setPlacarMandante(golsMandante);
        partida.setPlacarVisitante(golsVisitante);
        partida.setStatus(StatusPartida.FINALIZADA);

        if (golsMandante > golsVisitante) {
            partida.setVencedor(partida.getEquipeMandante());
        } else {
            partida.setVencedor(partida.getEquipeVisitante());
        }

        partidaRepository.save(partida);

        verificarEAvancarRodada(partida.getCampeonato(), partida.getRodada());
    }

    private void verificarEAvancarRodada(Campeonato campeonato, Integer rodadaAtual) {
        List<Partida> todasPartidas = partidaRepository.findByCampeonatoIdOrderByRodadaAscIdAsc(campeonato.getId());

        List<Partida> partidasDaRodada = todasPartidas.stream()
                .filter(p -> p.getRodada().equals(rodadaAtual))
                .toList();

        boolean todasFinalizadas = partidasDaRodada.stream()
                .allMatch(p -> p.getStatus() == StatusPartida.FINALIZADA);

        if (!todasFinalizadas) {
            return; 
        }

        //Final
        if (partidasDaRodada.size() == 1) {
            Equipe campeao = partidasDaRodada.get(0).getVencedor();
            campeonato.setCampeao(campeao);
            campeonato.setStatus(StatusCampeonato.FINALIZADO);
            campeonatoRepository.save(campeonato);
            return;
        }

        List<Equipe> vencedores = partidasDaRodada.stream()
                .map(p -> p.getVencedor())
                .toList();

        List<Arena> arenas = arenaRepository.findAll();
        Random random = new Random();
        int proximaRodada = rodadaAtual + 1;
        LocalDateTime dataHoraProxima = LocalDateTime.now().plusDays(proximaRodada * 2L).withHour(16).withMinute(0);

        List<Partida> proximosJogos = new ArrayList<>();
        for (int i = 0; i < vencedores.size(); i += 2) {
            Partida proximoJogo = new Partida();
            proximoJogo.setCampeonato(campeonato);
            proximoJogo.setRodada(proximaRodada);
            proximoJogo.setStatus(StatusPartida.AGENDADA);
            proximoJogo.setEquipeMandante(vencedores.get(i));
            proximoJogo.setEquipeVisitante(vencedores.get(i + 1));
            proximoJogo.setDataHora(dataHoraProxima.plusHours(i));

            if (!arenas.isEmpty()) {
                proximoJogo.setArena(arenas.get(random.nextInt(arenas.size())));
            }

            proximosJogos.add(proximoJogo);
        }

        partidaRepository.saveAll(proximosJogos);
    }

    @Transactional
    public void excluir(Long id) {
        if (!campeonatoRepository.existsById(id)) {
            throw new IllegalArgumentException("Campeonato não encontrado com ID: " + id);
        }
        campeonatoRepository.deleteById(id);
    }
}