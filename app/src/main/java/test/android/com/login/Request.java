package test.android.com.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.os.RemoteException;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultRedirectHandler;
import cz.msebera.android.httpclient.impl.client.LaxRedirectStrategy;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * Clase encargada de las conexiones asincrónicas http contra la API
 */
public class Request {



    private RQcomplete<String> callback;
    private String url;
    private String xml=null;
    private String response;
    private Context c;

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Request(RQcomplete<String> cb) {

        this.response = "error::null";
        this.callback = cb;


    }



    public void HTTPasync(final Context c, final ProgressBar progressBar, final String method){


        setC(c);
        MySSLSocketFactory socketFactory=null;
        // Create a trust manager that does not validate certificate chains

        /// We initialize a default Keystore
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // We load the KeyStore
            trustStore.load(null, null);
            // We initialize a new SSLSocketFacrory
            socketFactory = new MySSLSocketFactory(trustStore);
            // We set that all host names are allowed in the socket factory
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(88000);
        client.setConnectTimeout(88000);
        client.setResponseTimeout(88000);




        HttpEntity entity;
        try {
            entity = new StringEntity(getXml(), "UTF-8");
        } catch (IllegalArgumentException e) {

            return;
        }
        String  contentType = "string/xml;UTF-8";

        client.setSSLSocketFactory(socketFactory);
        client.addHeader("Authorization", "OAuth 2.0");
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.addHeader("Accept-Language", "en-US,en;q=0.5");

        client.setEnableRedirects(true);


        Log.d("relog","url: " + getUrl());
        client.post(c, getUrl(), entity,contentType, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();

            }

            @Override
            public void onFinish() {
                super.onFinish();
                if(progressBar != null){
                progressBar.setVisibility(View.GONE);}

            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                if (i == 200) {

                    String resultado = new String(bytes);
                    Log.d("itracker", "onSuccess: " + resultado);
                    setResponse(resultado);
                    /* poder avisar al hilo principal para ejecutar la actualizacion de la lista*/
                    callback.onTaskComplete(resultado,method);



                }else{ Log.d("itracker", "codigo: " + i);}

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Log.d("itracker", "falló la conexíón, codigo: " + i,throwable);
                setResponse("<?xml version=\"1.0\" encoding=\"UTF-8\"?><itracker><header></header><response><error>fallo de conexión</error></response></itracker>");
                callback.onTaskComplete(getResponse(),method);

            }
        });



    }





}

