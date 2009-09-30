package myles.search;
import myles.search.Link;
import java.util.LinkedList;

public class Result {
    // Atributos
    private String nombre;
    private String server;
    private int numLinks;
    private String fecha;
    private LinkedList<Link> links;
    private String URL;

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
    public String getServer(){ return server; }
    public int getNumLinks(){ return numLinks; }
    public String getFecha(){ return fecha; }
    public String getURL(){ return URL; }
    public Link getLink(int index){ return links.get(index); }
    public LinkedList<Link> getLinks(){ return links; }
    
}
