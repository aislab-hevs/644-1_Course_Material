package ch.hevs.aislab.demo.ui.transaction;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.adapter.ListAdapter;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.pojo.ClientWithAccounts;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.ui.MainActivity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;
import ch.hevs.aislab.demo.viewmodel.account.AccountListViewModel;

public class TransactionActivity extends BaseActivity {

    private static final String TAG = "TransactionActivity";

    private Spinner spinnerFromAccount;
    private Spinner spinnerToAccount;

    private SortedMap<ClientEntity, List<AccountEntity>> clientEntityMultimap;

    private ListAdapter<AccountEntity> adapterFromAccount;
    private ListAdapter<AccountEntity> adapterToAccount;

    private AccountListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_transaction, frameLayout);

        setTitle(getString(R.string.title_activity_transaction));
        navigationView.setCheckedItem(position);

        setupFromAccSpinner();
        setupToAccSpinner();
        setupViewModels();

        final Toast toast = Toast.makeText(this, getString(R.string.transaction_executed), Toast.LENGTH_LONG);
        Button transactionBtn = findViewById(R.id.btn_transaction);
        transactionBtn.setOnClickListener(view -> {
            if (executeTransaction()) {
                toast.show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == BaseActivity.position) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        /*
        The activity has to be finished manually in order to guarantee the navigation hierarchy working.
        */
        finish();
        return super.onNavigationItemSelected(item);
    }

    private void setupViewModels() {
        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        String user = settings.getString(BaseActivity.PREFS_USER, null);

        AccountListViewModel.Factory factory = new AccountListViewModel.Factory(
                getApplication(), user);
        viewModel = ViewModelProviders.of(this, factory).get(AccountListViewModel.class);
        viewModel.getOwnAccounts().observe(this, accountEntities -> {
            if (accountEntities != null) {
                updateFromAccSpinner(accountEntities);
            }
        });

        viewModel.getClientAccounts().observe(this, clientAccounts -> {
            if (clientAccounts != null) {
                setupMap(clientAccounts);
            }
        });
    }

    /*
    This is reinitializing the data for ToClient and ToAccount every time there are changes
    coming from the ViewModel.
     */
    private void setupMap(List<ClientWithAccounts> clientWithAccounts) {
        clientEntityMultimap = new TreeMap<>();
        for (ClientWithAccounts cA : clientWithAccounts) {
            clientEntityMultimap.put(cA.client, cA.accounts);
        }
        setupToClientSpinner();
    }

    private void setupFromAccSpinner() {
        spinnerFromAccount = findViewById(R.id.spinner_from);
        adapterFromAccount = new ListAdapter<>(this, R.layout.row_client, new ArrayList<>());
        spinnerFromAccount.setAdapter(adapterFromAccount);
    }

    private void setupToClientSpinner() {
        Spinner spinnerToClient = findViewById(R.id.spinner_toClient);
        ListAdapter<ClientEntity> adapterToClient = new ListAdapter<>(this, R.layout.row_client,
                new ArrayList<>(clientEntityMultimap.keySet())
        );
        spinnerToClient.setAdapter(adapterToClient);
        spinnerToClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateToAccSpinner((ClientEntity) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void setupToAccSpinner() {
        spinnerToAccount = findViewById(R.id.spinner_toAcc);
        adapterToAccount = new ListAdapter<>(this, R.layout.row_client, new ArrayList<>());
        spinnerToAccount.setAdapter(adapterToAccount);
    }

    private void updateToAccSpinner(ClientEntity client) {
        adapterToAccount.updateData(clientEntityMultimap.get(client));
    }

    private void updateFromAccSpinner(List<AccountEntity> accounts) {
        adapterFromAccount.updateData(new ArrayList<>(accounts));
    }

    private boolean executeTransaction() {
        EditText amountEditText = findViewById(R.id.transaction_amount);
        String stringAmount = amountEditText.getText().toString();
        Double amount;
        if (!stringAmount.isEmpty()) {
            amount = Double.parseDouble(stringAmount);
        } else {
            amountEditText.setError(getString(R.string.transaction_no_amount));
            amountEditText.requestFocus();
            return false;
        }

        AccountEntity fromAccount = (AccountEntity) spinnerFromAccount.getSelectedItem();
        AccountEntity toAccount = (AccountEntity) spinnerToAccount.getSelectedItem();
        if (fromAccount != null && toAccount != null) {
            if (amount <= 0.0d) {
                amountEditText.setError(getString(R.string.error_transaction_negativ));
                amountEditText.requestFocus();
                return false;
            }
            if (fromAccount.getBalance() - amount < 0.0d) {
                amountEditText.setError(getString(R.string.error_transaction));
                amountEditText.requestFocus();
                return false;
            }
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);
            viewModel.executeTransaction(fromAccount, toAccount, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "transaction: success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "transaction: failure", e);
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.transaction_no_selection), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
