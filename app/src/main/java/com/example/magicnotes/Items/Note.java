package com.example.magicnotes.Items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.magicnotes.Utils.ListItemConverter;
import java.util.List;

@Entity(tableName = "notes")
public class Note {

    public enum NoteType {
        TEXT, LIST, TIMED
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private NoteType type;
    private String title;
    private String description;
    private String content;

    @TypeConverters(ListItemConverter.class)
    private List<ListItem> listItems;

    private long expirationTimestamp;
    private long creationTimestamp;
    private long editedTimestamp;
    private boolean fixed;
    private boolean isSelected;

    public Note() {}

    //? Text
    public Note(String title, String description, String content) {
        this.type = NoteType.TEXT;
        this.title = title;
        this.description = description;
        this.content = content;
        this.creationTimestamp = System.currentTimeMillis();
        this.editedTimestamp = System.currentTimeMillis();
    }

    //? Lists
    public Note(String title, String description, List<ListItem> listItems) {
        this.type = NoteType.LIST;
        this.title = title;
        this.description = description;
        this.listItems = listItems;
        this.creationTimestamp = System.currentTimeMillis();
        this.editedTimestamp = System.currentTimeMillis();
    }

    //? Timed
    public Note(String title, String description, long expirationTimestamp) {
        this.type = NoteType.TIMED;
        this.title = title;
        this.description = description;
        this.expirationTimestamp = expirationTimestamp;
        this.creationTimestamp = System.currentTimeMillis();
        this.editedTimestamp = System.currentTimeMillis();
    }

    //? Full
    public Note(int id, NoteType type, String title, String description, String content,
                List<ListItem> listItems, long expirationTimestamp, boolean fixed) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.content = content;
        this.listItems = listItems;
        this.expirationTimestamp = expirationTimestamp;
        this.fixed = fixed;
        this.creationTimestamp = System.currentTimeMillis();
        this.editedTimestamp = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NoteType getType() {
        return type;
    }

    public void setType(NoteType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ListItem> getListItems() {
        return listItems;
    }

    public void setListItems(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    public long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getEditedTimestamp() {
        return editedTimestamp;
    }

    public void setEditedTimestamp(long editedTimestamp) {
        this.editedTimestamp = editedTimestamp;
    }

    public void updateEditedTimestamp() {
        this.editedTimestamp = System.currentTimeMillis();
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}