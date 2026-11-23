package web2_matheus.gerador_clash.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
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
import web2_matheus.gerador_clash.dto.UsuarioDTO;
import web2_matheus.gerador_clash.model.Papel;
import web2_matheus.gerador_clash.model.Usuario;
import web2_matheus.gerador_clash.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String busca,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "5") int size,
                         HttpServletRequest request,
                         Model model) {
        Page<Usuario> paginaUsuarios = usuarioService.listarPaginado(busca, page, size);
        PageWrapper<Usuario> pagina = new PageWrapper<>(paginaUsuarios, request);

        model.addAttribute("pagina", pagina);
        model.addAttribute("busca", busca);

        return "usuarios/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        List<Papel> todosPapeis = usuarioService.listarTodosPapeis();

        model.addAttribute("usuario", usuarioDTO);
        model.addAttribute("todosPapeis", todosPapeis);
        return "usuarios/form";
    }

    @PostMapping("/novo")
    public String salvarNovo(@Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("todosPapeis", usuarioService.listarTodosPapeis());
            return "usuarios/form";
        }

        try {
            usuarioService.salvar(usuarioDTO);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário cadastrado com sucesso!");
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("todosPapeis", usuarioService.listarTodosPapeis());
            model.addAttribute("mensagemErro", e.getMessage());
            return "usuarios/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setUsername(usuario.getUsername());
        dto.setAtivo(usuario.getAtivo());
        dto.setPapelIds(usuario.getPapeis().stream().map(Papel::getId).collect(Collectors.toSet()));

        model.addAttribute("usuario", dto);
        model.addAttribute("todosPapeis", usuarioService.listarTodosPapeis());
        return "usuarios/form";
    }

    @PostMapping("/{id}/editar")
    public String salvarEdicao(@PathVariable Long id,
                               @Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        usuarioDTO.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("todosPapeis", usuarioService.listarTodosPapeis());
            return "usuarios/form";
        }

        try {
            usuarioService.salvar(usuarioDTO);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário atualizado com sucesso!");
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("todosPapeis", usuarioService.listarTodosPapeis());
            model.addAttribute("mensagemErro", e.getMessage());
            return "usuarios/form";
        }
    }

    @PostMapping("/{id}/status")
    public String alternarStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.alternarStatus(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Status do usuário alterado com sucesso!");
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário excluído com sucesso!");
        return "redirect:/usuarios";
    }
}
