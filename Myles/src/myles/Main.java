/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myles;

import java.security.NoSuchAlgorithmException;
import myles.servers.*;
import myles.utils.*;
import java.util.LinkedList;
import myles.exceptions.*;
import myles.utils.MD5;

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
        try {
            VagosConnection vagos1 = new VagosConnection("myles", MD5.MD5("ojete"), "1");
            System.out.println(vagos1.connect());
            vagos1.search("counter");
            System.out.println(vagos1.disConnect());

        } catch (NotConnectedException e) {
            System.out.println(e);
        } catch (java.net.MalformedURLException e) {
            System.out.println("MalformedURLException");
        } catch (java.io.IOException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (java.lang.Exception e) {
            System.out.println(e);
        }
    }
}
