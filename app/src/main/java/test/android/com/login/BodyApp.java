package test.android.com.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;

/**
 * Clase encargada de manejar la UI de listas, tkt y creación
 */

public class BodyApp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,RQcomplete<String> {

    private TKTobj tkt;
    private int position;
    private CharSequence title;
    private GetProfile profile;
    private String estado;
    private ActionBarDrawerToggle toggle;
    private boolean ready = false;


    public TKTobj getTkt() {
        return tkt;
    }

    public void setTkt(TKTobj tkt) {
        this.tkt = tkt;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Metodo que recibe los procesos asincronicos
     * @param response
     * @param method
     */
    public void onTaskComplete (String response, String method){

            GetResponse parser = new GetResponse();
        try {
            if ("idteam".equalsIgnoreCase(method)){
                Log.d("profile","entra por idteam");
            }
            parser.execute(this.getApplicationContext(),response,method);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if("error".equals(User.getInstance().getLoginStatus())) {



            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), parser.getResponse(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null);


            snackbar.show();
            User.getInstance().setLoginStatus("OK");
            //onBackPressed();



        }else {

            if (ready == false) {

                switch (estado) {

                    case "inbox":
                        Log.d("profile","entra por inbox");
                        Fragment inbox = new List_fragment();
                        User.getInstance().setToogle(toggle);
                        FragmentManager inboxManager = getSupportFragmentManager();
                        inboxManager.beginTransaction().replace(R.id.fragmentContainer, inbox).addToBackStack(null).commit();
                        break;
                    case "generados":
                        Log.d("profile","entra por generados");
                        Fragment generated = new Generated_fragment();
                        User.getInstance().setToogle(toggle);
                        FragmentManager genManager = getSupportFragmentManager();
                        genManager.beginTransaction().replace(R.id.fragmentContainer, generated).addToBackStack(null).commit();
                        break;
                    case "ambos":

                        profile.setDatanodes(new DataNodes("IDTEAMS", null));
                        profile.Ejecute();
                        ready = true;

                }

            }else {

                Log.d("profile","entra por ambos");
                Fragment inbox = new List_fragment();
                User.getInstance().setToogle(toggle);
                FragmentManager inboxManager = getSupportFragmentManager();
                try {
                    inboxManager.beginTransaction().replace(R.id.fragmentContainer, inbox).addToBackStack(null).commitAllowingStateLoss();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }


            }



        }

    }

    @Override
    protected void onResume() {
        super.onResume();

            }

    private String ifExist() {

        String signal="";
        String[] a = {"33","39"};
        for (int cant = 0; cant<2; cant++) {
            Log.d("uriimg","size: " + User.getInstance().getAccess().size());
            for (int i = 0; i < User.getInstance().getAccess().size(); i++) {

                if (a[cant].equalsIgnoreCase(User.getInstance().getAccess().get(i))) {

                    signal +=a[cant];
                }
            }
        }
        Log.d("profile",signal);
        return signal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("profile","onresume profile");

        setContentView(R.layout.activity_body_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        switch (ifExist()){

            case "33":
                estado = "inbox";
                getSupportActionBar().setTitle("Lista de webApp");
                break;
            case "39":
                estado = "generados";
                getSupportActionBar().setTitle("Generados por mi equipo");
                break;
            case "3339":
                estado = "ambos";
                getSupportActionBar().setTitle("Lista de webApp");
                break;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //Guardo los datos de login
        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("authUser",User.getInstance().getUserName());
        editor.putString("authPass",User.getInstance().getPass());
        editor.commit();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        updateProfile();


    }

    private void updateProfile() {

        Request rq = new Request(this);
        webApp webApp = new webApp(this.getApplicationContext(), rq, null);

        /* Updating user profile */
        profile = new GetProfile(webApp,null);

       switch (estado){

           case "inbox":
               profile.Ejecute();
               break;
           case "generados":
               profile.setDatanodes(new DataNodes("IDTEAMS",null));
               profile.Ejecute();
               break;
           case "ambos":
               profile.Ejecute();
               break;

       }



    }


    public void onBackPressed() {

        Log.d("backbutton","back: " + User.getInstance().getBack());

        if (User.getInstance().getPath()=="0") {
            toggle.setDrawerIndicatorEnabled(true);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                Log.d("backbutton", "draweropen");
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Log.d("backbutton", "drawerclose");
                //finish();
            }


            //onBackPressed in Fragment
            int count = getSupportFragmentManager().getBackStackEntryCount();
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

            Log.d("backbutton", "fragment: " + count);

            if (count == 1) {

                getSupportFragmentManager().popBackStackImmediate();
                Log.d("backbutton", "listfragment");
                super.onBackPressed();

            } else if (count == 0) {
                Log.d("backbutton", "saliendo...");
                super.onBackPressed();
            } else {

                try {

                    FragmentManager child = current.getChildFragmentManager();
                    Log.d("backbutton", "other fragment");
                    //child.popBackStackImmediate(null,0);
                    getSupportFragmentManager().popBackStackImmediate();

                } catch (NullPointerException e) {


                }

            }
        }else if(User.getInstance().getPath()!="0"){

            User.getInstance().setAttach(false);
            Log.d("tagback","back a utilizar: " + User.getInstance().getBack());
            int lenght = FormObj.getInstance().getArrayHistoric().size();
            if (lenght == 1){

                FormObj.getInstance().getArrayHistoric().clear();
                FormObj.getInstance().getArrayHistoric().add("Ruta de creación");
                User.getInstance().setPath("0");

            }else{

                FormObj.getInstance().getArrayHistoric().remove(lenght-1);
                User.getInstance().setPath(User.getInstance().getBack());

            }


            Fragment fragment = new Open_fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment,"OPEN_FRAGMENT").addToBackStack(null).commit();
            getSupportFragmentManager().popBackStackImmediate();




        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.body_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Toast.makeText(getApplicationContext(),"Próximamente",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.inicio) {

            if(estado=="generados"){

                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), "No posee inbox de tratamiento", Snackbar.LENGTH_LONG);
                snackbar.show();
                title = getSupportActionBar().getTitle();

            }else {
                while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportFragmentManager().popBackStackImmediate();
                }
                fragmentoGenerico = new List_fragment();
                title = "Lista de webApp";
            }

        } else if (id == R.id.logout) {

            fragmentoGenerico = new Profile_fragment();
             title = "Perfil de usuario";


        } else if (id == R.id.search) {



            new DialogSearch().show(getSupportFragmentManager(),"DialogSearch");
            title = getSupportActionBar().getTitle();


        } else if (id == R.id.settings) {

            title = getSupportActionBar().getTitle().toString();
            Toast.makeText(getApplicationContext(),"Próximamente",Toast.LENGTH_LONG).show();

        }else if (id == R.id.mygenerated){

            //si tiene access 33/39
            Log.d("logestado",estado);
            if(estado=="inbox"){

                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), "No posee inbox de generados", Snackbar.LENGTH_LONG);
                snackbar.show();


            }else{

                while (getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportFragmentManager().popBackStackImmediate();
                }
                fragmentoGenerico = new Generated_fragment();
                title = "Generados por mi equipo";
            }

        }

        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragmentoGenerico).addToBackStack(null)
                    .commit();
        }


        getSupportActionBar().setTitle(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendMail(View view){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL,new String[] {"FAQ@domain.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
        intent.putExtra(Intent.EXTRA_TEXT, "Consulta enviada por: " + User.getInstance().getUserName());

        startActivity(Intent.createChooser(intent, "Envio de mail"));

    }


}
