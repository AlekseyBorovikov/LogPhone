package com.example.loginningphone_12.ui.adapters

import android.app.usage.UsageStats
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.content.pm.PackageManager;
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.loginningphone_12.databinding.ProcessItemBinding
import com.example.loginningphone_12.models.App


class ProcessAdapter(): RecyclerView.Adapter<ProcessAdapter.ProcessViewHolder>() {
    private var _binding: ProcessItemBinding ?= null
    private val binding get() = _binding!!

    //del
    var packageManager: PackageManager ?= null

    private val differCallbck = object : DiffUtil.ItemCallback<App>(){
        override fun areItemsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem.appName == newItem.appName
        }

        override fun areContentsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem.equals(newItem)
        }
    }

    val differ = AsyncListDiffer(this, differCallbck)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessAdapter.ProcessViewHolder {
        val layotInflater: LayoutInflater = LayoutInflater.from(parent.context)
        _binding = ProcessItemBinding.inflate(layotInflater, parent, false)
        val viewHolder: ProcessViewHolder = ProcessViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ProcessAdapter.ProcessViewHolder, position: Int) {
        val app = differ.currentList[position]
        holder.nameProcess.text = app.appName
        holder.duration.text = app.usageDuration
        holder.icon.setImageDrawable(app.appIcon)
        holder.progressBar.progress = app.usagePercentage
        holder.percent.text = "${app.usagePercentage} %"
    }

    inner class ProcessViewHolder(itemBinding: ProcessItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        val nameProcess: TextView = itemBinding.appNameTv
        val duration: TextView = itemBinding.usageDurationTv
        val percent: TextView = itemBinding.usagePercTv
        val icon: ImageView = itemBinding.iconImg
        val progressBar: ProgressBar = itemBinding.progressBar
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}