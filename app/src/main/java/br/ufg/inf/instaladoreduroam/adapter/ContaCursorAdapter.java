package br.ufg.inf.instaladoreduroam.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.ufg.inf.instaladoreduroam.R;
import br.ufg.inf.instaladoreduroam.data.EduroamContract;

/**
 * Created by Maycon on 09/10/2015.
 */
public class ContaCursorAdapter extends CursorAdapter {

    public ContaCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_conta, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtConta = (TextView) view.findViewById(R.id.txtNomeConta);
        txtConta.setText(cursor.getString(cursor.getColumnIndex(EduroamContract.ContaEntry.COLUMN_LOGIN_UNICO)));
    }
}
