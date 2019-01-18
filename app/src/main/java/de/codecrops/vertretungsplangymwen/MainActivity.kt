package de.codecrops.vertretungsplangymwen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.NotificationCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import de.codecrops.vertretungsplangymwen.R.layout.activity_main
import de.codecrops.vertretungsplangymwen.credentials.CredentialsManager
import de.codecrops.vertretungsplangymwen.data.VertretungData
import de.codecrops.vertretungsplangymwen.gui.VertretungsAdapter
import de.codecrops.vertretungsplangymwen.network.HttpGetRequest
import de.codecrops.vertretungsplangymwen.pushnotifications.AppNotificationManager
import de.codecrops.vertretungsplangymwen.service.BackgroundJob
import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author K1TR1K
 */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private enum class Day {
        TODAY, TOMORROW
    }

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

        setCompleteDataToday()

        addOnItemClickListener()

    }

    private fun setDataToday() {
        val currentDate = Date(Calendar.getInstance().timeInMillis)
        val extract = HttpGetRequest.extractToday(this)

        if(currentDate == extract.date) {
            for(v: VertretungData in extract.table) {
                DBManager.addVertretungsstunde(this, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, currentDate)
            }
            //TODO: Nach Klasse Filtern
            /*
            val adapter = VertretungsAdapter(extractTable, applicationContext)
            vertretungs_list.adapter = adapter
             */
            vertretungs_list.visibility = View.VISIBLE
            no_data.visibility = View.INVISIBLE
        } else {
            setDataTomorrow()
        }
    }

    private fun setDataTomorrow() {
        val currentDate = Date(Calendar.getInstance().timeInMillis)
        val extract = HttpGetRequest.extractTomorrow(this)

        if(currentDate == extract.date) {
            for(v: VertretungData in extract.table) {
                DBManager.addVertretungsstunde(this, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, currentDate)
            }
            //TODO: Nach Klasse Filtern
            /*
            val adapter = VertretungsAdapter(extractTable, applicationContext)
            vertretungs_list.adapter = adapter
             */
            vertretungs_list.visibility = View.VISIBLE
            no_data.visibility = View.INVISIBLE
        } else {
            //Keine Daten vorhanden
            vertretungs_list.visibility = View.INVISIBLE
            no_data.visibility = View.VISIBLE
        }
    }

    private fun setCompleteDataToday() {
        val extract = HttpGetRequest.extractToday(this)

        if(dateEqualsToday(extract.date)) {
            for(v: VertretungData in extract.table) {
                //DBManager.addVertretungsstunde(this, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, extract.date)
            }
            //TODO: Alles Bekommen
            /*
            val adapter = VertretungsAdapter(extractTable, applicationContext)
            vertretungs_list.adapter = adapter
             */

            val adapter = VertretungsAdapter(extract.table, applicationContext)
            vertretungs_list.adapter = adapter

            /*
            val adapter = vertretungs_list.adapter as ArrayAdapter<VertretungData>
            adapter.clear()

            for(i in extract.table) {
                adapter.insert(i, adapter.count)
            }

            adapter.notifyDataSetChanged()
            */
            vertretungs_list.visibility = View.VISIBLE
            no_data.visibility = View.INVISIBLE
        } else {
            setCompleteDataTomorrow()
        }
    }

    private fun setCompleteDataTomorrow() {
        val extract = HttpGetRequest.extractTomorrow(this)

        if(dateEqualsTomorrow(extract.date)) {
            for(v: VertretungData in extract.table) {
                //DBManager.addVertretungsstunde(this, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, extract.date)
            }
            //TODO: Alles Bekommen
            /*
            val adapter = VertretungsAdapter(extractTable, applicationContext)
            vertretungs_list.adapter = adapter
             */

            val adapter = vertretungs_list.adapter as ArrayAdapter<VertretungData>
            adapter.clear()

            for(i in extract.table) {
                adapter.insert(i, adapter.count)
            }

            adapter.notifyDataSetChanged()

            vertretungs_list.visibility = View.VISIBLE
            no_data.visibility = View.INVISIBLE
        } else {
            //Keine Daten vorhanden
            vertretungs_list.visibility = View.INVISIBLE
            no_data.visibility = View.VISIBLE
        }
    }

    private fun dateEqualsToday(d: Date) : Boolean {
        val date = Calendar.getInstance()
        date.time = d
        val current = Calendar.getInstance()
        if(date.get(Calendar.YEAR) == current.get(Calendar.YEAR) &&
                date.get(Calendar.MONTH) == current.get(Calendar.MONTH) &&
                date.get(Calendar.DAY_OF_MONTH) == current.get(Calendar.DAY_OF_MONTH)) {
                    return true
                }
        return false
    }

    private fun dateEqualsTomorrow(d: Date) : Boolean {
        val date = Calendar.getInstance()
        date.time = d
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)

        if(date.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                date.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                date.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
            return true
        }
        return false
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            finish()
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
            R.id.logout -> {
                CredentialsManager.deleteHTTPCredentials(this)
                val i = Intent(this, LoginActivity::class.java)
                i.putExtra("logout", true)
                finish()
                drawer_layout.closeDrawer(GravityCompat.START)
                startActivity(i)
            }
            R.id.help -> {
                val i = Intent(this, HelpActivity::class.java)
                drawer_layout.closeDrawer(GravityCompat.START)
                startActivity(i)
            }
            R.id.today -> {
                setDataToday()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.tomorrow -> {
                setDataTomorrow()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.tomorrow_complete -> {
                setCompleteDataTomorrow()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.today_complete -> {
                setCompleteDataToday()
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
    /*
    fun scheduleJob(v: View) {
        val componentName = ComponentName(this, BackgroundJob::class.java)
        val jobInfo = JobInfo.Builder(BackgroundJob.JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
    }

    fun chancelJob(v: View) {

    }
    */
}
