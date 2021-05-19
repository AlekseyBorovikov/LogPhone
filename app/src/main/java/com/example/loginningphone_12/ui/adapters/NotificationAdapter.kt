package com.example.loginningphone_12.ui.adapters

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.loginningphone_12.databinding.NotificationItemBinding
import com.example.loginningphone_12.models.Notification

class NotificationAdapter():  RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    private var _binding: NotificationItemBinding?= null
    private val binding get() = _binding!!

    //del
    var packageManager: PackageManager?= null

    private val differCallbck = object : DiffUtil.ItemCallback<Notification>(){
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.appName == newItem.appName
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.equals(newItem)
        }
    }

    val differ = AsyncListDiffer(this, differCallbck)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.NotificationViewHolder {
        val layotInflater: LayoutInflater = LayoutInflater.from(parent.context)
        _binding = NotificationItemBinding.inflate(layotInflater, parent, false)
        val viewHolder: NotificationViewHolder = NotificationViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: NotificationAdapter.NotificationViewHolder, position: Int) {
        val notification = differ.currentList[position]
        holder.title.text = notification.title
        holder.text.text = notification.text
        holder.icon.setImageBitmap(notification.notifIcon)
    }

    inner class NotificationViewHolder(itemBinding: NotificationItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        val title: TextView = itemBinding.tvTitle
        val text: TextView = itemBinding.tvText
        val icon: ImageView = itemBinding.iconImg
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}