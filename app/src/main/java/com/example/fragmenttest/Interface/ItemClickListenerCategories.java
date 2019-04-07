package com.example.fragmenttest.Interface;

import com.example.fragmenttest.objects.Categories;

public interface ItemClickListenerCategories {

    void OnItemClickCategoryUpdate(int position, Categories categoryData);
    void OnItemClickCategoryDelete(int position, Categories categoryData);
}
