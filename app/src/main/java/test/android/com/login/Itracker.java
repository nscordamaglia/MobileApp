package test.android.com.login;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Clase encargada de armar el xml para enviarlo a webApp
 */
public  class webApp {

    private final String url;
    private String response;
    private Hash hash;
    private ArrayList<TKTobj> arrayTKT;
    private String user;
    private String pass;
    private Context c;
    private Request rq;
    private ProgressBar progressBar;




    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Request getRq() {
        return rq;
    }

    public void setRq(Request rq) {
        this.rq = rq;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public ArrayList<TKTobj> getArrayTKT() {
        return arrayTKT;
    }

    public void setArrayTKT(ArrayList<TKTobj> arrayTKT) {
        this.arrayTKT = arrayTKT;
    }

    public Hash getHash() {
        return hash;
    }

    public void setHash(Hash hash) {
        this.hash = hash;
    }



    public webApp(Context c, Request rq, ProgressBar progressBar) {
        this.url = ConfigManager.getUrlwebApp();
        this.hash = new Hash();
        this.c = c;
        this.rq = rq;
        this.progressBar = progressBar;

    }


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


    void run(DataNodes d){

            //deberia saber que clase lo invoca para poder pedirle los datos
            MakeHeader header = new MakeHeader(d,hash);
            header.run();
            rq.setUrl(url);
            rq.setXml(header.getUrlParameters());
            Log.d("profile","method: " + d.getEditHeader().split(";")[2]);
            String method = d.getEditHeader().split(";")[2];
            String params ="";
            try {
                 params = d.getEditNodesData().split(";")[4];
            }catch(ArrayIndexOutOfBoundsException e){}
            if (method.equalsIgnoreCase("idsel_listteams") && params.equalsIgnoreCase("{\"filter\":\"mytkts_vista\"}")){

               method = "idteam";  Log.d("idsel_listteams","cambia metodo por " + method);
            }else if (!method.equalsIgnoreCase("login")){

                Log.d("relog","guardo params");
                StaticAction.getInstance().setXml(rq.getXml());
                StaticAction.getInstance().setMethod(method);

            }
            rq.HTTPasync(c, progressBar,method);




    }


    public void resend() {

        Log.d("relog","method: " + StaticAction.getInstance().getMethod() + " params: " + StaticAction.getInstance().getXml());
        MakeHeader newheader = new MakeHeader(StaticAction.getInstance().getXml());
        rq.setXml(newheader.getUrlParameters());
        rq.HTTPasync(c,progressBar,StaticAction.getInstance().getMethod());

    }
}
