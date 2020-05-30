package com.dev.app.lifefood.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.app.lifefood.CartActivity;
import com.dev.app.lifefood.MainActivity;
import com.dev.app.lifefood.R;
import com.dev.app.lifefood.util.CartDAO;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;


public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.ViewHolder> {

    Context context;
    List<CartDAO> MainCartList;

    public CartViewAdapter(Context context, List<CartDAO> TempList) {

        this.MainCartList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recyclerview_items,
                parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartDAO cartObj = MainCartList.get(position);
        holder.imageNameTextView.setText(cartObj.getProductName());
        holder.user_priceTextView.setText("Rs : " + cartObj.getActualPrice());
        holder.user_discountTextView.setText("Discount : " + cartObj.getDiscountPrice());
        holder.string_image_nameTextView.setText(cartObj.getImageURL());
        holder.selected_countTextView.setText(String.valueOf(cartObj.getSelected_count()));
        //holder.orderValueTextView.setText(String.valueOf(count));
        //Loading image from Glide library.        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);
        Glide.with(context).load(cartObj.getImageURL()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return MainCartList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView;
        public ImageView minusImageView;
        public ImageView plusImageView;
        public TextView selected_countTextView;

        public TextView user_priceTextView;
        public TextView mrpTextView;
        public TextView user_discountTextView;
        public TextView string_image_nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.details_imageView);
            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
            minusImageView = (ImageView) itemView.findViewById(R.id.minus);
            plusImageView = (ImageView) itemView.findViewById(R.id.plus);
            selected_countTextView = (TextView) itemView.findViewById(R.id.selected_count);

            user_priceTextView = (TextView) itemView.findViewById(R.id.user_price);
            user_discountTextView = (TextView) itemView.findViewById(R.id.user_discount);
            mrpTextView = (TextView) itemView.findViewById(R.id.mrp);
            string_image_nameTextView = (TextView) itemView.findViewById(R.id.imageStringName);
            //itemView.setOnClickListener((View.OnClickListener) this);
            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "You view coupons button for item position "+ getAdapterPosition(),
                            Toast.LENGTH_LONG).show();
                }
            });

            //Onclick event for plus or minus operations:
            plusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cnt = Integer.valueOf(selected_countTextView.getText().toString());
                    cnt++;
                    selected_countTextView.setText(String.valueOf(cnt));
                    refreshCartActivity(imageNameTextView.getText().toString().trim(),cnt);
                }
            });

            minusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cnt = Integer.valueOf(selected_countTextView.getText().toString());
                    if(cnt > 0){
                        cnt--;
                    }
                    selected_countTextView.setText(String.valueOf(cnt));
                    refreshCartActivity(imageNameTextView.getText().toString().trim(),cnt);
                }
            });
        }

        private void refreshCartActivity(String productName,int cnt) {
            //update the count in cart:
            CartDAO tempObj = new CartDAO();
            if(!MainActivity.cartObjList.isEmpty()){
                for(CartDAO item : MainActivity.cartObjList){
                    if(item.getProductName().equalsIgnoreCase(productName)){
                        tempObj = item;
                        MainActivity.cartObjList.remove(item);break;
                    }
                }
            }
            if(cnt>0) {
                tempObj.setSelected_count(cnt);
                MainActivity.cartObjList.add(tempObj);
            }
            Paper.book().write("carts", MainActivity.cartObjList);
            Intent i = new Intent(context, CartActivity.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            context.startActivity(i);
        }
    }//Inner class Ends here
}

