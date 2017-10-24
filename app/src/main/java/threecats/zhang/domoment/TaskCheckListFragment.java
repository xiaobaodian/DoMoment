package threecats.zhang.domoment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import layout.TitleFragment;
import layout.ViewFragment;

public class TaskCheckListFragment extends TitleFragment {

    @Override
    public String getTitle(){
        return "检查表";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_checklist, container, false);
    }

}
