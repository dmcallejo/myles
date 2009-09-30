package myles.config;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import org.jdom.input.DOMBuilder;



public class Config {

    public static LinkedList<Element> get_login_info() throws org.jdom.JDOMException, java.io.IOException{
        // Creamos el builder basado en SAX
        SAXBuilder builder = new SAXBuilder();
        // Construimos el arbol DOM a partir del fichero xml
        Document jdomConfig = builder.build(new FileInputStream("C:/config/serverdata.xml"));
        // rootConfig será el elemento raíz del XML
        List servers = jdomConfig.getRootElement().getChild("srvConfig").getChildren();
        LinkedList<Element> result = new LinkedList<Element>();
        for(int i = 0; i < servers.size(); i++){
            result.add((Element)servers.get(i));
        }
        return result;



    }
}