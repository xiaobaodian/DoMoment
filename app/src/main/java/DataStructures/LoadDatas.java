package DataStructures;

import android.os.AsyncTask;
import android.view.View;

import threecats.zhang.domoment.DoMoment;

/**
 * Created by zhang on 2017/9/1.
 */

public class LoadDatas extends AsyncTask<Void, Task, Boolean> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DoMoment.getDataManger().getTodoFragment().setProgressBarVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(Void... params) {

            //publishProgress(task);
        return true;
    }

    @Override
    protected void onProgressUpdate(Task... values) {
        super.onProgressUpdate(values);
        DoMoment.getDataManger().AddTask(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        DoMoment.getDataManger().getTodoFragment().setProgressBarVisibility(View.GONE);
        DoMoment.getDataManger().getCategoryList().BindDatas();
    }
}
