package br.ufg.inf.instaladoreduroam.task;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import br.ufg.inf.instaladoreduroam.Eduroam;

/**
 * Created by Maycon on 25/05/2015.
 */
/*
 *  Primeiro callback é parametro do método doInBackground
 *  Segundo callback é parametro do método onProgressUpdate.
 *  Terceiro callback é parametro do método onPostExecute e retorno do método doInBackground.
 */
public class AdicionarRedeTask extends AsyncTask<Void, Void, Void> {

    final static private String LOG_TAG = AdicionarRedeTask.class.getSimpleName();
    private final Context mContext;
    private IAnimationUpdated animationUpdated;

    public AdicionarRedeTask(Context context, IAnimationUpdated updated){
        this.mContext = context;
        this.animationUpdated = updated;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //TODO: Start Animação
        this.animationUpdated.startAnimation();
    }

    @Override
    protected Void doInBackground(Void... params) {

        adicionarRedeEduroam();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //TODO: interromper animação e o botão permantece ativo.
        this.animationUpdated.stopAnimation();
    }


    private void removeRede(WifiConfiguration found) {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);
        wifiManager.disconnect();
        wifiManager.removeNetwork(found.networkId);
        wifiManager.saveConfiguration();
        Log.d(LOG_TAG, "Removing network " + found.networkId);
    }

    private WifiConfiguration obterRede() {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);
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

        //TODO somente para teste.
        String usuario = "";
        String senha = "";
        //String ssid = "\"eduroam\"";

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

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error: " + e.toString());
            e.printStackTrace();
        }

        WifiManager wifiManag = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);

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


        //Toast.makeText(this, "Eduroam adicionada com sucesso!", Toast.LENGTH_LONG).show();
        Log.d(LOG_TAG, "Eduroam adicionada com sucesso!");
    }
}
