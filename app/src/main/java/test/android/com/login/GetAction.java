package test.android.com.login;

import android.util.Log;

/**
 * Clase que ejecuta el evento acción en un ticket
 */
public class GetAction extends Action{

    private final String method;
    private String status;
    private webApp service;
    private DataNodes datanodes;

    /**
     * Constructor
     * @param s
     */
    public GetAction(webApp s, TKTobj tkt) {
        super(s,tkt);
        this.method = "ACTION";
        this.status = "status";
        this.service = s;
        this.datanodes = new DataNodes(method,tkt);
    }

    public GetAction(webApp s) {
        super(s);
        this.service = s;
        this.method = "ACTION";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public webApp getService() {
        return service;
    }

    public void setService(webApp service) {
        this.service = service;
    }

    public DataNodes getDatanodes() {
        return datanodes;
    }

    public void setDatanodes(DataNodes datanodes) {
        this.datanodes = datanodes;
    }


    /**
     * Metodo que interpreta la respuesta del sitio remoto
     */
    @Override
    void Ejecute(){


        service.run(datanodes);
        Log.d("getaction","evento ok");


    }

    @Override
    void Resend() {
       service.resend();
    }
}
