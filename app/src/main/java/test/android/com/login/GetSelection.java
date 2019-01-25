package test.android.com.login;

import android.util.Log;

/**
 * Clase que lista las opciones de un select dentro de un formulario
 */
public class GetSelection extends Action{
    private final String method;
    private String status;
    private Itracker service;
    private DataNodes datanodes;

    /**
     * Constructor
     * @param s
     */
    public GetSelection(Itracker s, TKTobj tkt) {
        super(s,tkt);
        this.method = "SELECT";
        this.status = "status";
        this.service = s;
        this.datanodes = new DataNodes(method,tkt);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Itracker getService() {
        return service;
    }

    public void setService(Itracker service) {
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


        Log.d("getselection","busco select");
        service.run(datanodes);



    }
}
