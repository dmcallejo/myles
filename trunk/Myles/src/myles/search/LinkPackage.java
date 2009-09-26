/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myles.search;
import java.util.LinkedList;
/**
 *
 * @author User
 */
public class LinkPackage{

    private String server;
    private int server_id;
    private LinkedList<String> links;
    private int iterator = 0;

    public void add_link(String new_link){
        links.add(new_link);
    }
    public String get_link(int index){
        return links.get(index);
    }
    public int get_num_links(){
        return links.size();
    }
    public String get_server(){
        return this.server;
    }
    public int get_server_id(){
        return this.server_id;
    }
    public void flush(){
        this.links = null;
    }

    /**
     * Métodos que facilitan el acceso y recorrido mediante un simple
     * while(clase.hasNext()){ ....clase.next() }
     * @return
     */
    public boolean hasNext(){
        if(links.size() > iterator+1) return true;
        return false;
    }
    public String next(){
        iterator++;
        return links.get(iterator-1);
    }
}
