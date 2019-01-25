package test.android.com.login;

/**
 * Clase que lista las opciones de tipificación en la creación de un nuevo ticket
 */
public class GetNew extends Action {

    private final String method;
    private String status;
    private Itracker service;
    private DataNodes datanodes;

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
     * Constructor
     * @param s
     */
    public GetNew(Itracker s,TKTobj t) {
        super(s,t);
        this.method = "NEW";
        this.status = "status";
        this.service = s;
        this.datanodes = new DataNodes(method,t);
    }

    public GetNew(Itracker s) {
        super(s);
        this.service = s;
        this.method = "NEW";
    }

    @Override
    void Ejecute() {

        service.run(datanodes);
    }

    @Override
    void Resend() {
        service.resend();
    }
}
