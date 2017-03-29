package com.customview.kang.facebooksdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CmpTestActivity extends AppCompatActivity {
    TextView txtEmail,txtAge,txtName,txtGender;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmp_test);

        user = (User) getIntent().getSerializableExtra("user");

        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtName = (TextView)findViewById(R.id.txtName);
        txtGender= (TextView)findViewById(R.id.txtGender);

        txtEmail.setText(user.getEmail());
        txtName.setText(user.getName());

        txtGender.setText(user.getGender());
    }
}
