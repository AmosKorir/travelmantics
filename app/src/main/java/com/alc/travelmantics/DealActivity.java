package com.alc.travelmantics;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.alc.travelmantics.model.TravelDeal;
import com.alc.travelmantics.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DealActivity extends AppCompatActivity {
  public static final int REQUEST_CODE = 42;
  private FirebaseDatabase firebaseDatabase;
  private DatabaseReference databaseReference;
  private EditText txtTitle;
  private EditText txtDescription;
  private EditText txtPrice;
  private ImageView dealImage;
  private Button uploadButton;
  private TravelDeal travelDeal;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_insert);
    firebaseDatabase = FirebaseUtil.mFirebaseDatabase;
    databaseReference = FirebaseUtil.mDatabaseRefences;

    txtDescription = findViewById(R.id.txtDescription);
    txtPrice = findViewById(R.id.txtPrice);
    txtTitle = findViewById(R.id.txtTitle);
    uploadButton = findViewById(R.id.uploadButton);
    dealImage = findViewById(R.id.dealImage);

    uploadButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Insert Picture"), REQUEST_CODE);
      }
    });

    travelDeal = (TravelDeal) getIntent().getSerializableExtra("Deal");
    if (travelDeal != null) {
      txtPrice.setText(travelDeal.getTitle());
      txtDescription.setText(travelDeal.getDescription());
      txtTitle.setText(travelDeal.getTitle());
    } else {
      travelDeal = new TravelDeal();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.save_menu, menu);
    if (FirebaseUtil.isAdmin) {
      setEditTextEnabled(true);
      menu.findItem(R.id.delete_menu).setVisible(true);
      menu.findItem(R.id.save_menu).setVisible(true);
      uploadButton.setVisibility(View.VISIBLE);
    } else {
      setEditTextEnabled(false);
      menu.findItem(R.id.delete_menu).setVisible(false);
      menu.findItem(R.id.save_menu).setVisible(false);
      uploadButton.setVisibility(View.INVISIBLE);

    }
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
      deleteImage(travelDeal.getImageName());
      databaseReference.child(travelDeal.getId()).removeValue();
    }
  }

  private void setEditTextEnabled(boolean state) {
    txtTitle.setEnabled(state);
    txtDescription.setEnabled(state);
    txtPrice.setEnabled(state);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
      Uri imageUri = data.getData();
      final StorageReference ref =
          FirebaseUtil.storageReference.child(imageUri.getLastPathSegment());
      ref.putFile(imageUri)
          .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

              ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                  String downloadUrl = task.getResult().toString();
                  travelDeal.setImageUrl(downloadUrl);
                  travelDeal.setImageName(taskSnapshot.getStorage().getName());
                  showImage(downloadUrl);
                }
              });
            }
          });
    }
  }

  private void deleteImage(String name) {
    try{
      StorageReference ref = FirebaseUtil.storageReference.child(name);
      ref.delete();
    }catch (Exception e){

    }

  }

  private void showImage(String url) {
    int width = Resources.getSystem().getDisplayMetrics().widthPixels;
    Picasso.get()
        .load(url)
        .centerCrop()
        .resize(width, width * 2 / 3)
        .into(dealImage);
  }
}
