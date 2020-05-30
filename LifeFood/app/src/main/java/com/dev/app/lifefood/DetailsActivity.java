package com.dev.app.lifefood;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dev.app.lifefood.adapter.DetailsViewAdapter;
import com.dev.app.lifefood.util.ImageUploadInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailsActivity extends AppCompatActivity {

    // Creating DatabaseReference.
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        recyclerView = (RecyclerView) findViewById(R.id.details_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(
                this ,1,GridLayoutManager.VERTICAL,false);
        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(DetailsActivity.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Product Details.");

        // Showing progress dialog.
        progressDialog.show();

        //get the category from Intent :
        Intent pre_intent = getIntent();
        String st_category = pre_intent.getStringExtra("Category");

        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = fStore.collection("product_details");
        Query query = collectionReference.whereEqualTo("store_category",st_category);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Log.d("TAG", "onSuccess: LIST EMPTY");
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Product is not available  ", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    for(DocumentSnapshot document : queryDocumentSnapshots){
                        String name = document.getString("product_name");
                        String url = document.getString("image_url");
                        String category = document.getString("store_category");
                        String discount = document.getString("discount");
                        String quantity = document.getString("quantity");
                        String user_rate = document.getString("price");
                        ImageUploadInfo imageUploadInfo = new ImageUploadInfo();
                        imageUploadInfo.setImageName(name);
                        imageUploadInfo.setImageURL(url);
                        imageUploadInfo.setStoreCategory(category);
                        imageUploadInfo.setUser_discount(discount);
                        imageUploadInfo.setUser_price(user_rate);
                        imageUploadInfo.setDefault_quantity(quantity);
                        list.add(imageUploadInfo);
                    }
                    adapter = new DetailsViewAdapter(getApplicationContext(), list);

                    recyclerView.setAdapter(adapter);

                    // Hiding the progress dialog.
                    progressDialog.dismiss();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}

