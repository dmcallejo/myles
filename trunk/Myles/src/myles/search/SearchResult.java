/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myles.search;

import java.util.LinkedList;
import java.util.Iterator;
import myles.config.Config;
import myles.servers.DlServer;

/**
 *  Clase que aporta el resultado de búsqueda.
 * @author diego
 */
public class SearchResult {

    private String name;
    private String searchQuery;         // The text the user was looking for
    private String searchURL;           // The URL composed for the search
    private int[] servers;              // An array of server id's allowed to
                                        // be processed and shown.
    private String date;                // The date of the Search ¿?¿?
    private LinkedList<Result> results; // A list with all the results
    private int pages;                  // Number of result pages
    private int totalPages;             // Total result pages
    private int totalResults;           // Total results

    /**
     * Only class constructor, required params:
     * @param searchQuery
     * @param searchURL
     * @param servers
     * @param results
     * @param totalResults
     * @param totalPages
     */
    public SearchResult(String searchQuery, String searchURL, int[] servers, LinkedList<Result> results, int totalResults, int totalPages) {
        this.searchQuery = searchQuery;
        this.servers = servers;
        this.searchURL=searchURL;
        this.results = results;
        java.util.Date Date = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("k:mm dd/MM/yyyy");
        this.date = sdf.format(date);
        this.totalPages=totalPages;
        this.date = sdf.format(Date);
        this.totalPages=totalPages;
        this.totalResults=totalResults;
        this.pages=1;
    }

    /**
     * Permite añadir resultados despues de construida la clase.
     * @param r
     */
    public void addResult(Result r) {
        results.add(r);
    }

    /**
     * Añade una lista a los resultados. Para Extended Search
     * @param list
     */
    public void addResultList(LinkedList<Result> list, int newPages){
        this.results.addAll(list);
        this.pages+=newPages;
    }

    /**
     * Parsea el html del post 
     * 
     * @param htmlCode
     * @param name
     * @param servers
     * @return
     */
    public static LinkedList<Result> parseLinks(String htmlCode, String title, int[] servers, String url) {
        System.out.println("Comienza el parseo de links...");                   //@Debug
        LinkedList<String> uniqueComparing;
        String auxUrl;
        /**
         * Vamos a cambiar todas las ocurrencias de caracteres que puedan ser de formato y no de
         * información por un caracter poco usado y reconocible.
         */
        htmlCode = htmlCode.replaceAll(" ", "þ");
        htmlCode = htmlCode.replaceAll(">", "þ");
        htmlCode = htmlCode.replaceAll("<", "þ");
        htmlCode = htmlCode.replaceAll("\"", "þ");
        htmlCode = htmlCode.replaceAll("'", "þ");
        htmlCode = htmlCode.replaceAll("\n", "þ");
        LinkedList<Result> results = new LinkedList();
            // This results will store the downloaded results.
        LinkedList<DlServer> pivotServers = Config.getDlServers();
        int h = 999;    // Máx. level, just arbitrary

        /**
         * Check servers included in the result filter
         */
        if (servers[0] == 0) {
            servers = null;
            System.out.println("La búsqueda se hará a todos los servidores. "+servers);     // @Debug
        } else {
            h=servers.length;
        }

        int k = 0;
        Iterator downloadServerIterator = pivotServers.iterator();
        DlServer cur_dl_server;
        String[] sliced_html;
        String cur_url;
        while (k<h & downloadServerIterator.hasNext()) {
            uniqueComparing = new LinkedList<String>();
            cur_dl_server = (DlServer) downloadServerIterator.next();
            if (servers==null || cur_dl_server.get_id() == servers[k]) {
                Result tResult = new Result(title,cur_dl_server.get_id(),url);
                sliced_html = htmlCode.split(cur_dl_server.get_url());
                for (int i = 1; i < sliced_html.length; i++) {
                    /**
                     * Si el enlace contiene los típicos ... de un enlace abreviado,
                     * no lo utilizaremos.
                     */
                    if (!sliced_html[i].contains("...")) {
                        cur_url = sliced_html[i];
                        cur_url = cur_url.split("þ")[0];
                        auxUrl = "http://" + cur_dl_server.get_url() + cur_url;
                        if(!uniqueComparing.contains(auxUrl)){
                            System.out.println(auxUrl);      //Debug
                            tResult.addLink(auxUrl);
                            uniqueComparing.add(auxUrl);
                        }
                        
                    }
                }


                /*
                 * Una vez añadidos los enlaces al Result temporal, se añade al
                 * LL de Results que se retornará y se incrementa el iterador.
                 */
                System.out.println("Total links: "+tResult.getNumLinks()+"\n");

                results.add(tResult);
                k++;
            }


        }

        return results;
    }

    /*
     * Métodos observadores
     */
    public String date() {
        return this.date;
    }

    public String searchQuery() {
        return this.searchQuery;
    }

    public int[] servers() {
        return this.servers;
    }

    public LinkedList<Result> results() {
        return this.results;
    }

    public int numResults() {
        return this.results.size();
    }

    public int totalResults(){
        return this.totalResults;
    }

    public int pages(){
        return this.pages;
    }

    public int totalPages(){
        return this.totalPages;
    }

    public String searchURL(){
        return this.searchURL;
    }
}
