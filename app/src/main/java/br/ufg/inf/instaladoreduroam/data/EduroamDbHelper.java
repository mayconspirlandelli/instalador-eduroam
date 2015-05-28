package br.ufg.inf.instaladoreduroam.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.ufg.inf.instaladoreduroam.data.EduroamContract.ContaEntry;

/**
 * Created by Maycon on 28/05/2015.
 */
public class EduroamDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "eduroam.db";


    public EduroamDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CONTA_TABLE = "CREATE TABLE " + ContaEntry.TABLE_NAME + " ( " +
                ContaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContaEntry.COLUMN_LOGIN_UNICO + " TEXT NOT NULL, " +
                ContaEntry.COLUMN_SENHA + " TEXT NOT NULL " + " );";
        db.execSQL(SQL_CREATE_CONTA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: atualizar os dados, o artigo explica como atualizar a estrtura da tabela sem perder os dados:  http://stackoverflow.com/questions/3505900/sqliteopenhelper-onupgrade-confusion-android
        db.execSQL("DROP TABLE IF EXISTS " + ContaEntry.TABLE_NAME);
        onCreate(db);
    }
}
