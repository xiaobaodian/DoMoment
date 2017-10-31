package layout;

import android.support.v4.app.Fragment;

/**
 * Created by zhang on 2017/10/24.
 */

public class TitleFragment extends Fragment {
    private String title;
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
}
