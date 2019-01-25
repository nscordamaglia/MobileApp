package test.android.com.login;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Clase que busca las listas de itracer dentro del inbox de generados y arma los array para luego ser mostrados en la UI
 */
public class Generated_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,RQcomplete<String> {


    private ListView lv1;
    private ListTKTAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<TKTobj> tktList;
    private View view;
    private int position;
    private TKTobj tkt;
    private AppCompatActivity activity;
    private android.support.v7.app.ActionBar actionBar;
    private FloatingActionButton fab,fab1,fab2;
    private webApp webApp;

    public ListView getLv1() {
        return lv1;
    }

    public void setLv1(ListView lv1) {
        this.lv1 = lv1;
    }

    public ListTKTAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ListTKTAdapter adapter) {
        this.adapter = adapter;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public ArrayList<TKTobj> getTktList() {
        return tktList;
    }

    public void setTktList(ArrayList<TKTobj> tktList) {
        this.tktList = tktList;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public TKTobj getTkt() {
        return tkt;
    }

    public void setTkt(TKTobj tkt) {
        this.tkt = tkt;
    }



    public FloatingActionButton getFab() {
        return fab;
    }

    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }

    public ActionBar getActionBar() {
        return actionBar;
    }

    public void setActionBar(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    public Generated_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.list_fragment, container, false);
        if (getActivity().findViewById(R.id.transfer)!=null){getActivity().findViewById(R.id.transfer).setVisibility(View.VISIBLE); Log.d("imsettings","oncreate");}
        lv1 =(ListView)view.findViewById(R.id.listView);

        //creo el origen de tkt en arrayevent
        ArrayEvent.getInstance().setOrigin(this.getClass().getCanonicalName().toString());

        // Set swipe
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        updateList();

                                    }
                                }
        );


        // item onclick event
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                View v = view;
                TextView t = (TextView) v.findViewById(R.id.idtkt);
                String tkt = t.getText().toString();
                getTKTfromList(tkt);
                Fragment fragment = new TKT_fragment();
                Bundle bundle = new Bundle();
                bundle.putInt("position",getPosition());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment,"TKT_FRAGMENT").addToBackStack(null).commit();
                FloatingActionsMenu Menufab = (FloatingActionsMenu) activity.findViewById(R.id.fab);
                Menufab.collapse();



            }
        });

        return view;
    }

    private void updateList() {

        Calendar c = Calendar.getInstance();
        if(calculateFreq(ArrayTKTgen.getInstance().getDate(), c.getTime())==true || ArrayTKTgen.getInstance().getStatus().equalsIgnoreCase("empty")){

            Request rq = new Request(this);
            webApp = new webApp(this.getContext(), rq, null);

            /* Listing */
            GetListGen listing = new GetListGen(webApp,null);
            listing.Ejecute();

        }else{

            // Getting adapter by passing xml data ArrayList
            tktList = new ArrayList<>();
            tktList = ArrayTKTgen.getInstance().getArrayList();

            Log.d("webApp", "status: " + ArrayTKTgen.getInstance().getStatus());


            adapter = new ListTKTAdapter(view, tktList);
            lv1.setAdapter(adapter);

            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);

        }


    }

    /**
     * Metodo para calcular el tiempo en entre una busqueda y otra para utilizar el cache y optimizar conexiones
     * @param startDate
     * @param endDate
     * @return
     */
    private boolean calculateFreq(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();


        Log.d("timelog",
                "pasaron: " + different + " ms");

        if (different<120000){

            return false;

        }else{

            return true;

        }


    }

    private void getTKTfromList(String id) {


        int i = 0;
        TKTobj tkt = tktList.get(i);
        while (tkt.getId() != id){

            i++;
            tkt = tktList.get(i);

        }

        setTkt(tkt);
        setPosition(i);

    }

    @Override
    public void onRefresh() {

        updateList();
    }

    @Override
    /**
     * Metodo que recibe las respuestas asincrónicas
     */
    public void onTaskComplete(String response, String method) {

        GetResponse parser = new GetResponse();
        try {
            if ("ejecute".equalsIgnoreCase(method)){
                Log.d("getaction","callback en lst_fragment");
            }
            parser.execute(getContext(),response,method);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if("error".equals(parser.getResponse().substring(0,5))) {

            String error = parser.getResponse().substring(6,parser.getResponse().length());
            switch (error){

                case "emptylist":
                    Snackbar snackbar1 = Snackbar
                            .make(view.findViewById(R.id.layout_list_fragment), "Inbox vacío", Snackbar.LENGTH_LONG);


                    snackbar1.show();
                    User.getInstance().setLoginStatus("OK");
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case "Parametros invalidos en la solicitud||Sesion invalida o vencida218":
                    webApp.setUser(User.getInstance().getUserName());
                    webApp.setPass(User.getInstance().getPass());


                    /** Login **/
                    Login login = new Login(webApp, null);
                    login.Ejecute();

                    break;

                default:
                    swipeRefreshLayout.setRefreshing(false);
                    Snackbar snackbar2 = Snackbar
                            .make(view.findViewById(R.id.layout_list_fragment), "Lista Vacía", Snackbar.LENGTH_LONG);


                    snackbar2.show();
                    break;


            }



        }else{

            if ("login".equalsIgnoreCase(method)){

                Log.d("relog","entro por afterlog");
                //tengo que reenviar la accion anterior al login
                updateList();



            }else {

                // Getting adapter by passing xml data ArrayList
                tktList = new ArrayList<>();
                tktList = ArrayTKTgen.getInstance().getArrayList();

                Log.d("webApp", "status: " + ArrayTKTgen.getInstance().getStatus());


                adapter = new ListTKTAdapter(view, tktList);
                lv1.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                if (getActivity() != null) {

                    getActivity().findViewById(R.id.transfer).setVisibility(View.GONE);
                    Log.d("imsettings", "oculta imagen");
                } else {

                    Log.d("imsettings", "getactivity null");

                }

            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (AppCompatActivity) getActivity();
        actionBar = activity.getSupportActionBar();
        // update the actionbar to show the up carat/affordance
        if(actionBar == null){Log.d("actionbar","actionbar null");}
        actionBar.setTitle("Generados por mi equipo");
        User.getInstance().getToogle().setDrawerIndicatorEnabled(true);

        FloatingActionsMenu menu = (FloatingActionsMenu)activity.findViewById(R.id.fab);
        menu.setVisibility(View.VISIBLE);

        //********* Boton ordenar *****************
        fab1 = (FloatingActionButton) activity.findViewById(R.id.accion_ordenar);
        Drawable icon = getResources().getDrawable(android.R.drawable.ic_menu_sort_by_size);
        fab1.setImageDrawable(icon);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                swipeRefreshLayout.setRefreshing(true);
                FloatingActionsMenu Menufab = (FloatingActionsMenu) activity.findViewById(R.id.fab);
                Menufab.collapse();
                // Getting adapter by passing xml data ArrayList
                tktList = new ArrayList<>();
                tktList = ArrayTKTgen.getInstance().getArrayListInv();
                adapter = new ListTKTAdapter(view, tktList);
                lv1.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        fab1.showContextMenu();
        //************** Boton buscar ********************
        fab = (FloatingActionButton) activity.findViewById(R.id.accion_buscar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DialogSearch().show(getFragmentManager(),"DialogSearch");
                String title = (String) activity.getSupportActionBar().getTitle();
                activity.getSupportActionBar().setTitle(title);
                FloatingActionsMenu Menufab = (FloatingActionsMenu) activity.findViewById(R.id.fab);
                Menufab.collapse();
            }
        });
        fab.showContextMenu();
        //********** Boton crear *****************************
        fab2 = (FloatingActionButton) activity.findViewById(R.id.accion_crear);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User.getInstance().setPath("0");
                User.getInstance().setBack("0");
                FormObj.getInstance().getArrayHistoric().clear();
                FormObj.getInstance().getArrayHistoric().add("Ruta de creación");
                Log.d("tagback","path: " + User.getInstance().getPath() + " back: " + User.getInstance().getBack());
                Fragment fragment = new Open_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment,"OPEN_FRAGMENT").addToBackStack(null).commit();
                FloatingActionsMenu Menufab = (FloatingActionsMenu) activity.findViewById(R.id.fab);
                Menufab.collapse();
            }
        });
        fab2.showContextMenu();
    }
}
