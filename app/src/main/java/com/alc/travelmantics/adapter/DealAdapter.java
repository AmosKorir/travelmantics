package com.alc.travelmantics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.alc.travelmantics.DealActivity;
import com.alc.travelmantics.R;
import com.alc.travelmantics.model.TravelDeal;
import com.alc.travelmantics.util.FirebaseUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Korir on 8/2/19.
 * amoskrr@gmail.com
 */
public class DealAdapter extends RecyclerView.Adapter<DealAdapter.MyViewHolder> {
  private ArrayList<TravelDeal> travelDeals;
  private FirebaseDatabase firebaseDatabase;
  private DatabaseReference databaseReference;
  private ChildEventListener childEventListener;

  public DealAdapter(final Activity activity) {
    travelDeals = FirebaseUtil.mDeals;
    firebaseDatabase = FirebaseUtil.mFirebaseDatabase;
    FirebaseUtil.openFbReference("traveldeals", activity);
    databaseReference = FirebaseUtil.mDatabaseRefences;
    childEventListener = new ChildEventListener() {
      @Override public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        TravelDeal travelDeal = dataSnapshot.getValue(TravelDeal.class);
        travelDeal.setId(dataSnapshot.getKey());
        travelDeals.add(travelDeal);
        notifyItemInserted(travelDeals.size() - 1);
      }

      @Override public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

      }

      @Override public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    };
    databaseReference.addChildEventListener(childEventListener);
  }

  @NonNull @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
    return new MyViewHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    TravelDeal travelDeal = travelDeals.get(position);
    holder.bind(travelDeal);
  }

  @Override public int getItemCount() {
    return travelDeals.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    TextView tvDescription;
    TextView tvPrice;
    ImageView dealImage;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      tvDescription = itemView.findViewById(R.id.tvdescription);
      tvPrice = itemView.findViewById(R.id.tvPrice);
      tvTitle = itemView.findViewById(R.id.tvtitle);
      dealImage=itemView.findViewById(R.id.imageDeal);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          int position = getAdapterPosition();
          TravelDeal selected = travelDeals.get(position);
          v.getContext().startActivity(new Intent(v.getContext(), DealActivity.class)
              .putExtra("Deal", selected)
          );
        }
      });
    }

    public void bind(TravelDeal travelDeal) {
      tvTitle.setText(travelDeal.getTitle());
      tvPrice.setText(travelDeal.getPrice());
      tvDescription.setText(travelDeal.getDescription());
      Picasso.get()
          .load(travelDeal.getImageUrl())
          .into(dealImage);
    }
  }
}
