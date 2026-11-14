package app;

import java.util.List;

public class CarregarDados {
    private List<UsuarioJson> Usuarios;

    public List<UsuarioJson> getUsuarios() {
        return Usuarios;
    }
}

class UsuarioJson{
    private int id;
    private String usuario;
    private String senha;
    private String nome;
    private int saldo;

    // Getters para puxar os dados

    public int getId(){
        return id;
    }
    public String getUsuario(){
        return usuario;
    }
    public String getNome(){
        return nome;
    }
    public String getSenha(){
        return senha;
    }
    public int getSaldo(){
        return saldo;
    }
}