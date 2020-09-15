package ch.hevs.aislab.demo.ui.mgmt;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.repository.ClientRepository;
import ch.hevs.aislab.demo.ui.MainActivity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private ClientRepository repository;

    private Toast toast;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPwd1;
    private EditText etPwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        repository = ((BaseApp) getApplication()).getClientRepository();

        initializeForm();

        toast = Toast.makeText(this, getString(R.string.client_created), Toast.LENGTH_LONG);
    }

    private void initializeForm() {
        etFirstName = findViewById(R.id.firstName);
        etLastName = findViewById(R.id.lastName);
        etEmail = findViewById(R.id.email);
        etPwd1 = findViewById(R.id.password);
        etPwd2 = findViewById(R.id.passwordRep);
        Button saveBtn = findViewById(R.id.editButton);
        saveBtn.setOnClickListener(view -> saveChanges(
                etFirstName.getText().toString(),
                etLastName.getText().toString(),
                etEmail.getText().toString(),
                etPwd1.getText().toString(),
                etPwd2.getText().toString()
        ));
    }

    private void saveChanges(String firstName, String lastName, String email, String pwd, String pwd2) {
        if (!pwd.equals(pwd2) || pwd.length() < 5) {
            etPwd1.setError(getString(R.string.error_invalid_password));
            etPwd1.requestFocus();
            etPwd1.setText("");
            etPwd2.setText("");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.error_invalid_email));
            etEmail.requestFocus();
            return;
        }
        ClientEntity newClient = new ClientEntity(email, firstName, lastName, pwd);

        repository.register(newClient, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "createUserWithEmail: success");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "createUserWithEmail: failure", e);
                setResponse(false);
            }
        });
    }

    private void setResponse(Boolean response) {
        if (response) {
            toast.show();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            etEmail.setError(getString(R.string.error_used_email));
            etEmail.requestFocus();
        }
    }
}
