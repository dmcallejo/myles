package myles;

import java.util.logging.Level;
import java.util.logging.Logger;
import myles.servers.*;
import myles.config.*;
import myles.search.*;
import java.util.LinkedList;
import org.jdom.Element;

/**
 *
 * @author Juankar
 */
public class Main {

    public static void main(String[] args) {
        try {
            WebUpdate upd = new WebUpdate();
            LinkedList<Element> logins = Config.getLoginInfo();
            Element log = logins.getLast();
            VagosConnection v1 = new VagosConnection(log.getChildText("user"), log.getChildText("encpass"), "1");
            int[] servers = {1, 2};
            System.out.println("Hemos conectado? " + v1.is_connected() + " " + v1.user());
            v1.connect();
            System.out.println("Hemos conectado? " + v1.is_connected() + " " + v1.user());
            SearchResult res = v1.search("prototype", servers);
            System.out.println(res.totalPages()+"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            //v1.extendedSearch(res.searchURL(),res.totalPages(),res.servers());
            System.out.println(v1.extendSearch(res));
            System.out.println(v1.extendSearch(res));
            System.out.println(res.pages());



        } catch (Exception ex) {
            System.out.println(ex);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.exit(0);
        }

    }
}

