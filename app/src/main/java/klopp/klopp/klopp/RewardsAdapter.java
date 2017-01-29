package klopp.klopp.klopp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import klopp.klopp.klopp.R;

/**
 * Created by turupawn on 1/19/17.
 */
public class RewardsAdapter  extends ArrayAdapter<Reward> {

    Context context;
    public RewardsAdapter(Context context, int textViewResourceId, ArrayList<Reward> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView== null ) convertView = LayoutInflater.from(context).inflate(R.layout.reward_row, null);

        TextView myTextView1 = (TextView)convertView.findViewById(R.id.name);
        myTextView1.setText(getItem(position).name);

        TextView myTextView2 = (TextView)convertView.findViewById(R.id.klopps);
        myTextView2.setText(getItem(position).klopps+" Klopps");

        ImageLoader mImageLoader;
        NetworkImageView mNetworkImageView;
        mNetworkImageView = (NetworkImageView)convertView.findViewById(R.id.imagix);
        mImageLoader = MySingleton.getInstance(BusinessActivity.main_activity).getImageLoader();
        String image_url = BusinessActivity.main_activity.getString(R.string.base_url)+getItem(position).image_url;
        mNetworkImageView.setImageUrl(image_url, mImageLoader);

        return convertView;
    }
}
