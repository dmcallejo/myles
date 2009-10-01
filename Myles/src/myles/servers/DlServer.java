/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myles.servers;

/**
 *
 * @author User
 */
public class DlServer {
    // Atributos
    private int id;
    private String name;
    private String url;

    public DlServer(int id, String name, String url){
        this.id = id;
        this.name = name;
        this.url = url;
    }
    public int get_id(){ return id;  }
    public String get_name(){ return name; }
    public String get_url(){ return url; }
}
