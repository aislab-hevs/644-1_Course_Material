package ch.hevs.aislab.intro.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ch.hevs.aislab.intro.R;
import ch.hevs.aislab.intro.adapter.ClientAdapter;
import ch.hevs.aislab.intro.database.entity.ClientEntity;
import ch.hevs.aislab.intro.databinding.FragmentClientListBinding;
import ch.hevs.aislab.intro.util.OnAsyncEventListener;
import ch.hevs.aislab.intro.util.RecyclerViewItemClickListener;
import ch.hevs.aislab.intro.viewmodels.ClientListViewModel;

public class ClientListFragment extends Fragment {

    private static final String TAG = "ClientListFragment";

    private FragmentClientListBinding binding;

    private ClientAdapter clientAdapter;
    private ClientListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_list, container, false);

        clientAdapter = new ClientAdapter(new RecyclerViewItemClickListener() {
            @Override
            public void onClick(ClientEntity client) {
                Log.d(TAG, "clicked:" + client.toString());
                Bundle bundle = new Bundle();
                bundle.putLong("client_id", client.getId());
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_client_list_to_client_details, bundle);
            }

            @Override
            public void onLongClick(ClientEntity client) {
                Log.d(TAG, "longClicked:" + client.toString());
                createDeleteDialog(client);
            }
        });

        binding.clientList.setAdapter(clientAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                binding.clientList.getContext(),
                LinearLayoutManager.VERTICAL
        );
        binding.clientList.addItemDecoration(dividerItemDecoration);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ClientListViewModel.class);
        viewModel.getClients().observe(getViewLifecycleOwner(), myClients -> {
            if (myClients != null) {
                clientAdapter.setClientList(myClients);
            }
        });

        binding.createClientFab.setOnClickListener((v -> Navigation
                .findNavController(binding.getRoot())
                .navigate(R.id.action_client_list_to_client_details))
        );
    }

    @Override
    public void onDestroyView() {
        binding = null;
        clientAdapter = null;
        super.onDestroyView();
    }

    private void createDeleteDialog(final ClientEntity client) {
        LayoutInflater inflater = LayoutInflater.from(binding.getRoot().getContext());
        final View view = inflater.inflate(R.layout.dialog_delete_item, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(binding.getRoot().getContext()).create();
        alertDialog.setTitle(getString(R.string.title_activity_delete_account));
        alertDialog.setCancelable(false);

        final TextView deleteMessage = view.findViewById(R.id.tv_delete_item);
        deleteMessage.setText(String.format(getString(R.string.client_delete_msg), client.toString()));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_accept), (dialog, which) -> {
            Toast toast = Toast.makeText(
                    binding.getRoot().getContext(),
                    getString(R.string.client_deleted),
                    Toast.LENGTH_LONG
            );
            viewModel.deleteClient(client, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    toast.show();
                    Log.d(TAG, "deleteAccount: success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "deleteAccount: failure", e);
                }
            });
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
        alertDialog.setView(view);
        alertDialog.show();
    }
}