package com.example.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<User> mUsers;
    private OnUserClickListener mListener;

    public interface OnUserClickListener {
        void onUserClick(int position);
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.mListener = listener;
    }

    public UserAdapter(ArrayList<User> users) {
        this.mUsers = users;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = mUsers.get(position);
        holder.avatarImg.setImageResource(currentUser.getAvatarMockUpResource());
        holder.userNameTV.setText(currentUser.getName());

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatarImg;
        public TextView userNameTV;


        public UserViewHolder(@NonNull View itemView, OnUserClickListener listener) {
            super(itemView);
            avatarImg = itemView.findViewById(R.id.avatarImg);
            userNameTV = itemView.findViewById(R.id.userNameTV);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onUserClick(position);
                    }

                }
            });
        }
    }
}
