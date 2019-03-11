package com.ibnsaad.ddc;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ibnsaad.ddc.Network.RetrofitClient;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    EditText mNameText;
    @BindView(R.id.input_last_name)
    EditText mNameTextLastName;
    @BindView(R.id.input_address) EditText mAddressText;
    @BindView(R.id.input_email) EditText mEmailText;
    @BindView(R.id.input_mobile) EditText mMobileText;
    @BindView(R.id.input_password) EditText mPasswordText;
    @BindView(R.id.input_reEnterPassword) EditText mReEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button mSignupButton;
    @BindView(R.id.link_login)
    TextView mLoginLink;
    @BindView(R.id.input_birthday)
    EditText mBirthDay;
    @BindView(R.id.radio_group_gander)
    RadioGroup mRadioGroupGender;

    @BindView(R.id.radio_group_disability)
    RadioGroup mRadioGroupDisability;

    private RadioButton mRadioSexButton;
    Calendar myCalendar;

    private String disablitiy;
    private String mImageUri="";
    @BindView(R.id.image_select)
    ImageView mImageView;
    @BindView(R.id.image_to_select)
    ImageButton mImageButton;

    final int RESULT_LOAD_IMG=10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left, R.anim.push_left_out);
            }
        });

        getBirthDay();

        //for chose image
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

    }


    private void signup() {

        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

       mSignupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = mNameText.getText().toString();
        String lastName=mNameTextLastName.getText().toString();
        String address = mAddressText.getText().toString();
        String email = mEmailText.getText().toString();
        String mobile = mMobileText.getText().toString();
        String password = mPasswordText.getText().toString();

        String birthday=mBirthDay.getText().toString();

        int selectedId=mRadioGroupGender.getCheckedRadioButtonId();
        mRadioSexButton=(RadioButton)findViewById(selectedId);
        String gender=mRadioSexButton.getText().toString();



         //register method
        registerWithNetworkFaster(name,lastName,address,email,mobile,password,birthday,
                gender,mImageUri);



        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }
    public void onSignupSuccess() {
        mSignupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed",
                Toast.LENGTH_LONG).show();

        mSignupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = mNameText.getText().toString();
        String address = mAddressText.getText().toString();
        String email = mEmailText.getText().toString();
        String mobile = mMobileText.getText().toString();
        String password = mPasswordText.getText().toString();
        String reEnterPassword = mReEnterPasswordText.getText().toString();
        String birthday=mBirthDay.getText().toString();
        String lastName=mNameTextLastName.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mNameText.setError("at least 3 characters");
            valid = false;
        } else {
            mNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            mNameText.setError("at least 3 characters");
            valid = false;
        } else {
            mNameText.setError(null);
        }

        if (address.isEmpty()) {
            mAddressText.setError("Enter Valid Address");
            valid = false;
        } else {
            mAddressText.setError(null);
        }

         if (birthday.isEmpty()){
             mBirthDay.setError("Enter Your Birthday");
             valid=false;
         }else {
             mBirthDay.setError(null);
         }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            mMobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mMobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10
                || !(reEnterPassword.equals(password))) {
            mReEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            mReEnterPasswordText.setError(null);
        }
        if (mRadioGroupGender.getCheckedRadioButtonId()==-1){
            Toast.makeText(getApplicationContext(), "Please select Gender",
                    Toast.LENGTH_SHORT).show();
            valid=false;
        }

        if (mRadioGroupDisability.getCheckedRadioButtonId()==-1){
            Toast.makeText(getApplicationContext(), "Please select Disability",
                    Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (mImageUri==""){
            Toast.makeText(this, "Please Select Image"
                    , Toast.LENGTH_SHORT).show();
            valid=false;
        }

        return valid;

    }

    //for radio disability
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_deafness:
                if (checked)
                    // Pirates are the best
                    disablitiy="deafness";
                    Toast.makeText(this, disablitiy, Toast.LENGTH_SHORT).show();
                            
                    break;
            case R.id.radio_blindness:
                if (checked)
                    disablitiy="blindness";
                Toast.makeText(this, disablitiy, Toast.LENGTH_SHORT).show();
                    break;

            case R.id.radio_obstruction:
                if (checked)
                    disablitiy="obstruction";
                Toast.makeText(this, disablitiy, Toast.LENGTH_SHORT).show();
                    break;
        }
    }

    //make method for birthday
    private void getBirthDay()
    {
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        mBirthDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mBirthDay.setText(sdf.format(myCalendar.getTime()));
    }

    private void registerWithRetrofit(String name,String lastName, String address,
                                      String email,  String mobile,String password,
                                      String birthday,String gender,String documentation){



        Call<ResponseBody> call= RetrofitClient
                .getInstance().getApi()
                .createUser(
                        name,
                        lastName,
                        address,
                        mobile,
                        email,
                        password,
                        disablitiy,
                        gender,
                        birthday,
                        documentation);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                try {
                    Toast.makeText(RegisterActivity.this,"Register done..."+
                                    response.body().string(),
                            Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(RegisterActivity.this, "Error Register"+
                                t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerWithNetworkFaster(String name,String lastName, String address,
                                           String email,  String mobile,String password,
                                           String birthday,String gender,String documentation) {

                AndroidNetworking.post("http://disability.eb2a.com/disability/api/getdata.php?type=signup")
                .addBodyParameter(name)
                .addBodyParameter(lastName)
                .addBodyParameter(address)
                .addBodyParameter(mobile)
                .addBodyParameter(email)
                .addBodyParameter(password)
                .addBodyParameter(disablitiy)
                .addBodyParameter(gender)
                .addBodyParameter(birthday)
                .addBodyParameter(documentation)//documention
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(RegisterActivity.this,
                                    "response +"+response.getString("token")
                                    , Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            Toast.makeText(RegisterActivity.this,
                                    "JSONException "+e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(RegisterActivity.this, "Can't Register Now..."
                                +anError.getErrorBody(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                mImageView.setImageBitmap(selectedImage);
                mImageUri=imageUri.toString();
                Toast.makeText(RegisterActivity.this, mImageUri, Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(RegisterActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
