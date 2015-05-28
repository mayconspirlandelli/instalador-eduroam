package br.ufg.inf.instaladoreduroam.entidades;

/**
 * Created by Maycon on 26/05/2015.
 */
public class Conta {

    private String login;
    private String senha;

    public Conta() {
    }

    public Conta(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
