package co.roomapp.room.activity;

/**
 * Created by manager on 12/19/14.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;

import co.roomapp.room.R;
import co.roomapp.room.application.RoomApplication;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAUtils;

public class RARegisterCodeActivity extends Activity implements View.OnClickListener, TextWatcher {

    private RARegisterCodeActivity self;
    private EditText edtVerificationCode;
    private Button btnResendCode;
    private Button btnNext;
    private ImageButton btnBack;
    private String fullnumber;
    private TextView textSentTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        self = this;

        this.fullnumber = getIntent().getExtras().getString("fullnumber");

//        this.fullnumber = "8613898589731";
//        verification code = 548864

        setContentView(R.layout.activity_raregister_code);

        edtVerificationCode = (EditText)findViewById(R.id.edtName);
        btnResendCode = (Button)findViewById(R.id.btnResendcode);
        btnNext = (Button)findViewById(R.id.btnNext);
        btnBack = (ImageButton)findViewById(R.id.btnBack);

        textSentTo = (TextView)findViewById(R.id.labelSentNumber);

        textSentTo.setText(MessageFormat.format(getString(R.string.announce_activity_raregister_code), this.fullnumber));

        btnResendCode.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        edtVerificationCode.addTextChangedListener(this);
        edtVerificationCode.setOnClickListener(this);
        edtVerificationCode.clearFocus();
        edtVerificationCode.requestFocus();
        edtVerificationCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {


                }
                return false;
            }
        });

        edtVerificationCode.setSelection(0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    //-----------------------------------------------------
    //------------ Button delegate methods --------------
    //-----------------------------------------------------

    @Override
    public void onClick(View view)
    {
        if(view == btnBack)
        {

            gotoPreviousScreen();
        }
        else if(view == btnNext)
        {
            confirmCode();
        }
        else if(view == btnResendCode)
        {
            resendCode();
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

        if(charSequence.length()==6)
        {
            btnNext.setVisibility(View.VISIBLE);

        }
        else
        {
            btnNext.setVisibility(View.INVISIBLE);
            if(charSequence.length()>6)
            {
                edtVerificationCode.setText(edtVerificationCode.getText().subSequence(0, 6));
                edtVerificationCode.setSelection(6, 6);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {


    }

    //-----------------------------------------------------
    //------------ Logic methods --------------
    //-----------------------------------------------------
    public void resendCode()
    {
        this.btnResendCode.setEnabled(false);

        JSONObject params = new JSONObject();
        try {

            params.put("type","registerPhoneNumber");
            params.put("username", RAConstant.kRARemoteUser);
            params.put("authUsername",RAConstant.kRARemoteUser);
            params.put("authPassword",RAConstant.kRARemotePassword);
            params.put("phone",this.fullnumber);
            params.put("culture", Locale.getDefault().getLanguage());

            String jsonParams = params.toString();
            RequestParams param = new RequestParams();
            param.put("param", RAUtils.encryptRemote(jsonParams));
            String url = MessageFormat.format("http://{0}:{1}/registerPhoneNumber", RAConstant.HOST, RAConstant.PORT_NODEJS);
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, param,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                    btnResendCode.setEnabled(true);
                    try {
                        if(res.getInt("status") == 400){
                            new AlertDialog.Builder(RARegisterCodeActivity.this)
                                    .setTitle(R.string.error)
                                    .setMessage(res.getString("message"))
                                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            RARegisterCodeActivity.this.resendCode();
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    } catch (JSONException e) {

                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject res) {
                    btnResendCode.setEnabled(true);
                    try {

                            new AlertDialog.Builder(RARegisterCodeActivity.this)
                                    .setTitle(R.string.error)
                                    .setMessage(e.getLocalizedMessage())
                                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            RARegisterCodeActivity.this.resendCode();
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                    } catch (Exception ex) {

                    }
                }
                @Override
                public void onFinish() {

                }
            });

        } catch (JSONException e) {
            btnResendCode.setEnabled(true);
        } catch (UnsupportedEncodingException e) {
            btnResendCode.setEnabled(true);
        } catch (Exception e) {
            btnResendCode.setEnabled(true);
        }
    }

    public void confirmCode()
    {
        this.btnNext.setEnabled(false);

        JSONObject params = new JSONObject();
        try {

            params.put("type","validateCode");
            params.put("username", RAConstant.kRARemoteUser);
            params.put("authUsername",RAConstant.kRARemoteUser);
            params.put("authPassword",RAConstant.kRARemotePassword);
            params.put("phone",this.fullnumber);
            params.put("code",this.edtVerificationCode.getText().toString());
            params.put("culture", Locale.getDefault().getLanguage());

            String jsonParams = params.toString();
            RequestParams param = new RequestParams();
            param.put("param", RAUtils.encryptRemote(jsonParams));
            String url = MessageFormat.format("http://{0}:{1}/validateCode", RAConstant.HOST, RAConstant.PORT_NODEJS);
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, param,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                    btnNext.setEnabled(true);
                    try {
                        if(res.getInt("status") == 400){
                            new AlertDialog.Builder(RARegisterCodeActivity.this)
                                    .setTitle(R.string.error)
                                    .setMessage(res.getString("message"))
                                    .setPositiveButton(R.string.Re_Enter_Code, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            RARegisterCodeActivity.this.edtVerificationCode.setText("");
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

                            RAUtils.encryptObjectForKey(RAConstant.kRAOwnPhoneNumber,RARegisterCodeActivity.this.fullnumber);
                            RAUtils.encryptObjectForKey(RAConstant.kRAOwnJID, RARegisterCodeActivity.this.fullnumber + "@" + RAConstant.HOSTNAME);
                            RAUtils.encryptObjectForKey(RAConstant.kRAOwnPassword, RARegisterCodeActivity.this.edtVerificationCode.getText().toString() + "$" + RARegisterCodeActivity.this.edtVerificationCode.getText().toString());


                            Intent intent = new Intent(RoomApplication.getInstance().getApplicationContext(), RARegisterNameActivity.class);
                            intent.putExtra("fullnumber",fullnumber);

                            startActivity(intent);


                        }
                    } catch (Exception e) {

                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject res) {
                    btnNext.setEnabled(true);
                    try {
                        new AlertDialog.Builder(RARegisterCodeActivity.this)
                                .setTitle(R.string.error)
                                .setMessage(e.getLocalizedMessage())
                                .setPositiveButton(R.string.Re_Enter_Code, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        RARegisterCodeActivity.this.edtVerificationCode.setText("");
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
            btnNext.setEnabled(true);
        } catch (UnsupportedEncodingException e) {
            btnNext.setEnabled(true);
        } catch (Exception e) {
            btnNext.setEnabled(true);
        }
    }

    public void gotoPreviousScreen()
    {
        //Do something
        this.finish();
    }


}
