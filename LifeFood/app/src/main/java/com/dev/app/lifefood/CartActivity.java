package com.dev.app.lifefood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.app.lifefood.adapter.CartViewAdapter;
import com.dev.app.lifefood.util.CartDAO;
import com.dev.app.lifefood.util.ImageUploadInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {
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

    TextView total_priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //getting cart Object:
        List<CartDAO> cartlist = Paper.book().read("carts");
        //setting the total price
        total_priceTextView = findViewById(R.id.total_price);
        total_priceTextView.setText(String.valueOf(getTotal(cartlist)));

        recyclerView = (RecyclerView) findViewById(R.id.cart_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(
                this ,1,GridLayoutManager.VERTICAL,false);
        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(CartActivity.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Carts Details.");

        // Showing progress dialog.
        progressDialog.show();

        //get the from details screen :

        if(cartlist.isEmpty()){
            Toast.makeText(getApplicationContext(), "Hey Bhava, Ur cart is Empty", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            adapter = new CartViewAdapter(getApplicationContext(), cartlist);

            recyclerView.setAdapter(adapter);

            // Hiding the progress dialog.
            progressDialog.dismiss();
        }
    }

    private long getTotal( List<CartDAO> cartlist) {
        long totalValue=0;
        for(CartDAO item : cartlist){
            totalValue += item.getSelected_count() *
                    Integer.valueOf(item.getActualPrice().replace("Rs : ",""));
        }
        return totalValue;
    }

    boolean singleBack = false;
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "saved to cart", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}

