package br.ufg.inf.instaladoreduroam.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import br.ufg.inf.instaladoreduroam.entidades.Conta;
import br.ufg.inf.instaladoreduroam.data.EduroamContract.ContaEntry;


/**
 * Created by Maycon on 09/10/2015.
 */
public class ContaRepositorio {
    private Context context;

    public ContaRepositorio(Context ctx) {
        this.context = ctx;
    }

    public static ContentValues createContentValuesConta(Conta conta) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EduroamContract.ContaEntry.COLUMN_LOGIN_UNICO, conta.getLogin());
        contentValues.put(EduroamContract.ContaEntry.COLUMN_SENHA, conta.getSenha());
        return contentValues;
    }

    private long inserir(Conta conta) {
        Uri uri = context.getContentResolver().insert(ContaEntry.CONTENT_URI, createContentValuesConta(conta));

        long contaRowId = ContentUris.parseId(uri);
        if (contaRowId != -1) {
            //consumo.setID(consumoRowId);
            conta.setId(contaRowId);
        }
        return contaRowId;
    }

    private int atualizar(Conta conta) {
        Uri uri = ContaEntry.CONTENT_URI;
        int updatedRows = context.getContentResolver()
                .update(uri, createContentValuesConta(conta),
                        ContaEntry._ID + " = ? ",
                        new String[]{Long.toString(conta.getId())});

        return updatedRows;
    }

    public void salvar(Conta conta) {
        if (conta.getId() == 0) {
            inserir(conta);
        } else {
            atualizar(conta);
        }
    }

    public int excluir(Conta conta) {
        Uri uri = ContaEntry.CONTENT_URI;
        int deletedRows = context.getContentResolver()
                .delete(uri, ContaEntry._ID + " = ? ",
                        new String[]{Long.toString(conta.getId())});

        return deletedRows;
    }

    public CursorLoader pesquisarContas(Context ctx, String nomeConta) {

        String where = null;
        String[] whereArgs = null;
        if(nomeConta != null && !nomeConta.isEmpty()) {
            where = ContaEntry.COLUMN_LOGIN_UNICO + " LIKE ?";
            whereArgs = new String[]{ "%" + nomeConta + "%"};
        }

        return new CursorLoader(ctx,
                ContaEntry.CONTENT_URI,
                null,
                where,
                whereArgs,
                ContaEntry.COLUMN_LOGIN_UNICO);
    }

    public static Conta obterContaFromCursor(Cursor cursor) {
        Conta conta = new Conta();
        conta.setId(cursor.getLong(cursor.getColumnIndex(ContaEntry._ID)));
        conta.setLogin(cursor.getString(cursor.getColumnIndex(ContaEntry.COLUMN_LOGIN_UNICO)));
        conta.setSenha(cursor.getString(cursor.getColumnIndex(ContaEntry.COLUMN_SENHA)));
        return conta;
    }
}
