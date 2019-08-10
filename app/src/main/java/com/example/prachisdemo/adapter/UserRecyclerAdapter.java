package com.example.prachisdemo.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prachisdemo.R;
import com.example.prachisdemo.database.DatabaseHelper;
import com.example.prachisdemo.models.User;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {

    private List<User> listUsers;
    private DatabaseHelper databaseHelper;
    private Context context;

    public UserRecyclerAdapter(Context context, List<User> listUsers, DatabaseHelper databaseHelper) {
        this.listUsers = listUsers;
        this.context = context;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_user_recycler, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String email = sharedPreferences.getString("email", "");
        if (!email.equalsIgnoreCase(listUsers.get(position).getEmail())) {
            holder.textViewFName.setText(listUsers.get(position).getFirstName());
            holder.textViewLName.setText(listUsers.get(position).getLastName());
            holder.textViewGender.setText(listUsers.get(position).getGender());
            holder.textViewEmail.setText(listUsers.get(position).getEmail());
            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseHelper.deleteUser(listUsers.get(position).getEmail());
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.v(UserRecyclerAdapter.class.getSimpleName(), "" + listUsers.size());
        return listUsers.size();
    }


    /**
     * ViewHolder class
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewFName;
        public TextView textViewLName;
        public TextView textViewGender;
        public TextView textViewEmail;
        public ImageView imgDelete;

        public UserViewHolder(View view) {
            super(view);
            textViewFName = view.findViewById(R.id.textViewFirstName);
            textViewLName = (TextView) view.findViewById(R.id.textViewLastName);
            textViewGender = (TextView) view.findViewById(R.id.textViewGender);
            textViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
            imgDelete = view.findViewById(R.id.img_delete);
        }
    }


}
