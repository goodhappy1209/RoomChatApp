package co.roomapp.room.activity;

/**
 * Created by manager on 12/19/14.
 */

import android.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import co.roomapp.room.R;
import android.app.Activity;

public class RARegNameBottomMenuFragment extends Fragment implements View.OnClickListener {

    private Activity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_raregister_name_bottom_menu, container,false);
        Button b1 =(Button)view.findViewById(R.id.button1);
        Button b2 =(Button)view.findViewById(R.id.button2);
        Button b3 =(Button)view.findViewById(R.id.button3);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);

        return view;
    }

    public void setParent(Activity parent)
    {
        parentActivity = parent;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button1:
                if(parentActivity.getClass() == RARegisterNameActivity.class)
                    ((RARegisterNameActivity)parentActivity).takePhoto();
                if(parentActivity.getClass() == RAProfileSettingActivity.class)
                    ((RAProfileSettingActivity)parentActivity).takePhoto();
                break;
            case R.id.button2:
                if(parentActivity.getClass() == RARegisterNameActivity.class)
                    ((RARegisterNameActivity)parentActivity).selectPhoto();
                if(parentActivity.getClass() == RAProfileSettingActivity.class)
                    ((RAProfileSettingActivity)parentActivity).selectPhoto();
                break;
            case R.id.button3:
                if(parentActivity.getClass() == RARegisterNameActivity.class)
                    ((RARegisterNameActivity)parentActivity).editingStart();
                if(parentActivity.getClass() == RAProfileSettingActivity.class)
                    ((RAProfileSettingActivity)parentActivity).hideMenu();
                break;
        }
    }
}