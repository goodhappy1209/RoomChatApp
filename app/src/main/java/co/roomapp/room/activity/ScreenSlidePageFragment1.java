package co.roomapp.room.activity;

/**
 * Created by manager on 12/16/14.
 */
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import co.roomapp.room.R;

public class ScreenSlidePageFragment1 extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_rapage_1, container, false);

        return rootView;
    }

}
