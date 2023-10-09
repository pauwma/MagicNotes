package com.example.magicnotes.Items;

import java.util.List;

public class ListItem {
    private String content;
    private List<ListItem> subItems;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ListItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<ListItem> subItems) {
        this.subItems = subItems;
    }
}
