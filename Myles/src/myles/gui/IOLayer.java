package myles.gui;

import myles.search.*;

/**
 *
 * @author Ender
 */
public interface IOLayer {
    /**
     * Notification to the layer; a new result has been produced. The layer
     * should update.
     * @param newResult the new result to be added
     * @param parentSearch the search result it is related to.
     */
    public void addResult(Result newResult, SearchResult parentSearch);
    /**
     * Notification to the layer: a search has finished. All its results have
     * been processed.
     * @param finishedSearch
     */
    public void searchFinished(SearchResult finishedSearch);
    /**
     * Notification to the layer: the search is stopped and results will be no 
     * longer be served.
     * @param stoppedSearch
     */
    public void searchStopped(SearchResult stoppedSearch);


}
