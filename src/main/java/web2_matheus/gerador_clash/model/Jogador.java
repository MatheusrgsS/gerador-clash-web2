package web2_matheus.gerador_clash.model;

import java.util.Objects;

public class Jogador {

    private Long id;
    private String nome;
    private String posicao;
    private Integer numeroCamisa;
    
    // Referência para a equipe à qual o jogador pertence
    private Equipe equipe;

    public Jogador() {
    }

    public Jogador(Long id, String nome, String posicao, Integer numeroCamisa, Equipe equipe) {
        this.id = id;
        this.nome = nome;
        this.posicao = posicao;
        this.numeroCamisa = numeroCamisa;
        this.equipe = equipe;
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

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public Integer getNumeroCamisa() {
        return numeroCamisa;
    }

    public void setNumeroCamisa(Integer numeroCamisa) {
        this.numeroCamisa = numeroCamisa;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jogador jogador = (Jogador) o;
        return Objects.equals(id, jogador.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Jogador{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", posicao='" + posicao + '\'' +
                ", numeroCamisa=" + numeroCamisa +
                '}';
    }
}