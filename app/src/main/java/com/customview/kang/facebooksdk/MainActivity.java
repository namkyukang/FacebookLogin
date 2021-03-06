package com.customview.kang.facebooksdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    public static String TAG = "FaceBookLogin";
    final private static boolean SIGNUP = true;
    final private static boolean SIGNIN = false;
    boolean status;
    AccessToken accessToken;
    Intent intent;
    User user;
    LinearLayout linearSignIn, linearSignUp;
    EditText input_email_Up,input_password_Up,confirm_password_Up,input_Name,input_age,input_email_In,input_password_In;
    CheckBox cbFemale,cbmale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewInit();
        status = SIGNIN;


    }
    public void viewInit(){
        linearSignIn = (LinearLayout)findViewById(R.id.linearSignIn);
        linearSignUp = (LinearLayout)findViewById(R.id.linearSignUp);
        input_email_Up = (EditText)findViewById(R.id.input_email_Up);
        input_password_Up = (EditText)findViewById(R.id.input_password_Up);
        confirm_password_Up = (EditText)findViewById(R.id.confirm_password_Up);
        input_Name = (EditText)findViewById(R.id.input_Name);
        input_age=(EditText)findViewById(R.id.input_age);
        input_email_In = (EditText)findViewById(R.id.input_email_In);
        input_password_In = (EditText)findViewById(R.id.input_password_In);
        cbFemale = (CheckBox) findViewById(R.id.cbFemale);
        cbmale = (CheckBox) findViewById(R.id.cbMale);
    }
    public void click_signUp(View view){
        try{
            if(!validate_Up()){
                return;
            }
            user = new User();
            user.setEmail(input_email_Up.getText().toString());
            user.setPassword(input_password_Up.getText().toString());
            user.setName(input_Name.getText().toString());
            user.setAge(Integer.parseInt(input_age.getText().toString()));
            user.setGender(cbFemale.isChecked() ? "female" : "male");
            reset_signUp_editText();
            status = SIGNIN;
            signUp_Success();

        }catch(Exception e){
            Toast.makeText(this, "모든 정보를 기입해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        }

    }
    public void reset_signUp_editText(){
        input_email_Up.setText("");
        input_password_Up.setText("");
        input_Name.setText("");
        input_age.setText("");
        cbFemale.setChecked(false);
        cbmale.setChecked(false);

    }
    public void click_signUp_text(View view){
        linearSignIn.setVisibility(View.GONE);
        linearSignUp.setVisibility(View.VISIBLE);
        input_email_In.setText("");
        input_password_In.setText("");
        status = SIGNUP;
    }
    public void login_Success(){
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("로그인 중 ...");
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.show();


        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //finish();
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                }, 1500);
    }
    public void signUp_Success(){
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("가입 중...");
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.show();


        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //TODO : 서버에 사용자 정보 전송.
                        linearSignUp.setVisibility(View.GONE);
                        linearSignIn.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                }, 1500);
    }
    public void click_singIn(View view){
        //TODO : 서버에 사용자 조회, password 암호화
        if(!validate_in()){
            Toast.makeText(this, "Login 실패", Toast.LENGTH_SHORT).show();
            return;
        }
        if( input_email_In.getText().toString().equals("master@master.com")/*user.getEmail*/ &&
                        input_password_In.getText().toString().equals("master")/*user.getPassword*/) {
            //TODO : 사용자 있으면 Sign In - Login success
            User singIn_user = new User();
            singIn_user.setGender("female");
            singIn_user.setPassword("master");
            singIn_user.setAge(28);
            singIn_user.setName("Master");
            singIn_user.setEmail("master@master.com");

            intent = new Intent(MainActivity.this, CmpTestActivity.class);
            intent.putExtra("user", singIn_user);
            login_Success();


        }else{
            //TODO : 사용자 없으면 SetError - Login Fail
            Toast.makeText(this, "입력하신 E-mail 혹은 비밀번호가 옳지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void click_facebook_signUp(View view){
        user = new User();
        setLoginFaceBook();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if(status == SIGNIN){
            super.onBackPressed();
        }else if(status == SIGNUP){
            linearSignIn.setVisibility(View.VISIBLE);
            linearSignUp.setVisibility(View.GONE);
        }
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
        LoginButton loginButton = (LoginButton)findViewById(R.id.facebook_login_button_In);
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

                    user.setEmail(object.getString("email"));
                    user.setName(object.getString("name") );
                    user.setGender(object.getString("gender"));

                    intent = new Intent(MainActivity.this, CmpTestActivity.class);
                    intent.putExtra("user", user);
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
    public boolean validate_in(){
        boolean valid = true;

        String email = input_email_In.getText().toString();
        String password = input_password_In.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email_In.setError("E-mail 형식으로 입력해주세요");
            valid = false;
        } else {
            input_password_In.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            input_password_In.setError("비밀번호는 4자 이상 10자 이하입니다.");
            valid = false;
        } else {
            input_password_In.setError(null);
        }

        return valid;
    }
    public boolean validate_Up(){
        boolean valid = true;

        String name = input_Name.getText().toString();
        String email = input_email_Up.getText().toString();
        String password = input_password_Up.getText().toString();
        String age = input_age.getText().toString();
        String password_confirm = confirm_password_Up.getText().toString();

        if (name.isEmpty() || name.length() < 2) {
            input_Name.setError("이름이 최소 2자는 되야죠");
            valid = false;
        } else {
            input_Name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email_Up.setError("E-mail 형식으로 입력해주세요");
            valid = false;
        } else {
            input_email_Up.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            input_password_Up.setError("비밀번호는 4자 이상 10자 이하입니다.");
            valid = false;
        } else {
            input_password_Up.setError(null);
        }

        if (password_confirm.isEmpty()) {
            confirm_password_Up.setError("비밀번호를 확인해 주세요");
            valid = false;
        }else if(!password_confirm.equals(password)){
            confirm_password_Up.setError("비밀번호가 일치하지 않습니다.");
            valid = false;
        } else {
            confirm_password_Up.setError(null);
        }

        if (age.isEmpty() || Integer.parseInt(age) > 100  || Integer.parseInt(age) < 1) {
            input_age.setError("100세가 넘는다는게 말이 안됩니다.");
            valid = false;
        } else {
            input_age.setError(null);
        }
        if(!cbmale.isChecked() && !cbFemale.isChecked()){
            Toast.makeText(this, "성별을 체크해주세요", Toast.LENGTH_SHORT).show();
        }

        return valid;
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
