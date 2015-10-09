package br.ufg.inf.instaladoreduroam.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by Maycon on 09/10/2015.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static final long TEST_CONTA_ID = 10L;

    //content://br.ufg.inf.instaladoreduroam.app/conta"
    private static final Uri TEST_CONTA_DIR = EduroamContract.ContaEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = EduroamProvider.buildUriMatcher();

        assertEquals("Error: The WEATHER URI was matched incorrectly.",
                testMatcher.match(TEST_CONTA_DIR), EduroamProvider.CONTA);
    }
}
