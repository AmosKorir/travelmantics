package com.alc.travelmantics.util;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alc.travelmantics.ListActivity;
import com.alc.travelmantics.model.TravelDeal;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Korir on 8/2/19.
 * amoskrr@gmail.com
 */
public class FirebaseUtil {
  private static final int RC_SIGN_IN = 123;
  public static FirebaseDatabase mFirebaseDatabase;
  public static DatabaseReference mDatabaseRefences;
  public static FirebaseStorage mStorage;
  public static StorageReference storageReference;
  private static ListActivity activity;
  public static FirebaseAuth mAuth;
  public static boolean isAdmin;
  public static FirebaseAuth.AuthStateListener mAuthStateListener;

  public static FirebaseUtil firebaseUtil;

  public static ArrayList<TravelDeal> mDeals;

  private FirebaseUtil() {
  }

  public static void openFbReference(String ref, final Activity caller) {
    if (firebaseUtil == null) {
      firebaseUtil = new FirebaseUtil();
      mFirebaseDatabase = FirebaseDatabase.getInstance();
      mAuth=FirebaseAuth.getInstance();
      mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
          if (firebaseAuth.getCurrentUser() == null) {
            FirebaseUtil.signin();
          }else {
            String userId=firebaseAuth.getUid();
            checkAdmin(userId);
          }

          connectStorage();
        }

        private void checkAdmin(String userId) {
          isAdmin=false;
          DatabaseReference ref=mFirebaseDatabase.getReference().child("administrators").child(userId);
          ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              FirebaseUtil.isAdmin=true;
              activity.showMenu();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override public void onCancelled(@NonNull DatabaseError databaseError) {

            }
          });

        }
      };
    }
    mDeals = new ArrayList<>();
    activity = (ListActivity) caller;

    mDatabaseRefences = mFirebaseDatabase.getReference().child(ref);


  }

  private static void connectStorage() {
    mStorage=FirebaseStorage.getInstance();
    storageReference=mStorage.getReference().child("deals_pictures");
  }

  private static void signin() {
    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
        new AuthUI.IdpConfig.EmailBuilder().build(),

        new AuthUI.IdpConfig.GoogleBuilder().build());

    // Create and launch sign-in intent
    activity.startActivityForResult(
        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build(),
        RC_SIGN_IN);
  }

  public static void attachAuthListener() {
    mAuth.addAuthStateListener(mAuthStateListener);
  }

  public static void detachListener() {
    mAuth.removeAuthStateListener(mAuthStateListener);
  }
}
