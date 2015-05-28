package br.ufg.inf.instaladoreduroam.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import br.ufg.inf.instaladoreduroam.entidades.Conta;

/**
 * Created by Maycon on 28/05/2015.
 */
public class EduroamContract {

    private static final String LOG_TAG = EduroamContract.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "br.ufg.inf.instaladoreduroam.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CONTA = "conta"; //Nome da tela "Conta".


    public static ContentValues createContentValuesConta(Conta conta){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContaEntry.COLUMN_LOGIN_UNICO, conta.getLogin());
        contentValues.put(ContaEntry.COLUMN_SENHA, conta.getSenha());
        return contentValues;
    }


    public static final class ContaEntry implements BaseColumns {

        //content://br.ufg.inf.instaladoreduroam.app/conta
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTA).build();

        //vnd.android.cursor.dir/br.ufg.inf.instaladoreduroam.app/conta
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTA;

        //vnd.android.cursor.item/br.ufg.inf.instaladoreduroam.app/conta
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTA;

        public static final String TABLE_NAME = "conta";
        public static final String COLUMN_LOGIN_UNICO = "login";
        public static final String COLUMN_SENHA = "senha"; //TODO: criptografa a senha http://developer.android.com/training/articles/security-tips.html#Crypto

        //content://br.ufg.inf.instaladoreduroam.app/conta/id
        public static Uri buildEduroamUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }
}
