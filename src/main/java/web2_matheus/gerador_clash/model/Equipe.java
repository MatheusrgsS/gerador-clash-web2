package web2_matheus.gerador_clash.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "equipe")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 100)
    private String responsavel;

    @Column(length = 20)
    private String telefone;

    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Jogador> jogadores = new ArrayList<>();

    // Métodos auxiliares continuam aqui para facilitar o vinculo com Jogador
    public void adicionarJogador(Jogador jogador) {
        this.jogadores.add(jogador);
        jogador.setEquipe(this);
    }

    public void removerJogador(Jogador jogador) {
        this.jogadores.remove(jogador);
        jogador.setEquipe(null);
    }
}