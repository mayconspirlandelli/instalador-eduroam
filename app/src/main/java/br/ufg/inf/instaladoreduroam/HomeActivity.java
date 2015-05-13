package br.ufg.inf.instaladoreduroam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class HomeActivity extends ActionBarActivity {

   private Button btnAdicionarRede;

    @Override
    public void onStart() {
        super.onStart();
        inicializaObjetosDeTela();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
        Intent intent = new Intent(this, AdicionarActivity.class);
        startActivity(intent);
    }
}
