package com.example.prachisdemo.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prachisdemo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class WebServicesActivity extends AppCompatActivity {

    private Button btnGet, btnPost, btnPut, btnDelete;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private EditText name;
    private EditText job;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_services);

        name = findViewById(R.id.edt_name);
        job = findViewById(R.id.edt_job);

        mRequestQueue = Volley.newRequestQueue(this);

        btnGet = findViewById(R.id.btn_get);
        btnGet.setOnClickListener(v -> getRequest());

        btnPost = findViewById(R.id.btn_post);
        btnPost.setOnClickListener(v -> postRequest(name.getText().toString(), job.getText().toString()));

        btnPut = findViewById(R.id.btn_put);
        btnPut.setOnClickListener(v -> putRequest(name.getText().toString(), job.getText().toString()));

        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> deleteRequest());


    }


    //this method is for get.
    private void getRequest() {
        String urlGet = "https://api.androidhive.info/contacts/";
        //RequestQueue initialized
        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, urlGet, response -> {
            Log.e("GET response", response);
            parseGETResponse(response);
        }, error -> Toast.makeText(WebServicesActivity.this, "Error :", Toast.LENGTH_SHORT).show());
        mRequestQueue.add(mStringRequest);
    }

    private void parseGETResponse(String response) {
        try {
            JSONObject responseJsonObject = new JSONObject(response);
            JSONArray contactsJsonArray = responseJsonObject.getJSONArray("contacts");
            for (int i = 0; i < contactsJsonArray.length(); i++) {

                JSONObject contactsJsonArrayJSONObject = contactsJsonArray.getJSONObject(i);
                String name = contactsJsonArrayJSONObject.getString("name");
                String email = contactsJsonArrayJSONObject.getString("email");
                String address = contactsJsonArrayJSONObject.getString("address");
                String gender = contactsJsonArrayJSONObject.getString("gender");

                JSONObject phoneJsonObject = contactsJsonArrayJSONObject.getJSONObject("phone");
                String mobile = phoneJsonObject.getString("mobile");
                String home = phoneJsonObject.getString("home");
                String office = phoneJsonObject.getString("office");
            }
        } catch (Exception e) {
            Log.e("parseGETResponse", e.toString());
        }
    }


    //this method is for post.
    private void postRequest(final String name, final String job) {
        String url = "https://reqres.in/api/users";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // response
                    Log.e("Response", response);
                    parsePOSTResponse(response);
                    Toast.makeText(WebServicesActivity.this, "Successfully Created", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // error
                    Log.e("Error.Response", error.toString());

                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("job", job);
                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

    private void parsePOSTResponse(String response) {
        try {
            JSONObject responseJsonObject = new JSONObject(response);
            String createdAt = responseJsonObject.getString("createdAt");
            name.setText(createdAt);
        } catch (Exception e) {
            Log.e("parsePOSTResponse", e.toString());
        }
    }

//    try {
//        JSONObject postparams = new JSONObject();
//        postparams.put("name", name.getText().toString());
//        postparams.put("job", job.getText().toString());
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
//                Request.Method.POST,
//                url,
//                postparams,
//                new Response.Listener() {
//                    @Override
//                    public void onResponse(Object response) {
//                        parsePOSTResponse(response.toString());
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //Failure Callback
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(jsonObjReq);
//    } catch (Exception e) {
//        Log.e("postRequest", e + "");
//    }

    //this method is for put method.

    private void putRequest(final String name, final String job) {
        String url = "https://reqres.in/api/users/2";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url, response -> {
            // response
            Log.e("Response", response);
            parsePUTResponse(response);
            Toast.makeText(WebServicesActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
        },
                error -> {
                    // error
                    Log.e("Error.Response", error.toString());

                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("job", job);
                return params;
            }
        };
        mRequestQueue.add(putRequest);
    }

    private void parsePUTResponse(String response) {
        try {
            JSONObject responseJsonObject = new JSONObject(response);
            String updatedAt = responseJsonObject.getString("updatedAt");
            name.setText(updatedAt);
        } catch (Exception e) {
            Log.e("parsePUTResponse", e.toString());
        }
    }

// this method is for delete.

    private void deleteRequest() {
        String urlDelete = "https://reqres.in/api/users/2";
        StringRequest dr = new StringRequest(Request.Method.DELETE, urlDelete,
                response -> {
                    // response
                    Log.e("deleteReponse", ">>>>" + response + "<<<<");
                    Toast.makeText(WebServicesActivity.this, response, Toast.LENGTH_LONG).show();
                },
                error -> {
                    // error.

                }
        );
        mRequestQueue.add(dr);
    }


}