package test.android.com.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.w3c.dom.NodeList;

/**
 * Clase que parsea las respuestas asincrónicas para evaluar su tratamiento
 */
public class GetResponse {

    private String response;
    private NodeList nodelist;
    private int position;
    private String origin;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public NodeList getNodelist() {
        return nodelist;
    }

    public void setNodelist(NodeList nodelist) {
        this.nodelist = nodelist;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void execute(Context c, String resultado, String method) throws JSONException {



        XMLparser parser = new XMLparser(resultado);
        parser.setPosition(position);
        parser.setOrigin(origin);
        parser.setData("response");
        parser.ReadResult(method);
        Log.d("taglog", "response:" + method +  " - " + parser.getResponse().substring(0,5));


        setResponse(parser.getResponse());
        if("error".equals(parser.getResponse().substring(0,5))){

            Log.d("taglog", "error:" + method +  " - " + parser.getResponse().substring(0,5));
            User.getInstance().setLoginStatus(parser.getResponse().substring(6,parser.getResponse().length()));
            if("firewall".equalsIgnoreCase(parser.getResponse().substring(6,parser.getResponse().length()))){

                User.getInstance().setReport(resultado);

            }


        }else{

            User.getInstance().setLoginStatus("OK");
            Log.d("taglog", "method:" + method +  " - " + parser.getResponse().substring(0,5));

            if("login".equalsIgnoreCase(method)){

                User.getInstance().setHash(parser.getResponse());



            }else if("idsel_listteams".equalsIgnoreCase(method)){

                if("teamlist".equalsIgnoreCase(parser.getResponse())){


                    setNodelist(parser.getNodelist());
                    parser.UpdateProfile(getNodelist());


                }

            }
            else if("idteam".equalsIgnoreCase(method)){

                if("idteamlist".equalsIgnoreCase(parser.getResponse())){


                    setNodelist(parser.getNodelist());
                    parser.UpdateProfileIdTeam(getNodelist());


                }

            }else if("listteam".equalsIgnoreCase(method)){

                if("tktlist".equalsIgnoreCase(parser.getResponse())){


                    setNodelist(parser.getNodelist());
                    parser.UpdateTKT(getNodelist());


                }




            }else if("listmyteams".equalsIgnoreCase(method)){


                if("tktlist".equalsIgnoreCase(parser.getResponse())){


                    setNodelist(parser.getNodelist());
                    parser.UpdateTKTgen(getNodelist());


                }




            }else if ("geth".equalsIgnoreCase(method)){


                setNodelist(parser.getNodelist());
                parser.UpdateEvent(getNodelist());

            }else if ("ejecute".equalsIgnoreCase(method)){

                if(FormObj.getInstance().isSending()==true){

                    FormObj.getInstance().setSended(true);
                    setResponse(parser.getResponse());
                }


            }else if ("getform".equalsIgnoreCase(method)){

                //Limpio la lista de equipos antes de mostrar en la UI
                FormObj.getInstance().getNameselitem().clear();
                FormObj.getInstance().getIdselitem().clear();

                Log.d("getaction",parser.getResponse());
                setResponse(parser.getResponse());
                parser.UpdateForm(getResponse());


            }else if ("idsel_teamderive".equalsIgnoreCase(method)){

                setResponse(parser.getResponse());
                parser.UpdateSelection(getResponse());

            }else if ("get_tree_options".equalsIgnoreCase(method)){

                //Limpio la lista de equipos antes de mostrar en la UI
                FormObj.getInstance().getNameselitem().clear();
                FormObj.getInstance().getIdselitem().clear();

                setNodelist(parser.getNodelist());
                parser.UpdateOptions(getNodelist());
                setResponse(parser.getResponse());


            }else if("downloadfile".equalsIgnoreCase(method)){

                setNodelist(parser.getNodelist());
                parser.GetFile(getNodelist());

            }





        }

    }
}
