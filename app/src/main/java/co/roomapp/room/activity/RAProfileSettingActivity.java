package co.roomapp.room.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;

import co.roomapp.room.R;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAUtils;
import co.roomapp.room.widget.RARoundImageView;

import android.widget.ImageView;
import android.widget.EditText;
import android.graphics.Bitmap;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by a on 2/13/15.
 */
public class RAProfileSettingActivity extends Activity implements View.OnClickListener, TextWatcher{

    private ImageButton btnBack;
    private ImageView bgPhoto;
    private RARoundImageView userPhoto;
    private ImageButton btnTakeSelfie;
    private EditText edtName;
    private ImageButton btnOk;
    private RARegNameBottomMenuFragment actionMenu;
    private Bitmap photo;

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_raprofilesetting);

        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnTakeSelfie = (ImageButton)findViewById(R.id.btnTakeSelfie);
        btnOk = (ImageButton)findViewById(R.id.btnOk);
        bgPhoto = (ImageView)findViewById(R.id.bgPhoto);
        userPhoto = (RARoundImageView)findViewById(R.id.userPhoto);
        edtName = (EditText)findViewById(R.id.edtName);

        btnBack.setOnClickListener(this);
        btnTakeSelfie.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        edtName.setOnClickListener(this);

        actionMenu = (RARegNameBottomMenuFragment) getFragmentManager().findFragmentById(R.id.actionMenu);
        actionMenu.setParent(this);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.entry, R.animator.exit);
        ft.hide(actionMenu);
        ft.commit();

        photo = RAUtils.loadProfileImageURL("photo");
        String name = RAUtils.decryptObjectForKey(RAConstant.kRAOwnName);
        if(photo!=null) {
            Bitmap scaledImage = Bitmap.createScaledBitmap(photo, photo.getWidth()/3, photo.getHeight()/3, false);
            Bitmap blurImage = Bitmap.createScaledBitmap(scaledImage, photo.getWidth(), photo.getHeight(), false);
            bgPhoto.setImageBitmap(blurImage);
            userPhoto.setImageBitmap(photo);
        }
        if(name != null) edtName.setText(name);

        edtName.addTextChangedListener(this);
        edtName.setOnClickListener(this);
        edtName.clearFocus();
        edtName.requestFocus();
        edtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v)
    {
        if(v.getClass() == ImageButton.class)
        {
            if((ImageButton)v == btnBack)
            {
                finish();
            }
            else if((ImageButton)v == btnTakeSelfie)
            {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.entry, R.animator.exit);
                ft.show(actionMenu);
                ft.commit();
                btnOk.setVisibility(View.GONE);
            }
            else if((ImageButton)v == btnOk)
            {
                //Save changes and finish activity
                if(photo!=null) RAUtils.saveProfileImage(photo, "Photo");
                String name = edtName.getText().toString();
                try
                {
                    RAUtils.encryptObjectForKey(RAConstant.kRAOwnName, name);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                finish();
            }
        }
        else if(v.getClass() == EditText.class)
        {

        }
    }

    //-----------------------------------------------------
    //------------ EditText delegate methods --------------
    //-----------------------------------------------------

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void selectPhoto()
    {
        Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser( intent, "Select Picture" ), 0);
    }

    public void takePhoto()
    {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    public void hideMenu()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.entry, R.animator.exit);
        ft.hide(actionMenu);
        ft.commit();
        btnOk.setVisibility(View.VISIBLE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        hideMenu();

        if (resultCode == RESULT_OK) {

            Uri targetUri = data.getData();
            if (requestCode == 0) //Select existing photo
            {
                //Do something
                cropImage(targetUri);
            }
            else if (requestCode == 1) //Take a photo
            {
                //Do something
                cropImage(targetUri);
            }
            else if (requestCode == 2) //CROP PICTURE
            {
                Bundle extras = data.getExtras();
                photo = extras.getParcelable("data");
                userPhoto.setImageBitmap(photo);
            }
        }
    }

    private void cropImage(Uri targetUri){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(targetUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 640);
            cropIntent.putExtra("outputY", 640);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 2);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
