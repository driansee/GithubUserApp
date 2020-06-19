package com.drians.android.githubuserapp.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.drians.android.githubuserapp.R
import com.drians.android.githubuserapp.alarm.AlarmReceiver
import com.drians.android.githubuserapp.alarm.DatePickerFragment
import com.drians.android.githubuserapp.alarm.TimePickerFragment
import kotlinx.android.synthetic.main.activity_settings.*
import java.text.SimpleDateFormat
import java.util.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setTitle(R.string.menu_settings)

        buttonSetLanguage.setOnClickListener(this)

        // Listener one time alarm
        buttonOnceDate.setOnClickListener(this)
        buttonOnceTime.setOnClickListener(this)
        buttonSetOnceAlarm.setOnClickListener(this)

        // Listener repeating alarm
        buttonRepeatingTime.setOnClickListener(this)
        buttonSetRepeatingAlarm.setOnClickListener(this)
        buttonCancelRepeatingAlarm.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.buttonSetLanguage -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
            R.id.buttonOnceDate -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            R.id.buttonOnceTime -> {
                val timePickerFragmentOne = TimePickerFragment()
                    timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }
            R.id.buttonSetOnceAlarm -> {
                val onceDate = textOnceDate.text.toString()
                val onceTime = textOnceTime.text.toString()
                val onceMessage = editTextOnceMessage.text.toString()

                alarmReceiver.setOneTimeAlarm(this, AlarmReceiver.TYPE_ONE_TIME,
                    onceDate, onceTime, onceMessage)
            }
            R.id.buttonRepeatingTime -> {
                val timePickerFragmentRepeat = TimePickerFragment()
                    timePickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }
            R.id.buttonSetRepeatingAlarm -> {
                val repeatTime = textRepeatingTime.text.toString()
                val repeatMessage = editTextRepeatingMessage.text.toString()
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING,
                    repeatTime, repeatMessage)
            }
            R.id.buttonCancelRepeatingAlarm -> alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {

        // Siapkan date formatter-nya terlebih dahulu
        val calendar = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Set text dari textview once
        textOnceDate.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {

        // Siapkan time formatter-nya terlebih dahulu
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Set text dari textview berdasarkan tag
        when (tag) {
            TIME_PICKER_ONCE_TAG -> textOnceTime.text = dateFormat.format(calendar.time)
            TIME_PICKER_REPEAT_TAG -> textRepeatingTime.text = dateFormat.format(calendar.time)
            else -> {
            }
        }
    }
}