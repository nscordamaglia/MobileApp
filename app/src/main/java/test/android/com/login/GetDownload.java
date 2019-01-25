package test.android.com.login;

/**
 * Clase que descarga los adjuntos para verlos desde la app
 */
public class GetDownload extends Action {

    private final String method;
    private String status;
    private webApp service;
    private DataNodes datanodes;

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
     * Constructor
     * @param s
     */
    public GetDownload(webApp s,TKTobj t) {
        super(s,t);
        this.method = "DOWNLOAD";
        this.status = "status";
        this.service = s;
        this.datanodes = new DataNodes(method,t);
    }

    @Override
    void Ejecute() {

        service.run(datanodes);
    }
}
