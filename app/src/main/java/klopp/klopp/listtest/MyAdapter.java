package klopp.klopp.listtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by turupawn on 1/17/17.
 */
class MyAdapter extends ArrayAdapter<Business> {

    Context context;
    public MyAdapter(Context context, int textViewResourceId, ArrayList<Business> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView== null ) convertView = LayoutInflater.from(context).inflate(R.layout.thelinelayoutfile, null);

        TextView myTextView1 = (TextView)convertView.findViewById(R.id.business_name);
        TextView myTextView2 = (TextView)convertView.findViewById(R.id.business_description);
        myTextView1.setText(getItem(position).name);
        myTextView2.setText(getItem(position).description);


        ImageLoader mImageLoader;
        NetworkImageView mNetworkImageView;
        mNetworkImageView = (NetworkImageView)convertView.findViewById(R.id.business_image);
        mImageLoader = MySingleton.getInstance(MainActivity.main_activity).getImageLoader();
        String image_url = MainActivity.main_activity.getString(R.string.base_url)+getItem(position).image_url;
        mNetworkImageView.setImageUrl(image_url, mImageLoader);

        return convertView;
    }
}
