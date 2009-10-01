package myles.config;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import myles.servers.DlServer;



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
    public static LinkedList<DlServer> getDlServers(){
        try {
            LinkedList<DlServer> result = new LinkedList<DlServer>();
            SAXBuilder builder = new SAXBuilder();
            // Construimos el arbol DOM a partir del fichero xml
            Document jdomConfig = builder.build(new FileInputStream("C:/config/dlserverdata.xml"));
            // rootConfig será el elemento raíz del XML
            List servers = jdomConfig.getRootElement().getChildren();
            for (int i = 0; i < servers.size(); i++) {
                Element this_dl_server = (Element) servers.get(i);
                result.add(new DlServer(Integer.parseInt(this_dl_server.getAttributeValue("id")), this_dl_server.getAttributeValue("name"), this_dl_server.getAttributeValue("url")));
            }
            return result;
        } catch (JDOMException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    } 

}