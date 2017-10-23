package layout;

import android.support.v4.app.Fragment;

import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/10/23.
 */

public class ViewFragment extends Fragment {
    private String title;

    public void setTitle(int titleID){
        this.title = DoMoment.getRString(titleID);
    }

    public String getTitle(){
        return this.title;
    }
}
