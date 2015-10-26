package br.ufg.inf.instaladoreduroam.app;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.ufg.inf.instaladoreduroam.R;
import br.ufg.inf.instaladoreduroam.adapter.ContaCursorAdapter;
import br.ufg.inf.instaladoreduroam.data.ContaRepositorio;
import br.ufg.inf.instaladoreduroam.entidades.Conta;

/**
 * Created by Maycon on 09/10/2015.
 */
public class ContaListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

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
        getLoaderManager().initLoader(0, null, this);

        /** Registering context menu for the listview  https://developer.android.com/guide/topics/ui/menus.html */
        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Activity activity = getActivity();
        if (activity instanceof AoClicarNaConta) {
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_acao_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_action_edit:
                editarOuExcluirItemListaConta(info.position, false);
                return true;
            case R.id.menu_action_delete:
                editarOuExcluirItemListaConta(info.position, true);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Método para excluir/editar  a conta do item clicado.
     * @param position do item na LIstView.
     * @param excluir true para excluir item e false para editar item.
     */
    private void editarOuExcluirItemListaConta(int position, boolean excluir) {
        Activity activity = getActivity();
        Cursor cursor = (Cursor) mListView.getItemAtPosition(position);
        Conta conta = mRepositorio.obterContaFromCursor(cursor);

        if ((activity instanceof AoEditarConta) && (!excluir)) {
            AoEditarConta listener = (AoEditarConta) activity;
            listener.editarConta(conta);
        } else if ( (activity instanceof AoExcluirContas) && (excluir) )  {
            AoExcluirContas listener = (AoExcluirContas) activity;
            listener.exclusaoCompleta(conta);
            limparBusca();
        }
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

    public interface AoExcluirContas {
        void exclusaoCompleta(Conta conta);
    }

    public interface AoEditarConta {
        void editarConta(Conta conta);
    }
}
