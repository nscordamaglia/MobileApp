package test.android.com.login;


import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
 * Clase encargada de leer la estructura del xml y completarla para realizar la acción correspondiente
 */
public class MakeHeader {

    private String urlParameters;
    private Document document;
    private DataNodes d;
    private Hash hash;
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public MakeHeader(DataNodes d,Hash hash) {

        this.hash = hash;
        this.d = d;
        Log.d("log", "MakeHeader: ");
        toXML(ConfigManager.getXml());
    }

    public MakeHeader(String xml){

        /**
         * constructor para cambiar el hash al realizar un reenvio a la api
         */
        toXML(xml);
        Document doc = getDocument();
        doc.getElementsByTagName("hash").item(0).setTextContent(User.getInstance().getHash());
        toTXT(doc);

    }



    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }



    public String getUrlParameters() {
        return urlParameters;
    }

    public void setUrlParameters(String urlParameters) {
        this.urlParameters = urlParameters;
    }




    /**
     * Metodo que genera la estructura xml
     */
    void run(){

        String[] dataheader;
        String[] nodes;
        String[] datanodes;


        //edito los nodos cabecera
        dataheader = d.editHeader.split(";");

        document.getElementsByTagName("usr").item(0).setTextContent(User.getInstance().getUserName());
        document.getElementsByTagName("hash").item(0).setTextContent(User.getInstance().getHash());
        document.getElementsByTagName("class").item(0).setTextContent(dataheader[1]);
        document.getElementsByTagName("method").item(0).setTextContent(dataheader[2]);


            if (User.getInstance().getAttach()==true){
                Log.d("uriimg","entra por true");
                Node node = document.createElement("archivo.jpg");
                document.getElementsByTagName("files").item(0).appendChild(node);
                node.setTextContent(User.getInstance().getBase64());
                User.getInstance().setAttach(false);


            }



        //inserto los nodos de la accion y edito los nodos insertados
        nodes = d.createNodes.split(";");
        datanodes = d.editNodesData.split(";");

        for (int i = 0; i<nodes.length; i++){


            if("actions".equals(nodes[i])){

                setAction(datanodes[i]);
            }
            try {
                Node node = document.createElement(nodes[i]);
                document.getElementsByTagName("params").item(0).appendChild(node);
                Log.d("itracker", "datanode en: " + i + "valor: " + datanodes[i]);
                node.setTextContent(datanodes[i]);
            }catch(ArrayIndexOutOfBoundsException e){e.printStackTrace();}

        }



        toTXT(getDocument());




    }

    private void toTXT(Document document) {

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



        Log.d("itracker","toString: " + xml);
        setUrlParameters(xml);
    }

    private void toXML(String xml) {

        Document document = null;
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
            Log.d("itracker", "document: null ");
        }else {
            Log.d("itracker", "toXML: "+ xml);
        }
        setDocument(document);
    }


}
