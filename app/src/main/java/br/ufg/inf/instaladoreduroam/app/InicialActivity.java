package br.ufg.inf.instaladoreduroam.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import br.ufg.inf.instaladoreduroam.AdicionarRedeActivity;
import br.ufg.inf.instaladoreduroam.R;


public class InicialActivity extends ActionBarActivity {

   private Button btnAdicionarRede;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
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
        btnAdicionarRede = (Button) findViewById(R.id.btn_adicionar);
        btnAdicionarRede.setOnClickListener(btnAdicionarRedeOnClickListener);
    }

    final private View.OnClickListener btnAdicionarRedeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            abrirTelaAdicionar();
        }
    };

    private void abrirTelaAdicionar(){
        Intent intent = new Intent(this, AdicionarRedeActivity.class);
        startActivity(intent);
    }

    private void abrirTelaAtivarRede(){
        Intent intent = new Intent(this, RedeWifiActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(usarAppPrimeiraVez()) {
            abrirTelaBoasVindas();
        } else {
            if(verificarExisteConta()){
             abrirTelaRedeWiFiEduroam();
            } else {
                abrirTelaContas();
            }
        }
    }

    /**
     * Método responsável por verifica se é a primeira vez que é usado.
     * @return
     */
    private boolean usarAppPrimeiraVez(){
        //TODO usar o SharePreference.
        return false;
    }

    private void abrirTelaBoasVindas(){

    }

    /**
     * Método responsável por verifica se o aplicativo já tem um conta criada.
     * @return
     */
    private boolean verificarExisteConta(){
        return false;
    }

    private void abrirTelaRedeWiFiEduroam(){
        Intent intent = new Intent(this, RedeWifiActivity.class);
        startActivity(intent);
    }

    private void abrirTelaContas(){
        Intent intent = new Intent(this, ContasActivity.class);
        startActivity(intent);
    }

}
