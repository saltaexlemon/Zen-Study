package com.example.zenstudy;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Shop extends AppCompatActivity{

    //Constant variable for the URL of the database
    final String firebaseURL = "https://zenstudy-f8dab-default-rtdb.asia-southeast1.firebasedatabase.app/";
    final DatabaseReference reference = FirebaseDatabase.getInstance(firebaseURL).getReference().child(HomeMain.userID);

    //Initialization of variable to store items in arraylist
    private ArrayList<Item> itemArrayList = new ArrayList<>();

    //Initialization of variable for the number of bamboo (currency)
    private int bambooNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        //Initialization of elements in the shop page
        GridView skinsGridView = findViewById(R.id.skinsGridView);
        TextView bambooCurrency = findViewById(R.id.bambooNumber);
        Button backButton = findViewById(R.id.backButton);

        //Return to home activity
        backButton.setOnClickListener(v -> finish());

        //Retrieve data from Firebase
        DatabaseReference userReference = reference.child("User");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Retrieve the number of bamboo (currency)
                bambooNum = snapshot.child("Bamboo").getValue(int.class);

                //Display the number of bamboo (currency)
                bambooCurrency.setText(Integer.toString(bambooNum));
            }

            //Display database error if any
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Shop.this, "Unable to connected to database. Please try again. Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        //Retrieve data from firebase
        DatabaseReference itemReference = reference.child("Item");
        itemReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Clear the item array list
                itemArrayList.clear();

                //Loop to retrieve all data in Item
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //Retrieve the data for each item
                    String itemID = dataSnapshot.child("ItemID").getValue(String.class);
                    String itemName = dataSnapshot.child("ItemName").getValue(String.class);
                    int itemPrice = dataSnapshot.child("ItemPrice").getValue(int.class);
                    String itemRoomImage = dataSnapshot.child("ItemRoomImage").getValue(String.class);
                    String itemTimerImage = dataSnapshot.child("ItemTimerImage").getValue(String.class);
                    boolean isOwned = dataSnapshot.child("IsOwned").getValue(boolean.class);

                    //Add the item to the itemArraylist
                    itemArrayList.add(new Item(itemID + "", itemName + "", itemPrice, itemRoomImage, itemTimerImage, isOwned));
                }

                //Set the gridview for the itemArraylist
                ItemGVAdapter adapter = new ItemGVAdapter(Shop.this, itemArrayList);
                skinsGridView.setAdapter(adapter);
            }

            //Display database errors if any
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Shop.this, "Unable to connected to database. Please try again. Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        //Display dialog box when item in gridview is clicked
        skinsGridView.setOnItemClickListener((parent, view, position, id) -> {

            //Retrieve the item that was clicked
            Item item = itemArrayList.get(position);

            //Allow user to purchase skin if item is not owned
            if (!item.getIsOwned()){
                purchaseSkin(item);
            }

            //Allow user to equip skin if item is owned
            else{
                equipSkin(item);
            }
        });
    }

    /**
     * Allow users to purchase skins
     * @param item the item to be purchased
     */
    public void purchaseSkin(Item item){
        //Create a dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.shop_purchase, null));
        dialog = dialogBuilder.create();

        //Display the dialog box
        dialog.show();

        //Initialization of elements in the dialog box
        ImageView previewArea = dialog.findViewById(R.id.previewArea);
        Button purchaseYes = dialog.findViewById(R.id.purchaseYes);
        Button purchaseNo = dialog.findViewById(R.id.purchaseNo);

        //Display the skin image
        Picasso.get().load(item.getItemRoomImage()).into(previewArea);

        //Allow users to purchase the item
        purchaseYes.setOnClickListener(v -> {

            //Allow user to purchase item if bamboo is sufficient
            if (bambooNum >= item.getItemPrice()){

                //Deduct the user's bamboo after purchase
                bambooNum -= item.getItemPrice();

                //Edit the values in the database
                DatabaseReference userReference = reference;
                userReference.child("User").child("EquippedItemRoom").setValue(item.getItemRoomImage());
                userReference.child("User").child("EquippedItemTimer").setValue(item.getItemTimerImage());
                userReference.child("User").child("Bamboo").setValue(bambooNum);
                userReference.child("Item").child(item.getItemID()).child("IsOwned").setValue(true);

                //Close the dialog box
                dialog.dismiss();
            }

            //Prevent user from purchasing item if bamboo is insufficient
            else{
                Toast.makeText(Shop.this, "Insufficient bamboo.", Toast.LENGTH_SHORT).show();
            }
        });

        //Allow users to close the dialog box if they do not want to purchase the item
        purchaseNo.setOnClickListener(v -> dialog.dismiss());
    }

    /**
     * Allow users to equip skin
     * @param item the item to be equipped
     */
    public void equipSkin(Item item){
        //Create a dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.shop_equip, null));
        dialog = dialogBuilder.create();

        //Display the dialog box
        dialog.show();

        //Initialization of elements in the dialog box
        ImageView previewArea = dialog.findViewById(R.id.previewArea);
        Button equipYes = dialog.findViewById(R.id.equipYes);
        Button equipNo = dialog.findViewById(R.id.equipNo);

        //Display the skin image
        Picasso.get().load(item.getItemRoomImage()).into(previewArea);

        //Allow users to equip the skin
        equipYes.setOnClickListener(v -> {

            //Edit the values in the database
            DatabaseReference userReference = reference;
            userReference.child("User").child("EquippedItemRoom").setValue(item.getItemRoomImage());
            userReference.child("User").child("EquippedItemTimer").setValue(item.getItemTimerImage());

            //Close the dialog box
            dialog.dismiss();
        });

        //Allow users to close the dialog box if they do not want to equip the skin
        equipNo.setOnClickListener(v -> dialog.dismiss());
    }
}
