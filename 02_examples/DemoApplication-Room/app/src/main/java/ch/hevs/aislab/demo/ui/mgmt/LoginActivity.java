package ch.hevs.aislab.demo.ui.mgmt;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.repository.ClientRepository;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.ui.MainActivity;

import static ch.hevs.aislab.demo.database.AppDatabase.initializeDemoData;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private ProgressBar progressBar;

    private ClientRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_login);

        setContentView(R.layout.activity_login);

        repository = ((BaseApp) getApplication()).getClientRepository();
        progressBar = findViewById(R.id.progress);

        // Set up the login form.
        emailView = findViewById(R.id.email);

        passwordView = findViewById(R.id.password);

        Button emailSignInButton = findViewById(R.id.email_sign_in_button);
        emailSignInButton.setOnClickListener(view -> attemptLogin());

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(view -> startActivity(
                new Intent(LoginActivity.this, RegisterActivity.class))
        );

        Button demoDataButton = findViewById(R.id.demo_data_button);
        demoDataButton.setOnClickListener(view -> reinitializeDatabase());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    /**
     * Attempts to sign in or register the client specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            passwordView.setText("");
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            repository.getClient(email, getApplication()).observe(LoginActivity.this, clientEntity -> {
                if (clientEntity != null) {
                    if (clientEntity.getPassword().equals(password)) {
                        // We need an Editor object to make preference changes.
                        // All objects are from android.context.Context
                        SharedPreferences.Editor editor = getSharedPreferences(BaseActivity.PREFS_NAME, 0).edit();
                        editor.putString(BaseActivity.PREFS_USER, clientEntity.getEmail());
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        emailView.setText("");
                        passwordView.setText("");
                    } else {
                        passwordView.setError(getString(R.string.error_incorrect_password));
                        passwordView.requestFocus();
                        passwordView.setText("");
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    emailView.setError(getString(R.string.error_invalid_email));
                    emailView.requestFocus();
                    passwordView.setText("");
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void reinitializeDatabase() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.action_demo_data));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getString(R.string.reset_msg));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_reset), (dialog, which) ->{
            initializeDemoData(AppDatabase.getInstance(this));
            Toast.makeText(this, getString(R.string.demo_data_initiated), Toast.LENGTH_LONG).show();
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }
}

