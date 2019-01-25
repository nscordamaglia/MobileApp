package test.android.com.login;

import android.util.Log;

/**
 * Clase que lista los campos del formulario en caso de que una acción lo necesite
 */
public class GetForm extends Action {

    private final String method;
    private String status;
    private webApp service;
    private DataNodes datanodes;

    /**
     * Constructor
     * @param s
     */
    public GetForm(webApp s, TKTobj tkt) {
        super(s,tkt);
        this.method = "FORM";
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


        Log.d("getaction","busco form");
        service.run(datanodes);



    }

}
