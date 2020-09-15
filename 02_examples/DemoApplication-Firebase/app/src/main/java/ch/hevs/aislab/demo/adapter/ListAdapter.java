package ch.hevs.aislab.demo.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.hevs.aislab.demo.R;

public class ListAdapter<T> extends ArrayAdapter<T> {

    private int resource;
    private List<T> data;

    public ListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> data) {
        super(context, resource, data);
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getCustomView(position, convertView, parent);
    }

    public T getItem(int position) {
        return data.get(position);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(resource, parent, false);

            viewHolder = new ListAdapter.ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.tvClientView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListAdapter.ViewHolder) convertView.getTag();
        }
        T item = getItem(position);
        if (item != null) {
            viewHolder.itemView.setText(item.toString());
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView itemView;
    }

    public void updateData(List<T> data) {
        this.data.clear();
        this.data.addAll(data);
        //data = data;
        notifyDataSetChanged();
    }
}
