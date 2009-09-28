package myles.search;
import myles.search.Link;
import java.util.LinkedList;

public class Result {
    // Atributos
    private String Server;
    private int numLinks;
    private String fecha;
    private LinkedList<Link> links;
    private String URL;

    public Result(String Server, String URL){
        this.Server = Server;
        this.URL = URL;
        this.fecha = "No disponible";
    }
    public Result(String Server, String URL, String fecha){
        this.Server = Server;
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
    
}
