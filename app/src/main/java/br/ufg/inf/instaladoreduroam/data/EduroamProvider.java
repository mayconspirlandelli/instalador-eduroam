package br.ufg.inf.instaladoreduroam.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Maycon on 28/05/2015.
 */
public class EduroamProvider extends ContentProvider {

    private static final String LOG_TAG = EduroamProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private EduroamDbHelper mOpenHelper;

    static final int CONTA = 100;

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
        String id = EduroamContract.ContaEntry.getIdFromUri(uri);

        return sContaQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sContaPorId,
                new String[]{id},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = EduroamContract.CONTENT_AUTHORITY;

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // EduroamContract to help define the types to the UriMatcher.
        matcher.addURI(authority, EduroamContract.PATH_CONTA, CONTA);

        // 3) Return the new matcher!
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new EduroamDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CONTA:
                return EduroamContract.ContaEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            // "conta"
            case CONTA: {
                retCursor = mOpenHelper.getReadableDatabase().query(EduroamContract.ContaEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CONTA: {
                long rowsInserted = db.insert(EduroamContract.ContaEntry.TABLE_NAME, null, values);
                if (rowsInserted > 0)
                    returnUri = EduroamContract.ContaEntry.buildEduroamUri(rowsInserted);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) {
            selection = "1";
        }
        switch (match) {
            case CONTA:
                rowsDeleted = db.delete(EduroamContract.ContaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case CONTA:
                rowsUpdated = db.delete(EduroamContract.ContaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
