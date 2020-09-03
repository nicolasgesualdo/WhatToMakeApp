package com.example.whattomake3;

public class FridgeItem {
    private String ingText;

    public FridgeItem(){
        ingText = "";
    }

    public FridgeItem(String theIngText){
        ingText = theIngText;
    }

    public String getIngText(){
        return ingText;
    }

    public void setIngText(String theIngText){
        ingText = theIngText;
    }
}
