package io.github.gsantner.memetastic.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "SpellCheckingInspection", "UnusedReturnValue", "JavaDoc", "FieldCanBeLocal"})
public class MemeAssetConfig implements Serializable {

    public static class Config {
        private List<Font> _fonts;
        private List<Image> _images;

        public Config fromJson(JSONObject json) throws JSONException {
            List<Image> imagesList = new ArrayList<>();
            JSONArray jsonArr = json.getJSONArray("images");
            for (int i = 0; i < jsonArr.length(); i++) {
                try {
                    Image element = new Image().fromJson(jsonArr.getJSONObject(i));
                    imagesList.add(element);
                } catch (JSONException ignored) {
                }
            }
            setImages(imagesList);

            List<Font> fontList = new ArrayList<>();
            jsonArr = json.getJSONArray("fonts");
            for (int i = 0; i < jsonArr.length(); i++) {
                try {
                    Font element = new Font().fromJson(jsonArr.getJSONObject(i));
                    fontList.add(element);
                } catch (JSONException ignored) {
                }
            }
            setFonts(fontList);
            return this;
        }

        public JSONObject toJson() throws JSONException {
            JSONObject root = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (Image element : getImages()) {
                jsonArray.put(element.toJson());
            }
            root.put("images", jsonArray);

            jsonArray = new JSONArray();
            for (Font element : getFonts()) {
                jsonArray.put(element.toJson());
            }
            root.put("fonts", jsonArray);

            return root;
        }

        public List<Font> getFonts() {
            if (_fonts.isEmpty()) {
                setFonts(new ArrayList<Font>());
            }
            return _fonts;
        }

        public void setFonts(List<Font> fonts) {
            _fonts = fonts;
        }

        public List<Image> getImages() {
            if (_images.isEmpty()) {
                setImages(new ArrayList<Image>());
            }
            return _images;
        }

        public void setImages(List<Image> images) {
            _images = images;
        }
    }

    public static class Image {
        private List<String> _tags;
        private String _title;
        private String _filename;
        private long _addedAt;
        private long _updatedAt;

        public Image fromJson(JSONObject json) throws JSONException {
            setTitle(json.getString("title"));
            setFilename(json.getString("filename"));
            setAddedAt(json.getLong("added_at"));
            setUpdatedAt(json.optLong("updated_at", 0));

            List<String> tagsList = new ArrayList<>();
            JSONArray tags = json.getJSONArray("tags");
            for (int i = 0; i < tags.length(); i++) {
                tagsList.add(tags.getString(i));
            }
            setTags(tagsList);
            return this;
        }

        public JSONObject toJson() throws JSONException {
            JSONObject root = new JSONObject();
            root.put("title", getTitle());
            root.put("filename", getFilename());
            root.put("added_at", getAddedAt());
            root.put("tags", new JSONArray(getTags()));
            if (getUpdatedAt() != 0) {
                root.put("updated_at", getUpdatedAt());
            }
            return root;
        }


        public String getTitle() {
            if (TextUtils.isEmpty(_title)) {
                setTitle(getFilename());
            }
            return _title;
        }

        public void setTitle(String title) {
            _title = title;
        }

        public String getFilename() {
            return _filename;
        }

        public void setFilename(String filename) {
            _filename = filename;
        }

        public List<String> getTags() {
            if (_tags == null) {
                setTags(new ArrayList<String>());
            }
            return _tags;
        }

        public void setTags(List<String> tags) {
            _tags = tags;
        }

        public long getUpdatedAt() {
            return _updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            _updatedAt = updatedAt;
        }

        public long getAddedAt() {
            if (_addedAt == 0) {
                setAddedAt(System.currentTimeMillis());
            }
            return _addedAt;
        }

        public void setAddedAt(long addedAt) {
            _addedAt = addedAt;
        }
    }

    public static class Font {
        private String _title;
        private String _filename;

        public Font fromJson(JSONObject json) throws JSONException {
            setTitle(json.getString("title"));
            setFilename(json.getString("filename"));
            return this;
        }

        public JSONObject toJson() throws JSONException {
            JSONObject root = new JSONObject();
            root.put("title", getTitle());
            root.put("filename", getFilename());
            return root;
        }

        public String getTitle() {
            if (TextUtils.isEmpty(_title)) {
                setTitle(getFilename());
            }
            return _title;
        }

        public void setTitle(String title) {
            _title = title;
        }

        public String getFilename() {
            return _filename;
        }

        public void setFilename(String filename) {
            _filename = filename;
        }
    }
}
