package myles.config;
import org.jdom.input.SAXBuilder;
import org.jdom.*;
import myles.utils.HttpUtils;
import java.util.List;

/**
 *
 * @author diego
 */
public class WebUpdate {

    String coreVersion,GUIversion;

    public WebUpdate() throws java.net.MalformedURLException, java.io.IOException, JDOMException {
        System.out.println("Running WebUpdate...");
        String xml = HttpUtils.getText("http://www.labolera.net/myles/xml/appdata.xml");
        String[] versions = xml.split("version=\"");
        coreVersion=versions[2].split("\"")[0];
        GUIversion=versions[3].split("\"")[0];
        System.out.println("Core Version: "+coreVersion+"\nGUI version: "+GUIversion+"\n" +
                "Local Version: ");
        // Creamos el builder basado en SAX
        SAXBuilder builder = new SAXBuilder();
        // Construimos el arbol DOM a partir del fichero xml
        Document jdomConfig = builder.build(Config.class.getResourceAsStream("../xml/appdata.xml"));
        // rootConfig será el elemento raíz del XML
        System.out.println("Core: " +
                jdomConfig.getRootElement().getChild("appConfig").getChild("core").getAttributeValue("version"));
        System.out.println("GUI: " +
                jdomConfig.getRootElement().getChild("appConfig").getChild("gui").getAttributeValue("version"));


    }
}
