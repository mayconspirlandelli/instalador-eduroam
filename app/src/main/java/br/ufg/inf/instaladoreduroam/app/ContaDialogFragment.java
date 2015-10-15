package br.ufg.inf.instaladoreduroam.app;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import br.ufg.inf.instaladoreduroam.R;

/**
 * Created by Maycon on 09/10/2015.
 */
public class ContaDialogFragment extends DialogFragment {
    static ContaDialogFragment newInstance() {
        ContaDialogFragment f = new ContaDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_conta, container);
        EditText mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        getDialog().setTitle("Hello");

        return view;
    }

}
