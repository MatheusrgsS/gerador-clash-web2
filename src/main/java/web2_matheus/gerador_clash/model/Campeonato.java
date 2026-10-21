package web2_matheus.gerador_clash.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Campeonato {

    private Long id;
    private String nome;
    private LocalDate dataInicio;
    private StatusCampeonato status = StatusCampeonato.CRIADO;

    // Equipe consagrada como vencedora do torneio
    private Equipe campeao;

    // Equipes participantes inscritas no torneio
    private List<Equipe> equipes = new ArrayList<>();

    // Partidas que compõem as fases do torneio
    private List<Partida> partidas = new ArrayList<>();

    public Campeonato() {
    }

    public Campeonato(Long id, String nome, LocalDate dataInicio) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.status = StatusCampeonato.CRIADO;
    }

    // Métodos auxiliares de associação
    public void adicionarEquipe(Equipe equipe) {
        this.equipes.add(equipe);
    }

    public void removerEquipe(Equipe equipe) {
        this.equipes.remove(equipe);
    }

    public void adicionarPartida(Partida partida) {
        this.partidas.add(partida);
        partida.setCampeonato(this);
    }

    public void removerPartida(Partida partida) {
        this.partidas.remove(partida);
        partida.setCampeonato(null);
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public StatusCampeonato getStatus() {
        return status;
    }

    public void setStatus(StatusCampeonato status) {
        this.status = status;
    }

    public Equipe getCampeao() {
        return campeao;
    }

    public void setCampeao(Equipe campeao) {
        this.campeao = campeao;
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }

    public void setEquipes(List<Equipe> equipes) {
        this.equipes = equipes;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Campeonato that = (Campeonato) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Campeonato{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataInicio=" + dataInicio +
                ", status=" + status +
                '}';
    }
}