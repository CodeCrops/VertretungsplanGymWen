package de.codecrops.vertretungsplangymwen

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import de.codecrops.vertretungsplangymwen.gui.KlasseAdapter
import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import de.codecrops.vertretungsplangymwen.sqlite.PREFERENCETYPE
import kotlinx.android.synthetic.main.activity_klasse.*
import kotlinx.android.synthetic.main.dialog_add_klasse.*

class KlasseActivity : AppCompatActivity() {
    private val list = arrayListOf<String>()
    private val grades = arrayOf(5, 6, 7, 8, 9, 10, 11, 12)
    private val klassen = arrayOf('a', 'b', 'c', 'd', 'e', 'f', 'h')
    private lateinit var adapter: KlasseAdapter
    private var isCourse = false
    private var gradeDropdownPosition = 0
    private var spinnerSelectedByMachine = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_klasse)

        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle(R.string.klasse)
        }

        toolbar.setNavigationOnClickListener { super.onBackPressed() }
        addAddButtonClickListener()

        val gradeAdapter = ArrayAdapter<Int>(this, android.R.layout.simple_spinner_dropdown_item, grades)
        grade_dropdown.adapter = gradeAdapter

        addDropdownListeners(this)

        val klassenAdapter = ArrayAdapter<Char>(this, android.R.layout.simple_spinner_dropdown_item, klassen)
        klasse_dropdown.adapter = klassenAdapter

        for(preference in DBManager.getAllPreferences(this)) {
            list.add(preference.course)
        }
        adapter = KlasseAdapter(this, list, this)
        course_listView.adapter = adapter

        initGUI()
    }

    private fun addDropdownListeners(context: Context) {
        grade_dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(!spinnerSelectedByMachine) {
                    if(isCourse && !DBManager.getAllPreferences(context).isEmpty()) {
                        val dialog = AlertDialog.Builder(context)
                        dialog.setMessage("Durch den Wechsel gehen alle deine bisher gespeicherten Kurse verloren! Willst du das wirklich?")
                        dialog.setPositiveButton("JA") { dialog, which ->
                            DBManager.clearPreference(context)
                            if(position == 6 || position == 7) {
                                setCourseGUI()
                            } else {
                                setKlasseGUI()
                            }
                            gradeDropdownPosition = position
                            dialog.dismiss()
                        }
                        dialog.setNegativeButton("ABBRUCH") { dialog, which ->
                            setGradeDropdownPosition(gradeDropdownPosition)
                            dialog.dismiss()
                        }
                        dialog.show()
                        return
                    }
                    if(position == 6 || position == 7) {
                        DBManager.clearPreference(context)
                        setCourseGUI()
                        gradeDropdownPosition = position
                    } else {
                        setKlasseGUI()
                        fillDatabaseForKlasse()
                    }
                } else {
                    spinnerSelectedByMachine = false
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        klasse_dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    fillDatabaseForKlasse()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun fillDatabaseForKlasse() {
        if(!DBManager.getAllPreferences(this).isEmpty()) {
            val itemIndex: String = DBManager.getAllPreferences(this)[0].course.substring(0, 2)
            if (itemIndex[0] == '5' || itemIndex[0] == '6' || itemIndex[0] == '7' || itemIndex[0] == '8' || itemIndex[0] == '9' || itemIndex == "10") {
                DBManager.clearPreference(this)
                DBManager.addPreference(this, grade_dropdown.selectedItem.toString() + klasse_dropdown.selectedItem.toString(), PREFERENCETYPE.REGULÄR)
            }
        }
    }

    private fun initGUI() {
        if(!DBManager.getAllPreferences(this).isEmpty()) {
            val itemIndex : Int = DBManager.getAllPreferences(this)[0].course.substring(0, 1).toInt()
            when(itemIndex) {
                5 -> {
                    setGradeDropdownPosition(0)
                    setKlasseGUI()
                    initKlasseDropdown()
                }
                6 -> {
                    setGradeDropdownPosition(1)
                    setKlasseGUI()
                    initKlasseDropdown()
                }
                7 -> {
                    setGradeDropdownPosition(2)
                    setKlasseGUI()
                    initKlasseDropdown()
                }
                8 -> {
                    setGradeDropdownPosition(3)
                    setKlasseGUI()
                    initKlasseDropdown()
                }
                9 -> {
                    setGradeDropdownPosition(4)
                    setKlasseGUI()
                    initKlasseDropdown()
                }
                2 -> {
                    setGradeDropdownPosition(7)
                    setCourseGUI()
                }
                1 -> {
                    val itemIndex1 : String = DBManager.getAllPreferences(this)[0].course.substring(0, 2)
                    if(itemIndex1 == "10") {
                        setGradeDropdownPosition(5)
                        setKlasseGUI()
                        initKlasseDropdown()
                    } else {
                        setGradeDropdownPosition(6)
                        setCourseGUI()
                    }
                }
            }
        } else {
            setKlasseGUI()
        }

    }

    private fun initKlasseDropdown() {
        val klasse : Char = DBManager.getAllPreferences(this)[0].course.substring(1, 2).single()
        spinnerSelectedByMachine = true
        val index = klassen.indexOf(klasse)
        klasse_dropdown.setSelection(index, false)
    }

    private fun setGradeDropdownPosition(pos: Int) {
        spinnerSelectedByMachine = true
        grade_dropdown.setSelection(pos, false)
        gradeDropdownPosition = pos
    }

    private fun setKlasseGUI() {
        klasse_course_textView.text = getString(R.string.choose_klasse)
        addButton.visibility = View.INVISIBLE
        isCourse = false
        course_listView.visibility = View.INVISIBLE
        klasse_dropdown.visibility = View.VISIBLE
    }

    private fun setCourseGUI() {
        klasse_course_textView.text = getString(R.string.choose_course)
        addButton.visibility = View.VISIBLE
        notifyDataSetChanged()
        isCourse = true
        course_listView.visibility = View.VISIBLE
        klasse_dropdown.visibility = View.INVISIBLE
    }

    private fun addAddButtonClickListener() {
        addButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_add_klasse)
            dialog.ok.setOnClickListener {
                enterCourse(dialog)
            }
            dialog.cancel.setOnClickListener { dialog.cancel() }
            dialog.editText.setOnKeyListener { _, keyCode, event ->
                if(event.action == KeyEvent.ACTION_UP)
                    if(keyCode == KeyEvent.KEYCODE_ENTER) {
                        enterCourse(dialog)
                        true
                    }
                false
            }
            dialog.show()
        }
    }

    private fun enterCourse(dialog: Dialog) {
        val text = dialog.editText.text.toString()
        val preferenceList : ArrayList<String> = arrayListOf()
        for(p in DBManager.getAllPreferences(this)) {
            preferenceList.add(p.course)
        }
        if(grade_dropdown.selectedItemPosition == 6) {
            if(text.startsWith("1")) {
                if(!preferenceList.contains(text)) {
                    if(text.length >= 2 ) {
                        addItem(text)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Dieser Kursname ist zu kurz!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Dieser Kurs ist bereits vorhanden!", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Der Kurs muss mit \'1\' starten!", Toast.LENGTH_LONG).show()
            }
        } else if(grade_dropdown.selectedItemPosition == 7) {
            if(text.startsWith("2")) {
                if(!preferenceList.contains(text)) {
                    if(text.length >= 2 ) {
                        addItem(text)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Dieser Kursname ist zu kurz!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Dieser Kurs ist bereits vorhanden!", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Der Kurs muss mit \'2\' starten!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addItem(course: String) {
        DBManager.addPreference(this, course, PREFERENCETYPE.REGULÄR)
        notifyDataSetChanged()
    }

    fun removeItem(course: String) {
        DBManager.deletePreference(this, course)
        notifyDataSetChanged()
    }

    private fun notifyDataSetChanged() {
        list.clear()
        for(preference in DBManager.getAllPreferences(this)) {
            list.add(preference.course)
        }
        adapter.notifyDataSetChanged()
    }
}
