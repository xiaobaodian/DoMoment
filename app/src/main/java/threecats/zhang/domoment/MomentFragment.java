package threecats.zhang.domoment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by zhang on 2017/7/25.
 */

public class MomentFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_moment, container, false);

        Button button = view.findViewById(R.id.buttonMoment);
        button.setOnClickListener(view1 -> App.getMainActivity().showNavigation());

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("test",1);
        Toast.makeText(getActivity(), "onSaveInstanceState", Toast.LENGTH_SHORT).show();
    }

}
