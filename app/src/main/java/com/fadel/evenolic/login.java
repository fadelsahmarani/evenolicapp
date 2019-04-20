package com.fadel.evenolic;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import steelkiwi.com.library.DotsLoaderView;

public class login extends AppCompatActivity {
    DotsLoaderView dotsLoaderView ;
    String usertype="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dotsLoaderView= this.findViewById(R.id.dotsLoaderView);
        final EditText et_username=this.findViewById(R.id.username);
        final EditText et_password=this.findViewById(R.id.password);
        final RelativeLayout layout=this.findViewById(R.id.login_Layout);
        final TextView loginType=this.findViewById(R.id.login_userType);
        final int[] count = {0};
        final int[] switchValue = {0};

        this.findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String type=String.valueOf(switchValue[0]);
                loginUser(username,password,type);
            }
        });
        this.findViewById(R.id.register_nowbtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
             {
                    Intent intent=new Intent(login.this,register.class);
                    startActivity(intent);
             }
        });
        this.findViewById(R.id.switch_loginType).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                count[0] = count[0] +1;
                int colors1[] = { 0xff255779, 0xffa6c0cd };
                GradientDrawable gradientDrawable1 = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM, colors1);
                int colors2[] = { 0xFF3D5069, 0xff192943 };
                GradientDrawable gradientDrawable2 = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM, colors2);

                if(count[0]%2!=0){
                    loginType.setText("switch to login as client");
                    usertype="planner";
                    layout.setBackgroundDrawable(gradientDrawable1);
                    switchValue[0] =1;
                }else{
                    loginType.setText("switch to login as planner");
                    usertype="user";
                    layout.setBackgroundDrawable(gradientDrawable2);
                    switchValue[0] =0;
                }
            }
        });
    }
    private void loginUser(final String username, final String password,final String type) {
        //JSONObject postObject = new JSONObject();
        dotsLoaderView.show();
        RequestQueue queue =  Volley.newRequestQueue(this);
        //JSONObject historyObject = new JSONObject();
        final String url = "http://oasessolution.com/evenolicCon/login.php";
        /*Log.e("LoginActivityJsonObject",""+postObject);*/
        StringRequest objRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("LoginActivity","OnResponse: "+response);
                        //Toast.makeText(login.this, String.valueOf(response), Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String success = jsonResponse.getString("success");
                            if (success.equals("1")) {

                                Intent intent = new Intent(login.this, MainActivity.class);
                                intent.putExtra("usertype",usertype);
                                startActivity(intent);
                                dotsLoaderView.hide();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                                builder.setMessage(jsonResponse.getString("message"))
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                                dotsLoaderView.hide();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("OnError", String.valueOf(error.getMessage()));
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                params.put("type",type);
                return params;
            }
        };
        queue.add(objRequest);

    }
}
