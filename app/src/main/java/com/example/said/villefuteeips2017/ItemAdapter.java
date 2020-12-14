package com.example.said.villefuteeips2017;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by idriss on 1/17/18.
 */

public class ItemAdapter extends ArrayAdapter<Item> {

    public ItemAdapter(Context context, List<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item,parent, false);
        }

        ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ItemAdapter.ItemViewHolder();
            viewHolder.articlCategory = (TextView) convertView.findViewById(R.id.item_category);
            viewHolder.articleName = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.articlePrice = (TextView) convertView.findViewById(R.id.item_price);
            viewHolder.storelocality = (TextView) convertView.findViewById(R.id.item_locality);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);


            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<>
        Item item = getItem(position);
        viewHolder.articlCategory.setText(item.getArticlCategory());
        viewHolder.articleName.setText(item.getArticleName());
        viewHolder.articlePrice.setText(String.valueOf(item.getPrice()));
        viewHolder.storelocality.setText(item.getLocality());
        Bitmap imgBitMap= BitmapFactory.decodeByteArray(item.getImageImageData(),0,item.getImageImageData().length);
        viewHolder.avatar.setImageBitmap(imgBitMap);

        return convertView;
    }

    private class ItemViewHolder{
        public TextView articlCategory;
        public TextView articleName;
        public TextView articlePrice;
        public TextView storelocality;
        public ImageView avatar;

    }
}
