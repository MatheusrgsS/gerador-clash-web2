package web2_matheus.gerador_clash.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Partida {

    private Long id;
    private Integer rodada; // 1 = Oitavas/Quartas, 2 = Semifinal, 3 = Final, etc.
    private LocalDateTime dataHora;
    private Integer placarMandante;
    private Integer placarVisitante;
    private StatusPartida status = StatusPartida.AGENDADA;

    // Associações
    private Arena arena;
    private Equipe equipeMandante;
    private Equipe equipeVisitante;
    private Equipe vencedor;
    private Campeonato campeonato;

    public Partida() {
    }

    public Partida(Long id, Integer rodada, LocalDateTime dataHora, Arena arena, 
                   Equipe equipeMandante, Equipe equipeVisitante, Campeonato campeonato) {
        this.id = id;
        this.rodada = rodada;
        this.dataHora = dataHora;
        this.arena = arena;
        this.equipeMandante = equipeMandante;
        this.equipeVisitante = equipeVisitante;
        this.campeonato = campeonato;
        this.status = StatusPartida.AGENDADA;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRodada() {
        return rodada;
    }

    public void setRodada(Integer rodada) {
        this.rodada = rodada;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getPlacarMandante() {
        return placarMandante;
    }

    public void setPlacarMandante(Integer placarMandante) {
        this.placarMandante = placarMandante;
    }

    public Integer getPlacarVisitante() {
        return placarVisitante;
    }

    public void setPlacarVisitante(Integer placarVisitante) {
        this.placarVisitante = placarVisitante;
    }

    public StatusPartida getStatus() {
        return status;
    }

    public void setStatus(StatusPartida status) {
        this.status = status;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public Equipe getEquipeMandante() {
        return equipeMandante;
    }

    public void setEquipeMandante(Equipe equipeMandante) {
        this.equipeMandante = equipeMandante;
    }

    public Equipe getEquipeVisitante() {
        return equipeVisitante;
    }

    public void setEquipeVisitante(Equipe equipeVisitante) {
        this.equipeVisitante = equipeVisitante;
    }

    public Equipe getVencedor() {
        return vencedor;
    }

    public void setVencedor(Equipe vencedor) {
        this.vencedor = vencedor;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partida partida = (Partida) o;
        return Objects.equals(id, partida.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Partida{" +
                "id=" + id +
                ", rodada=" + rodada +
                ", status=" + status +
                ", placarMandante=" + placarMandante +
                ", placarVisitante=" + placarVisitante +
                '}';
    }
}