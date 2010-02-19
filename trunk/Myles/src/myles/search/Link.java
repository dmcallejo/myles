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
    private int hostingId;
    private String linkURL;

    public Link(int hostingId, String linkURL){
        this.hostingId = hostingId;   // Hosting server´s ID
        this.linkURL = linkURL;       // The proper link's URL value
    }

    // Observadores (getters)
    public int getServerId(){ return hostingId; }
    public String getLinkURL(){ return linkURL; }
}
