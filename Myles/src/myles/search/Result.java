package myles.search;
import java.util.LinkedList;

public class Result {
    // Atributos
    private String nombre;
    private int serverId;
    private int type; //tipo??
    private String fecha;
    private LinkedList<Link> links = new LinkedList();
    private String URL;
    private String Server;

        public Result(String nombre, int serverId, String URL){
        this.nombre=nombre;
        this.serverId = serverId;
        this.URL = URL;
        this.fecha = "No disponible";
    }
    public Result(String nombre, int serverId, String URL, String fecha){
        this.nombre=nombre;
        this.serverId = serverId;
        this.URL = URL;
        this.fecha = fecha;
    }

    public void addLink(String linkURL){
        links.add(new Link(this.serverId, linkURL));
    }


    // Observadores (getters)
    public String getServer(){ return Server; }
    public int getNumLinks(){ return links.size(); }
    public String getFecha(){ return fecha; }
    public String getURL(){ return URL; }
    public Link getLink(int index){ return links.get(index); }
    public LinkedList<Link> getLinks(){ return links; }
    public void setLinks(LinkedList<Link> new_links){ this.links = new_links; }

    
}
