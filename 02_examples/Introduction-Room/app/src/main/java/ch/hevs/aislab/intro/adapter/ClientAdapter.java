package ch.hevs.aislab.intro.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import ch.hevs.aislab.intro.R;
import ch.hevs.aislab.intro.database.entity.ClientEntity;
import ch.hevs.aislab.intro.databinding.ItemClientBinding;
import ch.hevs.aislab.intro.util.RecyclerViewItemClickListener;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private List<ClientEntity> clientList;
    private final RecyclerViewItemClickListener clickListener;

    public ClientAdapter(RecyclerViewItemClickListener clickListener) {
        this.clickListener = clickListener;
        setHasStableIds(true);
    }

    public void setClientList(final List<ClientEntity> clientList) {
        if (this.clientList == null) {
            this.clientList = clientList;
            notifyItemRangeInserted(0, clientList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ClientAdapter.this.clientList.size();
                }

                @Override
                public int getNewListSize() {
                    return clientList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

                    if (ClientAdapter.this.clientList instanceof ClientEntity) {
                        return (ClientAdapter.this.clientList.get(oldItemPosition)).getId().equals(
                                (clientList.get(newItemPosition)).getId());
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ClientEntity newClient = clientList.get(newItemPosition);
                    ClientEntity oldClient = ClientAdapter.this.clientList.get(newItemPosition);
                    return newClient.getId().equals(oldClient.getId())
                            && TextUtils.equals(newClient.getEmail(), oldClient.getEmail())
                            && TextUtils.equals(newClient.getFirstName(), oldClient.getFirstName())
                            && TextUtils.equals(newClient.getLastName(), oldClient.getLastName())
                            && newClient.getCreatedAt().equals(oldClient.getCreatedAt());
                }
            });
            this.clientList = clientList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    @NonNull
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClientBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_client,
                        parent, false);
        /*
        For some reasons, onLongClick is not recognized in the xml schema, therefore we implement
        both click and long click listeners programmatically.
         */
        binding.tvRecyclerView.setOnClickListener(v -> clickListener.onClick(binding.getClient()));
        binding.tvRecyclerView.setOnLongClickListener(v ->{
            clickListener.onLongClick(binding.getClient());
            return true;
        });
        return new ClientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        holder.binding.setClient(clientList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return clientList == null ? 0 : clientList.size();
    }

    static class ClientViewHolder extends RecyclerView.ViewHolder {

        final ItemClientBinding binding;

        public ClientViewHolder(ItemClientBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
