package myles.servers;

import myles.search.Search;

/**
 *
 * @author User
 */
public interface Connection {
    public int get_server_id();
    public boolean Connect() throws Exception;
    public Search search(String search_query);

}
