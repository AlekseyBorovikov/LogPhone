package com.example.loginningphone_12.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginningphone_12.MainActivity
import com.example.loginningphone_12.R
import com.example.loginningphone_12.databinding.NotificationFragmentBinding
import com.example.loginningphone_12.databinding.ProcessFragmentBinding
import com.example.loginningphone_12.services.NotificationService
import com.example.loginningphone_12.ui.adapters.NotificationAdapter
import com.example.loginningphone_12.ui.adapters.ProcessAdapter
import com.example.loginningphone_12.ui.view_models.LogViewModel
import com.example.loginningphone_12.util.Constants
import com.example.loginningphone_12.util.FormatStrings
import java.util.*
import java.util.concurrent.TimeUnit


class NotificationFragment: Fragment(R.layout.notification_fragment) {
    private var _binding: NotificationFragmentBinding ?= null
    private val binding get() = _binding!!
    private val TAG = "NotificationFragment"
    private var isRunningService = false
    private lateinit var notificationsRecyclerView: RecyclerView
    private lateinit var viewModel: LogViewModel
    lateinit var notificationsAdapter: NotificationAdapter

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotificationFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.getSavedNotifications().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            notificationsAdapter.differ.submitList(it)
        })

        isRunningService = NotificationService.is_running
        Log.d(TAG, "$isRunningService  ${NotificationService.is_running}")

        if(isRunningService){
            binding.btnNotificationListener.setImageResource(R.drawable.ic_stop)
        }

        binding.btnNotificationListener.setOnClickListener {
            if(!isRunningService) startService()
            else stopService()
        }
    }

    fun setupRecyclerView() {
        notificationsRecyclerView = binding.notificationsRecyclerView
        notificationsAdapter = NotificationAdapter()
        notificationsRecyclerView.apply {
            adapter = notificationsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun startService(){
        activity?.let {
            val serviceIntent = Intent(it, NotificationService::class.java)
            serviceIntent.action = Constants.ACTION.START_ACTION

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                ContextCompat.startForegroundService(it, serviceIntent)
            else it.startService(serviceIntent)
            binding.btnNotificationListener.setImageResource(R.drawable.ic_stop)
            isRunningService = true
        }
    }

    private fun stopService(){
        activity?.let{ activity->
            val serviceIntent = Intent(activity, NotificationService::class.java)
            serviceIntent.action = Constants.ACTION.STOP_ACTION
            activity.startService(serviceIntent)
            binding.btnNotificationListener.setImageResource(R.drawable.ic_play_arrow)
            isRunningService = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
