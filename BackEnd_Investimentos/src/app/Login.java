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

    public boolean validarsenha(String senhadigitada) {
        return this.senha.equals(senhadigitada);
    }

    public boolean validarusuario(String usuariodigitado){
        return this.usuario.equals(usuariodigitado);
    }
}
     class Validacaologin {
        public void Entrar(Login Usuario, String usuariodigitado, String senhadigitada){

            if(Usuario.validarusuario(usuariodigitado) && Usuario.validarsenha(senhadigitada)){
                System.out.printf("Senha correta!");
            }else{
                System.out.printf("Senha incorreta!");
            }
        }
    }

