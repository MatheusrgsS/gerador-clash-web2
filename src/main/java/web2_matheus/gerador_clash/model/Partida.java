package web2_matheus.gerador_clash.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "partida")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rodada;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "placar_mandante")
    private Integer placarMandante;

    @Column(name = "placar_visitante")
    private Integer placarVisitante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPartida status = StatusPartida.AGENDADA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arena_id")
    private Arena arena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_mandante_id", nullable = false)
    private Equipe equipeMandante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_visitante_id", nullable = false)
    private Equipe equipeVisitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vencedor_id")
    private Equipe vencedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campeonato_id", nullable = false)
    private Campeonato campeonato;
}