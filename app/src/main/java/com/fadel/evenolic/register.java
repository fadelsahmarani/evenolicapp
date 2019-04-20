package com.fadel.evenolic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final LinearLayout clientDiv=this.findViewById(R.id.clientDiv);
        final LinearLayout plannerDiv=this.findViewById(R.id.plannerDiv);
        final TextView user_type=this.findViewById(R.id.user_type);
        final int[] count = {0};

        final CardView clientRegister = this.findViewById(R.id.register_btn);
        final EditText et_email = this.findViewById(R.id.email);
        final EditText et_fname = this.findViewById(R.id.firstname);
        final EditText et_ltname = this.findViewById(R.id.lastname);
        final EditText et_uname = this.findViewById(R.id.username);
        final EditText et_password = this.findViewById(R.id.password);
        final EditText et_confirmPassword = this.findViewById(R.id.confirm_password);

        clientRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String first = et_fname.getText().toString();
                final String last = et_ltname.getText().toString();
                final String email = et_email.getText().toString();
                final String username = et_uname.getText().toString();
                final String password = et_password.getText().toString();
                final String confirmPassword = et_confirmPassword.getText().toString();
                HashMap<String,String> paramsClient = new HashMap<>();
                paramsClient.put("firstname", first);
                paramsClient.put("lastname", last);
                paramsClient.put("email", email);
                paramsClient.put("username", username);
                paramsClient.put("password", password);

                if(password.equals(confirmPassword)){
                    registerUser(paramsClient);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                    builder.setMessage(String.valueOf("password does not match"))
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                    //Toast.makeText(register.this,"password does not match", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final CardView plannerRegister = this.findViewById(R.id.planner_register_btn);
        final EditText et_pcompany = this.findViewById(R.id.planner_companyname);
        final EditText et_pname = this.findViewById(R.id.planner_name);
        final EditText et_pemail = this.findViewById(R.id.planner_email);
        final EditText et_puname = this.findViewById(R.id.planner_username);
        final EditText et_ppassword = this.findViewById(R.id.planner_password);
        final EditText et_pconfirmPassword = this.findViewById(R.id.planner_confirm_password);


        plannerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String company = et_pcompany.getText().toString();
                final String name = et_pname.getText().toString();
                final String email = et_pemail.getText().toString();
                final String username = et_puname.getText().toString();
                final String password = et_ppassword.getText().toString();
                final String confirmPassword = et_pconfirmPassword.getText().toString();
                HashMap<String,String>paramsPlanner = new HashMap<>();
                paramsPlanner.put("company", company);
                paramsPlanner.put("name", name);
                paramsPlanner.put("email", email);
                paramsPlanner.put("username", username);
                paramsPlanner.put("password", password);
                if(password.equals(confirmPassword)){
                    registerPlanner(paramsPlanner);
                }else{
                    //Toast.makeText(register.this,"password does not match", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                    builder.setMessage(String.valueOf("password does not match"))
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }


            }
        });

        this.findViewById(R.id.user_switch).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                count[0] = count[0] +1;
                clientDiv.setVisibility(View.INVISIBLE);
                plannerDiv.setVisibility(View.VISIBLE);
                if(count[0]%2!=0){
                    user_type.setText("switch to register as client");
                }else
                {
                    user_type.setText("switch to register as planner");
                    clientDiv.setVisibility(View.VISIBLE);
                    plannerDiv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void registerUser(final HashMap<String,String> paramJson) {
        RequestQueue queue =  Volley.newRequestQueue(this);

        final String REGISTER_REQUEST_URL = "http://oasessolution.com/evenolicCon/register.php";
        StringRequest objRequest = new StringRequest(Request.Method.POST, REGISTER_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(register.this, String.valueOf(response), Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String success = jsonResponse.getString("success");
                            String message = jsonResponse.getString("message");
                            if (success.equals("1")) {
                                AlertDialog.Builder alertbuilder = new AlertDialog.Builder(register.this);
                                alertbuilder.setMessage(String.valueOf(response))
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                                Intent intent = new Intent(register.this, login.class);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                                builder.setMessage(message)
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("OnError", String.valueOf(error.getMessage()));
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {

                return paramJson;
            }
        };
        queue.add(objRequest);

    }
    private void registerPlanner(final HashMap<String,String> paramJson) {
        RequestQueue queue =  Volley.newRequestQueue(this);

        final String REGISTER_REQUEST_URL = "http://oasessolution.com/evenolicCon/registerPlanner.php";
        StringRequest objRequest = new StringRequest(Request.Method.POST, REGISTER_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(register.this, String.valueOf(response), Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String success = jsonResponse.getString("success");
                            String message = jsonResponse.getString("message");
                            if (success.equals("1")) {
                                AlertDialog.Builder alertbuilder = new AlertDialog.Builder(register.this);
                                alertbuilder.setMessage(String.valueOf(response))
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                                Intent intent = new Intent(register.this, login.class);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                                builder.setMessage(message)
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("OnError", String.valueOf(error.getMessage()));
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {

                return paramJson;
            }
        };
        queue.add(objRequest);

    }
}
