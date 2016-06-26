package com.nwanvu.example.objectanimator.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nwanvu.example.objectanimator.R;
import com.nwanvu.example.objectanimator.entities.AppItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class AppItemAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private int[] mSectionIndices;
    private Character[] mSectionLetters;
    private ArrayList<AppItem> appItems;
    private final Context context;

    public AppItemAdapter(Context context, ArrayList<AppItem> appItems) {
        this.context = context;
        this.appItems = appItems;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        ViewHolder holder;// = (ViewHolder) view.getTag();
        if (view == null) {
            view = View.inflate(context, R.layout.item_app, null);
            holder = new ViewHolder();
            holder.appIcon = (ImageView) view.findViewById(R.id.ivAppIcon);
            holder.appName = (TextView) view.findViewById(R.id.tvAppName);
            holder.appRating = (RatingBar) view.findViewById(R.id.ratingBar);
            view.setTag(holder);
        } else holder = (ViewHolder) view.getTag();

        Picasso.with(context).load(appItems.get(i).getAppIcon())
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(holder.appIcon);
        holder.appName.setText(appItems.get(i).getAppName());
        holder.appRating.setRating((float) appItems.get(i).getAppRating());

        return view;
    }

    private int[] getSectionIndices() {
        if (appItems.size() <= 0) return new int[0];
        ArrayList<Integer> sectionIndices = new ArrayList<>();
        char lastFirstChar = appItems.get(0).getAppAuthor().charAt(0);
        sectionIndices.add(0);
        for (int i = 1; i < appItems.size(); i++) {
            if (appItems.get(i).getAppAuthor().charAt(0) != lastFirstChar) {
                lastFirstChar = appItems.get(i).getAppAuthor().charAt(0);
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private Character[] getSectionLetters() {
        Character[] letters = new Character[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = appItems.get(mSectionIndices[i]).getAppAuthor().charAt(0);
        }
        return letters;
    }

    @Override
    public int getCount() {
        return appItems.size();
    }

    @Override
    public Object getItem(int i) {
        return appItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setData(ArrayList<AppItem> phrases) {
        appItems = phrases;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    public ArrayList<AppItem> getData() {
        return appItems;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices == null) return 0;
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        if (mSectionIndices == null) return 0;
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = View.inflate(context, R.layout.list_item_header, null);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name
        CharSequence headerChar = appItems.get(position).getAppAuthor();
        holder.text.setText(headerChar.toString());

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return appItems.get(position).getAppAuthor().charAt(0);
    }

    class HeaderViewHolder {
        TextView text;
    }

    static class ViewHolder {
        ImageView appIcon;
        TextView appName;
        RatingBar appRating;
    }
}
