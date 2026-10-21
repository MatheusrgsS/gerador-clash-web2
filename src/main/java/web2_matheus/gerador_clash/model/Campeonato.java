package web2_matheus.gerador_clash.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "campeonato")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCampeonato status = StatusCampeonato.CRIADO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campeao_id")
    private Equipe campeao;

    @ManyToMany
    @JoinTable(
        name = "campeonato_equipe",
        joinColumns = @JoinColumn(name = "campeonato_id"),
        inverseJoinColumns = @JoinColumn(name = "equipe_id")
    )
    private List<Equipe> equipes = new ArrayList<>();

    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Partida> partidas = new ArrayList<>();

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
}