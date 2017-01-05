package co.roomapp.room.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import android.widget.Toast;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import co.roomapp.room.application.RoomApplication;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAUtils;
import co.wheelpicker.OnWheelClickedListener;
import co.wheelpicker.WheelView;
import co.roomapp.room.R;
import co.wheelpicker.adapters.WheelViewAdapter;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.EditorInfo;

import android.content.Context;
import android.graphics.Bitmap;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RARegisterPhoneActivity extends Activity implements View.OnClickListener,TextWatcher {

    private EditText phoneText;
    private TextView countryCode;
    private ImageView flag;
    private Button selectCode;
    private CheckBox checkTerms;
    private Button next;
    private ImageButton btnDismissPicker;

    private ArrayList<String> countryNameList;
    private ArrayList<String> countryCodeList;
    private ArrayList<Country> allCountriesList;

    private WheelView countryPickerView;
    private int selectedIndex;

    private boolean scrolling;
    private RARegisterPhoneActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;

        setContentView(R.layout.activity_raregister_phone);

        phoneText = (EditText)findViewById(R.id.edtName);
        countryCode = (TextView)findViewById(R.id.indicatif);
        flag = (ImageView)findViewById(R.id.flag);
        selectCode = (Button)findViewById(R.id.btn_selectcode);
        checkTerms = (CheckBox)findViewById(R.id.checkBox);
        next = (Button)findViewById(R.id.next);
        countryPickerView = (WheelView)findViewById(R.id.countryPickerView);
        btnDismissPicker = (ImageButton)findViewById(R.id.dismissPicker);


        selectCode.setOnClickListener(this);
        phoneText.addTextChangedListener(this);
        phoneText.setOnClickListener(this);
        next.setOnClickListener(this);
        phoneText.clearFocus();
        btnDismissPicker.setOnClickListener(this);

        phoneText.requestFocus();


        phoneText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    countryPickerView.setVisibility(View.INVISIBLE);

                    postPhone();
                }
                return false;
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        countryNameList = new ArrayList<String>();
        countryCodeList = new ArrayList<String>();

        String[] isoCountryCodes = Locale.getISOCountries();
        for (String countryCode : isoCountryCodes) {
            Locale locale = new Locale("", countryCode);
            String countryName = locale.getDisplayCountry();

            countryNameList.add(countryName);
            countryCodeList.add(countryCode);
        }


        allCountriesList = new ArrayList<Country>();

        try
        {
            String allCountriesString = readFileAsString(this);
            JSONArray jsonArray = new JSONArray(allCountriesString);
            int k=jsonArray.length();

            for (int i = 0; i < k; i++) {
                JSONObject jObject=jsonArray.getJSONObject(i);
                Country country = new Country();
                country.setCode(jObject.getString("code"));
                country.setName(jObject.getString("name"));
                country.setDialCode(jObject.getString("dial_code"));
                allCountriesList.add(country);
            }
        }
        catch (JSONException err){

        }
        catch(IOException err){

        }


        // Add the data to all countries list
        WheelViewAdapter wheelViewAdapter = new WheelViewAdapter() {
            @Override
            public int getItemsCount() {
                return countryNameList.size();
            }

            @Override
            public View getItem(int index, View convertView, ViewGroup parent) {

                LayoutInflater factory = LayoutInflater.from(self);
                View myView = factory.inflate(R.layout.country_layout, null);
                TextView countryNameView = (TextView)myView.findViewById(R.id.country_name);
                ImageView countryFlagView = (ImageView)myView.findViewById(R.id.flag);
                countryNameView.setText(countryNameList.get(index));
                String countryCode = countryCodeList.get(index);
//                int id = self.getResources().getIdentifier(countryCode, "drawable\\Flags", getPackageName());

                int id = self.getResources().getIdentifier("zflag_" + countryCode.toLowerCase(), "drawable", getPackageName());
                countryFlagView.setImageResource(id);
                return myView;
            }

            @Override
            public View getEmptyItem(View convertView, ViewGroup parent) {
                return null;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }
        };

        countryPickerView.addClickingListener(new OnWheelClickedListener() {
            @Override
            public void onItemClicked(WheelView wheel, int itemIndex) {

                    wheel.setCurrentItem(itemIndex, true);
                    selectedIndex = itemIndex;

            }

        });

        countryPickerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(countryPickerView.getCurrentItem());
            }
        });

        countryPickerView.setVisibleItems(3);
        countryPickerView.setViewAdapter(wheelViewAdapter);
        countryPickerView.setClickable(true);
        selectedIndex = countryCodeList.indexOf("US");
        countryPickerView.setCurrentItem(selectedIndex);

        btnDismissPicker.setVisibility(View.INVISIBLE);

    }


    private static String readFileAsString(Context context)
            throws java.io.IOException {
        InputStream inputStream = context.getResources().openRawResource(
                R.raw.countrycodes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuffer result = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();
        return result.toString();
    }


    public void changeFlag()
    {
        String countrycode = countryCodeList.get(selectedIndex);

        int id = self.getResources().getIdentifier("zflag_" + countrycode.toLowerCase(), "drawable", getPackageName());
        flag.setImageResource(id);

        int foundIndex = -1;
        for(int i = 0; i < allCountriesList.size(); i ++)
        {
            Country country = allCountriesList.get(i);
            String code = country.getCode();
            if(countrycode.equalsIgnoreCase(code))
            {
                foundIndex = i;
                break;
            }
        }
        if(foundIndex!=-1)
        {
            Country country = allCountriesList.get(foundIndex);
            String dialCode = country.getDialCode();
            countryCode.setText(dialCode);
        }

        InputMethodManager imm = (InputMethodManager)getSystemService(self.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(phoneText.getWindowToken(), InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        btnDismissPicker.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {

        if(view == selectCode){

            RelativeLayout contentView = (RelativeLayout)findViewById(R.id.regphRootLayout);
            contentView.setDrawingCacheEnabled(true);
            contentView.buildDrawingCache();
            Bitmap bm = contentView.getDrawingCache();


            Bitmap scaledBm = Bitmap.createScaledBitmap(bm, bm.getWidth()/2, bm.getHeight()/2, true);
            Bitmap enlargedBm = Bitmap.createScaledBitmap(scaledBm, bm.getWidth(), bm.getHeight(), true);


            RelativeLayout bgLayout = (RelativeLayout)findViewById(R.id.regphbgLayout);
            bgLayout.setAlpha(0);
            Drawable drawable = new BitmapDrawable(enlargedBm);
            contentView.setBackground(drawable);

            InputMethodManager imm = (InputMethodManager)getSystemService(self.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow( phoneText.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
            btnDismissPicker.setVisibility(View.VISIBLE);
            countryPickerView.setVisibility(View.VISIBLE);
        }
        if(view == phoneText){
            InputMethodManager imm = (InputMethodManager)getSystemService(self.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(phoneText.getWindowToken(), InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            btnDismissPicker.setVisibility(View.INVISIBLE);
        }

        if(view == next){

            if(this.phoneText.getText().length()<2){

                new AlertDialog.Builder(RARegisterPhoneActivity.this)
                        .setTitle(R.string.error)
                        .setMessage(R.string.Please_enter_phone_number)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return ;
            }

            if(!this.checkTerms.isChecked()){

                new AlertDialog.Builder(RARegisterPhoneActivity.this)
                        .setTitle(R.string.Terms_and_Privacy)
                        .setMessage(R.string.You_must_agree_to_the_Terms_and_Privacy)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return ;
            }

            postPhone();
        }

        if(view == btnDismissPicker)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(self.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(phoneText.getWindowToken(), InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            btnDismissPicker.setVisibility(View.INVISIBLE);
            selectedIndex = countryPickerView.getCurrentItem();
            changeFlag();

            RelativeLayout contentView = (RelativeLayout)findViewById(R.id.regphRootLayout);
            contentView.setBackground(null);
            RelativeLayout bgLayout = (RelativeLayout)findViewById(R.id.regphbgLayout);
            bgLayout.setAlpha(1);

            contentView.setDrawingCacheEnabled(false);

        }

    }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        if(charSequence.length()>10)
        {

        }
        else
        {

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(phoneText.length()>0){
            next.setVisibility(View.VISIBLE);
        }else{
            next.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }



    private void postPhone(){
        next.setEnabled(false);

        JSONObject params = new JSONObject();
        try {

            String phone = this.phoneText.getText().toString();

            if(phone.charAt(0)=='0' || (phone.charAt(0)=='1' && RAUtils.formatPhoneNumber(this.countryCode.getText().toString()).equals("1")) && phone.length()>1){
                phone = phone.substring(1);
                this.phoneText.setText(phone);
            }


            final String fullnumber = RAUtils.formatPhoneNumber(this.countryCode.getText().toString() + this.phoneText.getText().toString());

            params.put("type","registerPhoneNumber");
            params.put("username", RAConstant.kRARemoteUser);
            params.put("authUsername",RAConstant.kRARemoteUser);
            params.put("authPassword",RAConstant.kRARemotePassword);
            params.put("phone",fullnumber);
            params.put("culture",Locale.getDefault().getLanguage());

            String jsonParams = params.toString();
            RequestParams param = new RequestParams();
            param.put("param", RAUtils.encryptRemote(jsonParams));

            String url = MessageFormat.format("http://{0}:{1}/registerPhoneNumber", RAConstant.HOST, RAConstant.PORT_NODEJS);

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, param,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                    next.setEnabled(true);
                    try {
                        if(res.getInt("status") == 400){
                            new AlertDialog.Builder(RARegisterPhoneActivity.this)
                                    .setTitle(R.string.error)
                                    .setMessage(res.getString("message"))
                                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            RARegisterPhoneActivity.this.postPhone();
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

                            RAUtils.setObjectForKey(RAConstant.kRAOwnCountryCode,RARegisterPhoneActivity.this.countryCode.getText().toString().replace("+",""));

                            Intent intent = new Intent(RoomApplication.getInstance().getApplicationContext(), RARegisterCodeActivity.class);
                            intent.putExtra("fullnumber", fullnumber);

                            startActivity(intent);
                        }
                    } catch (JSONException e) {

                    }


                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject res) {
                    next.setEnabled(true);
                    try {
                        new AlertDialog.Builder(RARegisterPhoneActivity.this)
                                .setTitle(R.string.error)
                                .setMessage(e.getLocalizedMessage())
                                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        RARegisterPhoneActivity.this.postPhone();
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
            next.setEnabled(true);
        } catch (UnsupportedEncodingException e) {
            next.setEnabled(true);
        } catch (Exception e) {
            next.setEnabled(true);
        }



    }


    public boolean onTouch(View v, MotionEvent event) {
        boolean isReleased = event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL;
        boolean isPressed = event.getAction() == MotionEvent.ACTION_DOWN;

        if (isReleased) {

        } else if (isPressed) {

            InputMethodManager imm = (InputMethodManager)getSystemService(self.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(phoneText.getWindowToken(), InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            btnDismissPicker.setVisibility(View.INVISIBLE);

        }
        return false;
    }





    private class Country {

        private String code;
        private String name;
        private String dialCode;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDialCode() {
            return dialCode;
        }

        public void setDialCode(String dialCode) {
            this.dialCode = dialCode;
        }

    }

}
