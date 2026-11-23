package web2_matheus.gerador_clash.config;

import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import web2_matheus.gerador_clash.model.Papel;
import web2_matheus.gerador_clash.model.Usuario;
import web2_matheus.gerador_clash.repository.PapelRepository;
import web2_matheus.gerador_clash.repository.UsuarioRepository;

@Component
public class SecurityDataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityDataInitializer(UsuarioRepository usuarioRepository,
                                   PapelRepository papelRepository,
                                   PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Garantir papéis
        Papel roleAdmin = papelRepository.findByNome("ROLE_ADMIN")
                .orElseGet(() -> papelRepository.save(new Papel("ROLE_ADMIN")));

        Papel roleOrganizador = papelRepository.findByNome("ROLE_ORGANIZADOR")
                .orElseGet(() -> papelRepository.save(new Papel("ROLE_ORGANIZADOR")));

        // Garantir usuário ADMIN inicial
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador do Sistema");
            admin.setUsername("admin");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setAtivo(true);
            admin.setPapeis(Set.of(roleAdmin));
            usuarioRepository.save(admin);
        }

        // Garantir usuário ORGANIZADOR inicial
        if (!usuarioRepository.existsByUsername("organizador")) {
            Usuario organizador = new Usuario();
            organizador.setNome("Organizador de Torneios");
            organizador.setUsername("organizador");
            organizador.setSenha(passwordEncoder.encode("organizador123"));
            organizador.setAtivo(true);
            organizador.setPapeis(Set.of(roleOrganizador));
            usuarioRepository.save(organizador);
        }
    }
}
