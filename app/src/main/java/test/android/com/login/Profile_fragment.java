package test.android.com.login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;


/**
 * Clase que genera la UI de perfil de usuario con los datos extraidos de la api
 */
public class Profile_fragment extends Fragment {


    private TextView usuario,equipo;
    private AppCompatActivity activity;

    public Profile_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.profile_fragment, container, false);
        String user = User.getInstance().getUserName();
        usuario = (TextView) view.findViewById(R.id.texto_nombre);
        usuario.setText(user);


        ArrayList<String> listaEquipos = new ArrayList<String>();
        listaEquipos.addAll(User.getInstance().getListTeamName());
        listaEquipos.addAll(User.getInstance().getListTeamGenName());

        for (int i=0;i<listaEquipos.size();i++){

            equipo = new TextView(getContext());
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.eq_profile);
            String eq = listaEquipos.get(i);
            Log.d("profile",user + " / " + eq);
            equipo.setText(eq);
            layout.addView(equipo);
        }

        CardView logout = (CardView) view.findViewById(R.id.card_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("logout","logout");
                User.getInstance().setUserName(null);
                User.getInstance().setPass(null);
                //borro datos de login
                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("authUser",null);
                editor.putString("authPass",null);
                editor.commit();
                //borro actualizacion de listas
                ArrayTKTgen.getInstance().setStatus("empty");
                ArrayTKT.getInstance().setStatus("empty");
                //inicio pantalla de login
                Intent login = new Intent(getContext(),MainActivity.class);
                getActivity().startActivity(login);
                getActivity().finish();

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (AppCompatActivity) getActivity();
        android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar == null){
            Log.d("actionbar","actionbar null");}
        actionBar.setTitle("Perfil de usuario");
        User.getInstance().getToogle().setDrawerIndicatorEnabled(false);
        User.getInstance().getToogle().setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        FloatingActionsMenu menu = (FloatingActionsMenu)activity.findViewById(R.id.fab);
        menu.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

}
