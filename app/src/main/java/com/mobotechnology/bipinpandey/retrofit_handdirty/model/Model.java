package com.mobotechnology.bipinpandey.retrofit_handdirty.model;

/**
 * Created by Achmad SkyDev on 4/11/19.
 */
public class Model {

    private String name;
    private String description;
    private int privacy;

    public Model(String name, String description, int privacy) {
        this.name = name;
        this.description = description;
        this.privacy = privacy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }
}
