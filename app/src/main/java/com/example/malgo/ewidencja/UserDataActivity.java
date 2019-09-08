package com.example.malgo.ewidencja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.malgo.ewidencja.db.UserDAO;
import com.example.malgo.ewidencja.pojo.User;

public class UserDataActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userSurname;
    private EditText userAddress;
    private EditText userPostcode;
    private EditText userCity;
    private Button saveChangesBtn;
    private UserDAO userDAO;
    private User user;
    private String userLogin;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        GlobalClass m_global = (GlobalClass)getApplicationContext();
        userLogin = m_global.getUserLogin();
        password = m_global.getPassword();

        userName = (EditText)findViewById(R.id.user_data_name);
        userSurname = (EditText)findViewById(R.id.user_data_surname);
        userAddress = (EditText)findViewById(R.id.user_data_address);
        userPostcode = (EditText)findViewById(R.id.user_data_postcode);
        userCity = (EditText)findViewById(R.id.user_data_city);
        saveChangesBtn = (Button)findViewById(R.id.user_data_save_btn);
        saveChangesBtn.setVisibility(View.GONE);

        userDAO = new UserDAO(this, password);

        setUserData();

        userName.addTextChangedListener(new GenericTextWatcher(userName));
        userSurname.addTextChangedListener(new GenericTextWatcher(userSurname));
        userAddress.addTextChangedListener(new GenericTextWatcher(userAddress));
        userPostcode.addTextChangedListener(new GenericTextWatcher(userPostcode));
        userCity.addTextChangedListener(new GenericTextWatcher(userCity));
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void setUserData() {
        user = userDAO.getUserByLogin(userLogin);
        userName.setText(user.getName());
        userSurname.setText(user.getSurname());
        userAddress.setText(user.getAddress());
        userPostcode.setText(user.getPostcode());
        userCity.setText(user.getCity());
    }

    public void saveChanges(View view) {
        user.setName(userName.getText().toString());
        user.setSurname(userSurname.getText().toString());
        user.setAddress(userAddress.getText().toString());
        user.setPostcode(userPostcode.getText().toString());
        user.setCity(userCity.getText().toString());

        userDAO.updateUser(user);
        this.finish();
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        public void afterTextChanged(Editable editable) {
            saveChangesBtn.setVisibility(View.VISIBLE);
//            switch(view.getId()){
//                case R.id.user_data_name:
//                    saveChangesBtn.setVisibility(View.VISIBLE);
//                    break;
//                case R.id.user_data_surname:
//                    saveChangesBtn.setVisibility(View.VISIBLE);
//                    break;
//                case R.id.user_data_address:
//                    saveChangesBtn.setVisibility(View.VISIBLE);
//                    break;
//                case R.id.user_data_postcode:
//                    saveChangesBtn.setVisibility(View.VISIBLE);
//                    break;
//                case R.id.user_data_city:
//                    saveChangesBtn.setVisibility(View.VISIBLE);
//                    break;
//            }
        }
    }
}
