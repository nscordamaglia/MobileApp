package test.android.com.login;



import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *Parseador XML
 */
public class XMLparser {

    private String data;
    private String response;
    private Document document;
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

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    XMLparser(String r) {

        this.response = r;

    }

    public void Extract(){


        String texto = null;
        String data = null;

        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try{
        factory=XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        parser=factory.newPullParser();


            parser.setInput(new ByteArrayInputStream(getResponse().getBytes()), null);


        int evenType = parser.getEventType();

        while(evenType!=XmlPullParser.END_DOCUMENT){

            String tagname = parser.getName();

            switch (evenType){

                case XmlPullParser.START_TAG:

                    break;
                case XmlPullParser.TEXT:
                    texto = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if(tagname.equalsIgnoreCase("error")){

                        data="error:"+texto;
                    }else if(tagname.equalsIgnoreCase("hash")){


                        data=texto;
                        Log.d("webApp", "hash: " + data);

                    }
                    break;
                default:
                    break;

            }
            evenType=parser.next();


          }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setData(data);

    }





    private String toTXT(Document document) {

        String xml = null;
      try {
          //paso el document a string para ser enviado
          DOMSource domSource = new DOMSource(document);
          StringWriter writer = new StringWriter();
          StreamResult result = new StreamResult(writer);
          TransformerFactory tf = TransformerFactory.newInstance();
          Transformer transformer = tf.newTransformer();
          transformer.transform(domSource, result);
           xml = writer.toString();
      }catch (TransformerConfigurationException ex) {

      } catch (TransformerException ex) {

      }



        return xml;
    }

    private void toXML(String x) {

        Document document = null;

        String xml = x.replaceAll(">\\s*<", "><");
        if (xml == null) {

            xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                    "<webApp><header></header><response>" +
                    "<error>error::null</error>" +
                    "</response></webApp>";
            Log.d("webApp","xml harcoding: " + xml);

        }


            try {
                //paso el xml String a document para ser editado
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilderFactory.setNamespaceAware(false);
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                //transformo el string para poder generar el documento
                InputSource is = new InputSource(new StringReader(xml));
                document = documentBuilder.parse(is);
            }catch (ParserConfigurationException ex) {

            } catch (SAXException ex) {

            } catch (IOException ex) {

            }



        if (document == null){
            Log.d("webApp", " document null ");
        }
        setDocument(document);
    }



    public void ReadResult(String method) {

        Log.d("responselog", getResponse());
        toXML(getResponse());
        //si existe response-->error devuelvo error, de lo contrario devuelvo hash
        Document doc = getDocument();

        String tag = "";
        String value = "";
            try {
                Log.d("taglog","tags response:" + doc.getElementsByTagName(getData()).item(0).getChildNodes().getLength());
                tag = doc.getElementsByTagName(getData()).item(0).getFirstChild().getNodeName().toString();
                Log.d("taglog", "tag: " + tag);
                value = doc.getElementsByTagName(getData()).item(0).getFirstChild().getTextContent().toString();
                Log.d("taglog", "value: " + value);
                try {
                    if (value.substring(0,5).equalsIgnoreCase("error")){

                        tag = value.substring(0,5).toLowerCase();
                        value = value.substring(5,value.length());

                    }
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                Log.d("taglog", "value: " + value);
            }catch(NullPointerException e){

               try {
                   Log.d("taglog", "verifico tags response:" + doc.getElementsByTagName(getData()).item(0).getChildNodes().getLength());
                   value = "emptylist";
                   tag = "error";
               }catch(NullPointerException n){


                   value = "firewall";
                   tag = "error";
               }


            }
            if ("error".equalsIgnoreCase(tag)){

            setResponse(tag+":"+value);
            Log.d("errordisplay", tag+":"+value);

            }else{

                if("login".equalsIgnoreCase(method)){

                    String hash = doc.getElementsByTagName("hash").item(0).getTextContent().toString();
                    String access = doc.getElementsByTagName("access").item(0).getTextContent().toString();
                    User.getInstance().setAccess(User.getInstance().AccessToList(access));
                    setResponse(hash);
                    Log.d("taglog","method: " + method + " hash: " + hash + " access: " + access);

                }else if("idsel_listteams".equalsIgnoreCase(method)){

                    setNodelist(doc.getElementsByTagName("list"));

                    try{

                        setResponse(doc.getElementsByTagName("list").item(0).getFirstChild().getNodeName()+"list");

                    }catch(NullPointerException e){

                        setResponse("emptyList");

                    }


                }else if("idteam".equalsIgnoreCase(method)){

                    setNodelist(doc.getElementsByTagName("list"));

                    try{

                        setResponse("idteamlist");

                    }catch(NullPointerException e){

                        setResponse("emptyList");

                    }

                }else if("listteam".equalsIgnoreCase(method)){

                     setNodelist(doc.getElementsByTagName("list"));

                    try{

                        setResponse(doc.getElementsByTagName("list").item(0).getFirstChild().getNodeName()+"list");

                    }catch(NullPointerException e){

                        setResponse("emptyList");

                    }


                }else if("listmyteams".equalsIgnoreCase(method)){

                    setNodelist(doc.getElementsByTagName("list"));

                    try{

                        setResponse(doc.getElementsByTagName("list").item(0).getFirstChild().getNodeName()+"list");

                    }catch(NullPointerException e){

                        setResponse("emptyList");

                    }


                }else if("geth".equalsIgnoreCase(method)){

                    setNodelist(doc.getElementsByTagName("data"));

                    try{

                        setResponse(doc.getElementsByTagName("ths").item(0).getFirstChild().getNodeName()+"list");

                    }catch(NullPointerException e){

                        setResponse("emptyList");

                    }


                }else if("get_tree_options".equalsIgnoreCase(method)){

                    setNodelist(doc.getElementsByTagName("tree"));

                }else if("ejecute".equalsIgnoreCase(method)){

                    if(FormObj.getInstance().isSending()==true){


                        Log.d("newlog", "extraifo tkt");
                        toXML(getResponse());
                        Document d = getDocument();

                        NodeList node = d.getElementsByTagName("data");
                        NodeList prev = node.item(0).getChildNodes();




                        try {
                            String idtkt = ((ReadTag(prev, "id")).trim());
                            setResponse(idtkt);
                            Log.d("newlog","id extraido: " + idtkt);
                        } catch (NullPointerException e1) {

                            Log.d("newlog", "error de extraccion");
                        }

                    }


                }else if("downloadfile".equalsIgnoreCase(method)){

                    setNodelist(doc.getElementsByTagName("file"));

                }



            }

    }



    public String ReadTag(NodeList node,String tag) {


        int i= 0;

        Log.d("readtag"," lenght " + node.getLength());
        try {
            while (!tag.equalsIgnoreCase(node.item(i).getNodeName()) && i < node.getLength() - 1) {

                i++;
                Log.d("readtag", " item: " + i + " tag:" + node.item(i).getNodeName().toString());


            }
        }catch(NullPointerException e){

            return "trash";
        }

        Log.d("readtag"," content: " + node.item(i).getTextContent() + " from tag: " + node.item(i).getNodeName());
        if (tag.equalsIgnoreCase(node.item(i).getNodeName())){

            return node.item(i).getTextContent();

        }else{

            return "";

        }


    }

    public void ReadFiles(NodeList node,String tag,EventObj event){

        int i= 0;

        Log.d("filelog"," lenght " + node.getLength());
        while (!tag.equalsIgnoreCase(node.item(i).getNodeName())&& i < node.getLength()-1){

            i++;Log.d("filelog"," item: "+ i + " tag:" + node.item(i).getNodeName().toString());

        }


        NodeList files =  node.item(i).getChildNodes();

        for (int x=0; x< files.getLength(); x++) {

            event.getvAttach().add(files.item(x).getTextContent());
            Log.d("filelog","file: " + files.item(x).getTextContent() );

        }

    }

    public void ReadNode(NodeList node, ElementObj element) {

        String required = null,numeric = null, regex = null;

        int lenghtE = node.getLength();
        for (int i = 0; i<lenghtE; i++){

            if(node.item(i).getNodeName().toString().equalsIgnoreCase("validations")) {

                int lenght = node.item(i).getChildNodes().getLength();
                Log.d("getform","validation tiene " + lenght + " nodo.");
                for (int x = 0; x<lenght; x++){

                    String tag =  node.item(i).getChildNodes().item(x).getNodeName().toString();

                    switch (tag){

                        case "required":
                            required = node.item(i).getChildNodes().item(x).getTextContent().toString().trim();
                            Log.d("getform", "leo en nodo: " + node.item(i).getNodeName().toString() + "->" + node.item(i).getChildNodes().item(x).getNodeName().toString()+"->" + node.item(i).getChildNodes().item(x).getTextContent().toString());
                            if(required.equalsIgnoreCase("true")){

                                Log.d("getform", "extraigo: " + node.item(i).getChildNodes().item(x).getNodeName().toString() + "->" + required );
                                element.setRequired(true);

                            }
                            break;
                        case "numeric":
                            numeric = node.item(i).getChildNodes().item(x).getTextContent().toString().trim();
                            Log.d("getform", "leo en nodo: " + node.item(i).getNodeName().toString() + "->" + node.item(i).getChildNodes().item(x).getNodeName().toString()+"->"+node.item(i).getChildNodes().item(x).getTextContent().toString());
                            if(numeric.equalsIgnoreCase("true")){

                                Log.d("getform", "extraigo: " + node.item(i).getChildNodes().item(x).getNodeName().toString() + "->" + numeric);
                               element.setNumeric(true);

                            }
                            break;
                        case "regex":
                            String regular = node.item(i).getChildNodes().item(x).getTextContent().toString().trim();
                            regex = regular.substring(1,regular.length()-1);
                            Log.d("getform", "leo en nodo: " + node.item(i).getNodeName().toString() + "->" + node.item(i).getChildNodes().item(x).getNodeName().toString()+"->"+node.item(i).getChildNodes().item(x).getTextContent().toString());
                            boolean b = Pattern.matches(regex, "0123456789");
                            if(b == true){

                                Log.d("getform", "extraigo: " + node.item(i).getChildNodes().item(x).getNodeName().toString() + "->" + regex);
                                element.setNumeric(true);

                            }
                            break;
                    }


                }

            }


        }



    }

    public void GetSelect(NodeList node, ElementObj element){

        //Creo los array que van a contener las listas
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> id = new ArrayList<String>();

        Log.d("getselect","size ids: " + FormObj.getInstance().getIdselitem().size());
        Log.d("getselect","size names: " + FormObj.getInstance().getNameselitem().size());

        int lenghtE = node.getLength();
        for (int i = 0; i<lenghtE; i++) {

            if (node.item(i).getNodeName().toString().equalsIgnoreCase("option")) {

                int lenght = node.item(i).getChildNodes().getLength();
                Log.d("getselect", "select tiene " + lenght + " nodo.");
                for (int x = 0; x < lenght; x++) {

                    String tag =  node.item(i).getChildNodes().item(x).getNodeName().toString();

                    switch (tag){
                        case "value":

                            Log.d("getselect", "leo en nodo: " + node.item(i).getNodeName().toString() + "->"
                                    + node.item(i).getChildNodes().item(x).getNodeName().toString()+"->"
                                    + node.item(i).getChildNodes().item(x).getTextContent().toString());

                            //FormObj.getInstance().getIdselitem().add(node.item(i).getChildNodes().item(x).getTextContent().toString());
                            id.add(node.item(i).getChildNodes().item(x).getTextContent().toString());
                            break;

                        case "text":
                            Log.d("getselect", "leo en nodo: " + node.item(i).getNodeName().toString() + "->"
                                    + node.item(i).getChildNodes().item(x).getNodeName().toString()+"->"
                                    + node.item(i).getChildNodes().item(x).getTextContent().toString());

                            //FormObj.getInstance().getNameselitem().add(node.item(i).getChildNodes().item(x).getTextContent().toString());
                            name.add(node.item(i).getChildNodes().item(x).getTextContent().toString());
                            break;
                    }
                }
            }
        }
        FormObj.getInstance().getIdselitem().add(id);
        FormObj.getInstance().getNameselitem().add(name);

    }

    public void UpdateTKT(NodeList nodelist) throws JSONException {

        Log.d("webApp","updatetkt");
            int lenghtTKT = nodelist.item(0).getChildNodes().getLength();
            ArrayList<TKTobj> arrayList = new ArrayList<TKTobj>();

        for (int i = 0; i<lenghtTKT; i++){

            String id = ReadTag(nodelist.item(0).getChildNodes().item(i).getChildNodes(),"id");
            String FA = ReadTag(nodelist.item(0).getChildNodes().item(i).getChildNodes(),"FA");
            String origen_json = JsonParser("{\"tipif\":"+ReadTag(nodelist.item(0).getChildNodes().item(i).getChildNodes(),"origen_json")+"}");
            String equiposname = ReadTag(nodelist.item(0).getChildNodes().item(i).getChildNodes(),"usr_o.equiposname");
            TKTobj tkt = new TKTobj();
            tkt.setId(id);
            tkt.setFA(FA);
            tkt.setEquiposname(equiposname);
            tkt.setTipif(origen_json);
            arrayList.add(tkt);


        }

            //arraytkt singleton
            ArrayTKT array = ArrayTKT.getInstance();
            array.setArrayList(arrayList);
            array.setStatus("full");
            Calendar c = Calendar.getInstance();
            ArrayTKT.getInstance().setDate(c.getTime());



    }

    public void UpdateTKTgen(NodeList nodelist) throws JSONException {

        Log.d("webApp","updatetkt");
        int lenghtTKT = nodelist.item(0).getChildNodes().getLength();
        ArrayList<TKTobj> arrayList = new ArrayList<TKTobj>();

        for (int i = 0; i<lenghtTKT; i++){

            String id = ReadTag(nodelist.item(0).getChildNodes().item(i).getChildNodes(),"id");
            String FA = ReadTag(nodelist.item(0).getChildNodes().item(i).getChildNodes(),"FA");
            String origen_json = JsonParser("{\"tipif\":"+ReadTag(nodelist.item(0).getChildNodes().item(i).getChildNodes(),"origen_json")+"}");
            //String equiposname = ReadTag(nodelist.item(0).getChildNodes().item(i).getChildNodes(),"usr_o.equiposname");
            TKTobj tkt = new TKTobj();
            tkt.setId(id);
            tkt.setFA(FA);
            //tkt.setEquiposname(equiposname);
            tkt.setTipif(origen_json);
            arrayList.add(tkt);


        }

        //arraytkt singleton
        ArrayTKTgen array = ArrayTKTgen.getInstance();
        array.setArrayList(arrayList);
        array.setStatus("full");
        Calendar c = Calendar.getInstance();
        ArrayTKTgen.getInstance().setDate(c.getTime());



    }

    private String JsonParser(String json) throws JSONException {
        String tipificacion ="";


        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Getting JSON Array node
        JSONArray tip = jsonObj.getJSONArray("tipif");

        // looping through All Students
        for (int i = 0; i < tip.length(); i++) {
            JSONObject c = tip.getJSONObject(i);

            tipificacion += c.getString("ans")+"->";

        }

        Log.d("webApp","tipificacion: " + tipificacion);
        return tipificacion.substring(0,tipificacion.length()-2);
    }

    public void UpdateProfile(NodeList nodelist) {

        int lenghtTKT = nodelist.item(0).getChildNodes().getLength();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        ArrayList<Integer> arrayListFilter = new ArrayList<Integer>();
        ArrayList<String> arrayListName = new ArrayList<String>();

        for (int i = 0; i<lenghtTKT; i++){

            String id = nodelist.item(0).getChildNodes().item(i).getChildNodes().item(0).getTextContent();
            Log.d("profile","equipo: " + id);
            String namefull = nodelist.item(0).getChildNodes().item(i).getChildNodes().item(1).getTextContent();
            Log.d("profile","nombre: " + namefull);
            arrayList.add(Integer.parseInt(id));
            arrayListFilter.add(Integer.parseInt(id));
            arrayListName.add(namefull);


        }

        User.getInstance().setListTeam(arrayList);
        User.getInstance().setListTeamFilter(arrayListFilter);
        Log.d("profile","listteamfilter lenght: " + User.getInstance().getListTeamFilter().size());
        User.getInstance().setListTeamName(arrayListName);

    }

    public void UpdateEvent(NodeList nodelist) {

        NodeList data = nodelist;
        NodeList ths;
        NodeList actions;
        //extraigo los nodelist de eventos y acciones
        int dataLenght = data.item(0).getChildNodes().getLength();

        for (int i=0;i<dataLenght;i++){


            if (data.item(0).getChildNodes().item(i).getNodeName().toString().equalsIgnoreCase("ths")){

                ths = data.item(0).getChildNodes().item(i).getChildNodes();
                findEvent(ths);

            }else if (data.item(0).getChildNodes().item(i).getNodeName().toString().equalsIgnoreCase("actions")){

                actions = data.item(0).getChildNodes().item(i).getChildNodes();
                findAction(actions);


            }


        }



    }

    private void findAction(NodeList actions) {

        NodeList action;
        int lenght = actions.getLength();

        EventObj event = new EventObj();

        for (int i=0;i<lenght;i++){


            ActObj act = new ActObj();
            action = actions.item(i).getChildNodes();

            act.setAlias(ReadTag(action, "alias"));
            act.setNombre(ReadTag(action, "nombre"));
            act.setFormulario(ReadTag(action, "formulario"));
            event.getArrayactions().add(act);

        }

        if(origin.equalsIgnoreCase("List_fragment")){

            try {
                ArrayTKT.getInstance().getArrayList().get(position).getArrayevent().add(event);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }else if(origin.equalsIgnoreCase("Generated_fragment")){

            try {
                ArrayTKTgen.getInstance().getArrayList().get(position).getArrayevent().add(event);
            } catch (IndexOutOfBoundsException e) {
                Log.d("tktid","sale por catch x lista vacia");
                e.printStackTrace();
            }

        }else if(origin.equalsIgnoreCase("search")){

            ArrayTKTsearch.getInstance().getArrayList().get(position).getArrayevent().add(event);

        }

    }

    private void findEvent(NodeList ths) {


        NodeList th;
        int lenght = ths.getLength();

        Log.d("eventos","cantidad de eventos: " + lenght);
        for (int i=0;i<lenght;i++){

            EventObj event = new EventObj();
            //por cada th bsuca si tiene adjunto
                    try{

                        Log.d("buscofile", ths.item(i).getChildNodes().item(2).getNodeName().toString());
                        //leer los archivos dentro del tag y guardarlos
                        //en un array
                        //String attach = ReadTag(ths.item(i).getChildNodes(), "files");
                        //Log.d("buscofile", attach);
                        //event.getvAttach().add(attach);
                        ReadFiles(ths.item(i).getChildNodes(), "files",event);

                    }catch(NullPointerException e){}


                th = ths.item(i).getChildNodes();

                if (th.getLength() == 1){
                    Log.d("itform","sin itform");
                    NodeList actionNodeList = th.item(0).getChildNodes();
                    Node actionNode = th.item(0);
                    try {

                        event.setAlias(ReadTag(actionNodeList, "alias"));
                        event.setDate(ReadTag(actionNodeList, "date"));
                        event.setValue(ReadTag(actionNodeList, "value"));

                    }catch (NullPointerException e){

                        event.setValue("-");
                    }

                }else{


                    Log.d("itform","con itform");
                    NodeList actionNodeList = th.item(0).getChildNodes();Log.d("itform","action lenght: " + actionNodeList.getLength());
                    Node actionNode = th.item(0);
                    NodeList itformNodeList = th.item(1).getChildNodes();Log.d("itform","itform lenght: " + itformNodeList.getLength());
                    Node itformNode = th.item(1);

                    try {

                        event.setAlias(ReadTag(actionNodeList, "alias"));
                        event.setDate(ReadTag(actionNodeList, "date"));
                        event.setValue(ReadTag(actionNodeList, "value"));

                        for (int x=0;x<itformNodeList.getLength();x++){


                            NodeList e = itformNodeList.item(x).getChildNodes();

                            Log.d("itform","nodename: " + itformNodeList.item(x).getNodeName().toString());
                            ElementObj element = new ElementObj();
                            String type = ReadTag(itformNodeList.item(x).getChildNodes(),"type");
                            if (type.equalsIgnoreCase("select")){

                                String val = null;
                                String nombre = null;

                                //buscar dentro de los tag <option> el que tenga <value>#value</value>
                                //y escribir el <nombre>#nombre</nombre>

                                String value = ReadTag(itformNodeList.item(x).getChildNodes(),"value");

                                for (int i_elem = 0; i_elem<e.getLength(); i_elem++) {
                                    //recorro el element para buscar el tag option
                                    if (e.item(i_elem).getNodeName().toString().equalsIgnoreCase("option")) {

                                        //detecto cuantos tag tengo dentro y extrago su valor
                                        int nodelenght = e.item(i_elem).getChildNodes().getLength();

                                        Log.d("getselect", "select tiene " + nodelenght + " nodo.");
                                        for (int i_opt = 0; i_opt < nodelenght; i_opt++) {

                                            String tag =  e.item(i_elem).getChildNodes().item(i_opt).getNodeName().toString();

                                            switch (tag){
                                                case "value":

                                                    Log.d("getselect", "leo en nodo: " + e.item(i_elem).getNodeName().toString() + "->"
                                                            + e.item(i_elem).getChildNodes().item(i_opt).getNodeName().toString()+"->"
                                                            + e.item(i_elem).getChildNodes().item(i_opt).getTextContent().toString());
                                                    val =  e.item(i_elem).getChildNodes().item(i_opt).getTextContent().toString();


                                                    break;
                                                case "text":
                                                    Log.d("getselect", "leo en nodo: " + e.item(i_elem).getNodeName().toString() + "->"
                                                            + e.item(i_elem).getChildNodes().item(i_opt).getNodeName().toString()+"->"
                                                            + e.item(i_elem).getChildNodes().item(i_opt).getTextContent().toString());
                                                    nombre =  e.item(i_elem).getChildNodes().item(i_opt).getTextContent().toString();

                                                    break;
                                        }

                                    }
                                    //consulto si existe similitud entre los values
                                        if (value.equalsIgnoreCase(val)){
                                            element.setValue(nombre);
                                        }
                                    }
                                }


                            }else{

                                element.setValue(ReadTag(itformNodeList.item(x).getChildNodes(),"value"));
                            }

                            Log.d("itform","value: " + element.getValue());
                            element.setLabel(ReadTag(itformNodeList.item(x).getChildNodes(),"label"));
                            Log.d("itform","label: " + element.getLabel());
                            event.getArrayelements().add(element);

                        }

                    }catch (NullPointerException e){

                        event.setValue("-");
                        Log.d("itform"," valor nulo");
                    }



                }

            if(origin.equalsIgnoreCase("List_fragment")){

                try {
                    ArrayTKT.getInstance().getArrayList().get(position).getArrayevent().add(event);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }else if(origin.equalsIgnoreCase("Generated_fragment")){

                try {
                    ArrayTKTgen.getInstance().getArrayList().get(position).getArrayevent().add(event);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }else if(origin.equalsIgnoreCase("search")){

                ArrayTKTsearch.getInstance().getArrayList().get(position).getArrayevent().add(event);

            }



        }


    }

    public void UpdateForm(String response) {

        toXML(response);
        Document doc = getDocument();

        NodeList node = doc.getElementsByTagName("itform");
        NodeList itform = node.item(0).getChildNodes();

        ArrayList<ElementObj> array = new ArrayList<ElementObj>();


        for (int i=0;i<itform.getLength();i++){

            Log.d("getform",String.valueOf(itform.getLength()));
            Log.d("getform",itform.item(i).getNodeName().toString());
            NodeList e = itform.item(i).getChildNodes();
            ElementObj element = new ElementObj();
            try {
                element.setLabel((ReadTag(e,"label")).trim());
            } catch (NullPointerException e1) {
                e1.printStackTrace();
            }
            try {
                element.setType((ReadTag(e,"type")).trim());
            } catch (NullPointerException e1) {
                e1.printStackTrace();
            }
            //armo el select para mostrar opciones
            if(element.getType().equalsIgnoreCase("select")){

                GetSelect(e,element);

            }
            element.setId((ReadTag(e,"id")).trim());
            try {
                element.setComment((ReadTag(e,"comment")).trim());
                Log.d("search_comment", element.getComment());
            } catch (NullPointerException e1) {
                e1.printStackTrace();
            }
            try {
                element.setPath((ReadTag(e,"path")).trim());
            } catch (NullPointerException e1) {
                e1.printStackTrace();
            }
            Log.d("getform",element.getLabel()+"/"+element.getType());
            try {
                ReadNode(e,element);
            }catch (NullPointerException empty){Log.d("getform","entra por catch");}


            Log.d("getform",element.getLabel() + "->" + element.getType() + "->" + element.isNumeric());
            array.add(element);

        }

        FormObj.getInstance().setArrayElemnt(array);

    }

    public void UpdateSelection(String response) {

        //Creo array de items
        ArrayList<String> idlist = new ArrayList<String>();
        ArrayList<String> namelist = new ArrayList<String>();



        toXML(response);
        Document doc = getDocument();

        NodeList node = doc.getElementsByTagName("list");
        NodeList list = node.item(0).getChildNodes();

        for (int i = 0; i<list.getLength();i++){

            Log.d("getselection",String.valueOf(node.getLength()));
            NodeList team = list.item(i).getChildNodes();
            String id = ReadTag(team,"id");
            String nombre = ReadTag(team,"nombre");
            idlist.add(id);
            namelist.add(nombre);


        }

        FormObj.getInstance().getNameselitem().add(namelist);
        FormObj.getInstance().getIdselitem().add(idlist);


    }

    public void UpdateProfileIdTeam(NodeList nodelist) {


        Log.d("idsel_listteams","update profile team");
        int lenghtTKT = nodelist.item(0).getChildNodes().getLength();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        ArrayList<Integer> arrayListFilter = new ArrayList<Integer>();
        ArrayList<String> arrayListName = new ArrayList<String>();

        for (int i = 0; i<lenghtTKT; i++){

            String id = nodelist.item(0).getChildNodes().item(i).getChildNodes().item(0).getTextContent();Log.d("profile",id);
            String namefull = nodelist.item(0).getChildNodes().item(i).getChildNodes().item(1).getTextContent();Log.d("profile",namefull);
            arrayList.add(Integer.parseInt(id));
            arrayListFilter.add(Integer.parseInt(id));
            arrayListName.add(namefull);


        }

        User.getInstance().setListTeamGen(arrayList);
        User.getInstance().setListTeamGenFilter(arrayListFilter);
        User.getInstance().setListTeamGenName(arrayListName);
        Log.d("profile",User.getInstance().getListTeamGenToString());
    }


    public void UpdateOptions(NodeList nodelist) {

        Log.d("option","updateoption");

        int lenght = nodelist.item(0).getChildNodes().getLength();
        ArrayList<OptionObj> arrayList = new ArrayList<OptionObj>();

        for (int i = 0; i<lenght; i++){

            Node node = nodelist.item(0).getChildNodes().item(i);
            Log.d("option",node.getNodeName().toString());
            switch (node.getNodeName().toString()){

                case "previous":
                    GetBack(getResponse());
                    break;
                case "options":
                    NodeList optNode = node.getChildNodes();
                    int optNodeLenght = optNode.getLength();
                    GetOptions(optNode,optNodeLenght);
                    break;
                case "opendata":
                    /*NodeList itform = node.getChildNodes();
                    ParseForm(itform);*/
                    ArrayOptions.getInstance().getArrayList().clear();
                    UpdateForm(getResponse());
                    break;

            }



        }



    }

    private void GetBack(String response) {

        Log.d("tagback", "tag back");
        toXML(response);
        Document doc = getDocument();

        NodeList node = doc.getElementsByTagName("previous");
        NodeList prev = node.item(0).getChildNodes();




            try {
                String path = ((ReadTag(prev, "back")).trim());
                if (path.equalsIgnoreCase("none")||path.equalsIgnoreCase("")){

                    User.getInstance().setBack("0");

                }else{

                    User.getInstance().setBack(path);
                }

                Log.d("tagback","back extraido: " + User.getInstance().getBack());
            } catch (NullPointerException e1) {

                Log.d("tagback", "back 0");
            }



    }


    private void GetOptions(NodeList optNode, int optNodeLenght) {

        ArrayList<OptionObj> array = new ArrayList<>();


        for (int i = 0; i < optNodeLenght; i++){

            OptionObj obj = new OptionObj();
            String title = ReadTag(optNode.item(i).getChildNodes(),"title");
            Log.d("option",title);
            String destiny = ReadTag(optNode.item(i).getChildNodes(),"destiny");
            Log.d("option",destiny);
            obj.setOption(title);
            obj.setPath(destiny);
            array.add(obj);


        }

        ArrayOptions.getInstance().setArrayList(array);
        setResponse("option");
    }

    public void GetFile(NodeList nodelist) {

        String name,data;
        NodeList file = nodelist;
        int lenght = file.item(0).getChildNodes().getLength();
        Log.d("filelog","get base64, lenght: " + lenght);
        for (int i=0; i<lenght; i++){

            Log.d("filelog",file.item(0).getChildNodes().item(i).getNodeName().toString()+"->"+file.item(0).getChildNodes().item(i).getTextContent().toString());
            String tag = file.item(0).getChildNodes().item(i).getNodeName().toString();
            switch (tag){

                case "name":
                    name = file.item(0).getChildNodes().item(i).getTextContent().toString();
                    break;
                case "data":
                    data = file.item(0).getChildNodes().item(i).getTextContent().toString();
                    User.getInstance().setBase64(data);
                    break;


            }

        }
    }
}
