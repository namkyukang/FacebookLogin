package com.customview.kang.facebooksdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.hardware.camera2.params.Face;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    public static String TAG = "FaceBookLogin";
    AccessToken accessToken;
    Intent intent;
    FaceBookUser faceBookUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        faceBookUser = new FaceBookUser();
        setLoginFaceBook();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //AppEventsLogger.deactivateApp(this);

    }
    private void setLoginFaceBook(){
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                Logger.print(TAG, "=======================onSuccess========================");
                Logger.print(TAG, "User ID: " + accessToken.getUserId());
                Logger.print(TAG, "Auth Token: " + accessToken.getToken());
                getUserInfo();
            }
            @Override
            public void onCancel() {
                Log.w(TAG, "=================Cancel============================");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "==============Error==========================", error);
            }
        });
    }
    private void getUserInfo(){

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {

                    faceBookUser.setEmail(object.getString("email"));
                    faceBookUser.setName(object.getString("name") );
                    faceBookUser.setGender(object.getString("gender"));

                    intent = new Intent(MainActivity.this, CmpTestActivity.class);
                    intent.putExtra("user", faceBookUser);
                    //finish();
                    startActivity(intent);
                } catch (JSONException e) {e.printStackTrace();}

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public static final String getKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", "===============================Hashkey :"+keyHash + "|end");
                return keyHash;
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Mainactivity","getKeyHash Error1 = " + e.getMessage());

        } catch (NoSuchAlgorithmException e) {
            Log.d("Mainactivity","getKeyHash Error2 = " + e.getMessage());
        }
        return "";
    }
}
