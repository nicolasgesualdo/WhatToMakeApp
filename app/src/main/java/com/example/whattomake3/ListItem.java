package com.example.whattomake3;

public class ListItem {
    private String ingText;

    public ListItem(){
        ingText = "";
    }

    public ListItem(String theIngText){
        ingText = theIngText;
    }

    public String getIngText(){
        return ingText;
    }

    public void setIngText(String theIngText){
        ingText = theIngText;
    }
}
