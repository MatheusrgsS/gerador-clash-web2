package web2_matheus.gerador_clash.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class RelatoriosController {

    private final RelatoriosService relatoriosService;

    public RelatoriosController(RelatoriosService relatoriosService) {
        this.relatoriosService = relatoriosService;
    }

    @GetMapping("/relatorios/campeonatos/{id}/pdf")
    public ResponseEntity<byte[]> gerarRelatorioCampeonatoPartidas(@PathVariable Long id) {
        log.debug("Gerando relatório do campeonato id {}", id);

        byte[] relatorio = relatoriosService.gerarRelatorioCampeonatoPartidas(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=Campeonato_" + id + ".pdf")
                .body(relatorio);
    }
}
