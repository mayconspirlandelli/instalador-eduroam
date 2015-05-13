package br.ufg.inf.instaladoreduroam;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static final String SSID_NAME = "\"eduroam\"";
    private final String LOG_TAG = "InstaladorEduroam." + MainActivity.class.getSimpleName();


    private static final String INT_PRIVATE_KEY = "private_key";
    private static final String INT_PHASE2 = "phase2";
    private static final String INT_PASSWORD = "password";
    private static final String INT_IDENTITY = "identity";
    private static final String INT_EAP = "eap";
    private static final String INT_CLIENT_CERT = "client_cert";
    private static final String INT_CA_CERT = "ca_cert";
    private static final String INT_ANONYMOUS_IDENTITY = "anonymous_identity";
    private static final String INT_ENTERPRISEFIELD_NAME = "android.net.wifi.WifiConfiguration$EnterpriseField";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onStart() {
        super.onStart();
        ///getConfigEduroam("senha", "usuario@ufg.br");
        saveEapConfig("senha", "usuario@ufg.br");

        //readEapConfig(findNetworkInExistingConfig());

    }


    void saveEapConfig(String passString, String userName) {
        /********************************Configuration Strings****************************************************/
        final String ENTERPRISE_EAP = "PEAP";
       //final String ENTERPRISE_CLIENT_CERT = "keystore://USRCERT_CertificateName";
     //  final String ENTERPRISE_PRIV_KEY = "USRPKEY_CertificateName";
        //CertificateName = Name given to the certificate while installing it

    /*Optional Params- My wireless Doesn't use these*/
        final String ENTERPRISE_PHASE2 = "auth=MSCHAPV2";
       // final String ENTERPRISE_ANON_IDENT = "ABC";
      //  final String ENTERPRISE_CA_CERT = ""; // If required: "keystore://CACERT_CaCertificateName"
        /********************************Configuration Strings****************************************************/

        /*Create a WifiConfig*/
        WifiConfiguration selectedConfig = new WifiConfiguration();

        /*AP Name*/
        selectedConfig.SSID = SSID_NAME;

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
                if (wcClass.getName().equals(INT_ENTERPRISEFIELD_NAME)) {
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
                if (wcefField.getName().equals(INT_ANONYMOUS_IDENTITY))
                    wcefAnonymousId = wcefField;
                else if (wcefField.getName().equals(INT_CA_CERT))
                    wcefCaCert = wcefField;
                else if (wcefField.getName().equals(INT_CLIENT_CERT))
                    wcefClientCert = wcefField;
                else if (wcefField.getName().equals(INT_EAP))
                    wcefEap = wcefField;
                else if (wcefField.getName().equals(INT_IDENTITY))
                    wcefIdentity = wcefField;
                else if (wcefField.getName().equals(INT_PASSWORD))
                    wcefPassword = wcefField;
                else if (wcefField.getName().equals(INT_PHASE2))
                    wcefPhase2 = wcefField;
                else if (wcefField.getName().equals(INT_PRIVATE_KEY))
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
                wcefSetValue.invoke(wcefEap.get(selectedConfig), ENTERPRISE_EAP);
            } else {
                wcefEap.set(selectedConfig, ENTERPRISE_EAP);
            }
        /*EAP Phase 2 Authentication*/
            if (!noEnterpriseFieldType) {
                wcefSetValue.invoke(wcefPhase2.get(selectedConfig), ENTERPRISE_PHASE2);
            } else {
                wcefPhase2.set(selectedConfig, ENTERPRISE_PHASE2);
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
                wcefSetValue.invoke(wcefIdentity.get(selectedConfig), userName);
            } else {
                wcefIdentity.set(selectedConfig, userName);
            }
        /*EAP Password*/
            if (!noEnterpriseFieldType) {
                wcefSetValue.invoke(wcefPassword.get(selectedConfig), passString);
            } else {
                wcefPassword.set(selectedConfig, passString);
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
    }

    /**
     * If the given ssid name exists in the settings, then change its password to the one given here, and save
     *
     * @param ssid
     */
    private WifiConfiguration findNetworkInExistingConfig() {
        WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
        List<WifiConfiguration> existingConfigs = wifi.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            Log.d("TESTE", existingConfig.SSID);
            if (existingConfig.SSID.contains(SSID_NAME)) {
                return existingConfig;
            }
        }
        return null;
    }


    void readEapConfig(WifiConfiguration config)     {
        /*Get the WifiService */
        WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
        /*Get All WIfi configurations*/
//        List<WifiConfiguration> configList = wifi.getConfiguredNetworks();
//        /*Now we need to search appropriate configuration i.e. with name SSID_Name*/
//        for(int i = 0; i <= configList.size(); ++i)
//        {
//            if(configList.get(i).SSID.contentEquals("eduroam"))
//            {
//                /*We found the appropriate config now read all config details*/
//                Iterator<WifiConfiguration> iter =  configList.iterator();
//                WifiConfiguration config = configList.get(i);


                /*I dont think these fields have anything to do with EAP config but still will
                 * print these to be on safe side*/
                try {
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[SSID]" + config.SSID);

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[BSSID]" + config.BSSID);

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[HIDDEN SSID]" + config.hiddenSSID);

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[PASSWORD]" + config.preSharedKey);

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[ALLOWED ALGORITHMS]");

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[LEAP]" + config.allowedAuthAlgorithms.get(WifiConfiguration.AuthAlgorithm.LEAP));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[OPEN]" + config.allowedAuthAlgorithms.get(WifiConfiguration.AuthAlgorithm.OPEN));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[SHARED]" + config.allowedAuthAlgorithms.get(WifiConfiguration.AuthAlgorithm.SHARED));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[GROUP CIPHERS]");

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[CCMP]" + config.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.CCMP));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>" , "[TKIP]" + config.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.TKIP));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP104]" + config.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.WEP104));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP40]" + config.allowedGroupCiphers.get(WifiConfiguration.GroupCipher.WEP40));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[KEYMGMT]");

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[IEEE8021X]" + config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[NONE]" + config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.NONE));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WPA_EAP]" + config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WPA_PSK]" + config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[PairWiseCipher]");

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[CCMP]" + config.allowedPairwiseCiphers.get(WifiConfiguration.PairwiseCipher.CCMP));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[NONE]" + config.allowedPairwiseCiphers.get(WifiConfiguration.PairwiseCipher.NONE));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[TKIP]" + config.allowedPairwiseCiphers.get(WifiConfiguration.PairwiseCipher.TKIP));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[Protocols]");

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[RSN]" + config.allowedProtocols.get(WifiConfiguration.Protocol.RSN));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WPA]" + config.allowedProtocols.get(WifiConfiguration.Protocol.WPA));

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[PRE_SHARED_KEY]" + config.preSharedKey);

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP Key Strings]");

                    String[] wepKeys = config.wepKeys;
                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP KEY 0]" + wepKeys[0]);

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP KEY 1]" + wepKeys[1]);

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP KEY 2]" + wepKeys[2]);

                    Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[WEP KEY 3]" + wepKeys[3]);


                }
                //catch(IOException e)
                catch(Exception e)
                {
                    Toast toast1 = Toast.makeText(this, "Failed to write Logs to ReadConfigLog.txt", 3000);
                    Toast toast2 = Toast.makeText(this, "Please take logs using Logcat", 5000);
                    Log.e("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "Could not write to ReadConfigLog.txt" + e.getMessage());
                }
                /*reflection magic*/
                /*These are the fields we are really interested in*/
                try
                {
                    // Let the magic start
                    Class[] wcClasses = WifiConfiguration.class.getClasses();
                    // null for overzealous java compiler
                    Class wcEnterpriseField = null;

                    for (Class wcClass : wcClasses)
                        if (wcClass.getName().equals(INT_ENTERPRISEFIELD_NAME))
                        {
                            wcEnterpriseField = wcClass;
                            break;
                        }
                    boolean noEnterpriseFieldType = false;
                    if(wcEnterpriseField == null)
                        noEnterpriseFieldType = true; // Cupcake/Donut access enterprise settings directly

                    Field wcefAnonymousId = null, wcefCaCert = null, wcefClientCert = null, wcefEap = null, wcefIdentity = null, wcefPassword = null, wcefPhase2 = null, wcefPrivateKey = null;
                    Field[] wcefFields = WifiConfiguration.class.getFields();
                    // Dispatching Field vars
                    for (Field wcefField : wcefFields)
                    {
                        if (wcefField.getName().trim().equals(INT_ANONYMOUS_IDENTITY))
                            wcefAnonymousId = wcefField;
                        else if (wcefField.getName().trim().equals(INT_CA_CERT))
                            wcefCaCert = wcefField;
                        else if (wcefField.getName().trim().equals(INT_CLIENT_CERT))
                            wcefClientCert = wcefField;
                        else if (wcefField.getName().trim().equals(INT_EAP))
                            wcefEap = wcefField;
                        else if (wcefField.getName().trim().equals(INT_IDENTITY))
                            wcefIdentity = wcefField;
                        else if (wcefField.getName().trim().equals(INT_PASSWORD))
                            wcefPassword = wcefField;
                        else if (wcefField.getName().trim().equals(INT_PHASE2))
                            wcefPhase2 = wcefField;
                        else if (wcefField.getName().trim().equals(INT_PRIVATE_KEY))
                            wcefPrivateKey = wcefField;
                    }
                    Method wcefValue = null;
                    if(!noEnterpriseFieldType)
                    {
                        for(Method m: wcEnterpriseField.getMethods())
                            //System.out.println(m.getName());
                            if(m.getName().trim().equals("value")){
                                wcefValue = m;
                                break;
                            }
                    }

                /*EAP Method*/
                    String result = null;
                    Object obj = null;
                    if(!noEnterpriseFieldType)
                    {
                        obj = wcefValue.invoke(wcefEap.get(config), null);
                        String retval = (String)obj;
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP METHOD]" + retval);

                    }
                    else
                    {
                        obj = wcefEap.get(config);
                        String retval = (String)obj;
                    }

                /*phase 2*/
                    if(!noEnterpriseFieldType)
                    {
                        result = (String) wcefValue.invoke(wcefPhase2.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP PHASE 2 AUTHENTICATION]" + result);

                    }
                    else
                    {
                        result = (String) wcefPhase2.get(config);
                    }

                /*Anonymous Identity*/
                    if(!noEnterpriseFieldType)
                    {
                        result = (String) wcefValue.invoke(wcefAnonymousId.get(config),null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP ANONYMOUS IDENTITY]" + result);

                    }
                    else
                    {
                        result = (String) wcefAnonymousId.get(config);
                    }

                /*CA certificate*/
                    if(!noEnterpriseFieldType)
                    {
                        result = (String) wcefValue.invoke(wcefCaCert.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP CA CERTIFICATE]" + result);

                    }
                    else
                    {
                        result = (String)wcefCaCert.get(config);

                    }

                /*private key*/
                    if(!noEnterpriseFieldType)
                    {
                        result = (String) wcefValue.invoke(wcefPrivateKey.get(config),null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP PRIVATE KEY]" + result);

                    }
                    else
                    {
                        result = (String)wcefPrivateKey.get(config);
                    }

                /*Identity*/
                    if(!noEnterpriseFieldType)
                    {
                        result = (String) wcefValue.invoke(wcefIdentity.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP IDENTITY]" + result);

                    }
                    else
                    {
                        result = (String)wcefIdentity.get(config);
                    }

                /*Password*/
                    if(!noEnterpriseFieldType)
                    {
                        result = (String) wcefValue.invoke(wcefPassword.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP PASSWORD]" + result);

                    }
                    else
                    {
                        result = (String)wcefPassword.get(config);
                    }

                /*client certificate*/
                    if(!noEnterpriseFieldType)
                    {
                        result = (String) wcefValue.invoke(wcefClientCert.get(config), null);
                        Log.d("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "[EAP CLIENT CERT]" + result);

                        Toast toast1 = Toast.makeText(this, "All config data logged to ReadConfigLog.txt", 3000);
                        Toast toast2 = Toast.makeText(this, "Extract ReadConfigLog.txt from SD CARD", 5000);
                    }
                    else
                    {
                        result = (String)wcefClientCert.get(config);
                    }

                }
                catch(Exception e)
                //catch(IOException e)
                {
                    Toast toast1 = Toast.makeText(this, "Failed to write Logs to ReadConfigLog.txt", 3000);
                    Toast toast2 = Toast.makeText(this, "Please take logs using Logcat", 5000);
                    Log.e("<<<<<<<<<<WifiPreference>>>>>>>>>>>>", "Could not write to ReadConfigLog.txt" + e.getMessage());
                }
//                catch(Exception e)
//                {
//                    e.printStackTrace();
//                }

            }
        //}
    //}

}
