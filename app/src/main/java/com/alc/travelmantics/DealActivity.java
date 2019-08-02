package com.alc.travelmantics;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alc.travelmantics.model.TravelDeal;
import com.alc.travelmantics.util.FirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {
  private FirebaseDatabase firebaseDatabase;
  private DatabaseReference databaseReference;
  private EditText txtTitle;
  private EditText txtDescription;
  private EditText txtPrice;
  private TravelDeal travelDeal;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_insert);
    firebaseDatabase = FirebaseUtil.mFirebaseDatabase;
    FirebaseUtil.openFbReference("traveldeals");
    databaseReference = FirebaseUtil.mDatabaseRefences;

    txtDescription = findViewById(R.id.txtDescription);
    txtPrice = findViewById(R.id.txtPrice);
    txtTitle = findViewById(R.id.txtTitle);

    travelDeal = (TravelDeal) getIntent().getSerializableExtra("Deal");
    if (travelDeal != null) {
      txtPrice.setText(travelDeal.getTitle());
      txtDescription.setText(travelDeal.getDescription());
      txtTitle.setText(travelDeal.getDescription());
    } else {
      travelDeal = new TravelDeal();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.save_menu, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.save_menu:
        saveDeal();
        Toast.makeText(this, "Deal saved", Toast.LENGTH_SHORT).show();
        clean();
        backTolist();
        return true;

      case R.id.delete_menu:
        deleteDeal();
        Toast.makeText(this, "Deal deleted", Toast.LENGTH_SHORT).show();
        clean();
        backTolist();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void clean() {
    txtTitle.setText("");
    txtPrice.setText("");
    txtDescription.setText("");
    txtTitle.requestFocus();
  }

  private void saveDeal() {
    travelDeal.setTitle(txtTitle.getText().toString().trim());
    travelDeal.setDescription(txtDescription.getText().toString().trim());
    travelDeal.setPrice(txtPrice.getText().toString().trim());
    if (travelDeal.getId() == null) {
      databaseReference.push().setValue(travelDeal);
    } else {
      databaseReference.child(travelDeal.getId()).setValue(travelDeal);
    }
  }

  private void backTolist() {
    startActivity(new Intent(this, ListActivity.class));
  }

  private void deleteDeal() {
    if (travelDeal.getId() == null) {
      Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_SHORT).show();
    } else {
      databaseReference.child(travelDeal.getId()).removeValue();
    }
  }
}
