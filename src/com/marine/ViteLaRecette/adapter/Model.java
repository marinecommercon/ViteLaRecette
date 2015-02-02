package com.marine.ViteLaRecette.adapter;

/**
 * Created by Marine on 02/02/2015.
 */
public class Model {

    private String name;
    private boolean selected;


    public Model(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}