package io.github.gsantner.memetastic.data;

import android.graphics.Typeface;

import java.io.File;

/**
 * Contains the path and the name of a data
 */
public class MemeFont {
    private final String filePath;
    private final String fontName;
    private Typeface font;

    /**
     * Constructor that takes and saves the filepath to the data and the data itself
     *
     * @param filePath path where the data is saved
     * @param font     the platform specific data type for the data at the path
     */
    public MemeFont(String filePath, Typeface font) {
        this.font = font;
        this.filePath = filePath;

        // Extract fontName
        String name = filePath;
        int sep = name.lastIndexOf(File.separator);
        name = name.substring(sep + 1);
        sep = name.lastIndexOf(".");
        fontName = name.substring(0, sep);

    }


    public String getFontName() {
        return fontName;
    }

    public Typeface getFont() {
        return font;
    }
}