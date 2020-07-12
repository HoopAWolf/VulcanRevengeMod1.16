package com.hoopawolf.dmm.data;

import java.util.ArrayList;

public class EatItemData
{
    String itemID;
    int duration,
            amplifier,
            foodAmount;
    ArrayList<Integer> listOfEffects;

    EatItemData()
    {
        listOfEffects = new ArrayList<>();
    }

    public String getItemID()
    {
        return itemID;
    }

    public int getDuration()
    {
        return duration;
    }

    public int getAmplifier()
    {
        return amplifier;
    }

    public int getFoodAmount()
    {
        return foodAmount;
    }

    public ArrayList<Integer> getListOfEffects()
    {
        return listOfEffects;
    }
}
