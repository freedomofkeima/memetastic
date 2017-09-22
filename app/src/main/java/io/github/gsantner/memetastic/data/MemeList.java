package io.github.gsantner.memetastic.data;

import net.gsantner.opoc.util.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MemeList implements MemeOriginInterface {
    private final static String MEMETASTIC_CONFIG_FILE = ".memetastic.conf.json";
    private final static String[] MEMETASTIC_IMAGES_EXTS = {"png", "jpg", "jpeg"};

    private boolean mIsTemplate;
    private File[] mFiles;
    private String mFilePath;
    private String mThumbnailPath;
    private Map<String, String> mMissingThumbnails = new HashMap<>();
    private int mLength;

    public static void loadFromFolder(File folder) {
        if (!folder.exists() && !folder.mkdirs()) {
            return;
        }
        File configFile = new File(folder, MEMETASTIC_CONFIG_FILE);
        if (configFile.exists()) {
            try {
                String contents = FileUtils.readTextFile(configFile);
                JSONObject json = new JSONObject(contents);
                MemeConfig.Config list = new MemeConfig.Config().fromJson(json);
            } catch (JSONException ignored) {
            }
        }
    }

    /**
     * Constructor that takes the path to the created memes and path to the thumbnail folder of the created memes
     *
     * @param folderPathContainingPics path to the already created memes
     * @param subfolderThumbnails      path to the thumbnails of the created memes
     */
    public MemeList(File folderPathContainingPics, final String subfolderThumbnails) {
        mFilePath = folderPathContainingPics.getAbsolutePath();
        mFiles = folderPathContainingPics.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                File thumbnailFile = new File(new File(dir, subfolderThumbnails), filename);
                File memeFile = new File(dir, filename);
                boolean ok = thumbnailFile.isFile() && memeFile.isFile();
                if (memeFile.isFile() && !thumbnailFile.exists()) {
                    mMissingThumbnails.put(memeFile.getAbsolutePath(), thumbnailFile.getAbsolutePath());
                }

                return ok;
            }
        });
        Arrays.sort(mFiles, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return (int) (f2.lastModified() - f1.lastModified());
            }
        });
        mThumbnailPath = subfolderThumbnails;
        mLength = mFiles.length;
    }

    @Override
    public String getPath(int position, boolean bThumbnail) {
        if (bThumbnail)
            return getThumbnailPath(position);
        return getFilepath(position);
    }

    @Override
    public int getLength() {
        return mLength;
    }

    @Override
    public boolean isAsset() {
        return false;
    }

    @Override
    public String getFilepath(int position) {
        return mFilePath + File.separator + mFiles[position].getName();
    }

    @Override
    public String getThumbnailPath(int position) {
        return mFilePath + File.separator + mThumbnailPath + File.separator + mFiles[position].getName();
    }

    @Override
    public boolean showFavButton() {
        return false;
    }

    @Override
    public void shuffleList() {
        List<File> l = new ArrayList<>(Arrays.asList(mFiles));
        Collections.shuffle(l);
        mFiles = l.toArray(new File[l.size()]);
    }

    @Override
    public boolean isTemplate() {
        return mIsTemplate;
    }

    public void setIsTemplate(boolean isTemplate) {
        this.mIsTemplate = isTemplate;
    }

    public Map<String, String> getMissingThumbnails() {
        return mMissingThumbnails;
    }
}