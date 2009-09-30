/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myles;

import java.security.NoSuchAlgorithmException;
import myles.servers.*;
import myles.utils.*;
import java.util.LinkedList;
/**
 *
 * @author User
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println("Conexión...");
        VagosConnection vagos1 = new VagosConnection("ju4nk4r", "ninguna");
        try {
                System.out.println(vagos1.connect());
                System.out.println(vagos1.disConnect());







            /*System.out.println(vagos1.Connect() + "\nDesconexión...");
            System.out.println(vagos1.disConnect());*/
            //System.out.println("\n\n"+HttpUtils.getPage("http://www.vagos.es", null, "www.vagos.es"));







        }/* catch (java.net.MalformedURLException e) {
            System.out.println("MalformedURLException");
        } catch (java.io.IOException e) {
            System.out.println(e.getLocalizedMessage());
        }*/
        catch (java.lang.Exception e) {
            System.out.println(e);
        }
    }
}
