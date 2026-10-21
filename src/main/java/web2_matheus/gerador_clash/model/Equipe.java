package web2_matheus.gerador_clash.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Equipe {

    private Long id;
    private String nome;
    private String responsavel;
    private String telefone;

    // Relacionamento 1:N (Uma equipe possui vários jogadores)
    private List<Jogador> jogadores = new ArrayList<>();

    public Equipe() {
    }

    public Equipe(Long id, String nome, String responsavel, String telefone) {
        this.id = id;
        this.nome = nome;
        this.responsavel = responsavel;
        this.telefone = telefone;
    }

    // Métodos auxiliares para gerenciar a associação com consistência
    public void adicionarJogador(Jogador jogador) {
        this.jogadores.add(jogador);
        jogador.setEquipe(this);
    }

    public void removerJogador(Jogador jogador) {
        this.jogadores.remove(jogador);
        jogador.setEquipe(null);
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

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipe equipe = (Equipe) o;
        return Objects.equals(id, equipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Equipe{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", responsavel='" + responsavel + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}