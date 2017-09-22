package io.github.gsantner.memetastic.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "SpellCheckingInspection", "UnusedReturnValue", "JavaDoc", "FieldCanBeLocal"})
public class MemeAssetList implements Serializable {
    private static final List<MemeFont> _fonts = new ArrayList<>();
    private static final List<MemeImage> _images = new ArrayList<>();

    public static List<MemeFont> getFonts() {
        return _fonts;
    }

    public static List<MemeImage> getImages() {
        return _images;
    }

    public static class MemeFont {
        public MemeAssetConfig.Font _font;
        public File _fullPath;
    }

    public static class MemeImage {
        public MemeAssetConfig.Image _image;
        public File _fullPath;
    }
}
