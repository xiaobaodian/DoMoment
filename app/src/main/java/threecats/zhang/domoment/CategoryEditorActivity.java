package threecats.zhang.domoment;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import DataStructures.CategoryBase;
import DataStructures.CustomCategory;
import adapter.CategoryBackgroupAdapter;

import static threecats.zhang.domoment.DoMoment.getContext;

public class CategoryEditorActivity extends AppCompatActivity {

    private TextInputEditText categoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_editor);
        categoryTitle = (TextInputEditText)findViewById(R.id.CategoryTitle);
        categoryTitle.setText(DoMoment.getCurrentCategory().getTitle());
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RecyclerView);
        CategoryBackgroupAdapter backgroupAdapter = new CategoryBackgroupAdapter(DoMoment.getDataManger().getCategoryList().getCategoryThemebackground());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(backgroupAdapter);
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CategoryBase currentCategory = DoMoment.getCurrentCategory();
        if (currentCategory.getCategoryID() >= 10) {
            DoMoment.getDataManger().UpdateCustomCategory((CustomCategory)currentCategory);
        }
    }

}
