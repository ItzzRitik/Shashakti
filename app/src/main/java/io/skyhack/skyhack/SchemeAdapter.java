package io.skyhack.skyhack;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ramotion.foldingcell.FoldingCell;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SchemeAdapter extends ArrayAdapter<Schemes> {
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    HomeActivity context;
    private View.OnClickListener defaultRequestBtnClickListener;
    public SchemeAdapter(HomeActivity context, List<Schemes> objects) {
        super(context, 0, objects);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Schemes item = getItem(position);
        FoldingCell folding = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (folding == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            folding = (FoldingCell) vi.inflate(R.layout.view_folding, parent, false);
            viewHolder.title = folding.findViewById(R.id.title);
            viewHolder.title.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/exo2_bold.otf"));
            viewHolder.desc = folding.findViewById(R.id.desc);
            viewHolder.desc.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/exo2.ttf"));
            viewHolder.thumbnail = folding.findViewById(R.id.thumbnail);
            viewHolder.banner = folding.findViewById(R.id.banner);
            viewHolder.title_in = folding.findViewById(R.id.title_in);
            viewHolder.title_in.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/exo2_bold.otf"));
            viewHolder.desc_in = folding.findViewById(R.id.desc_in);
            viewHolder.desc_in.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/exo2.ttf"));

            viewHolder.details = folding.findViewById(R.id.details);
            viewHolder.details.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/exo2.ttf"));
            viewHolder.details2 = folding.findViewById(R.id.details2);
            viewHolder.details2.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/exo2.ttf"));
            viewHolder.apply = folding.findViewById(R.id.apply_button);
            viewHolder.apply.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/vdub.ttf"));
            viewHolder.apply.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.setBackgroundResource(R.drawable.interest_pressed);((TextView)v).setTextColor(Color.parseColor("#ffffff"));
                            break;
                        case MotionEvent.ACTION_UP:
                            v.setBackgroundResource(R.drawable.interest);((TextView)v).setTextColor(Color.parseColor("#ff611c"));
                            context.apply.setVisibility(View.VISIBLE);
                            context.apply.loadUrl("https://goo.gl/forms/zVE1Eg8xqin4WU5f2");
                            break;
                    }
                    return true;
                }
            });;
            folding.setTag(viewHolder);
        }
        else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                folding.unfold(true);
            } else {
                folding.fold(true);
            }
            viewHolder = (ViewHolder) folding.getTag();
        }

        if (null == item)
            return folding;

        viewHolder.title.setText(item.getTitle());
        viewHolder.desc.setText(item.getDesc());
        viewHolder.thumbnail.setImageBitmap(item.getThumbnail());
        viewHolder.banner.setImageBitmap(item.getThumbnail());
        viewHolder.title_in.setText(item.getTitle());
        viewHolder.desc_in.setText(item.getDesc());
        viewHolder.details.setText(item.getDetails()[0]);
        viewHolder.details2.setText(item.getDetails()[0]);
        return folding;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView title,desc,title_in,desc_in,details,details2,apply;
        ImageView thumbnail,banner;
    }
}
