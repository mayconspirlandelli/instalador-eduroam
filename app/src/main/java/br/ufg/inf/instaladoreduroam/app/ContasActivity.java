package br.ufg.inf.instaladoreduroam.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import br.ufg.inf.instaladoreduroam.R;
import br.ufg.inf.instaladoreduroam.data.ContaRepositorio;
import br.ufg.inf.instaladoreduroam.entidades.Conta;


/**
 * Created by Maycon on 13/05/2015.
 */
public class ContasActivity extends ActionBarActivity
        implements ContaListFragment.AoClicarNaConta,
        ContaDialogFragment.AoSalvarConta,
        ContaListFragment.AoExcluirContas,
        ContaListFragment.AoEditarConta {

    private Button btnAdicionarRede;
    private ContaListFragment mListFragament;
    private Toolbar mToolbar;
    private ContaRepositorio mRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_contas);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_fragment_conta);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mRepositorio = new ContaRepositorio(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_new:
                adicionarConta();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Carrega a ContaListFragment.
        if (!verificarExisteConta()) {
            //Carrega o ContaDialogFragment.
            abrirTelaEdicaoConta(null);
        }
    }

    private void abrirTelaEdicaoConta(Conta conta) {
        ContaDialogFragment contaDialog = ContaDialogFragment.newInstance(conta);
        contaDialog.abrirDialog(getSupportFragmentManager());
    }

    /**
     * Método responsável por verifica se o aplicativo já tem um conta criada.
     *
     * @return
     */
    private boolean verificarExisteConta() {
        if (mRepositorio.verificarExisteConta()) {
            return true;
        } else {
            return false;
        }
    }

    private void abrirTelaRedeWiFiEduroam(Conta conta) {
        Intent intent = new Intent(this, RedeWifiActivity.class);
        startActivity(intent);
    }

    private void adicionarConta() {
        abrirTelaEdicaoConta(null);
    }

    @Override
    public void clicouNaConta(Conta conta) {
        abrirTelaRedeWiFiEduroam(conta);
    }

    /**
     *  Método responsável por excluir as contas selecionadas.
     * @param conta
     */
    @Override
    public void salvouConta(Conta conta) {
        mRepositorio.salvar(conta);
    }

    /**
     * Método responsável por excluir as contas selecionadas.
     * @param conta
     */
    @Override
    public void exclusaoCompleta(Conta conta) {
        mRepositorio.excluir(conta);
    }

    @Override
    public void editarConta(Conta conta) {
        abrirTelaEdicaoConta(conta);
    }
}
