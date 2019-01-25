package test.android.com.login;

/**
 * Clase que lista los eventos por ticket para ser mostrados en la UI
 */
public class GetEvent extends Action{

    private final String method;
    private String status;
    private Itracker service;
    private DataNodes datanodes;




    /**
     * Constructor
     * @param s
     */
    public GetEvent(Itracker s, TKTobj tkt) {
        super(s,tkt);
        this.method = "EVENT";
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


        service.run(datanodes);


    }

}
