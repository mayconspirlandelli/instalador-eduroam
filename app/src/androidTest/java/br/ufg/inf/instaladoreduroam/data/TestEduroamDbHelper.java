package br.ufg.inf.instaladoreduroam.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by Maycon on 28/05/2015.
 */
public class TestEduroamDbHelper extends AndroidTestCase {

    public static final String LOG_TAG = TestEduroamDbHelper.class.getSimpleName();

    void deletarBancoDeDados() {
        mContext.deleteDatabase(EduroamDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deletarBancoDeDados();
    }

    public void testCriacaoDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(EduroamContract.ContaEntry.TABLE_NAME);

        //================ Verifica a criação do banco de dados e as tabelas. ===================

        mContext.deleteDatabase(EduroamDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new EduroamDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        //Verifica se o banco de dados foi criado.
        assertTrue("Erro: O banco de dados não foi criado corretamente!", c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        //Verifica se as tabelas do banco foram criadas.
        assertTrue("Erro: Seu banco de dados não criou todas as tabelas!", tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + EduroamContract.ContaEntry.TABLE_NAME + ")", null);

        //Verifica se as colunas de cada tabela foram criadas corretamente.
        assertTrue("Erro: This means that we were unable to query the database for table information.", c.moveToFirst());

        //================ Verifica a criação das colunas de cada tabela. ===================

        final HashSet<String> contaColumnHashSet = new HashSet<String>();
        contaColumnHashSet.add(EduroamContract.ContaEntry._ID);
        contaColumnHashSet.add(EduroamContract.ContaEntry.COLUMN_LOGIN_UNICO);
        contaColumnHashSet.add(EduroamContract.ContaEntry.COLUMN_SENHA);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            contaColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required CONTA entry columns", contaColumnHashSet.isEmpty());
        db.close();
    }

    public void testContaTabela() {

        long contaRowId;

        EduroamDbHelper dbHelper = new EduroamDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertEquals(true, db.isOpen());

        ContentValues testValues = TestUtilities.createContentValuesConta();
        contaRowId = db.insert(EduroamContract.ContaEntry.TABLE_NAME, null, testValues);
        assertTrue(contaRowId != -1);

        Cursor cursor = db.query(EduroamContract.ContaEntry.TABLE_NAME, null, null, null, null, null, null);

        assertTrue("Error: No Records returned from weather query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed.", cursor, testValues);
        assertFalse("Error: More than one record returned form weather query.", cursor.moveToNext());

        cursor.close();
        db.close();
    }


}
