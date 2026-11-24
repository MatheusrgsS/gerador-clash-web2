package web2_matheus.gerador_clash.core.report;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JaspersoftUtil {

    private final DataSource dataSource;

    public JaspersoftUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public byte[] gerarRelatorio(String caminhoRelatorio) {
        return gerarRelatorio(caminhoRelatorio, new HashMap<>());
    }

    public byte[] gerarRelatorio(String caminhoRelatorio, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }

        try (Connection connection = dataSource.getConnection()) {
            JasperReport relatorioMestre = carregarOuCompilarRelatorio(caminhoRelatorio);

            try {
                JasperReport subRelatorio = carregarOuCompilarRelatorio("/relatorios/SubJogadores.jasper");
                params.put("SUBREPORT_JOGADORES", subRelatorio);
            } catch (Exception e) {
                log.error("Subrelatório SubJogadores não carregado: {}", e.getMessage(), e);
            }

            params.putIfAbsent("SUBREPORT_DIR", "/relatorios/");

            JasperPrint jasperPrint = JasperFillManager.fillReport(relatorioMestre, params, connection);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            log.error("Erro ao gerar relatório: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao gerar o relatório em PDF: " + e.getMessage(), e);
        }
    }

    private JasperReport carregarOuCompilarRelatorio(String caminho) throws Exception {
        InputStream jasperStream = getClass().getResourceAsStream(caminho);
        if (jasperStream != null) {
            return (JasperReport) JRLoader.loadObject(jasperStream);
        }

        String caminhoJrxml = caminho.replaceAll("\\.jasper$", ".jrxml");
        InputStream jrxmlStream = getClass().getResourceAsStream(caminhoJrxml);
        if (jrxmlStream != null) {
            return JasperCompileManager.compileReport(jrxmlStream);
        }

        throw new IllegalArgumentException("Arquivo de relatório não encontrado: " + caminho);
    }
}