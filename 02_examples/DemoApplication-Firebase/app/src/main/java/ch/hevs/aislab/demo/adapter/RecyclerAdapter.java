package ch.hevs.aislab.demo.adapter;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.util.RecyclerViewItemClickListener;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<T> data;
    private RecyclerViewItemClickListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView textView;
        ViewHolder(TextView textView) {
            super(textView);
            this.textView = textView;
        }
    }

    public RecyclerAdapter(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(view -> listener.onItemClick(view, viewHolder.getAdapterPosition()));
        v.setOnLongClickListener(view -> {
            listener.onItemLongClick(view, viewHolder.getAdapterPosition());
            return true;
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        T item = data.get(position);
        if (item.getClass().equals(AccountEntity.class))
            holder.textView.setText(((AccountEntity) item).getName());
        if (item.getClass().equals(ClientEntity.class))
            holder.textView.setText(((ClientEntity) item).getFirstName() + " " + ((ClientEntity) item).getLastName());
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public void setData(final List<T> data) {
        if (this.data == null) {
            this.data = data;
            notifyItemRangeInserted(0, data.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RecyclerAdapter.this.data.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    if (RecyclerAdapter.this.data instanceof AccountEntity) {
                        return ((AccountEntity) RecyclerAdapter.this.data.get(oldItemPosition)).getId().equals(((AccountEntity) data.get(newItemPosition)).getId());
                    }
                    if (RecyclerAdapter.this.data instanceof ClientEntity) {
                        return ((ClientEntity) RecyclerAdapter.this.data.get(oldItemPosition)).getEmail().equals(
                                ((ClientEntity) data.get(newItemPosition)).getEmail());
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (RecyclerAdapter.this.data instanceof AccountEntity) {
                        AccountEntity newAccount = (AccountEntity) data.get(newItemPosition);
                        AccountEntity oldAccount = (AccountEntity) RecyclerAdapter.this.data.get(newItemPosition);
                        return newAccount.getId().equals(oldAccount.getId())
                                && Objects.equals(newAccount.getName(), oldAccount.getName())
                                && Objects.equals(newAccount.getBalance(), oldAccount.getBalance())
                                && newAccount.getOwner().equals(oldAccount.getOwner());
                    }
                    if (RecyclerAdapter.this.data instanceof ClientEntity) {
                        ClientEntity newClient = (ClientEntity) data.get(newItemPosition);
                        ClientEntity oldClient = (ClientEntity) RecyclerAdapter.this.data.get(newItemPosition);
                        return Objects.equals(newClient.getEmail(), oldClient.getEmail())
                                && Objects.equals(newClient.getFirstName(), oldClient.getFirstName())
                                && Objects.equals(newClient.getLastName(), oldClient.getLastName())
                                && newClient.getPassword().equals(oldClient.getPassword());
                    }
                    return false;
                }
            });
            this.data = data;
            result.dispatchUpdatesTo(this);
        }
    }
}
