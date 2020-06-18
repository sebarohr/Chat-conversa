package com.example.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    private EditText user, pass;
    private ServicioWeb servicio;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user= findViewById(R.id.editText);
        pass= findViewById(R.id.editText2);
        button = findViewById(R.id.button);

        button.setOnClickListener(this);
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("http://chat-conversa.unnamed-chile.com/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        servicio = retrofit.create(ServicioWeb.class);

    }
    public void register(View view){
        Intent registro = new Intent(this, RegisterActivity.class);
        startActivity(registro);
        finish();
    }

     // llamar login , llamar registrar , 4 clases recibir  login=respuestaws --
    @Override
    public void onClick(View v) {
        Login login= new Login(user.getText().toString(), pass.getText().toString() , "AK47");
        final Call<RespuestaWS> resp = servicio.login(login);
        resp.enqueue(new Callback<RespuestaWS>() {
            @Override
            public void onResponse(Call<RespuestaWS> call, Response<RespuestaWS> response) {
               if(response.isSuccessful()){
                   Log.d("Retrofit",response.body().getStatus_code());
                   Log.d("Retrofit",response.body().getData().getId()+"");
                   Log.d("Retrofit",response.body().getToken());
                   token= response.body().getToken();
                   int id=response.body().getData().getId();
                   String username = response.body().getData().getUsername();
                   parametroLogout(token,id,username);

               }else{
                   System.out.println(response);
               }
            }

            @Override
            public void onFailure(Call<RespuestaWS> call, Throwable t) {

            }
        });

    }
    public void parametroLogout(String token, int id, String username){
        Intent intent = new Intent(this, LogoutActivity.class);
        Bundle parametros = new Bundle();
        parametros.putString("username",username);
        parametros.putInt("id",id);
        parametros.putString("token",token);
        intent.putExtras(parametros);
        startActivity(intent);
        finish();
    }

}
