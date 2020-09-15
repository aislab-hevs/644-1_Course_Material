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

    private List<T> mData;
    private RecyclerViewItemClickListener mListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;
        ViewHolder(TextView textView) {
            super(textView);
            mTextView = textView;
        }
    }

    public RecyclerAdapter(RecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(view -> mListener.onItemClick(view, viewHolder.getAdapterPosition()));
        v.setOnLongClickListener(view -> {
            mListener.onItemLongClick(view, viewHolder.getAdapterPosition());
            return true;
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        T item = mData.get(position);
        if (item.getClass().equals(AccountEntity.class))
            holder.mTextView.setText(((AccountEntity) item).getName());
        if (item.getClass().equals(ClientEntity.class))
            holder.mTextView.setText(((ClientEntity) item).getFirstName() + " " + ((ClientEntity) item).getLastName());
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public void setData(final List<T> data) {
        if (mData == null) {
            mData = data;
            notifyItemRangeInserted(0, data.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mData.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof AccountEntity) {
                        return ((AccountEntity) mData.get(oldItemPosition)).getId().equals(((AccountEntity) data.get(newItemPosition)).getId());
                    }
                    if (mData instanceof ClientEntity) {
                        return ((ClientEntity) mData.get(oldItemPosition)).getEmail().equals(
                                ((ClientEntity) data.get(newItemPosition)).getEmail());
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof AccountEntity) {
                        AccountEntity newAccount = (AccountEntity) data.get(newItemPosition);
                        AccountEntity oldAccount = (AccountEntity) mData.get(newItemPosition);
                        return newAccount.getId().equals(oldAccount.getId())
                                && Objects.equals(newAccount.getName(), oldAccount.getName())
                                && Objects.equals(newAccount.getBalance(), oldAccount.getBalance())
                                && newAccount.getOwner().equals(oldAccount.getOwner());
                    }
                    if (mData instanceof ClientEntity) {
                        ClientEntity newClient = (ClientEntity) data.get(newItemPosition);
                        ClientEntity oldClient = (ClientEntity) mData.get(newItemPosition);
                        return Objects.equals(newClient.getEmail(), oldClient.getEmail())
                                && Objects.equals(newClient.getFirstName(), oldClient.getFirstName())
                                && Objects.equals(newClient.getLastName(), oldClient.getLastName())
                                && newClient.getPassword().equals(oldClient.getPassword());
                    }
                    return false;
                }
            });
            mData = data;
            result.dispatchUpdatesTo(this);
        }
    }
}
