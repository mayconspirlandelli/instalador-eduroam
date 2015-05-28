package br.ufg.inf.instaladoreduroam.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Maycon on 28/05/2015.
 */
public class EduroamProvider extends ContentProvider {

    private static final String LOG_TAG = EduroamProvider.class.getSimpleName();

    //private static final UriMatcher sUriMatcher = buildUriMatcher();
    private EduroamDbHelper mOpenHelper;

    static final int CONTA = 100;
    static final int CONTA_ID = 101;


    private static SQLiteQueryBuilder sContaQueryBuilder = new SQLiteQueryBuilder();
    static {
        sContaQueryBuilder.setTables(EduroamContract.ContaEntry.TABLE_NAME);
    }

    private static final String sContaPorId =
            EduroamContract.ContaEntry.TABLE_NAME + "." +
                    EduroamContract.ContaEntry._ID + " = ? ";


    //Retorna todos os registros da tabela Conta.
    private Cursor getTodosContas(Uri uri, String[] projection, String sortOrder) {
        return sContaQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getContaPorId(Uri uri, String[] projection, String sortOrder) {
        String id =  EduroamContract.ContaEntry.getIdFromUri(uri);

        return sContaQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sContaPorId,
                new String[]{id},
                null,
                null,
                sortOrder
        );
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
