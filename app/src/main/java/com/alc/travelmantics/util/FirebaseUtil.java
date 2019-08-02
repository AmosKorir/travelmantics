package com.alc.travelmantics.util;

import com.alc.travelmantics.model.TravelDeal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

/**
 * Created by Korir on 8/2/19.
 * amoskrr@gmail.com
 */
public class FirebaseUtil {
  public static FirebaseDatabase mFirebaseDatabase;
  public static DatabaseReference mDatabaseRefences;
  public static FirebaseUtil firebaseUtil;

  public static ArrayList<TravelDeal> mDeals;

  private FirebaseUtil() {
  }
  public static void openFbReference(String ref){
    if (firebaseUtil==null) {
      firebaseUtil = new FirebaseUtil();
      mFirebaseDatabase = FirebaseDatabase.getInstance();
    }
    mDeals= new ArrayList<>();

    mDatabaseRefences=mFirebaseDatabase.getReference().child(ref);
  }
}
