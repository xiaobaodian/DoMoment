package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**

 */
public class TodoMakeOutFragment extends TitleFragment {

    private String title = DoMoment.getRString(R.string.viewgroup_completed_title);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getTitle(){
        return DoMoment.getRString(R.string.viewgroup_completed_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_makeout, container, false);
    }

    public int getTaskCount(){
        return 11;
    }

}
