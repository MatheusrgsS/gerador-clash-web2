package web2_matheus.gerador_clash.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web2_matheus.gerador_clash.core.web.pagination.PageWrapper;
import web2_matheus.gerador_clash.model.Campeonato;
import web2_matheus.gerador_clash.model.Equipe;
import web2_matheus.gerador_clash.model.Partida;
import web2_matheus.gerador_clash.service.CampeonatoService;
import web2_matheus.gerador_clash.service.EquipeService;

@Controller
@RequestMapping("/campeonatos")
public class CampeonatoController {

    private final CampeonatoService campeonatoService;
    private final EquipeService equipeService;

    public CampeonatoController(CampeonatoService campeonatoService, EquipeService equipeService) {
        this.campeonatoService = campeonatoService;
        this.equipeService = equipeService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String busca,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpServletRequest request,
            Model model) {
        Page<Campeonato> paginaCampeonatos = campeonatoService.listarPaginado(busca, page, size);
        PageWrapper<Campeonato> pagina = new PageWrapper<>(paginaCampeonatos, request);

        model.addAttribute("pagina", pagina);
        model.addAttribute("busca", busca);

        return "campeonatos/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        List<Equipe> todasEquipes = equipeService.listarTodas();
        model.addAttribute("equipes", todasEquipes);
        return "campeonatos/form";
    }

    @PostMapping("/novo")
    public String criar(@RequestParam String nome,
            @RequestParam(name = "equipeIds", required = false) List<Long> equipeIds,
            RedirectAttributes redirectAttributes) {
        try {
            Campeonato campeonato = campeonatoService.criarCampeonato(nome, equipeIds);
            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Campeonato criado e chaves sorteadas com sucesso!");
            return "redirect:/campeonatos/" + campeonato.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/campeonatos/novo";
        }
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Campeonato campeonato = campeonatoService.buscarPorId(id);
        List<Partida> partidas = campeonatoService.listarPartidasDoCampeonato(id);

        Map<Integer, List<Partida>> partidasPorRodada = partidas.stream()
                .collect(Collectors.groupingBy(p -> p.getRodada()));

        model.addAttribute("campeonato", campeonato);
        model.addAttribute("partidasPorRodada", partidasPorRodada);

        return "campeonatos/chaveamento";
    }

    @PostMapping("/{campeonatoId}/partidas/{partidaId}/placar")
    public String registrarPlacar(@PathVariable Long campeonatoId,
            @PathVariable Long partidaId,
            @RequestParam(required = false) String placarMandante,
            @RequestParam(required = false) String placarVisitante,
            RedirectAttributes redirectAttributes) {
        try {
            if (placarMandante == null || placarMandante.isBlank()
                    || placarVisitante == null || placarVisitante.isBlank()) {
                throw new IllegalArgumentException("Informe um placar válido para ambas as equipes.");
            }
            int golsMandante = Integer.parseInt(placarMandante.trim());
            int golsVisitante = Integer.parseInt(placarVisitante.trim());
            campeonatoService.registrarPlacar(campeonatoId, partidaId, golsMandante, golsVisitante);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Placar registrado com sucesso!");
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Informe apenas números inteiros válidos para o placar.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/campeonatos/" + campeonatoId;
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        campeonatoService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Campeonato excluído com sucesso!");
        return "redirect:/campeonatos";
    }
}