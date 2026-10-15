package app;

public class Login {

    private String usuario;
    private String senha;

    public Login(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getusuario() {
        return usuario;
    }

    public void setUsuario(String NovoUsuario){
        this.usuario = NovoUsuario;
    }

    public void setSenha(String NovaSenha){
        this.senha = NovaSenha;
    }

    public boolean validarsenha(String senhadigitada) {
        return this.senha.equals(senhadigitada);
    }

    public boolean validarusuario(String usuariodigitado){
        return this.usuario.equals(usuariodigitado);
    }
}
     class Validacaologin {
        public boolean Entrar(Login Usuario, String usuariodigitado, String senhadigitada){

            if(Usuario.validarusuario(usuariodigitado) && Usuario.validarsenha(senhadigitada)){
                    return true;
            }else{
                System.out.printf("Senha e usuario incorretos!");
                    return false;
            }
        }
        public void ValidarSenha(Login TrocaSenha, String UsuarioAntigo, String NovaSenha){
            if(TrocaSenha.validarusuario(UsuarioAntigo)){
                TrocaSenha.setSenha(NovaSenha);
                System.out.printf("Senha Trocada com Sucesso!");
            }else{
                System.out.printf("Falha ao alterar a Senha");
            }
        }
        public void ValidarUsuario(Login TrocaUsuario, String SenhaAntiga, String NovoUsuario){
            if(TrocaUsuario.validarsenha(SenhaAntiga)){
                TrocaUsuario.setUsuario(NovoUsuario);
                System.out.printf("Usuario Trocado com Sucesso!");
            }else{
                System.out.printf("Falha ao alterar o Usuario");
            }
        }
    }

