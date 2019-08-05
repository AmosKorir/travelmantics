package com.alc.travelmantics;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alc.travelmantics.adapter.DealAdapter;
import com.alc.travelmantics.util.FirebaseUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ListActivity extends AppCompatActivity {
  private RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.listactivity_menu, menu);
    MenuItem insertMenu=menu.findItem(R.id.insert_menu);
    if (FirebaseUtil.isAdmin==true){
      insertMenu.setVisible(true);
    }else {
      insertMenu.setVisible(false);
    }
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.insert_menu:
        startActivity(new Intent(this, DealActivity.class));
        return true;
      case R.id.logout_menu:
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
              public void onComplete(@NonNull Task<Void> task) {
                FirebaseUtil.attachAuthListener();
              }
            });
        FirebaseUtil.detachListener();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override protected void onPause() {
    super.onPause();
    FirebaseUtil.openFbReference("traveldeals", ListActivity.this);
    FirebaseUtil.detachListener();
  }

  @Override protected void onResume() {
    super.onResume();
    FirebaseUtil.openFbReference("traveldeals", ListActivity.this);
    FirebaseUtil.attachAuthListener();
    recyclerView = findViewById(R.id.deal_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(new DealAdapter(ListActivity.this));
  }

  public void showMenu() {
    invalidateOptionsMenu();
  }
}
