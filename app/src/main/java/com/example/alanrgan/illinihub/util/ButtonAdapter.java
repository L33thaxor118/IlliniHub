package com.example.alanrgan.illinihub.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alanrgan.illinihub.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.SimpleViewHolder> {
  private final SortedArrayList<String> objects;
  private final SortedArrayList<String> buttonLabels;
  private ClickListener mListener;
  private Context context;

  public ButtonAdapter(@NonNull Context context, SortedArrayList<String> objects, ClickListener listener) {
    this.context = context;
    this.objects = objects;
    this.buttonLabels = new SortedArrayList<>();
    this.buttonLabels.addAll(objects);

    mListener = listener;
  }

  public static class SimpleViewHolder extends RecyclerView.ViewHolder {
    public final Button button;

    public SimpleViewHolder(View view) {
      super(view);
      button = (Button) view.findViewById(R.id.tag_button);
    }
  }

  public void filter(String q) {
    final String query = q.toLowerCase();
    objects.clear();
    if (query.length() == 0) {
      buttonLabels.forEach(objects::insertSorted);
    } else {
      buttonLabels.stream()
          .filter(lbl -> lbl.toLowerCase().contains(query))
          .forEach(objects::insertSorted);
    }

    notifyDataSetChanged();
  }

  @Override
  public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(this.context).inflate(R.layout.tag, parent, false);
    return new SimpleViewHolder(view);
  }

  @Override
  public void onBindViewHolder(SimpleViewHolder holder, final int position) {
    holder.button.setText(objects.get(position));
    holder.button.setOnClickListener(evt -> mListener.onButtonAdapterItemClick(this, objects.get(position), position));
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getItemCount() {
    return this.objects.size();
  }

  public interface ClickListener {
    void onButtonAdapterItemClick(ButtonAdapter adapter, String item, int position);
  }
}
