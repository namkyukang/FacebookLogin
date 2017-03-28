package com.customview.kang.facebooksdk;

import android.content.Intent;
import android.hardware.camera2.params.Face;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CmpTestActivity extends AppCompatActivity {
    TextView txtEmail,txtAge,txtName,txtGender;
    FaceBookUser faceBookUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmp_test);

        faceBookUser = (FaceBookUser) getIntent().getSerializableExtra("user");

        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtName = (TextView)findViewById(R.id.txtName);
        txtGender= (TextView)findViewById(R.id.txtGender);

        txtEmail.setText(faceBookUser.getEmail());
        txtName.setText(faceBookUser.getName());
        txtGender.setText(faceBookUser.getGender());
    }
}
