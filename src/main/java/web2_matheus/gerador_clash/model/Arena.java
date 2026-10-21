package web2_matheus.gerador_clash.model;

import java.util.Objects;

public class Arena {

    private Long id;
    private String nome;
    private String endereco;
    private Integer capacidade;

    // Construtor padrão (sem argumentos)
    public Arena() {
    }

    // Construtor completo para facilitar instanciação
    public Arena(Long id, String nome, String endereco, Integer capacidade) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidade = capacidade;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    // equals e hashCode baseados no id (essenciais em coleções Java)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena) o;
        return Objects.equals(id, arena.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Arena{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", capacidade=" + capacidade +
                '}';
    }
}