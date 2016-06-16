package br.ufg.inf.instaladoreduroam.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import br.ufg.inf.instaladoreduroam.data.EduroamContract.ContaEntry;

/**
 * Created by Maycon on 09/10/2015.
 */
public class TestEduroamProvider extends AndroidTestCase {

    private static final String LOG_CAT = TestEduroamProvider.class.getSimpleName();

    /**
     * Deleta por meio do Provider.
     */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                EduroamContract.ContaEntry.CONTENT_URI,
                null, null);

        Cursor cursor = mContext.getContentResolver().query(
                EduroamContract.ContaEntry.CONTENT_URI,
                null, null, null, null);

        assertEquals("Error: Records not deleted from Conta table during delete",
                0, cursor.getCount());
        cursor.close();
    }

    /**
     * Delete por meio SQLiteDatabase.
     */
    public void deleteAllRecordsFromDB() {
        EduroamDbHelper dbHelper = new EduroamDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(ContaEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                EduroamProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: EduroamProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + EduroamContract.CONTENT_AUTHORITY,
                    providerInfo.authority, EduroamContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: EduroamProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
          This test doesn't touch the database.  It verifies that the ContentProvider returns
          the correct type for each type of URI that it can handle.
    */
    public void testGetType() {
        //content://br.ufg.inf.instaladoreduroam.app/conta
        // vnd.android.cursor.dir/br.ufg.inf.instaladoreduroam.app/conta/
        String type = mContext.getContentResolver().getType(ContaEntry.CONTENT_URI);
        assertEquals("Error", ContaEntry.CONTENT_TYPE, type);
    }

    /**
     * Testa o insert e select via SQLiteDatabase.
     */
    public void testBasicConsumoQuery() {
        // insert our test records into the database
        EduroamDbHelper dbHelper = new EduroamDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createContentValuesConta();
        long contaRowId = db.insert(ContaEntry.TABLE_NAME, null, testValues);
        assertTrue("Unable to Insert ContaEntry into the Database", contaRowId != -1);
        db.close();

        // Test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                ContaEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicConsumoQuery", cursor, testValues);
    }


    /**
     * Testa update, select e insert via ContentProvider.
     */
    public void testUpdateConta() {
        ContentValues testValues = TestUtilities.createContentValuesConta();

        Uri uri = mContext.getContentResolver().insert(ContaEntry.CONTENT_URI, testValues);
        long contaRowId = ContentUris.parseId(uri);

        assertTrue(contaRowId != -1);
        Log.d(LOG_CAT, "Nova linha id: " + contaRowId);

        ContentValues updatedValues = new ContentValues(testValues);
        updatedValues.put(ContaEntry._ID, contaRowId);
        updatedValues.put(ContaEntry.COLUMN_LOGIN_UNICO, "jose");

        Cursor contaCursor = mContext.getContentResolver().query(ContaEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        contaCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                ContaEntry.CONTENT_URI, updatedValues, ContaEntry._ID + "= ?",
                new String[]{Long.toString(contaRowId)});

        assertEquals(count, 1);

        tco.waitForNotificationOrFail();

        contaCursor.unregisterContentObserver(tco);
        contaCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                ContaEntry.CONTENT_URI,
                null,   // projection
                ContaEntry._ID + " = " + contaRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateConta.  Error validating conta entry update.",
                cursor, updatedValues);
        cursor.close();
    }

    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createContentValuesConta();

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(ContaEntry.CONTENT_URI, true, tco);

        Uri uri = mContext.getContentResolver().insert(ContaEntry.CONTENT_URI, testValues);
        tco.waitForNotificationOrFail();

        long contaRowId = ContentUris.parseId(uri);
        assertTrue(contaRowId != -1);
        Log.d(LOG_CAT, "Nova linha id: " + contaRowId);

        Cursor cursor = mContext.getContentResolver().query(
                ContaEntry.CONTENT_URI,
                null,   // projection
                ContaEntry._ID + " = " + contaRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testConta. Erro na validacao da tabela Conta.",
                cursor, testValues);

    }

//    public void testDeleteRecords() {
//        testInsertReadProvider();
//
//        // Register a content observer for our conta delete.
//        TestUtilities.TestContentObserver contaObserver = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(ContaEntry.CONTENT_URI, true, contaObserver);
//
//        deleteAllRecordsFromProvider();
//        contaObserver.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(contaObserver);
//    }

}
