package com.analyst.fragmenttest.objects;

public class DiagramValues {
    private String categoryName;
    private float categoryPercentage;

    public DiagramValues(String name, float value){
        this.categoryName = name;
        this.categoryPercentage = value;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public float getTotalCategoryValue() {
        return categoryPercentage;
    }

    public void setCategoryPercentage(float totalCategoryValue) {
        this.categoryPercentage = totalCategoryValue;
    }
}
