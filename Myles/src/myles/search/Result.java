package myles.search;
import java.util.Iterator;
import java.util.LinkedList;
import myles.config.Config;
import myles.servers.DlServer;

public class Result {
    // Atributos
    private String nombre;
    private String server;
    private int type;
    private int numLinks;
    private String fecha;
    private LinkedList<Link> links;
    private String URL;
    private String Server;

        public Result(String nombre, String server, String URL){
        this.nombre=nombre;
        this.server = server;
        this.URL = URL;
        this.fecha = "No disponible";
    }
    public Result(String nombre, String server, String URL, String fecha){
        this.nombre=nombre;
        this.server = server;
        this.URL = URL;
        this.fecha = fecha;
    }

    public void addLink(int serverId, String linkURL){
        links.add(new Link(serverId, linkURL));
        this.numLinks++;
    }


    // Observadores (getters)
    public String getServer(){ return Server; }
    public int getNumLinks(){ return numLinks; }
    public String getFecha(){ return fecha; }
    public String getURL(){ return URL; }
    public Link getLink(int index){ return links.get(index); }
    public LinkedList<Link> getLinks(){ return links; }

    public static Result parseLinks(String html_code){
        System.out.println("Comienza el parseo de links...");

        html_code = html_code.replaceAll(" ", "þ");
        html_code = html_code.replaceAll(">", "þ");
        html_code = html_code.replaceAll("<", "þ");
        html_code = html_code.replaceAll("\"", "þ");
        html_code = html_code.replaceAll("'", "þ");
        html_code = html_code.replaceAll("\n", "þ");

        LinkedList<DlServer> pivot_servers = Config.getDlServers();
        Iterator dl_server_iterator = pivot_servers.iterator();
        DlServer cur_dl_server;
        String[] sliced_html;
        String cur_url;
        while (dl_server_iterator.hasNext()){
            cur_dl_server = (DlServer)dl_server_iterator.next();
            sliced_html = html_code.split(cur_dl_server.get_url());           
            for(int i=1; i<sliced_html.length; i++){
                /**
                 * Si el enlace contiene los típicos ... de un enlace abreviado,
                 * no lo utilizaremos.
                 */
                if(!sliced_html[i].contains("...")){
                    cur_url = sliced_html[i];
                    cur_url = cur_url.split("þ")[0];
                    System.out.println("http://"+cur_dl_server.get_url()+cur_url);
                }
            }

        }
        return null;
    }
}
