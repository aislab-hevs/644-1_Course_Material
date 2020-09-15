package ch.hevs.aislab.demo.ui.account;

import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;
import ch.hevs.aislab.demo.viewmodel.account.AccountViewModel;

public class EditAccountActivity extends BaseActivity {

    private static final String TAG = "EditAccountActivity";

    private AccountEntity account;
    private String owner;
    private boolean isEditMode;
    private Toast toast;
    private EditText etAccountName;

    private AccountViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_edit_account, frameLayout);

        navigationView.setCheckedItem(position);

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        owner = settings.getString(BaseActivity.PREFS_USER, null);

        etAccountName = findViewById(R.id.accountName);
        etAccountName.requestFocus();
        Button saveBtn = findViewById(R.id.createAccountButton);
        saveBtn.setOnClickListener(view -> {
            saveChanges(etAccountName.getText().toString());
            onBackPressed();
            toast.show();
        });

        Long accountId = getIntent().getLongExtra("accountId", 0L);
        if (accountId == 0L) {
            setTitle(getString(R.string.title_activity_create_account));
            toast = Toast.makeText(this, getString(R.string.account_created), Toast.LENGTH_LONG);
            isEditMode = false;
        } else {
            setTitle(getString(R.string.title_activity_edit_account));
            saveBtn.setText(R.string.action_update);
            toast = Toast.makeText(this, getString(R.string.account_edited), Toast.LENGTH_LONG);
            isEditMode = true;
        }

        AccountViewModel.Factory factory = new AccountViewModel.Factory(
                getApplication(), accountId);
        viewModel = ViewModelProviders.of(this, factory).get(AccountViewModel.class);
        if (isEditMode) {
            viewModel.getAccount().observe(this, accountEntity -> {
                if (accountEntity != null) {
                    account = accountEntity;
                    etAccountName.setText(account.getName());
                }
            });
        }
    }

    private void saveChanges(String accountName) {
        if (isEditMode) {
            if(!"".equals(accountName)) {
                account.setName(accountName);
                viewModel.updateAccount(account, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "updateAccount: success");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "updateAccount: failure", e);
                    }
                });
            }
        } else {
            AccountEntity newAccount = new AccountEntity();
            newAccount.setOwner(owner);
            newAccount.setBalance(0.0d);
            newAccount.setName(accountName);
            viewModel.createAccount(newAccount, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "createAccount: success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "createAccount: failure", e);
                }
            });
        }
    }
}
