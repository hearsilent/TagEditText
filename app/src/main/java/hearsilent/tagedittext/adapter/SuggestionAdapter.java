package hearsilent.tagedittext.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import hearsilent.tagedittext.R;
import hearsilent.tagedittext.models.SuggestionModel;

public class SuggestionAdapter extends ArrayAdapter<SuggestionModel> {

    private final Context mContext;

    private final int mResourceId;
    private final List<SuggestionModel> mList, mTempList, mSuggestionList;

    public SuggestionAdapter(@NonNull Context context, int resourceId,
                             List<SuggestionModel> items) {
        super(context, resourceId, items);
        mContext = context;
        mResourceId = resourceId;
        mList = items;
        mTempList = new ArrayList<>(items);
        mSuggestionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(mResourceId, parent, false);
        }
        SuggestionModel model = getItem(position);
        ImageView avatarImageView = view.findViewById(R.id.imageView_avatar);
        TextView nameTextView = view.findViewById(R.id.textView_name);
        TextView usernameTextView = view.findViewById(R.id.textView_username);
        Glide.with(mContext.getApplicationContext()).load(model.getAvatar()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_user_default_24dp)).into(avatarImageView);
        nameTextView.setText(model.getName());
        usernameTextView.setText(model.getUsername());
        return view;
    }

    @NonNull
    @Override
    public SuggestionModel getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            SuggestionModel model = (SuggestionModel) resultValue;
            return model.getTag();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                String query = charSequence.toString().toLowerCase();
                mSuggestionList.clear();
                for (SuggestionModel model : mTempList) {
                    // Use can filter yourself Id here
                    /*
                    if (model.getId() == Self.get().getID()) {
                        continue;
                    }
                    */
                    if (model.getLabel().toLowerCase().contains(query)
                            || ("@" + model.getUsername()).toLowerCase().contains(query)) {
                        mSuggestionList.add(model);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mSuggestionList;
                filterResults.count = mSuggestionList.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            if (filterResults != null && filterResults.count > 0) {
                SuggestionAdapter.this.addAll((ArrayList<SuggestionModel>) filterResults.values);
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };
}