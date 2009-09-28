package myles.config;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import java.io.*;
import java.util.List;
import org.jdom.input.DOMBuilder;



public class Config {

    public static List get_login_info(int server_id) throws org.jdom.JDOMException, java.io.IOException{
        // Creamos el builder basado en SAX
        SAXBuilder builder = new SAXBuilder();
        // Construimos el arbol DOM a partir del fichero xml
        Document jdomConfig = builder.build(new FileInputStream("C:/config/serverdata.xml"));
        // rootConfig será el elemento raíz del XML
        Element xml_server = jdomConfig.getRootElement().getChild("srvConfig").getChild("Server"+server_id);
        if(xml_server == null) return null;
        return xml_server.getAttributes();
    }
}