package br.ufg.inf.instaladoreduroam.data;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by Maycon on 28/05/2015.
 */
public class TestEduroamContract extends AndroidTestCase {

    private static final String LOG_TAG = TestEduroamContract.class.getSimpleName();
    private static final long TEST_ID = 22L;

    public void testBuildConta() {
        Uri uri = EduroamContract.ContaEntry.buildEduroamUri(TEST_ID);

        Log.d(LOG_TAG, uri.toString());
        assertNotNull("Erro: Null Uri returned.", uri);

        assertEquals("Erro:", String.valueOf(TEST_ID), uri.getLastPathSegment());

        //content://br.ufg.inf.instaladoreduroam.app/conta/id
        assertEquals("Erro:", uri.toString(), "content://br.ufg.inf.instaladoreduroam.app/conta/22");

        String id = EduroamContract.ContaEntry.getIdFromUri(uri);
        assertEquals("Error:", String.valueOf(TEST_ID), id);
    }
}
