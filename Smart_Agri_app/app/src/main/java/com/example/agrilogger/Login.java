package com.example.agrilogger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private TextInputLayout editTextpassword,editTextEmail;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    Animation topAnim, bottomAnim,left2right;
    ImageView imagelogo,fst,usms;
    TextView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.buttom_animation);
        left2right = AnimationUtils.loadAnimation(this,R.anim.right_anim);


        imagelogo=findViewById(R.id.LogoLogin);
        imagelogo.setAnimation(topAnim);
        fst=findViewById(R.id.fst);
        fst.setAnimation(bottomAnim);
        usms=findViewById(R.id.usms);
        usms.setAnimation(bottomAnim);
        logo=findViewById(R.id.bienvenue);
        logo.setAnimation(left2right);
         editTextEmail =   findViewById(R.id.username);
         editTextpassword =  findViewById(R.id.password);
         mAuth=FirebaseAuth.getInstance();
         progressBar = findViewById(R.id.progress_);

        editTextEmail.getEditText().setText("1234@gmail.com");
        editTextpassword.getEditText().setText("1234567");
    }
    public void loginEvent (View v){
        String txt_email = editTextEmail.getEditText().getText().toString().trim();
        String txt_password = editTextpassword.getEditText().getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches() || txt_email.isEmpty()){
            editTextEmail.setError("Enter un e-mail valide");
            editTextEmail.requestFocus();

        }
        if(txt_password.isEmpty() || txt_password.length()<6){
            editTextEmail.setError("Enter un mot de pass valide");
            editTextEmail.requestFocus();
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Connexion avec succés", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,Dashboard.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Connexion échoué", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }
}