package io.github.gsantner.memetastic.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "SpellCheckingInspection", "UnusedReturnValue", "JavaDoc", "FieldCanBeLocal"})
public class MemeData implements Serializable {
    private static final List<Font> _fonts = new ArrayList<>();
    private static final List<Image> _images = new ArrayList<>();
    private static final List<Image> _createdMemes = new ArrayList<>();
    private static final HashMap<String, List<Image>> _imagesWithTags = new HashMap<>();

    public static List<Font> getFonts() {
        return _fonts;
    }

    public static List<Image> getImages() {
        return _images;
    }

    public static List<Image> getCreatedMemes() {
        for (Image image : _createdMemes) {
            image.isTemplate = false;
        }
        return _createdMemes;
    }

    public static void clearImagesWithTags() {
        _imagesWithTags.clear();
    }

    public static Image findImage(File filePath) {
        for (Image img : _images) {
            if (img.fullPath.equals(filePath)) {
                return img;
            }
        }
        return null;
    }

    public static synchronized List<Image> getImagesWithTag(String tag) {
        if (_imagesWithTags.containsKey(tag)) {
            return _imagesWithTags.get(tag);
        }
        boolean isOtherTag = tag.equals("other");
        List<Image> newlist = new ArrayList<>();
        for (Image image : getImages()) {
            for (String imgTag : image.data.getTags()) {
                if (imgTag.equals(tag)) {
                    newlist.add(image);
                    break;
                }
            }
            if (isOtherTag && image.data.getTags().isEmpty()) {
                newlist.add(image);
            }
        }
        _imagesWithTags.put(tag, newlist);
        return newlist;
    }

    public static class Font {
        public MemeConfig.Font data;
        public File fullPath;
    }

    public static class Image {
        public MemeConfig.Image data;
        public File fullPath;
        public boolean isTemplate;
    }
}
