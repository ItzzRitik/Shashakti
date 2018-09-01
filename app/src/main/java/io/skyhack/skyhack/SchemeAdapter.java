package io.skyhack.skyhack;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
public class SchemeAdapter extends RecyclerView.Adapter<SchemeAdapter.MyViewHolder> {
    private List<Schemes> schemes;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        ImageView thumbnail;
        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            thumbnail = view.findViewById(R.id.thumbnail);
            desc = view.findViewById(R.id.desc);
        }
    }
    public SchemeAdapter(List<Schemes> schemes) {
        this.schemes = schemes;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Schemes scheme = schemes.get(position);
        holder.title.setText(scheme.getTitle());
        holder.desc.setText(scheme.getDesc());
        holder.thumbnail.setImageBitmap(scheme.getThumbnail());
    }
    @Override
    public int getItemCount() {
        return schemes.size();
    }
}