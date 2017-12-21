package threecats.zhang.domoment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 由 zhang 于 2017/7/25 创建
 */

public class FocusFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_focus, container, false);

        Button button = view.findViewById(R.id.buttonFocus);
        button.setOnClickListener(view1 -> App.self().getMainActivity().showNavigation());

        return view;
    }


}
