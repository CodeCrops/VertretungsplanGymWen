package de.codecrops.vertretungsplangymwen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import de.codecrops.vertretungsplangymwen.R.layout.activity_main
import de.codecrops.vertretungsplangymwen.data.VertretungData
import de.codecrops.vertretungsplangymwen.gui.VertretungsAdapter
import de.codecrops.vertretungsplangymwen.network.HttpGetRequest
import de.codecrops.vertretungsplangymwen.pushnotifications.AppNotificationManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val vertretungsNotification =
            de.codecrops.vertretungsplangymwen.pushnotifications.AppNotificationManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        createNotificationChannel()
        setNavigationViewListener()
        addDrawerListener()

        nav_view.setNavigationItemSelectedListener(this)

        val toolbar: Toolbar = toolbar
        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        val aToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(aToggle)
        aToggle.syncState()

        val extractTable = HttpGetRequest.extractToday(this).table
        val adapter = VertretungsAdapter(extractTable, applicationContext)

        vertretungs_list.adapter = adapter

        addOnItemClickListener()
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setNavigationViewListener() {
        val navigationView = nav_view
        navigationView.setNavigationItemSelectedListener(this)
    }

    //Verhindert, dass das 3 Punkte Menü oben rechts erstellt wird.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Click Listener für die Elemente im NavigationDrawer
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.website -> {
                val i = Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://gym-wen.de/vp/"))
                startActivity(i)
                drawer_layout.closeDrawer(GravityCompat.START)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(AppNotificationManager.DEFAULT_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun addDrawerListener() {
        drawer_layout.addDrawerListener(
                object : DrawerLayout.DrawerListener {
                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                    }

                    override fun onDrawerOpened(drawerView: View) {

                    }

                    override fun onDrawerClosed(drawerView: View) {

                    }

                    override fun onDrawerStateChanged(newState: Int) {

                    }
                }
        )
    }

    private fun addOnItemClickListener() {
        vertretungs_list.setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as VertretungData
            val intent = Intent(this, VertretungsContentActivity::class.java)
            intent.apply {
                putExtra("klasse", item.klasse)
                putExtra("stunde", item.stunde)
                putExtra("vertretung", item.vertretung)
                putExtra("fach", item.fach)
                putExtra("raum", item.raum)
                putExtra("kommentar", item.kommentar)
            }
            startActivity(intent)
        }
    }
}
