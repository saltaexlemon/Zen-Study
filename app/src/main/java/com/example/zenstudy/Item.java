package com.example.zenstudy;

public class Item {

    private String itemID;
    private String itemName;
    private int itemPrice;
    private String itemRoomImage;
    private String itemTimerImage;
    private boolean isOwned;

    public Item(String itemID, String itemName, int itemPrice, String itemRoomImage, String itemTimerImage, boolean isOwned){
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemRoomImage = itemRoomImage;
        this.itemTimerImage = itemTimerImage;
        this.isOwned = isOwned;
    }

    //Empty constructor
    public Item(){

    }

    //Getters
    public String getItemID(){return itemID;}
    public String getItemName(){return itemName;}
    public int getItemPrice(){return itemPrice;}
    public String getItemRoomImage(){return itemRoomImage;}
    public String getItemTimerImage(){return itemTimerImage;}
    public boolean getIsOwned(){return isOwned;}

    //Setters
    public void setItemID(String itemID){this.itemID = itemID;}
    public void setItemName(String itemName){this.itemName = itemName;}
    public void setItemPrice(int itemPrice){this.itemPrice = itemPrice;}
    public void setItemRoomImage(String itemRoomImage){this.itemRoomImage = itemRoomImage;}
    public void setItemTimerImage(String itemTimerImage){this.itemTimerImage = itemTimerImage;}
    public void setIsOwned(boolean isOwned){this.isOwned = isOwned;}
}
