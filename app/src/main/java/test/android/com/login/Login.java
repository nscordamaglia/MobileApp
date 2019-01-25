package test.android.com.login;



/**
 * Clase que se encarga de generar la conexión con itracker dando como resultado el hash de conexión utilizado a lo
 * largo de la sesión
 */
public class Login extends Action{

    private String user;
    private String pass;
    private final String method;
    private String status;
    private String servicename;
    private Itracker service;
    private DataNodes datanodes;




    /**
     * Constructor
     * @param s
     */
    public Login(Itracker s,TKTobj t) {
        super(s,t);

        this.method = "LOGIN";
        this.servicename = s.getClass().getCanonicalName().toString();
        servicename = servicename.replace(".", "-");
        String[] classname = servicename.split("-");
        //System.out.println(classname[0]);
        //System.out.println(classname[1]);
        this.servicename = classname[1];
        this.status="error::null";
        this.service = s;
        this.datanodes = new DataNodes(method,t);


    }

    public DataNodes getDatanodes() {
        return datanodes;
    }

    public void setDatanodes(DataNodes datanodes) {
        this.datanodes = datanodes;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public Itracker getService() {
        return service;
    }

    public void setService(Itracker service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    /**
     * Metodo que interpreta la respuesta del sitio remoto
     */
    @Override
    void Ejecute(){


        datanodes.setEditNodesData(service.getUser()+";"+service.getPass());
        service.run(datanodes);



    }


}
