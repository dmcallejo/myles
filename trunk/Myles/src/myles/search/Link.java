/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myles.search;

/**
 *
 * @author User
 */
public class Link {
    // Atributos
    private int serverId;
    private String linkURL;

    public Link(int serverId, String linkURL){
        this.serverId = serverId;
        this.linkURL = linkURL;
    }

    // Observadores (getters)
    public int getServerId(){ return serverId; }
    public String getLinkURL(){ return linkURL; }
}
