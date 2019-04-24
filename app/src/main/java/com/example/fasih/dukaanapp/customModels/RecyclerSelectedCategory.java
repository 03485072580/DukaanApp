package com.example.fasih.dukaanapp.customModels;

/**
 * Created by Fasih on 04/02/19.
 */

public class RecyclerSelectedCategory {
    private int categoryImageResource;
    private String categoryTvText;

    public RecyclerSelectedCategory(int categoryImageResource, String categoryTvText) {
        this.categoryImageResource = categoryImageResource;
        this.categoryTvText = categoryTvText;
    }

    public int getCategoryImageResource() {
        return categoryImageResource;
    }

    public void setCategoryImageResource(int categoryImageResource) {
        this.categoryImageResource = categoryImageResource;
    }

    public String getCategoryTvText() {
        return categoryTvText;
    }

    public void setCategoryTvText(String categoryTvText) {
        this.categoryTvText = categoryTvText;
    }
}
