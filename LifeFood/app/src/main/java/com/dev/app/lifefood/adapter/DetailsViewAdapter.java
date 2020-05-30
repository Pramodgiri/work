package com.dev.app.lifefood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.app.lifefood.MainActivity;
import com.dev.app.lifefood.R;
import com.dev.app.lifefood.util.CartDAO;
import com.dev.app.lifefood.util.ImageUploadInfo;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;


public class DetailsViewAdapter extends RecyclerView.Adapter<DetailsViewAdapter.ViewHolder> {

    Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;

    public DetailsViewAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_recyclerview_items,
                parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);

        holder.imageNameTextView.setText(UploadInfo.getImageName());
        holder.user_priceTextView.setText("Rs : "+UploadInfo.getUser_price());
        holder.user_discountTextView.setText("Discount : "+UploadInfo.getUser_discount());
        holder.string_image_nameTextView.setText(UploadInfo.getImageURL());
        holder.selected_countTextView.setText(String.valueOf(UploadInfo.getSelected_count()));
        //holder.orderValueTextView.setText(String.valueOf(count));
        //Loading image from Glide library.        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
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
        public Button addCartButton;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.details_imageView);
            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
            minusImageView = (ImageView) itemView.findViewById(R.id.minus);
            plusImageView = (ImageView) itemView.findViewById(R.id.plus);
            selected_countTextView = (TextView) itemView.findViewById(R.id.selected_count);
            addCartButton=(Button) itemView.findViewById(R.id.add_cart);

            user_priceTextView = (TextView) itemView.findViewById(R.id.user_price);
            user_discountTextView = (TextView) itemView.findViewById(R.id.user_discount);
            mrpTextView = (TextView) itemView.findViewById(R.id.mrp);
            string_image_nameTextView = (TextView) itemView.findViewById(R.id.imageStringName);
            //itemView.setOnClickListener((View.OnClickListener) this);

            addCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartDAO obj = new CartDAO();
                    obj.setProductName(imageNameTextView.getText().toString());
                    obj.setMrpPrice(mrpTextView.getText().toString());
                    obj.setDiscountPrice(user_discountTextView.getText().toString());
                    obj.setActualPrice(user_priceTextView.getText().toString());
                    obj.setSelected_count(Integer.valueOf(selected_countTextView.getText().toString()));
                    obj.setImageURL(string_image_nameTextView.getText().toString());
                    MainActivity.cartObjList.add(obj);
                    Paper.book().write("carts", MainActivity.cartObjList);
                    Toast.makeText(context, "Ur product is Updated "+ getAdapterPosition(),
                            Toast.LENGTH_LONG).show();
                }
            });

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

                }
            });
        }
    }//Inner class Ends here
}

