package br.ufg.inf.instaladoreduroam.entidades;

import java.io.Serializable;

/**
 * Created by Maycon on 26/05/2015.
 */
public class Conta implements Serializable {

    private long id;
    private String login;
    private String senha;

    public Conta() {
    }

    public Conta(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public Conta(long id, String login, String senha) {
        this.id = id;
        this.login = login;
        this.senha = senha;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
