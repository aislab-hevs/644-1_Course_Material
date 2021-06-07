package ch.hevs.aislab.intro.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

import ch.hevs.aislab.intro.R;
import ch.hevs.aislab.intro.database.entity.ClientEntity;
import ch.hevs.aislab.intro.databinding.FragmentClientDetailsBinding;
import ch.hevs.aislab.intro.util.OnAsyncEventListener;
import ch.hevs.aislab.intro.viewmodels.ClientViewModel;

public class ClientDetailsFragment extends Fragment {

    private static final String TAG = "ClientDetailsFragment";
    private static final String KEY_CLIENT_ID = "client_id";

    private boolean isEditing;
    private boolean creationMode;

    private Toast statusToast;
    private long clientId;

    private FragmentClientDetailsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_client_details, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (creationMode) {
            MenuItem item_create = menu.findItem(R.id.action_create);
            item_create.setVisible(creationMode);
        } else {
            MenuItem item_edit = menu.findItem(R.id.action_edit);
            item_edit.setVisible(!isEditing);
            MenuItem item_done = menu.findItem(R.id.action_done);
            item_done.setVisible(isEditing);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            isEditing = !isEditing;
            binding.setIsEditing(isEditing);
            requireActivity().invalidateOptionsMenu();
            return true;
        } else if (id == R.id.action_done || id == R.id.action_create) {
            persistChanges();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_client_details, container, false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        clientId = requireArguments().getLong(KEY_CLIENT_ID);
        creationMode = clientId == 0L;
        binding.setIsEditing(creationMode);

        ClientViewModel.Factory factory = new ClientViewModel.Factory(
                requireActivity().getApplication(), clientId);

        final ClientViewModel viewModel = new ViewModelProvider(this, factory)
                .get(ClientViewModel.class);

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setClientViewModel(viewModel);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    private void persistChanges() {
        String firstName = Objects.requireNonNull(binding.firstName.getText()).toString();
        String lastName = Objects.requireNonNull(binding.lastName.getText()).toString();
        String email = Objects.requireNonNull(binding.email.getText()).toString();

        OnAsyncEventListener asyncEventListener = new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                int stringId;
                if (creationMode) {
                    stringId = R.string.client_created;
                } else {
                    stringId = R.string.client_edited;
                }
                statusToast = Toast.makeText(getContext(), getString(stringId), Toast.LENGTH_LONG);
                statusToast.show();
                Navigation.findNavController(binding.getRoot()).navigateUp();
            }

            @Override
            public void onFailure(Exception e) {
                statusToast = Toast.makeText(getContext(), getString(R.string.action_error), Toast.LENGTH_LONG);
                statusToast.show();
                Log.e(TAG, "Error during persisting changes!", e);
            }
        };

        if (validateInput(firstName,lastName,email)) {
            ClientEntity clientEntity = new ClientEntity(email, firstName, lastName);
            if (creationMode) {
                clientEntity.setCreatedAt(ZonedDateTime.now());
                binding.getClientViewModel().createClient(clientEntity, asyncEventListener);
            } else {
                clientEntity.setId(clientId);
                clientEntity.setCreatedAt(
                        binding.getClientViewModel()
                                .getInstant(binding.createdAt.getText().toString())
                );
                binding.getClientViewModel().updateClient(clientEntity, asyncEventListener);
            }
        }
    }

    private boolean validateInput(String firstName, String lastName, String email) {
        boolean validated = true;
        if (TextUtils.isEmpty(lastName)) {
            binding.lastName.setError(getString(R.string.error_empty_field));
            binding.lastName.requestFocus();
            validated = false;
        }
        if (TextUtils.isEmpty(firstName)) {
            binding.firstName.setError(getString(R.string.error_empty_field));
            binding.firstName.requestFocus();
            validated = false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.setError(getString(R.string.error_invalid_email));
            binding.email.requestFocus();
            validated = false;
        }
        return validated;
    }
}