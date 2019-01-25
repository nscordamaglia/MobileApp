package test.android.com.login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import org.json.JSONException;

/**
 * Clase encargada de generar las UI de Login y su lógica (Muestra errores o falta de conexión)
 */

public class MainActivity extends AppCompatActivity implements RQcomplete<String>{

    private ProgressBar progressBar;
    private EditText user,pass;
    private Button button;
    private webApp webApp;
    private ImageView img;
    private String defUser;
    private String defPass;






    public void onTaskComplete (String response, String method){


        GetResponse parser = new GetResponse();
        try {
            parser.execute(getApplicationContext(),response,method);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if("error".equals(parser.getResponse().substring(0,5))) {

            String error = parser.getResponse().substring(6,parser.getResponse().length());
            switch (error){

                case "firewall":
                    Snackbar snackbar1 = Snackbar
                            .make(findViewById(android.R.id.content), parser.getResponse(), Snackbar.LENGTH_LONG);

                    snackbar1.show();

                    default:
                        img = (ImageView) findViewById(R.id.imgLogin);
                        img.setVisibility(View.INVISIBLE);
                        Log.d("autologin","error de login");
                        loginScreen();
                        Snackbar snackbar2 = Snackbar
                                .make(findViewById(android.R.id.content), parser.getResponse(), Snackbar.LENGTH_LONG);

                        snackbar2.show();


            }




        }else{

            Intent i = new Intent(this,BodyApp.class);
            //i.putExtra("hash",User.getInstance().getHash());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(i);
            finish();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //comparo con las preferencias guardadas
        SharedPreferences sharedPref = this.getApplicationContext().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
         defUser = sharedPref.getString("authUser",null);
         defPass = sharedPref.getString("authPass",null);
        Log.d("webApp","user: " + defUser);

       if(defUser == null ) {

           loginScreen();

       }else {



               setContentView(R.layout.activity_main);
               Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
               setSupportActionBar(toolbar);
               img = (ImageView) findViewById(R.id.imgLogin);
               img.setVisibility(View.VISIBLE);
               progressBar = (ProgressBar) findViewById(R.id.progressBar);
               User.getInstance().setUserName(defUser);
               User.getInstance().setPass(defPass);


       }



    }

    private void loginScreen() {

        //si no existe el archivo de login lo creo y me autentico
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = (EditText) findViewById(R.id.userText);
        pass = (EditText) findViewById(R.id.passText);
        button = (Button)findViewById(R.id.button);
        TextInputLayout userLayout = (TextInputLayout) findViewById(R.id.userTextLayout);
        userLayout.setVisibility(View.VISIBLE);
        TextInputLayout passLayout = (TextInputLayout) findViewById(R.id.passTextLayout);
        passLayout.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLogin();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("autologin","entra por onresume");
        user = (EditText) findViewById(R.id.userText);
        pass = (EditText) findViewById(R.id.passText);
        button = (Button)findViewById(R.id.button);
        TextInputLayout userLayout = (TextInputLayout) findViewById(R.id.userTextLayout);
        userLayout.setVisibility(View.INVISIBLE);
        TextInputLayout passLayout = (TextInputLayout) findViewById(R.id.passTextLayout);
        passLayout.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        img = (ImageView) findViewById(R.id.imgLogin);
        img.setVisibility(View.VISIBLE);
        processLogin();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.clear();
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
            User.getInstance().setUserName(user.getText().toString());
            User.getInstance().setPass(pass.getText().toString());
            processLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void eventoOnClick(View v){

        if (user.getText().length() != 0 && pass.getText().length() != 0) {
                    User.getInstance().setUserName(user.getText().toString());
                    User.getInstance().setPass(pass.getText().toString());
                    processLogin();

                }else{
            User.getInstance().setLoginStatus("sinred");

            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),"Debe completar user/pass", Snackbar.LENGTH_LONG);


            snackbar.show();

        }

    }

    private void processLogin() {

        if(isNetworkAvailable()==true) {

            progressBar.setVisibility(View.VISIBLE);
            img.setImageDrawable(getResources().getDrawable(R.mipmap.login));
            Request rq = new Request(this);
            webApp = new webApp(this.getApplicationContext(), rq, progressBar);
            Log.d("autologin", "user: " + defUser + " pass: " + defPass);
            webApp.setUser(User.getInstance().getUserName());
            webApp.setPass(User.getInstance().getPass());


            /** Login **/
            Login login = new Login(webApp, null);
            login.Ejecute();
        }else{

            TextInputLayout userLayout = (TextInputLayout) findViewById(R.id.userTextLayout);
            userLayout.setVisibility(View.INVISIBLE);
            TextInputLayout passLayout = (TextInputLayout) findViewById(R.id.passTextLayout);
            passLayout.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            img = (ImageView) findViewById(R.id.imgLogin);

            img.setImageDrawable(getResources().getDrawable(R.mipmap.clouderror));
            img.getLayoutParams().height = 300;
            img.getLayoutParams().width = 300;
            img.setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "Sin servicio de Datos/WiFi", Snackbar.LENGTH_LONG);

            snackbar.show();

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    processLogin();
                }
            });

        }
    }

    private  boolean isNetworkAvailable()
    {
        ConnectivityManager cmanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInformation= cmanager .getActiveNetworkInfo();
        if (netInformation!= null && netInformation.isAvailable() && netInformation.isConnected())
        {
            return true;
        }
        return false;
    }






}
