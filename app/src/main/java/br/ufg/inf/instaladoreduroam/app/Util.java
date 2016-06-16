package br.ufg.inf.instaladoreduroam.app;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Maycon on 08/06/2016.
 */
public class Util {

    public static void campoObrigatorio(Context context, String texto) {
        Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
    }
}
