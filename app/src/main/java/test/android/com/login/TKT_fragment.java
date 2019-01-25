package test.android.com.login;



import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import org.json.JSONException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


/**
 * Clase que genera la UI de tickets, con sus eventos y sus correspondientes acciones y adjuntos
 */
public class TKT_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,RQcomplete<String>{

    private TKTobj tkt;
    private View view;
    private ListView events;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<TKTobj> evtList;
    private ListEventAdapter adapter;
    private int position;
    private Itracker itracker;
    private DialogFragment dialog;
    private boolean  idsel = false;
    private AppCompatActivity activity;
    private FloatingActionButton fab;
    private String origin;





    public void onTaskComplete (String response,String method){

        GetResponse parser = new GetResponse();
        parser.setPosition(position);
        parser.setOrigin(origin);
        Log.d("tktid","posicion: " + position + " origen: " + origin);

        try {


            if(origin.equalsIgnoreCase("List_fragment")){

                try {
                    ArrayTKT.getInstance().getArrayList().get(position).clearEvents();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }else if(origin.equalsIgnoreCase("Generated_fragment")){

                try {
                    ArrayTKTgen.getInstance().getArrayList().get(position).clearEvents();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }else if (origin.equalsIgnoreCase("search")){


                try {
                    ArrayTKTsearch.getInstance().getArrayList().get(position).clearEvents();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            parser.execute(getContext(),response,method);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if("error".equals(parser.getResponse().substring(0,5))) {

            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.waiting);
            progressBar.setVisibility(View.INVISIBLE);
            String error = parser.getResponse().substring(6,parser.getResponse().length());
            switch (error){

                case "emptylist":
                    Snackbar snackbar1 = Snackbar
                            .make(view.findViewById(R.id.layout_tkt_fragment), parser.getResponse(), Snackbar.LENGTH_LONG);


                    snackbar1.show();
                    User.getInstance().setLoginStatus("OK");
                    swipeRefreshLayout.setRefreshing(false);

                    break;

                case "Parametros invalidos en la solicitud||Sesion invalida o vencida218":
                    itracker.setUser(User.getInstance().getUserName());
                    itracker.setPass(User.getInstance().getPass());


                    /** Login **/
                    Login login = new Login(itracker, null);
                    login.Ejecute();

                    break;

                case "firewall":
                    try{

                        dialog.dismiss();
                    }catch (NullPointerException n){}
                    Snackbar snackbar3 = Snackbar
                            .make(view.findViewById(R.id.layout_tkt_fragment), "Fallo inesperado", Snackbar.LENGTH_LONG)
                            .setAction("REPORTAR", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    sendMail(view,User.getInstance().getReport());
                                }
                            });


                    snackbar3.show();
                    User.getInstance().setLoginStatus("firewall");
                    swipeRefreshLayout.setRefreshing(false);

                    break;

                case "No se pudo cargar objeto||107":
                    User.getInstance().setLoginStatus("OK");
                    LayoutInflater inflater = activity.getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) activity.findViewById(R.id.toast_layout_root));

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Ticket Inválido");
                    text.setPadding(15,0,15,0);

                    Toast toast = new Toast(activity.getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    swipeRefreshLayout.setRefreshing(false);
                    activity.getSupportFragmentManager().popBackStackImmediate();
                    break;

                case "Ticket invalido.#1":
                    User.getInstance().setLoginStatus("OK");
                    LayoutInflater inflater2 = activity.getLayoutInflater();
                    View layout2 = inflater2.inflate(R.layout.custom_toast,
                            (ViewGroup) activity.findViewById(R.id.toast_layout_root));

                    TextView text2 = (TextView) layout2.findViewById(R.id.text);
                    text2.setText("Ticket Inválido");
                    text2.setPadding(15,0,15,0);

                    Toast toast2 = new Toast(activity.getApplicationContext());
                    toast2.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                    toast2.setDuration(Toast.LENGTH_LONG);
                    toast2.setView(layout2);
                    toast2.show();
                    swipeRefreshLayout.setRefreshing(false);
                    activity.getSupportFragmentManager().popBackStackImmediate();
                    break;

                default:
                    swipeRefreshLayout.setRefreshing(false);
                    try{

                        dialog.dismiss();
                    }catch (NullPointerException n){ }
                    Snackbar snackbar4 = Snackbar
                            .make(view.findViewById(R.id.layout_tkt_fragment), error, Snackbar.LENGTH_LONG);


                    snackbar4.show();
                    break;
            }


        }else{


            if ("login".equalsIgnoreCase(method)){

                Log.d("relog","entro por afterlog");
                //tengo que reenviar la accion anterior al login
                afterLogin();
                //GetAction action = new GetAction(itracker);
                //action.Resend();


            }else if ("ejecute".equalsIgnoreCase(method)){

                Log.d("getaction","callback en tkt_fragment");
                try{

                    dialog.dismiss();
                }catch (NullPointerException n){

                    Log.d("getaction","no hay dialogForm");
                }
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.waiting);
                progressBar.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(true);
                updateEvent();

            }else if ("getform".equalsIgnoreCase(method) && "OK".equalsIgnoreCase(User.getInstance().getLoginStatus())){

                Log.d("getaction","evaluo form");
                //lanzo el dialog --> necesito FormObj

                int secciones = FormObj.getInstance().getArrayElemnt().size();
                Log.d("getaction","cant elem: " + secciones);
                for (int i=0;i<secciones;i++) {

                    String type = FormObj.getInstance().getArrayElemnt().get(i).getType();

                    if("idsel".equalsIgnoreCase(type)){

                        idsel = true;


                    }else{



                    }
                }

                if (idsel == true){

                    Log.d("getselection","entro a pedir los items");
                    GetSelection select = new GetSelection(itracker,tkt);
                    select.Ejecute();

                }else{

                    updateEvent();
                    dialog = new DialogForm();
                    dialog.show(getFragmentManager(),"DialogForm");
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putString("origin", origin);Log.d("tktid","envio origin: " + origin);
                    dialog.setArguments(args);
                    ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.waiting);
                    progressBar.setVisibility(View.INVISIBLE);


                }




            }else if ("idsel_teamderive".equalsIgnoreCase(method)){

                Log.d("getselection","ya tengo los items");
                updateEvent();
                dialog = new DialogForm();
                dialog.show(getFragmentManager(),"DialogForm");
                Bundle args = new Bundle();
                args.putInt("position", position);
                args.putString("origin", origin);Log.d("tktid","envio origin: " + origin);
                dialog.setArguments(args);
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.waiting);
                progressBar.setVisibility(View.INVISIBLE);

            }else if (!"downloadfile".equalsIgnoreCase(method)){

                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.waiting);
                progressBar.setVisibility(View.INVISIBLE);

