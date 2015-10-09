package br.ufg.inf.instaladoreduroam.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import br.ufg.inf.instaladoreduroam.R;
import br.ufg.inf.instaladoreduroam.entidades.Conta;

/**
 * Created by Maycon on 13/05/2015.
 */
public class ContasActivity extends ActionBarActivity implements ContaListFragment.AoClicarNaConta  {

    private Button btnAdicionarRede;

    private FragmentManager mFragmentManager;
    private ContaListFragment mListFragament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inicializaObjetosDeTela() {
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFragmentManager = getSupportFragmentManager();

        if (verificarExisteConta()) {
            exibirListaContas();
            //Carrega a ContaListFragment.

        } else {
            abrirTelaEdicaoConta();
            //Carrega o ContaDialogFragment.
        }
    }


    private void exibirListaContas() {
        ContaListFragment fragment = new ContaListFragment();
        mFragmentManager.beginTransaction().replace(R.id.content_frame_conta, fragment).commit();
    }

    private void abrirTelaEdicaoConta() {
        //TODO: abrir o EdicaoContaFragment.
        ContaDialogFragment fragment = new ContaDialogFragment();
        mFragmentManager.beginTransaction().replace(R.id.content_frame_conta, fragment).commit();

    }
    
    /**
     * Método responsável por verifica se o aplicativo já tem um conta criada.
     *
     * @return
     */
    private boolean verificarExisteConta() {
        return true;
    }

    private void abrirTelaRedeWiFiEduroam(Conta conta) {
        Intent intent = new Intent(this, RedeWifiActivity.class);
        startActivity(intent);
    }


    //Pagina 277
    private void adicionarConta(){
        abrirTelaEdicaoConta();
    }

    private void removerConta(){}

    private void editarConta(){
        abrirTelaEdicaoConta(); //Envia a Conta
    }

    private void salvarConta(){}


    @Override
    public void clicouNaConta(Conta conta) {
        abrirTelaRedeWiFiEduroam(conta);
    }
}
