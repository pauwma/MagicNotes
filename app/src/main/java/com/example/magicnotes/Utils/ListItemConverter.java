package com.example.magicnotes.Utils;

import androidx.room.TypeConverter;
import com.example.magicnotes.Items.ListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class ListItemConverter {

    @TypeConverter
    public static List<ListItem> fromString(String value) {
        Type listType = new TypeToken<List<ListItem>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<ListItem> list) {
        return new Gson().toJson(list);
    }
}