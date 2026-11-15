package web2_matheus.gerador_clash.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web2_matheus.gerador_clash.core.web.pagination.PageWrapper;
import web2_matheus.gerador_clash.model.Equipe;
import web2_matheus.gerador_clash.model.Jogador;
import web2_matheus.gerador_clash.service.EquipeService;

@Controller
@RequestMapping("/equipes")
public class EquipeController {

    private final EquipeService equipeService;

    public EquipeController(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String busca,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "5") int size,
                         HttpServletRequest request,
                         Model model) {
        Page<Equipe> paginaEquipes = equipeService.listarPaginado(busca, page, size);
        PageWrapper<Equipe> pagina = new PageWrapper<>(paginaEquipes, request);
        
        model.addAttribute("pagina", pagina);
        model.addAttribute("busca", busca);
        
        return "equipes/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("equipe", new Equipe());
        return "equipes/form";
    }

    @PostMapping("/novo")
    public String salvarNovo(@Valid @ModelAttribute("equipe") Equipe equipe,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "equipes/form"; 
        }
        equipeService.salvar(equipe);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Equipe cadastrada com sucesso!");
        return "redirect:/equipes";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Equipe equipe = equipeService.buscarPorId(id);
        model.addAttribute("equipe", equipe);
        return "equipes/form";
    }

    @PostMapping("/{id}/editar")
    public String salvarEdicao(@PathVariable Long id,
                               @Valid @ModelAttribute("equipe") Equipe equipe,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "equipes/form";
        }
        equipe.setId(id);
        equipeService.salvar(equipe);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Equipe atualizada com sucesso!");
        return "redirect:/equipes";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        equipeService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Equipe excluída com sucesso!");
        return "redirect:/equipes";
    }

    @GetMapping("/{id}/jogadores")
    public String gerenciarJogadores(@PathVariable Long id, Model model) {
        Equipe equipe = equipeService.buscarPorId(id);
        model.addAttribute("equipe", equipe);
        model.addAttribute("novoJogador", new Jogador());
        return "equipes/jogadores";
    }

    @PostMapping("/{id}/jogadores")
    public String adicionarJogador(@PathVariable Long id,
                                   @Valid @ModelAttribute("novoJogador") Jogador jogador,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Equipe equipe = equipeService.buscarPorId(id);
            model.addAttribute("equipe", equipe);
            return "equipes/jogadores";
        }
        equipeService.adicionarJogador(id, jogador);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Jogador adicionado com sucesso!");
        return "redirect:/equipes/" + id + "/jogadores";
    }

    @PostMapping("/{id}/jogadores/{jogadorId}/excluir")
    public String removerJogador(@PathVariable Long id,
                                 @PathVariable Long jogadorId,
                                 RedirectAttributes redirectAttributes) {
        equipeService.removerJogador(id, jogadorId);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Jogador removido com sucesso!");
        return "redirect:/equipes/" + id + "/jogadores";
    }
}
