package myles.config;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import myles.servers.DlServer;
import myles.utils.MD5;

public class Config {

    public static LinkedList<Element> getLoginInfo() throws org.jdom.JDOMException, java.io.IOException {
        // Creamos el builder basado en SAX
        SAXBuilder builder = new SAXBuilder();
        // Construimos el arbol DOM a partir del fichero xml
        Document jdomConfig = builder.build(Config.class.getResourceAsStream("../xml/serverdata.xml"));
        // rootConfig será el elemento raíz del XML
        List servers = jdomConfig.getRootElement().getChild("srvConfig").getChildren();
        LinkedList<Element> result = new LinkedList<Element>();
        for (int i = 0; i < servers.size(); i++) {
            result.add((Element) servers.get(i));
        }
        return result;
    }
    public static boolean addLoginInfo(int type, String name, String URL, String user, String pass) throws java.security.NoSuchAlgorithmException, org.jdom.JDOMException, java.io.IOException {
        /**
         * Primero de todo, construimos el child que vamos a "appendar" al árobl XML.
         * Chof
         */
       Element newChild = new Element("Server");
       newChild.setAttribute("type", Integer.toString(type));
       newChild.setAttribute("name", name);
       newChild.setAttribute("url", URL);
       Element active = new Element("active");
       active.setText("1");
       Element xml_user = new Element("user");
       xml_user.setText(user);
       Element encpass = new Element("encpass");
       encpass.setText(MD5.MD5(pass));
       newChild.addContent(active);
       newChild.addContent(xml_user);
       newChild.addContent(encpass);
        /**
         * Child preparado para meterse al árbol!
         */
        // Creamos el builder basado en Sax, again
       SAXBuilder builder = new SAXBuilder();
       // Construimos...
       Document jdomConfig = builder.build(Config.class.getResourceAsStream("../xml/serverdata.xml"));
       // rootConfig...

       Element servers = jdomConfig.getRootElement().getChild("srvConfig");
       servers.addContent(newChild);

       
       XMLOutputter xml_out = new XMLOutputter();
       FileWriter fileWriter=new FileWriter("../xml/serverdata.xml");
       BufferedWriter out = new BufferedWriter(fileWriter);
       xml_out.output(jdomConfig, out);

       // Hasta luego!
        return true;

    }

    public static LinkedList<DlServer> getDlServers() {
        try {
            LinkedList<DlServer> result = new LinkedList<DlServer>();
            SAXBuilder builder = new SAXBuilder();
            // Construimos el arbol DOM a partir del fichero xml
            Document jdomConfig = builder.build(Config.class.getResourceAsStream("../xml/dlserverdata.xml"));
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