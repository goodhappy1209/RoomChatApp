package co.roomapp.room.activity;

/**
 * Created by manager on 12/16/14.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.roomapp.room.R;

public class ScreenSlidePageFragment3 extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_rapage_3, container, false);

        return rootView;
    }

}
