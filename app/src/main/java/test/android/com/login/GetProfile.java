package test.android.com.login;

/**
 * Clase que completa el perfil del usuario conectado
 */
public class GetProfile extends Action {

    private String method;
    private String status;
    private webApp service;
    private DataNodes datanodes;



    public String getMethod() {
        return method;
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

    public GetProfile(webApp s,TKTobj t) {
        super(s,t);
        this.method = "PROFILE";
        this.status = "profile";
        this.service = s;
        this.datanodes = new DataNodes(method,t);
    }

    @Override
    void Ejecute() {


        service.run(datanodes);
    }
}
