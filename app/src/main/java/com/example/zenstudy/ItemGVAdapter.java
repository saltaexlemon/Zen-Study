package com.example.zenstudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemGVAdapter extends ArrayAdapter<Item> {

    //Constructor
    public ItemGVAdapter(@NonNull Context context, ArrayList<Item> itemArrayList){
        super(context, 0, itemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        //Display the card view
        View listitemView = convertView;
        if (listitemView == null){
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.shop_item_card, parent, false);
        }

        //Retrieve item from arraylist
        Item item = getItem(position);

        //Initialization of elements in the card view
        TextView skinName = listitemView.findViewById(R.id.skinName);
        TextView skinPrice = listitemView.findViewById(R.id.skinPrice);
        ImageView skinImage = listitemView.findViewById(R.id.skinImage);
        ImageView bambooIcon = listitemView.findViewById(R.id.bambooIcon);
        RelativeLayout skinPriceArea = listitemView.findViewById(R.id.skinPriceArea);

        //Set the text for skin name
        skinName.setText(item.getItemName());

        //Determine if the skin is owned by the user
        if (item.getIsOwned()){
            //Display "Owned"
            skinPrice.setText(R.string.shop_skin_owned);

            //Remove display of the bamboo icon
            bambooIcon.setVisibility(View.GONE);

            //Display the background image of the box
            skinPriceArea.setBackgroundResource(R.drawable.card_item_price_owned);
        }

        else {
            //Display the item price
            skinPrice.setText(item.getItemPrice() + "");

            //Display the bamboo icon
            bambooIcon.setVisibility(View.VISIBLE);

            //Display the background image of the box
            skinPriceArea.setBackgroundResource(R.drawable.card_item_price_not_owned);
        }

        //Display the skin image
        Picasso.get().load(item.getItemRoomImage()).into(skinImage);

        return listitemView;
    }
}
