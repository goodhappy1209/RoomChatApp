package co.roomapp.room.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.FragmentTransaction;
import android.content.Intent;
import co.roomapp.room.R;
import co.roomapp.room.application.RoomApplication;
import co.roomapp.room.manager.RAXMPPManager;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAObservingService;
import co.roomapp.room.utils.RAUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Environment;
import android.net.Uri;
import android.provider.MediaStore;
import android.content.pm.ActivityInfo;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by manager on 12/19/14.
 */
public class RARegisterNameActivity extends Activity implements View.OnClickListener, TextWatcher{


    private RARegisterNameActivity self;
    private EditText edtName;
    private Button btnDone;
    private RelativeLayout blackLayout;
    private String fullnumber;
    private Bitmap profilePicture;

    private RARegNameBottomMenuFragment bmfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        self = this;

        this.fullnumber = getIntent().getExtras().getString("fullnumber");

        setContentView(R.layout.activity_raregister_name);

        edtName = (EditText)findViewById(R.id.edtName);
        btnDone = (Button)findViewById(R.id.btnDone);

        blackLayout = (RelativeLayout)findViewById(R.id.regnameBlackLayout);

        bmfragment = (RARegNameBottomMenuFragment) getFragmentManager().findFragmentById(R.id.regnameBottomMenuFrag);
        bmfragment.setParent(this);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.entry, R.animator.exit);
        ft.hide(bmfragment);
        ft.commit();

        btnDone.setOnClickListener(this);
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    //-----------------------------------------------------
    //------------ Button delegate methods --------------
    //-----------------------------------------------------

    @Override
    public void onClick(View view)
    {
        if(view == btnDone)
        {
            editingDone();
        }
        else if(view == edtName)
        {
            editingStart();
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

        System.out.println(charSequence);
        if(charSequence.length() > 0)
        {
            btnDone.setVisibility(View.VISIBLE);
        }
        else
        {
            btnDone.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {


    }

    //-----------------------------------------------------
    //------------ Logic methods --------------
    //-----------------------------------------------------

    public void editingDone()
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(self.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow( edtName.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
        blackLayout.setAlpha(0.3f);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.entry, R.animator.exit);
        ft.show(bmfragment);
        ft.commit();
    }

    public void editingStart()
    {

        blackLayout.setAlpha(0.0f);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.entry, R.animator.exit);
        ft.hide(bmfragment);
        ft.commit();

        InputMethodManager imm = (InputMethodManager)getSystemService(self.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(edtName.getWindowToken(), InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
        /*File folder = new File(Environment.getExternalStorageDirectory() + "/"
                + getResources().getString(R.string.app_name));

        if (!folder.exists()) {
            folder.mkdir();
        }
        final Calendar c = Calendar.getInstance();
        String new_Date = c.get(Calendar.DAY_OF_MONTH) + "-"
                + ((c.get(Calendar.MONTH)) + 1) + "-" + c.get(Calendar.YEAR)
                + " " + c.get(Calendar.HOUR) + "-" + c.get(Calendar.MINUTE)
                + "-" + c.get(Calendar.SECOND);
        String imageUrl = String.format(
                Environment.getExternalStorageDirectory() + "/"
                        + getResources().getString(R.string.app_name)
                        + "/%s.png", "image(" + new_Date + ")");
//        DealershipApplication.setVehicleImage(imageUrl);

        File photo = new File(imageUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("exit", "false");
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));*/
        startActivityForResult(intent, 1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        this.btnDone.setVisibility(View.INVISIBLE);

        blackLayout.setAlpha(0.0f);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.entry, R.animator.exit);
        ft.hide(bmfragment);
        ft.commit();


        if (resultCode == RESULT_OK) {

            Uri targetUri = data.getData();
            if (requestCode == 0) //Select existing photo
            {
                //Do something
                cropImage(targetUri);
            } else if (requestCode == 1) //Take a photo
            {
                //Do something
                cropImage(targetUri);


            }else if (requestCode == 2) //CROP PICTURE
            {

                Bundle extras = data.getExtras();
                this.profilePicture = extras.getParcelable("data");
                postRegister();
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

    public void postRegister(){


        try {
            String im = RAUtils.bitmapToBase64(this.profilePicture);
            JSONObject params = new JSONObject();

            params.put("type","updateProfile");
            params.put("username", RAUtils.decryptObjectForKey(RAConstant.kRAOwnPhoneNumber));
            params.put("authUsername",RAUtils.decryptObjectForKey(RAConstant.kRAOwnPhoneNumber));
            params.put("authPassword",RAUtils.decryptObjectForKey(RAConstant.kRAOwnPassword));
            params.put("name",this.edtName.getText().toString().trim());
            params.put("im",im);
            params.put("culture", Locale.getDefault().getLanguage());

            String jsonParams = params.toString();
            RequestParams param = new RequestParams();
            param.put("param", RAUtils.encryptRemote(jsonParams));
            String url = MessageFormat.format("http://{0}:{1}/updateProfile", RAConstant.HOST, RAConstant.PORT_NODEJS);
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, param,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject res) {

                    try {
                        if(res.getInt("status") == 400){
                            RARegisterNameActivity.this.btnDone.setVisibility(View.VISIBLE);
                            new AlertDialog.Builder(RARegisterNameActivity.this)
                                    .setTitle(R.string.error)
                                    .setMessage(res.getString("message"))
                                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            postRegister();
                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else{

                            RAUtils.saveProfileImage(RARegisterNameActivity.this.profilePicture,"Photo");

                            String name = RARegisterNameActivity.this.edtName.getText().toString().trim();
                            String[] arrayname = name.split(" ");
                            if(arrayname.length>0){
                                RAUtils.encryptObjectForKey(RAConstant.kRAOwnFirstName,arrayname[0].trim());
                            }else{
                                RAUtils.encryptObjectForKey(RAConstant.kRAOwnFirstName,name.trim());
                            }
                            RAUtils.encryptObjectForKey(RAConstant.kRAOwnName,name.trim());

                            Toast.makeText(RARegisterNameActivity.this,"Registration Done",Toast.LENGTH_LONG).show();

                            RAObservingService.getInstance().addObserver(RAConstant.kXMPPAuthenticationObserver,RoomApplication.getInstance());

                            RAXMPPManager.getInstance().setupStream();
                            RAXMPPManager.getInstance().connect();


                        }
                    } catch (Exception e) {

                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject res) {

                    try {
                        RARegisterNameActivity.this.btnDone.setVisibility(View.VISIBLE);
                        new AlertDialog.Builder(RARegisterNameActivity.this)
                                .setTitle(R.string.error)
                                .setMessage(e.getLocalizedMessage())
                                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        postRegister();
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } catch (Exception e1) {

                    }
                }
                @Override
                public void onFinish() {

                }
            });

        } catch (JSONException e) {

        } catch (UnsupportedEncodingException e) {

        } catch (Exception e) {

        }



    }

}
