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
    private int[] servers;
    private String date;
    private LinkedList<Result> results;

    public SearchResult(String name, int[] servers, LinkedList<Result> results) {
        this.name = name;
        this.servers = servers;
        this.results = results;
        java.util.Date date = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("k:mm dd/MM/yyyy");
        this.date = sdf.format(date);
    }

    /**
     * Permite añadir resultados despues de construida la clase.
     * @param r
     */
    public void addResult(Result r) {
        results.add(r);
    }

    /**
     * Parsea el html del post 
     * 
     * @param html_code
     * @param name
     * @param servers
     * @return
     */
    public static LinkedList<Result> parseLinks(String html_code, String title, int[] servers, String url) {
        System.out.println("Comienza el parseo de links...");
        LinkedList<String> cmp_unique;
        String aux_url;
        html_code = html_code.replaceAll(" ", "þ");
        html_code = html_code.replaceAll(">", "þ");
        html_code = html_code.replaceAll("<", "þ");
        html_code = html_code.replaceAll("\"", "þ");
        html_code = html_code.replaceAll("'", "þ");
        html_code = html_code.replaceAll("\n", "þ");
        LinkedList<Result> results = new LinkedList();
        LinkedList<DlServer> pivot_servers = Config.getDlServers();
        int h = 9999;
        if (servers[0] == 0) {
            servers = null;
            System.out.println("La búsqueda se hará a todos los servidores. "+servers);
        } else {
            h=servers.length;
        }
        int k = 0;
        Iterator dl_server_iterator = pivot_servers.iterator();
        DlServer cur_dl_server;
        String[] sliced_html;
        String cur_url;
        while (k<h & dl_server_iterator.hasNext()) {
            cmp_unique = new LinkedList<String>();
            cur_dl_server = (DlServer) dl_server_iterator.next();
            if (servers==null || cur_dl_server.get_id() == servers[k]) {
                Result tResult = new Result(title,cur_dl_server.get_id(),url);
                sliced_html = html_code.split(cur_dl_server.get_url());
                for (int i = 1; i < sliced_html.length; i++) {
                    /**
                     * Si el enlace contiene los típicos ... de un enlace abreviado,
                     * no lo utilizaremos.
                     */
                    if (!sliced_html[i].contains("...")) {
                        cur_url = sliced_html[i];
                        cur_url = cur_url.split("þ")[0];
                        aux_url = "http://" + cur_dl_server.get_url() + cur_url;
                        if(!cmp_unique.contains(aux_url)){
                            System.out.println(aux_url);      //Debug
                            tResult.addLink(aux_url);
                            cmp_unique.add(aux_url);
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

    public String name() {
        return this.name;
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
}
