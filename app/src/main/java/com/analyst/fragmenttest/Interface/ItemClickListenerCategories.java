package com.analyst.fragmenttest.Interface;

import com.analyst.fragmenttest.objects.Categories;

public interface ItemClickListenerCategories {

    void OnItemClickCategoryUpdate(int position, Categories categoryData);
    void OnItemClickCategoryDelete(int position, Categories categoryData);
}