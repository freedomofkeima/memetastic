package io.github.gsantner.memetastic.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "SpellCheckingInspection", "UnusedReturnValue", "JavaDoc", "FieldCanBeLocal"})
public class MemeData implements Serializable {
    private static final List<Font> _fonts = new ArrayList<>();
    private static final List<Image> _images = new ArrayList<>();

    public static List<Font> getFonts() {
        return _fonts;
    }

    public static List<Image> getImages() {
        return _images;
    }

    public static class Font {
        public MemeConfig.Font font;
        public File fullPath;
    }

    public static class Image {
        public MemeConfig.Image image;
        public File fullPath;
        public boolean isTemplate;
    }
}
