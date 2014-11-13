package com.example.william.courserater;

/**
 * Created by William on 2014-11-01.
 */
public class ItemRating {

    private String label;
    private Float value;

    public ItemRating(String label, Float value){
        super();
        this.label = label;
        this.value = value;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }


}
