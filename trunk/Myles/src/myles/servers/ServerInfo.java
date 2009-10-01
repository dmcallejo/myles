package myles.servers;

import org.jdom.Element;

/**
 * Esta clase sirve para almacenar los datos de cada servidor de busqueda,
 * leidos del archivo serverinfo.xml
 * @author dmcelectrico & juankar
 */
public class ServerInfo {
    private String name;
    private String URL;
    private int type;
    public ServerInfo(Element e){
        this.name = e.getAttributeValue("name");
        this.URL = e.getAttributeValue("url");
        this.type = Integer.parseInt(e.getAttributeValue("type"));
    }

    public String name(){ return name; }
    public String url(){ return URL; }
    public int type(){ return type; }

}
