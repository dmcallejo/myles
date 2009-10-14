package myles;

import java.util.logging.Level;
import java.util.logging.Logger;
import myles.servers.*;
import myles.config.*;
import java.util.LinkedList;
import org.jdom.Element;
/**
*
* @author Juankar
*/

public class Main {
    public static void main(String[] args){
        try {

            LinkedList<Element> logins = Config.getLoginInfo();
            Element log = logins.getLast();
            VagosConnection v1 = new VagosConnection(log.getChildText("user"),log.getChildText("encpass"), "1");
            int[] servers = {1,2};
            System.out.println("Hemos conectado? "+v1.is_connected()+" "+v1.user());
            v1.connect();
            System.out.println("Hemos conectado? "+v1.is_connected()+" "+v1.user());
            v1.search("prototype",servers);
            

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

