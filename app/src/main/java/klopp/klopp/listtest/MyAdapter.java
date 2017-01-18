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
class MyAdapter extends ArrayAdapter<String> {

    Context context;
    public MyAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView== null ) convertView = LayoutInflater.from(context).inflate(R.layout.thelinelayoutfile, null);

        TextView myTextView1 = (TextView)convertView.findViewById(R.id.yourFirstTextView);
        myTextView1.setText(getItem(position)); //getItem gets the item (String in this case) from the list we specified when creating the adapter.
        //position is the current position of the list, and since each position has two items, we have to multiply the position by 2 to get to the right item-list-position.


        ImageLoader mImageLoader;
        ImageView mImageView;
        NetworkImageView mNetworkImageView;

        mNetworkImageView = (NetworkImageView)convertView.findViewById(R.id.networkImageViewOther);
        mImageLoader = MySingleton.getInstance(MainActivity.main_activity).getImageLoader();

        String image = MainActivity.main_activity.getString(R.string.base_url);
        String shit = getItem(position);
        boolean fuck = false;
        for(int i=0;i<shit.length();i++)
        {
            if(shit.charAt(i)=='/')
                fuck = true;
            if(fuck)
            {
                image+=shit.charAt(i);
            }
        }

        TextView myTextView2 = (TextView)convertView.findViewById(R.id.yourSecondTextView);
        myTextView2.setText(image);
        myTextView2.setBackgroundColor(0xFFFF00FF); //Any color. Use setBackgroundResource to use a resource object (drawable etc.)

        mNetworkImageView.setImageUrl(image, mImageLoader);

        return convertView;
    }
}
