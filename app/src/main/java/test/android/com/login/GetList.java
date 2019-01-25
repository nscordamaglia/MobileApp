package test.android.com.login;

/**
 * Clase que se extiende de Action y usa como parametro al servicio que instancia y lista los itracker de tratamiento por equipo.
 * @author Nicolas Scordamaglia
 */
class GetList extends Action{

    private final String method;
    private String status;
    private Itracker service;
    private DataNodes datanodes;

    /**
     * Constructor
     * @param s
     */
    public GetList(Itracker s,TKTobj t) {
        super(s,t);
        this.method = "LISTING";
        this.status = "status";
        this.service = s;
        this.datanodes = new DataNodes(method,t);
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
