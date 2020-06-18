package com.example.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterActivity extends AppCompatActivity {
    private ServicioWeb servicio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://chat-conversa.unnamed-chile.com/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        servicio=retrofit.create(ServicioWeb.class);


    }


    public void onClick(View view){
        TextInputLayout name = findViewById(R.id.nameLabel);
        TextInputLayout lastname = findViewById(R.id.lastnameLabel);
        TextInputLayout run = findViewById(R.id.runLabel);
        TextInputLayout username = findViewById(R.id.usernameLabel);
        TextInputLayout email = findViewById(R.id.emailLabel);
        TextInputLayout password = findViewById(R.id.passwordLabel);
        String token="SARU12";

        User usuario = new User(name.getEditText().getText().toString(),
                lastname.getEditText().getText().toString(),
                run.getEditText().getText().toString(),
                username.getEditText().getText().toString(),
                email.getEditText().getText().toString(),
                password.getEditText().getText().toString(),
                token);

        Call<RespuestaWSRegister> call = servicio.create(usuario);
        call.enqueue(new Callback<RespuestaWSRegister>() {
            @Override
            public void onResponse(Call<RespuestaWSRegister> call, Response<RespuestaWSRegister> response) {
                if(response.isSuccessful() && response!= null && response.body() != null){
                    Log.d("Retrofit","Exito: "+ response.body().toString());

                }else{
                    Gson gson= new Gson();
                    ErrorResponse res = new ErrorResponse();
                    try{
                        res = gson.fromJson(response.errorBody().string(),ErrorResponse.class);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Log.d("Retrofit","Error body convert: "+ res);
                }

            }

            @Override
            public void onFailure(Call<RespuestaWSRegister> call, Throwable t) {

            }
        });
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cancel(View view){

        Intent cancel = new Intent(this, MainActivity.class);
        startActivity(cancel);
        finish();
    }

}
