package co.roomapp.room.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

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

/**
 * Created by manager on 1/3/15.
 */
public class RAExploreActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rasearch);

        exploreRooms();
    }

    void exploreRooms()
    {

        JSONObject params = new JSONObject();
        try {

            String username = RAUtils.decryptObjectForKey(RAConstant.kRAOwnPhoneNumber);
            String authUsername = RAUtils.decryptObjectForKey(RAConstant.kRAOwnPhoneNumber);
            String authPassword = RAUtils.decryptObjectForKey(RAConstant.kRAOwnPassword);

            String culture = Locale.getDefault().getLanguage();

            System.out.print("Username:");
            System.out.println(username);
            System.out.print("Authusername:");
            System.out.println(authUsername);
            System.out.print("Authpassword:");
            System.out.println(authPassword);
            System.out.print("Culture:");
            System.out.println(culture);

            params.put("type","searchLastActiveRoom");
            params.put("username", username);
            params.put("authUsername", authUsername);
            params.put("authPassword", authPassword);
            params.put("culture", culture);

            String jsonParams = params.toString();
            RequestParams param = new RequestParams();
            param.put("param", RAUtils.encryptRemote(jsonParams));

            String url = MessageFormat.format("http://{0}:{1}/searchHashtags", RAConstant.HOST, RAConstant.PORT_NODEJS);

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, param,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject res) {

                    try {
                        if(res.getInt("status") == 400){
                            new AlertDialog.Builder(RAExploreActivity.this)
                                    .setTitle(R.string.error)
                                    .setMessage(res.toString())
                                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            RAExploreActivity.this.exploreRooms();
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }else{//success

                            //Do something
                            System.out.println("Retrieve rooms list");
                        }
                    } catch (JSONException e) {

                    }


                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject res) {
                    try {
                        new AlertDialog.Builder(RAExploreActivity.this)
                                .setTitle(R.string.error)
                                .setMessage(e.getLocalizedMessage())
                                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        RAExploreActivity.this.exploreRooms();
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