                // Getting adapter by passing xml data ArrayList
                evtList = new ArrayList<>();

                if(origin.equalsIgnoreCase("List_fragment")){

                    evtList = ArrayTKT.getInstance().getArrayList();
                    Log.d("tktid","entra por listing, lenght: " + ArrayTKT.getInstance().getArrayList().size());

                }else if(origin.equalsIgnoreCase("Generated_fragment")){

                    evtList = ArrayTKTgen.getInstance().getArrayList();
                    Log.d("tktid","entra por generados, lenght: " + ArrayTKTgen.getInstance().getArrayList().size());

                }else if(origin.equalsIgnoreCase("search")){

                    evtList = ArrayTKTsearch.getInstance().getArrayList();
                    Log.d("tktid","entra por busqueda, lenght: " + ArrayTKTsearch.getInstance().getArrayList().size());

                }


                adapter = new ListEventAdapter(view, evtList,position,this);
                events.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);


                try {
                    if (getActivity().findViewById(R.id.transfer)!=null){getActivity().findViewById(R.id.transfer).setVisibility(View.GONE);}

                        fab = (FloatingActionButton) activity.findViewById(R.id.accion_ordenar);
                        Drawable icon = getResources().getDrawable(android.R.drawable.arrow_down_float);
                        fab.setImageDrawable(icon);
                        fab.setTitle("Ir al final");
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                events.setSelection(events.getCount());
                                FloatingActionsMenu Menufab = (FloatingActionsMenu) activity.findViewById(R.id.fab);
                                Menufab.collapse();
                            }
                        });
                        fab.showContextMenu();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }else{
                Log.d("filelog", "creo el archivo, lenght: " + User.getInstance().getBase64().length());
                FileOutputStream fos = null;
                File filePath = null;
                try {
                    if (User.getInstance().getBase64() != null) {

                        filePath = new File("/mnt/sdcard/download//" + FormObj.getInstance().getFile());
                        fos = new FileOutputStream(filePath,true);
                        byte[] decodedString = android.util.Base64.decode(User.getInstance().getBase64(), android.util.Base64.DEFAULT);
                        fos.write(decodedString);
                        fos.flush();
                        fos.close();
                    }

                } catch (Exception e) {

                } finally {
                    if (fos != null) {
                        fos = null;
                    }
                }
                openImage(filePath);
            }


        }


    }


    public  TKT_fragment() {

    }


    @Override
    public void onResume() {
        super.onResume();
        activity = (AppCompatActivity) getActivity();
        android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar == null){Log.d("actionbar","actionbar null");}
        actionBar.setTitle(tkt.getId());
        User.getInstance().getToogle().setDrawerIndicatorEnabled(false);
        User.getInstance().getToogle().setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });


        FloatingActionsMenu menu = (FloatingActionsMenu)activity.findViewById(R.id.fab);
        menu.setVisibility(View.VISIBLE);
        Log.d("reportlog","pasa por onresume");

        int count = activity.getSupportFragmentManager().getBackStackEntryCount();
        Log.d("backbutton", "fragment: " + count);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        if (bundle.getInt("position")== -1){

            origin = "search";
            Log.d("tktid", "no busco de la lista");
            this.tkt = new TKTobj();
            tkt.setId(bundle.getString("tktid"));
            ArrayTKTsearch.getInstance().getArrayList().clear();
            ArrayTKTsearch.getInstance().getArrayList().add(tkt);

        }else{


            this.position = bundle.getInt("position");
            origin = ArrayEvent.getInstance().getOrigin().substring(23,ArrayEvent.getInstance().getOrigin().length());
            if(origin.equalsIgnoreCase("List_fragment")){

                this.tkt = ArrayTKT.getInstance().getArrayList().get(position);

            }else if(origin.equalsIgnoreCase("Generated_fragment")){

                this.tkt = ArrayTKTgen.getInstance().getArrayList().get(position);

            }

        }


        view =  inflater.inflate(R.layout.tkt_fragment, container, false);
        if (getActivity().findViewById(R.id.transfer)!=null){getActivity().findViewById(R.id.transfer).setVisibility(View.VISIBLE);}
        events =(ListView)view.findViewById(R.id.listView);



        // Set swipe
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        updateEvent();

                                    }
                                }
        );




        return view;



    }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }



    private void updateEvent() {

        Request rq = new Request(this);
        itracker = new Itracker(this.getContext(), rq, null);

        /* Listing */
        GetEvent events = new GetEvent(itracker,tkt);
        events.Ejecute();
        FloatingActionsMenu Menufab = (FloatingActionsMenu) activity.findViewById(R.id.fab);
        Menufab.collapse();

    }

    private void afterLogin() {


        GetAction action = new GetAction(itracker);
        action.Resend();

    }


    @Override
    public void onRefresh() {
        updateEvent();
    }

    private void sendMail(View view, String report){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL,new String[] {"FAQ@domain.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
        intent.putExtra(Intent.EXTRA_TEXT, "Consulta enviada por: " + User.getInstance().getUserName() + "\n"+  report);

        startActivity(Intent.createChooser(intent, "Envio de mail"));
        User.getInstance().setReport(null);

    }

    private void openImage(File file){

        PackageManager pm = activity.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = file.getName().substring(file.getName().indexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);

        intent.setDataAndType(Uri.fromFile(file), type);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Determine if Android can resolve this implicit Intent
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info != null) {

            try {
                activity.getApplicationContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity.getApplicationContext(), "No existe aplicación para ver imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
