package de.codecrops.vertretungsplangymwen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity;
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_contact.*
import android.widget.AdapterView.OnItemSelectedListener



/**
 * @author K1TR1K
 */

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        setSupportActionBar(toolbar)

        val content = arrayOf("Problem melden", "Feature anfragen", "Feedback geben")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, content)
        dropdown.adapter = adapter

        dropdown.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                addSpinnerListener(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle(R.string.contact)
        }

        toolbar.setNavigationOnClickListener { super.onBackPressed() }

        fab.setOnClickListener {
            if(name.text.isEmpty() || subject.text.isEmpty() || description.text.isEmpty()){
                Toast.makeText(this, getString(R.string.empty_text), Toast.LENGTH_SHORT).show();
            } else {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "message/rfc822"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.developer_mail)))
                intent.putExtra(Intent.EXTRA_SUBJECT, "${name.text} | ${subject.text}")
                intent.putExtra(Intent.EXTRA_TEXT, description.text)
                try {
                    startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private fun addSpinnerListener(pos: Int) {
        when(pos) {
            0 -> { text.text = getString(R.string.issue_description) }
            1 -> { text.text = getString(R.string.feature_description) }
            2 -> { text.text = getString(R.string.feedback_description) }
        }
    }

}
