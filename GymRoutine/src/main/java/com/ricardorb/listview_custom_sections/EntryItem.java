package com.ricardorb.listview_custom_sections;


public class EntryItem implements Item {

    public final String title;
    public final String subtitle;

    public EntryItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    public boolean isSection() {
        return false;
    }

}
