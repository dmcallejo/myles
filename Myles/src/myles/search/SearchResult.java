package myles.search;

import java.util.LinkedList;

/**
 *
 * @author User
 */
public class SearchResult {

    /**
     * Atributos
     */
    private int numLinks;
    private int numServers;
    private String title;
    private String[] post;
    private LinkedList<LinkPackage> links;
    public int iterator = 0;

    /**
     * Constructores
     * @param title
     * @param post
     */
    public SearchResult(String title, String[] post){
        this.title = title;
        this.post = post;
    }
    public SearchResult(String title){
        this.title = title;
    }
    public SearchResult(){}

    /**
     * Métodos get
     * @return
     */
    public String get_title(){
        return this.title;
    }
    public String[] get_post(){
        return this.post;
    }
    public int get_num_links(){
        return this.numLinks;
    }
    public int get_num_servers(){
        return this.numServers;
    }
    public LinkPackage get_link(int index){
        return links.get(index);
    }

    /**
     * Métodos que facilitan el acceso y recorrido mediante un simple
     * while(clase.hasNext()){ ....clase.next() }
     * @return
     */
    public boolean hasNext(){
        if(links.size() > (iterator + 1)) return true;
        return false;
    }
    public LinkPackage next(){
        iterator++;
        return links.get(iterator-1);
    }
}
