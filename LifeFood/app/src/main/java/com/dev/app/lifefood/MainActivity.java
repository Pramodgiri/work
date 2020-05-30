package com.dev.app.lifefood;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.app.lifefood.adapter.HomeViewAdapter;
import com.dev.app.lifefood.util.CartDAO;
import com.dev.app.lifefood.util.ImageUploadInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    // Session Manager Class
    SessionManager session;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView navigationView;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    public static List<CartDAO> cartObjList = new ArrayList<>();
    List<ImageUploadInfo> list = null;
    RecyclerView.Adapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the paper library

        Paper.init(getApplicationContext());
        Paper.book().write("carts", cartObjList);

        FirebaseApp.initializeApp(this);
        // Session class instance
        session = new SessionManager(getApplicationContext());

        //getting data from firebase

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView)findViewById(R.id.nv);
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.home);
            homeActivity();
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.profile:
                       startActivity(new Intent(getApplicationContext(), ProfileActivity.class));break;
                    case R.id.home:
                        homeActivity();
                        break;
                        //startActivity(new Intent(getApplicationContext(), DetailsActivity.class));break;
                       // Toast.makeText(MainActivity.this, "Account",Toast.LENGTH_SHORT).show();break;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));break;
                    case R.id.order:
                        Toast.makeText(MainActivity.this, "My Cart",Toast.LENGTH_SHORT).show();break;
                    case R.id.logout:
                        logout();break;
                    case R.id.upload_product:
                        startActivity(new Intent(getApplicationContext(), UploadActivity.class));break;
                        //Toast.makeText(MainActivity.this, "Upload",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;
                }
                dl.closeDrawer(GravityCompat.START);
                return true;

            }
        });
        /*===================Login Page Activity in Main Activity =================================*/
        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManager.KEY_EMAIL);

        /**
         * below activity is done for navigation header image and text.
         */
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.profile_name);
        navUsername.setText(email);
        final ImageView main_profileImage = headerView.findViewById(R.id.main_profile_image);
        StorageReference profileRef = storageReference.child("user_profiles/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(main_profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("First time error : ","Ignore");
            }
        });

    }
    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to Log Out ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface df,int i){
                        // Clear the session data
                        // This will clear all session data and
                        // redirect user to LoginActivity
                        session.logoutUser();
                    }
                })

                .setNegativeButton("No",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface df,int i){
                        df.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    boolean singleBack = false;

    @Override
    public void onBackPressed() {
        if (singleBack) {
            // Disable going back to the MainActivity
            this.moveTaskToBack(true);
            return;
        }

        this.singleBack = true;
        Toast.makeText(this, "Double Back to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                singleBack=false;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
    /**
     *
     */

    public void homeActivity(){
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(
                this ,2,GridLayoutManager.VERTICAL,false);
        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);

        // Assign activity this to progress dialog.
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Product Category Loading....");

        // Showing progress dialog.
        progressDialog.show();
        // Adding Add Value Event Listener to databaseReference.
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = fStore.collection("product_details");
        com.google.firebase.firestore.Query query = collectionReference.whereEqualTo("store_category","Home");
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
                    list = new ArrayList<>();
                    for(DocumentSnapshot document : queryDocumentSnapshots){
                        String name = document.getString("product_name");
                        String url = document.getString("image_url");
                        String category = document.getString("store_category");
                        ImageUploadInfo imageUploadInfo = new ImageUploadInfo();
                        imageUploadInfo.setImageName(name);
                        imageUploadInfo.setImageURL(url);
                        imageUploadInfo.setStoreCategory(category);
                        list.add(imageUploadInfo);
                    }
                    adapter = new HomeViewAdapter(getApplicationContext(), list);

                    recyclerView.setAdapter(adapter);
                    list = null;
                    adapter=null;
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
}

