package web2_matheus.gerador_clash.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "O nome de usuário (login) é obrigatório")
    @Size(min = 3, max = 50, message = "O login deve ter entre 3 e 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "O login deve conter apenas letras, números, ponto, hífen ou underline")
    private String username;

    private String senha;

    private Boolean ativo = true;

    @NotEmpty(message = "Selecione ao menos um papel/permissão para o usuário")
    private Set<Long> papelIds = new HashSet<>();
}
