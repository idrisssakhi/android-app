package com.example.said.villefuteeips2017;

/**
 * Created by idriss on 1/11/18.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class StoreAdapter extends ArrayAdapter<Store> {

    public StoreAdapter(Context context, List<Store> stores) {
        super(context, 0, stores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_store,parent, false);
        }

        StoreViewHolder viewHolder = (StoreViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new StoreViewHolder();
            viewHolder.StoreName = (TextView) convertView.findViewById(R.id.store_name);
            viewHolder.locality = (TextView) convertView.findViewById(R.id.Store_locality);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.Store_phone);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            viewHolder.description = (TextView) convertView.findViewById(R.id.store_description);
            viewHolder.manageLayout = (LinearLayout) convertView.findViewById(R.id.manageLayout);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<>
        Store store = getItem(position);
        viewHolder.StoreName.setText(store.getStoreName());
        viewHolder.locality.setText(store.getStoreLocality());
        viewHolder.phone.setText(store.getStorePhone());
        Bitmap imgBitMap= BitmapFactory.decodeByteArray(store.getStoreImageData(),0,store.getStoreImageData().length);
        viewHolder.avatar.setImageBitmap(imgBitMap);
        viewHolder.description.setText(store.getStoreDescription());

        viewHolder.manageLayout.setId(store.getId());

        return convertView;
    }

    private class StoreViewHolder{
        public TextView StoreName;
        public TextView locality;
        public TextView phone;
        public ImageView avatar;
        public TextView description;
        public LinearLayout manageLayout;

    }
}
