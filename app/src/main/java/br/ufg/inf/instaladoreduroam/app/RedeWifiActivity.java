package br.ufg.inf.instaladoreduroam.app;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import br.ufg.inf.instaladoreduroam.R;
import br.ufg.inf.instaladoreduroam.task.AdicionarRedeTask;
import br.ufg.inf.instaladoreduroam.task.IAnimationUpdated;

/**
 * Created by Maycon on 13/05/2015.
 * Classe responsável por gerenciar a rede Wifi.
 */
public class RedeWifiActivity extends ActionBarActivity implements IAnimationUpdated {

    private ImageButton btnAtivarRede;
    private AnimationDrawable redeAnimation;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rede_wifi);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        btnAtivarRede = (ImageButton) findViewById(R.id.btn_ativar_rede);
        btnAtivarRede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ativarRede();
            }
        });


        //Spinner na Toolbar Android v21
        //https://blog.danielbetts.net/2015/01/02/material-design-spinner-toolbar-style-fix/
        //http://stackoverflow.com/questions/26755878/how-can-i-fix-the-spinner-style-for-android-4-x-placed-on-top-of-the-toolbar


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ativar_rede, menu);
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
            startActivity(new Intent(this, PreferenciasActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ativarRede() {
        AdicionarRedeTask task = new AdicionarRedeTask(RedeWifiActivity.this, this);
        task.execute();
    }

    private void desativarRede() {

    }

    @Override
    public void startAnimation() {

        btnAtivarRede.setBackgroundResource(R.drawable.animation_rede);
        redeAnimation = (AnimationDrawable) btnAtivarRede.getBackground();
        redeAnimation.start();
    }


    @Override
    public void stopAnimation() {
        btnAtivarRede.setBackgroundResource(R.drawable.animation_rede);
        redeAnimation = (AnimationDrawable) btnAtivarRede.getBackground();
        redeAnimation.stop();
        btnAtivarRede.setBackgroundResource(R.drawable.ic_eduroam_3);
    }
}
