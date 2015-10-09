package br.ufg.inf.instaladoreduroam.app;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.ufg.inf.instaladoreduroam.adapter.ContaCursorAdapter;
import br.ufg.inf.instaladoreduroam.data.ContaRepositorio;
import br.ufg.inf.instaladoreduroam.entidades.Conta;

/**
 * Created by Maycon on 09/10/2015.
 */
public class ContaListFragment extends ListFragment
        implements AdapterView.OnItemLongClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    ContaRepositorio mRepositorio;
    ListView mListView;
    CursorAdapter mAdapter;
    String mTextoBusca;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRepositorio = new ContaRepositorio(getActivity());
        mAdapter = new ContaCursorAdapter(getActivity(), null);
        mListView = getListView(); //Obtem os itens da ListView da tela atual.
        setListAdapter(mAdapter);
        mListView.setOnItemLongClickListener(this);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Activity activity = getActivity();
        if(activity instanceof  AoClicarNaConta) {
            Cursor cursor = (Cursor) l.getItemAtPosition(position);
            Conta conta = mRepositorio.obterContaFromCursor(cursor);

            AoClicarNaConta listener = (AoClicarNaConta) activity;
            listener.clicouNaConta(conta);
        }
    }

    public void buscar(String nomeConta) {
        mTextoBusca = TextUtils.isEmpty(nomeConta) ? null : nomeConta;
        getLoaderManager().restartLoader(0, null, this);
    }

    public void limparBusca() {
        buscar(null);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO: Abrir o ContaDialogFragment ao segurar um item da lista.
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mRepositorio.pesquisarContas(getActivity(), mTextoBusca);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public interface AoClicarNaConta {
        void clicouNaConta(Conta conta);
    }
}
