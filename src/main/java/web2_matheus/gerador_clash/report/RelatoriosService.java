package web2_matheus.gerador_clash.report;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import web2_matheus.gerador_clash.core.report.JaspersoftUtil;

@Service
public class RelatoriosService {

    private final JaspersoftUtil jaspersoftUtil;

    public RelatoriosService(JaspersoftUtil jaspersoftUtil) {
        this.jaspersoftUtil = jaspersoftUtil;
    }

    public byte[] gerarRelatorioCampeonatoPartidas(Long campeonatoId) {
        Map<String, Object> params = new HashMap<>();
        params.put("CAMPEONATO_ID", campeonatoId);

        return jaspersoftUtil.gerarRelatorio("/relatorios/CampeonatoPartidasCompleto.jasper", params);
    }
}
