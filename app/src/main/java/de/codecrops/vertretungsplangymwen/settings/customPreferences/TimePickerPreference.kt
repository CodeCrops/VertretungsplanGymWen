package de.codecrops.vertretungsplangymwen.settings.customPreferences

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import de.codecrops.vertretungsplangymwen.R

class TimePickerPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {

    //weiter auf http://www.infiniteimprob.com/blog/custom-preferences/ & https://stackoverflow.com/questions/6425702/layout-for-timepicker-based-dialogpreference

    lateinit var mTimePicker : TimePicker
    var currentTime : Float = 0.toFloat()

    init {
        dialogLayoutResource = R.layout.custom_clock_preference
        positiveButtonText = "OK"
        negativeButtonText = "Abbrechen"
    }

    //Values
    private val DEFAULT_TIME : Float = 0.toFloat()

    private fun getTime(hour: Int, minute: Int) : Float {
        return hour.toFloat() + minute.toFloat()/100
    }

    private fun getHour(value: Float) : Int {
        return value.toInt()
    }

    private fun getMinute(value: Float) : Int {
        return (value.toInt() - getHour(value)) * 100
    }

    override fun onCreateDialogView(): View {
        var view : View = super.onCreateDialogView()
        mTimePicker = view.findViewById(R.id.timePicker) as TimePicker
        mTimePicker.setIs24HourView(true)

        if(Build.VERSION.SDK_INT >= 23) {
            mTimePicker.hour = getHour(currentTime)
            mTimePicker.minute = getMinute(currentTime)
        } else {
            mTimePicker.currentHour = getHour(currentTime)
            mTimePicker.currentMinute = getMinute(currentTime)
        }

        mTimePicker.setOnTimeChangedListener { timePicker, hour, minute ->
            run {
                this.currentTime = getTime(hour, minute)
            }
        }

        var messageTextView : TextView = view.findViewById(R.id.messageTextView)
        if(dialogMessage.isNullOrBlank()) {
            messageTextView.visibility = View.GONE
        } else {
            messageTextView.text = dialogMessage
        }

        return view
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getFloat(index, DEFAULT_TIME)
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any) {
        if(restorePersistedValue) {
            if(defaultValue == null) {
                currentTime = this.getPersistedFloat(DEFAULT_TIME)
            } else {
                currentTime = defaultValue as Float
                if(shouldPersist()) {
                    persistFloat(currentTime)
                }
            }
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if(positiveResult) {
            persistFloat(currentTime)
        }
    }
}