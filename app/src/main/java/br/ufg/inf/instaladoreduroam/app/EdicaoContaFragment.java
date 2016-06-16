package br.ufg.inf.instaladoreduroam.app;

import android.content.Intent;
import android.support.v4.app.Fragment;

import br.ufg.inf.instaladoreduroam.entidades.Conta;

/**
 * Created by Maycon on 26/05/2015.
 */

/** Não precisa dessa classe por entando. 08/6/16 */

public class EdicaoContaFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();

        if(verificarParametrosRecebidos()){
            //Preenche na tela o login e senha.
            //Seta o titulo da Actionbar como "Editar Conta".
        } else {
            //Seta o titulo da Actionbar como "Adicionar Conta".

        }
    }


    private boolean verificarParametrosRecebidos() {
//get Conta
        return false;
    }

   private void onChangeListerLogin(){
       if(!validarLogin()){
           exibirAlerta("Login inválido");
       }
   }

    private void onChangeListerSenha(){
        if(!validarSenha()){
            exibirAlerta("Senha inválida");
        }
    }

    private boolean validarLogin(){
        //Disparado pelo evento apos edição.
        return true;
    }

    private boolean validarSenha(){
        //Disparado pelo evento apos edição.
        return true;
    }

    private boolean validarDados(){
        if (true) {
            exibirAlerta("Informe o login");
        }
        if (true) {
            exibirAlerta("Informe a senha");
        }
        return true;
    }


    private void exibirAlerta(String mensagem){}


    private void salvarConta(){

        //apos a validações.
        abrirTelaRedeWiFiEduroam(new Conta());

    }


    //Chamar esse metodo da ContasAtivity.
    private void abrirTelaRedeWiFiEduroam(Conta conta){
        Intent intent = new Intent(getActivity(), RedeWifiActivity.class);
        startActivity(intent);
    }

}
