package com.example.loginningphone_12.tools

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
import com.example.loginningphone_12.MainActivity
import com.example.loginningphone_12.ui.view_models.LogViewModel
import com.example.loginningphone_12.util.FormatStrings
import java.util.*

class DatePicker(val logViewModel: LogViewModel): DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dataSuccess = logViewModel.getAppsByDate(cal.time)
        if (!dataSuccess) {
            view?.let {
                Toast.makeText(
                        it.context, "Нет данных об использовании приложений в " +
                        "${FormatStrings.formatDate(cal.time.time)}",
                        Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}