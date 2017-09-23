package io.github.gsantner.memetastic.data;

/**
 * Interface that contains all necessary methods to access memes from different sources
 */
public interface MemeOriginInterface {

    public String getPath(int position, boolean bThumbnail);

    /**
     * gets and returns the number of available images
     *
     * @return the number of available images
     */
    public int getLength();

    /**
     * indicates if the data is saved in the assets or somewhere else
     *
     * @return true if the data is saved in the assets or somewhere else
     */
    public boolean isAsset();

    /**
     * creates and returns a valid path to a specific data
     *
     * @param position the position in the array/list of available images
     * @return the path to the data at the specified position
     */
    public String getFilepath(int position);

    /**
     * creates and returns a valid path to a specific thumbnail of an data
     *
     * @param position the position in the array/list of available images
     * @return the path to the thumbnail of the data at the specified position
     */
    public String getThumbnailPath(int position);

    /**
     * indicates the data should be able to be set as a favorite or not
     *
     * @return true if the data is favoritable, else false
     */
    public boolean showFavButton();

    /**
     * Shuffle the list of items
     */
    public void shuffleList();

    /**
     * Tell if the data is to be used as template
     *
     * @return true if is template
     */
    public boolean isTemplate();
}
