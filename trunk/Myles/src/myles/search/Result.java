package myles.search;
import java.util.LinkedList;
/**
 * La clase result, que engloba un número de clases Link.
 * Representa una página de resultados dentro de una búsqueda, con sus
 * enlaces almacenados como Links.
 * @author Ender
 */
public class Result {
    // Atributos
    private String searchQuery;
        // The search query
    private int serverId;
        // The Search Server ID relative to Myles.servers
    private String date;
        // The date of the post, if possible
    private LinkedList<Link> links = new LinkedList();
        // A list which encapsulates all the links in the search page.
    private String URL;
        // The URL of the Search Server's search results page.
    private String Server;
        // The name of the Search Server ¿?¿?

    /**
     * Class constructor given 3 params:
     * @param busqueda the text in the search query
     * @param serverId Search Server's ID
     * @param URL the search results URL.
     */
    public Result(String searchQuery, int serverId, String URL){
        this.searchQuery=searchQuery;
        this.serverId = serverId;
        this.URL = URL;
        this.date = "No disponible";
    }
    /**
     * Class constructor given 4 params
     * @param busqueda the text in the search query
     * @param serverId Search Server's ID
     * @param URL the search results URL
     * @param fecha the date of the search result
     */
    public Result(String searchQuery, int serverId, String URL, String date){
        this.searchQuery=searchQuery;
        this.serverId = serverId;
        this.URL = URL;
        this.date = date;
    }

    /**
     * Adds a new link to the list.
     * @param linkURL
     */
    public void addLink(String linkURL){
        links.add(new Link(this.serverId, linkURL));
    }


    // Observadores (getters)
    public String getServer(){ return Server; }
    public int getNumLinks(){ return links.size(); }
    public String getFecha(){ return date; }
    public String getURL(){ return URL; }
    public Link getLink(int index){ return links.get(index); }
    public LinkedList<Link> getLinks(){ return links; }
    public void setLinks(LinkedList<Link> new_links){ this.links = new_links; }

    
}
