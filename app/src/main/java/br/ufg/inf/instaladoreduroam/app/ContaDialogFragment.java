package br.ufg.inf.instaladoreduroam.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import br.ufg.inf.instaladoreduroam.R;
import br.ufg.inf.instaladoreduroam.entidades.Conta;

/**
 * Created by Maycon on 09/10/2015.
 */
public class ContaDialogFragment extends DialogFragment
        implements TextView.OnEditorActionListener,
        CheckBox.OnCheckedChangeListener,
        Toolbar.OnClickListener,
Toolbar.OnMenuItemClickListener{

    private static final String DIALOG_TAG = "editDialog";
    private static final String EXTRA_CONTA = "conta";

    private EditText editLogin;
    private EditText editSenha;
    private CheckBox chckExibirSenha;
    private Toolbar mToolbar;

    private Conta mConta;

    public static ContaDialogFragment newInstance(Conta conta) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CONTA, conta);
        ContaDialogFragment dialog = new ContaDialogFragment();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConta = (Conta) getArguments().getSerializable(EXTRA_CONTA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_conta, container, false);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar_dialog);
        mToolbar.setTitle(R.string.title_dialog_fragment_conta_novo);
        mToolbar.inflateMenu(R.menu.menu_dialog_fragment_contas);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_clear_mtrl_alpha);
        mToolbar.setNavigationOnClickListener(this);
        mToolbar.setOnMenuItemClickListener(this);


        //Exibir o teclado virtual ao exibir o Dialog.
        //SOFT_INPUT_ADJUST_NOTHING - o teclado sobrepoe a tela.
        //FLAG_LOCAL_FOCUS_MODE - foca no EditText.
        //SOFT_INPUT_STATE_VISIBLE - abre o teclado automaticamente.
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE |
//        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
// ou o efeito é o mesmo.
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE |
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING |
                        WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE);



        //Remover o título do Dialog e substituir pelo title do Toolbar.
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        editLogin = (EditText) view.findViewById(R.id.edit_user);
        editLogin.requestFocus();

        editSenha = (EditText) view.findViewById(R.id.edit_password);
        editSenha.setOnEditorActionListener(this);

        chckExibirSenha = (CheckBox) view.findViewById(R.id.chck_exibir_senha);
        chckExibirSenha.setOnCheckedChangeListener(this);

        if (mConta != null) {
            //Setar o titulo do Dialog - Editar
            mToolbar.setTitle(R.string.title_dialog_fragment_conta_editar);
            editLogin.setText(mConta.getLogin());
            editSenha.setText(mConta.getSenha());
        }
        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            salvarConta();
        }
        return false;
    }

    public void abrirDialog(FragmentManager fm) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(fm, DIALOG_TAG);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            //Exibe a senha.
            editSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            //Oculta a senha.
            editSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    /**
     * Metodo para o botão de "X" para fechar o DialogFragment
     * @param v
     */
    @Override
    public void onClick(View v) {
        dismiss();
    }

    private void salvarConta(){
        Activity activity = getActivity();
        if (activity instanceof AoSalvarConta) {
            if (mConta == null) {
                mConta = new Conta(
                        editLogin.getText().toString(),
                        editSenha.getText().toString());
            } else {
                mConta.setLogin(editLogin.getText().toString());
                mConta.setSenha(editSenha.getText().toString());
            }
            AoSalvarConta listener = (AoSalvarConta) activity;
            listener.salvouConta(mConta);

            //Fecha o dialog.
            dismiss();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_action_save:
                salvarConta();
                return true;
        }
        return false;
    }


    public interface AoSalvarConta {
        void salvouConta(Conta conta);
    }
}
