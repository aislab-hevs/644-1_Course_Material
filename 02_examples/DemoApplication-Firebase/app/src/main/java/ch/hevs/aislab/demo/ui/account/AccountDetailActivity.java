package ch.hevs.aislab.demo.ui.account;

import androidx.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;
import ch.hevs.aislab.demo.viewmodel.account.AccountViewModel;

public class AccountDetailActivity extends BaseActivity {

    private static final String TAG = "AccountDetailActivity";

    private static final int EDIT_ACCOUNT = 1;

    private AccountEntity account;
    private TextView tvBalance;
    private NumberFormat defaultFormat;

    private AccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_account, frameLayout);

        String accountId = getIntent().getStringExtra("accountId");

        initiateView();

        AccountViewModel.Factory factory = new AccountViewModel.Factory(
                getApplication(), accountId);
        viewModel = new ViewModelProvider(this, factory).get(AccountViewModel.class);
        viewModel.getAccount().observe(this, accountEntity -> {
            if (accountEntity != null) {
                account = accountEntity;
                updateContent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, EDIT_ACCOUNT, Menu.NONE, getString(R.string.title_activity_edit_account))
                .setIcon(R.drawable.ic_edit_white_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == EDIT_ACCOUNT) {
            Intent intent = new Intent(this, EditAccountActivity.class);
            intent.putExtra("accountId", account.getId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateView() {
        tvBalance = findViewById(R.id.accBalance);
        defaultFormat = NumberFormat.getCurrencyInstance();

        Button depositBtn = findViewById(R.id.depositButton);
        depositBtn.setOnClickListener(view -> generateDialog(R.string.action_deposit));

        Button withdrawBtn = findViewById(R.id.withdrawButton);
        withdrawBtn.setOnClickListener(view -> generateDialog(R.string.action_withdraw));
    }

    private void updateContent() {
        if (account != null) {
            setTitle(account.getName());
            tvBalance.setText(defaultFormat.format(account.getBalance()));
            Log.i(TAG, "Activity populated.");
        }
    }

    private void generateDialog(final int action) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.account_actions, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(action));
        alertDialog.setCancelable(false);


        final TextInputEditText accountMovement = view.findViewById(R.id.account_movement);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Double amount = Double.parseDouble(accountMovement.getText().toString());
                Toast toast = Toast.makeText(AccountDetailActivity.this, getString(R.string.error_withdraw), Toast.LENGTH_LONG);

                if (action == R.string.action_withdraw) {
                    if (account.getBalance() < amount) {
                        toast.show();
                        return;
                    }
                    Log.i(TAG, "Withdrawal: " + amount.toString());
                    account.setBalance(account.getBalance() - amount);
                }
                if (action == R.string.action_deposit) {
                    Log.i(TAG, "Deposit: " + amount.toString());
                    account.setBalance(account.getBalance() + amount);
                }
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
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.setView(view);
        alertDialog.show();
    }
}
