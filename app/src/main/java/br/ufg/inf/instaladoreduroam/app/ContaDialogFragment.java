package br.ufg.inf.instaladoreduroam.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import br.ufg.inf.instaladoreduroam.R;
import br.ufg.inf.instaladoreduroam.entidades.Conta;

/**
 * Created by Maycon on 09/10/2015.
 */
public class ContaDialogFragment extends DialogFragment
        implements TextView.OnEditorActionListener {

    private static final String DIALOG_TAG = "editDialog";
    private static final String EXTRA_CONTA = "conta";

    private EditText txtLogin;
    private EditText txtSenha;
    private Conta mConta;

    public static ContaDialogFragment newInstance(Conta conta) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CONTA, conta);
        ContaDialogFragment dialog = new ContaDialogFragment();
        dialog.setArguments(bundle);
        return dialog;
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new AlertDialog.Builder(getActivity())
//                .setIcon(R.drawable.ic_eduroam_1)
//                .setTitle(R.string.title_dialog_fragment_conta)
//                .setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                .setNegativeButton("Cancelar",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        })
//                .create();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConta = (Conta) getArguments().getSerializable(EXTRA_CONTA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_conta, container, false);

        //Exibir o teclado virtual ao exibir o Dialog.
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //Setar o titulo do Dialog
        getDialog().setTitle(R.string.title_dialog_fragment_conta);

        txtLogin = (EditText) view.findViewById(R.id.edit_user);
        txtLogin.requestFocus();
        txtSenha = (EditText) view.findViewById(R.id.edit_password);
        txtSenha.setOnEditorActionListener(this);

        if (mConta != null) {
            txtLogin.setText(mConta.getLogin());
            txtSenha.setText(mConta.getSenha());
        }

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {

            Activity activity = getActivity();
            if (activity instanceof AoSalvarConta) {
                if (mConta == null) {
                    mConta = new Conta(
                            txtLogin.getText().toString(),
                            txtSenha.getText().toString());
                } else {
                    mConta.setLogin(txtLogin.getText().toString());
                    mConta.setSenha(txtSenha.getText().toString());
                }
                AoSalvarConta listener = (AoSalvarConta) activity;
                listener.salvouConta(mConta);

                //Fecha o dialog.
                dismiss();
                return true;
            }
        }
        return false;
    }

    public void abrirDialog(FragmentManager fm) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(fm, DIALOG_TAG);
        }
    }

    public interface AoSalvarConta {
        void salvouConta(Conta conta);
    }
}
