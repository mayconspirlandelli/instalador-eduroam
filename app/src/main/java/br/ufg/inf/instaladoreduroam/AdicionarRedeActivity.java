package br.ufg.inf.instaladoreduroam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


public class AdicionarRedeActivity extends ActionBarActivity {

    private static final String LOG_TAG = AdicionarRedeActivity.class.getSimpleName();
    private Button btnConectar;
    EditText editUsuario;
    EditText editSenha;
    EditText editSsid;
    CheckBox chckLembrarSenha;
    CheckBox chckExibirSenha;


    @Override
    public void onStart() {
        super.onStart();
        inicializaObjetosDeTela();
        //OBSER SHARED PREFERENCE
        exibirUltimaConfiguracao();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_rede);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adicionar_rede, menu);
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
        btnConectar = (Button) findViewById(R.id.btn_conectar);
        btnConectar.setOnClickListener(btnConectarOnClickListener);
        editSenha = (EditText) findViewById(R.id.edit_password);
        editUsuario = (EditText) findViewById(R.id.edit_user);
        editSsid = (EditText) findViewById(R.id.edit_ssid);

        chckExibirSenha = (CheckBox) findViewById(R.id.chck_exibir_senha);
        chckExibirSenha.setOnCheckedChangeListener(chckExibirSenhaOnCheckedChangeListener);
        chckLembrarSenha = (CheckBox) findViewById(R.id.chck_lembrar_senha);
        //chckLembrarSenha.setOnClickListener(chckLembrarSenhaOnClickListener);
    }

    final private View.OnClickListener btnConectarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            adicionarRedeEduroam();
        }
    };


    final private CompoundButton.OnCheckedChangeListener chckExibirSenhaOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked) {
                //Exibe a senha.
                editSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                //Oculta a senha.
                editSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
    };


    /**
     * Exibe as configurações (SSID, login único e senha) anteriormente salvas.
     */
    private void exibirUltimaConfiguracao() {
        SharedPreferences sharedPref = this.getPreferences(this.MODE_PRIVATE);
        //String valorDefault = getResources().getString(R.string.prefs_ssid_default);
        //editSsid.setText(sharedPref.getString(getString(R.string.prefs_ssid), valorDefault));
        editUsuario.setText(sharedPref.getString(getString(R.string.prefs_usuario), null));
        editSenha.setText(sharedPref.getString(getString(R.string.prefs_senha), null));
    }


    private void removeRede(WifiConfiguration found) {
        WifiManager wifiManager = (WifiManager) getSystemService(this.WIFI_SERVICE);
        wifiManager.disconnect();
        wifiManager.removeNetwork(found.networkId);
        wifiManager.saveConfiguration();
        Log.d(LOG_TAG, "Removing network " + found.networkId);
    }

    private WifiConfiguration obterRede() {
        WifiManager wifiManager = (WifiManager) getSystemService(this.WIFI_SERVICE);
        List<WifiConfiguration> networks = wifiManager.getConfiguredNetworks();

        if (networks != null) {
            if (networks.size() > 0) {
                WifiConfiguration foundConfig = null;
                for (WifiConfiguration config : networks) {
                    if (config.SSID.equals(Eduroam.SSID_NAME)) {
                        foundConfig = config;
                        break;
                    }
                }
                return foundConfig;
            }
        }
        return null;
    }


    private void adicionarRedeEduroam() {

        String usuario = editUsuario.getText().toString();
        String senha = editSenha.getText().toString();
        //String ssid = editSsid.getText().toString();

        /*Create a WifiConfig*/
        //WifiConfiguration selectedConfig = new WifiConfiguration();
        WifiConfiguration selectedConfig = obterRede();

        if (selectedConfig == null) {
            selectedConfig = new WifiConfiguration();
        } else {
            removeRede(selectedConfig);
        }

        /*AP Name*/
            selectedConfig.SSID = Eduroam.SSID_NAME;
            //selectedConfig.SSID = ssid;

        /*Priority*/
            selectedConfig.priority = 40;

        /*Enable Hidden SSID*/
            selectedConfig.hiddenSSID = false;

            //AuthAlgorithm
            selectedConfig.allowedAuthAlgorithms.clear();
            selectedConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);


        /*Key Mgmnt*/
            selectedConfig.allowedKeyManagement.clear();
            selectedConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
            selectedConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);

        /*Group Ciphers*/
            selectedConfig.allowedGroupCiphers.clear();
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        /*Pairwise ciphers*/
            selectedConfig.allowedPairwiseCiphers.clear();
            selectedConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            selectedConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

        /*Protocols*/
            selectedConfig.allowedProtocols.clear();
            selectedConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            selectedConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

            // Enterprise Settings
            // Reflection magic here too, need access to non-public APIs
            try {
                // Let the magic start
                Class[] wcClasses = WifiConfiguration.class.getClasses();
                // null for overzealous java compiler
                Class wcEnterpriseField = null;

                for (Class wcClass : wcClasses)
                    if (wcClass.getName().equals(Eduroam.INT_ENTERPRISEFIELD_NAME)) {
                        wcEnterpriseField = wcClass;
                        break;
                    }
                boolean noEnterpriseFieldType = false;
                if (wcEnterpriseField == null)
                    noEnterpriseFieldType = true; // Cupcake/Donut access enterprise settings directly

                Field wcefAnonymousId = null, wcefCaCert = null, wcefClientCert = null, wcefEap = null, wcefIdentity = null, wcefPassword = null, wcefPhase2 = null, wcefPrivateKey = null, wcefEngine = null, wcefEngineId = null;
                Field[] wcefFields = WifiConfiguration.class.getFields();
                // Dispatching Field vars
                for (Field wcefField : wcefFields) {
                    if (wcefField.getName().equals(Eduroam.INT_ANONYMOUS_IDENTITY))
                        wcefAnonymousId = wcefField;
                    else if (wcefField.getName().equals(Eduroam.INT_CA_CERT))
                        wcefCaCert = wcefField;
                    else if (wcefField.getName().equals(Eduroam.INT_CLIENT_CERT))
                        wcefClientCert = wcefField;
                    else if (wcefField.getName().equals(Eduroam.INT_EAP))
                        wcefEap = wcefField;
                    else if (wcefField.getName().equals(Eduroam.INT_IDENTITY))
                        wcefIdentity = wcefField;
                    else if (wcefField.getName().equals(Eduroam.INT_PASSWORD))
                        wcefPassword = wcefField;
                    else if (wcefField.getName().equals(Eduroam.INT_PHASE2))
                        wcefPhase2 = wcefField;
                    else if (wcefField.getName().equals(Eduroam.INT_PRIVATE_KEY))
                        wcefPrivateKey = wcefField;
//                else if (wcefField.getName().equals("engine"))
//                    wcefEngine = wcefField;
//                else if (wcefField.getName().equals("engine_id"))
//                    wcefEngineId = wcefField;
                }


                Method wcefSetValue = null;
                if (!noEnterpriseFieldType) {
                    for (Method m : wcEnterpriseField.getMethods())
                        //System.out.println(m.getName());
                        if (m.getName().trim().equals("setValue"))
                            wcefSetValue = m;
                }


        /*EAP Method*/
                if (!noEnterpriseFieldType) {
                    wcefSetValue.invoke(wcefEap.get(selectedConfig), Eduroam.ENTERPRISE_EAP);
                } else {
                    wcefEap.set(selectedConfig, Eduroam.ENTERPRISE_EAP);
                }
        /*EAP Phase 2 Authentication*/
                if (!noEnterpriseFieldType) {
                    wcefSetValue.invoke(wcefPhase2.get(selectedConfig), Eduroam.ENTERPRISE_PHASE2);
                } else {
                    wcefPhase2.set(selectedConfig, Eduroam.ENTERPRISE_PHASE2);
                }
//        /*EAP Anonymous Identity*/
//            if (!noEnterpriseFieldType) {
//                wcefSetValue.invoke(wcefAnonymousId.get(selectedConfig), ENTERPRISE_ANON_IDENT);
//            } else {
//                wcefAnonymousId.set(selectedConfig, ENTERPRISE_ANON_IDENT);
//            }
//        /*EAP CA Certificate*/
//            if (!noEnterpriseFieldType) {
//                wcefSetValue.invoke(wcefCaCert.get(selectedConfig), ENTERPRISE_CA_CERT);
//            } else {
//                wcefCaCert.set(selectedConfig, ENTERPRISE_CA_CERT);
//            }
//        /*EAP Private key*/
//            if (!noEnterpriseFieldType) {
//                wcefSetValue.invoke(wcefPrivateKey.get(selectedConfig), ENTERPRISE_PRIV_KEY);
//            } else {
//                wcefPrivateKey.set(selectedConfig, ENTERPRISE_PRIV_KEY);
//            }
        /*EAP Identity*/
                if (!noEnterpriseFieldType) {
                    wcefSetValue.invoke(wcefIdentity.get(selectedConfig), usuario);
                } else {
                    wcefIdentity.set(selectedConfig, usuario);
                }
        /*EAP Password*/
                if (!noEnterpriseFieldType) {
                    wcefSetValue.invoke(wcefPassword.get(selectedConfig), senha);
                } else {
                    wcefPassword.set(selectedConfig, senha);
                }
//        /*EAp Client certificate*/
//            if (!noEnterpriseFieldType) {
//                wcefSetValue.invoke(wcefClientCert.get(selectedConfig), ENTERPRISE_CLIENT_CERT);
//            } else {
//                wcefClientCert.set(selectedConfig, ENTERPRISE_CLIENT_CERT);
//            }
        /*Engine fields*/
//            if(!noEnterpriseFieldType)
//            {
//                wcefSetValue.invoke(wcefEngine.get(wifiConf), "1");
//                wcefSetValue.invoke(wcefEngineId.get(wifiConf), "keystore");
//            }

                // Adhoc for CM6
                //if non-CM6 fails gracefully thanks to nested try-catch

//            try{
//                Field wcAdhoc = WifiConfiguration.class.getField("adhocSSID");
//                Field wcAdhocFreq = WifiConfiguration.class.getField("frequency");
//                //wcAdhoc.setBoolean(selectedConfig, prefs.getBoolean(PREF_ADHOC,
//                //      false));
//                wcAdhoc.setBoolean(selectedConfig, false);
//                int freq = 2462;    // default to channel 11
//                //int freq = Integer.parseInt(prefs.getString(PREF_ADHOC_FREQUENCY,
//                //"2462"));     // default to channel 11
//                //System.err.println(freq);
//                wcAdhocFreq.setInt(selectedConfig, freq);
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error: " + e.toString());
                e.printStackTrace();
            }

            WifiManager wifiManag = (WifiManager) getSystemService(this.WIFI_SERVICE);

            wifiManag.disconnect();

            boolean res1 = wifiManag.setWifiEnabled(true);
            int res = wifiManag.addNetwork(selectedConfig);
            Log.d(LOG_TAG, "add Network returned " + res);
            boolean b = wifiManag.enableNetwork(selectedConfig.networkId, false);
            Log.d(LOG_TAG, "enableNetwork returned " + b);
            boolean c = wifiManag.saveConfiguration();
            Log.d(LOG_TAG, "Save configuration returned " + c);
            boolean d = wifiManag.enableNetwork(res, true);
            Log.d(LOG_TAG, "enableNetwork returned " + d);

            wifiManag.reconnect();


        Toast.makeText(this, "Eduroam adicionada com sucesso!", Toast.LENGTH_LONG).show();


        salvarConfiguracoesRede();
        abrirNavegadorInternet();

    }

    private void salvarConfiguracoesRede() {
        SharedPreferences sharedPref = this.getPreferences(this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.prefs_usuario), editUsuario.getText().toString());
        editor.putString(getString(R.string.prefs_senha), editSenha.getText().toString());
        editor.commit();
    }

    private void abrirNavegadorInternet() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.uri_site_inf)));
        startActivity(browserIntent);
    }

}
