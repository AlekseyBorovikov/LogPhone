package com.example.loginningphone_12.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginningphone_12.MainActivity
import com.example.loginningphone_12.R
import com.example.loginningphone_12.databinding.ProcessFragmentBinding
import com.example.loginningphone_12.ui.adapters.ProcessAdapter
import com.example.loginningphone_12.ui.view_models.LogViewModel
import java.util.*
import androidx.lifecycle.Observer
import com.example.loginningphone_12.tools.DatePicker
import com.example.loginningphone_12.util.FormatStrings
import java.util.concurrent.TimeUnit


class ProcessFragment: Fragment(R.layout.process_fragment) {
    private var _binding: ProcessFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var processesRecyclerView: RecyclerView
    private lateinit var viewModel: LogViewModel
    lateinit var appsAdapter: ProcessAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = ProcessFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.apps.observe(viewLifecycleOwner, Observer { appList ->
            appsAdapter.differ.submitList(appList.appsList)

            val cal: Calendar = GregorianCalendar.getInstance()
            cal.timeInMillis = appList.time
            binding.processDateStart.text = "Данные от: ${FormatStrings.formatDate(TimeUnit.MILLISECONDS.toMillis(appList.time))}"
        })

        binding.fab.setOnClickListener {
            viewModel.getAppsToday()
            Toast.makeText(activity, "Обновлено.", Toast.LENGTH_LONG).show()
        }

        binding.btnPickDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                    activity as MainActivity, DatePicker(viewModel), cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    fun setupRecyclerView() {
        processesRecyclerView = binding.processesRecyclerView
        appsAdapter = ProcessAdapter()
        processesRecyclerView.apply {
            adapter = appsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}