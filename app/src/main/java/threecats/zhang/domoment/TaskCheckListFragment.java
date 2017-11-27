package threecats.zhang.domoment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import threecats.zhang.domoment.DataStructures.Task;
import threecats.zhang.domoment.layout.TitleFragment;

public class TaskCheckListFragment extends TitleFragment {
    private Task task = DoMoment.getDataManger().getEditorTask();

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
