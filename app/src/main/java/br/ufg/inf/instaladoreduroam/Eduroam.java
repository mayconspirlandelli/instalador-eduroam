package br.ufg.inf.instaladoreduroam;

/**
 * Created by Maycon on 20/03/2015.
 */
public class Eduroam {

    public static final String SSID_NAME = "\"eduroam\"";
    public static final String INT_PRIVATE_KEY = "private_key";
    public static final String INT_PHASE2 = "phase2";
    public static final String INT_PASSWORD = "password";
    public static final String INT_IDENTITY = "identity";
    public static final String INT_EAP = "eap";
    public static final String INT_CLIENT_CERT = "client_cert";
    public static final String INT_CA_CERT = "ca_cert";
    public static final String INT_ANONYMOUS_IDENTITY = "anonymous_identity";
    public static final String INT_ENTERPRISEFIELD_NAME = "android.net.wifi.WifiConfiguration$EnterpriseField";

    /**
     * *****************************Configuration Strings***************************************************
     */
    public static final String ENTERPRISE_EAP = "PEAP";
    //final String ENTERPRISE_CLIENT_CERT = "keystore://USRCERT_CertificateName";
    //  final String ENTERPRISE_PRIV_KEY = "USRPKEY_CertificateName";
    //CertificateName = Name given to the certificate while installing it

    /*Optional Params- My wireless Doesn't use these*/
    public static final String ENTERPRISE_PHASE2 = "auth=MSCHAPV2";
    // final String ENTERPRISE_ANON_IDENT = "ABC";
    //  final String ENTERPRISE_CA_CERT = ""; // If required: "keystore://CACERT_CaCertificateName"
    /********************************Configuration Strings****************************************************/


}
