package co.roomapp.room.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.Fragment;
import co.roomapp.room.R;
import co.roomapp.room.widget.RAInputAccessoryView;

/**
 * Created by a on 2/5/15.
 */
public class RATimelinePostMediaFragment extends Fragment implements View.OnClickListener{

    private RAInputAccessoryView delegate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.input_accessory_take_media, container, false);
        Button b1 =(Button)view.findViewById(R.id.btnTakePhoto);
        Button b2 =(Button)view.findViewById(R.id.btnTakeVideo);
        Button b3 =(Button)view.findViewById(R.id.btnCancel);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);

        return view;
    }

    public void setParent(RAInputAccessoryView _delegate)
    {
        delegate = _delegate;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnTakePhoto:
                delegate.takePhoto();
                break;
            case R.id.btnTakeVideo:
                delegate.takeVideo();
                break;
            case R.id.btnCancel:
                delegate.hideMediaActionBar();
                break;
        }
    }
}
