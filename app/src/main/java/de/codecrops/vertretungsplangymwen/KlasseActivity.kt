package de.codecrops.vertretungsplangymwen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import de.codecrops.vertretungsplangymwen.gui.KlasseAdapter
import kotlinx.android.synthetic.main.activity_klasse.*

class KlasseActivity : AppCompatActivity() {
    private val list = arrayListOf<String?>()
    private val grades = arrayOf(5, 6, 7, 8, 9, 10, 11, 12)
    private val klassen = arrayOf('a', 'b', 'c', 'd', 'e', 'f', 'h')
    private lateinit var adapter: KlasseAdapter

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

        setStandardVisibility()

        val gradeAdapter = ArrayAdapter<Int>(this, android.R.layout.simple_spinner_dropdown_item, grades)
        grade_dropdown.adapter = gradeAdapter

        grade_dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 6 || position == 7) {
                    course_textView.visibility = View.VISIBLE
                    course_listView.visibility = View.VISIBLE
                    klasse_textView.visibility = View.INVISIBLE
                    klasse_dropdown.visibility = View.INVISIBLE
                } else {
                    course_textView.visibility = View.INVISIBLE
                    course_listView.visibility = View.INVISIBLE
                    klasse_textView.visibility = View.VISIBLE
                    klasse_dropdown.visibility = View.VISIBLE
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        val klassenAdapter = ArrayAdapter<Char>(this, android.R.layout.simple_spinner_dropdown_item, klassen)
        klasse_dropdown.adapter = klassenAdapter

        //TODO: Hier Liste der Klassen aus Datenbank holen
        adapter = KlasseAdapter(this, list, this)
        course_listView.adapter = adapter

        if(list.isEmpty())  addItem()
    }

    private fun setStandardVisibility() {
        course_textView.visibility = View.INVISIBLE
        course_listView.visibility = View.INVISIBLE
    }

    fun addItem() {
        list.add(null)
        adapter.notifyDataSetChanged()
    }
}
