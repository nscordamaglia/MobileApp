package test.android.com.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Nicolas Scordamaglia on 12/02/2016.
 */
public class DialogSearch extends DialogFragment {

    private View view;




    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //When you touch outside of dialog bounds,
                        //the dialog gets canceled and this method executes.
                    }
                }
        );
        return createSimpleDialog();
    }

    /**
     * Crea un diálogo de alerta sencillo
     * @return Nuevo diálogo
     */
    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater  inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_search, null);

        builder.setView(view);

        Button search = (Button) view.findViewById(R.id.search_boton);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //buscar tkt segun lo ingresado en EditText
                EditText tktid = (EditText)view.findViewById(R.id.tkt_input);

                if(TextUtils.isEmpty(tktid.getText().toString())){

                    tktid.setError("Debe ingresar un número");
                    return;

                }else{

                    Log.d("searchlog","lenght: " + tktid.getText().length());
                    Fragment fragment = new TKT_fragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",-1);
                    bundle.putString("tktid",tktid.getText().toString());
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment,"TKT_FRAGMENT").addToBackStack(null).commit();
                    dismiss();


                }


            }
        });
        return builder.create();
    }



}
