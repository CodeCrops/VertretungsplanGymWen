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
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import de.codecrops.vertretungsplangymwen.R.layout.activity_main
import de.codecrops.vertretungsplangymwen.credentials.CredentialsManager
import de.codecrops.vertretungsplangymwen.data.VertretungData
import de.codecrops.vertretungsplangymwen.gui.VertretungsAdapter
import de.codecrops.vertretungsplangymwen.network.HttpGetRequest
import de.codecrops.vertretungsplangymwen.pushnotifications.AppNotificationManager
import de.codecrops.vertretungsplangymwen.sqlite.DBContracts
import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author K1TR1K
 */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private enum class VertretungsOption {
        TODAY, TODAY_FILTERED, NEXT_DAY, NEXT_DAY_FILTERED
    }

    private lateinit var vertretungsOption: VertretungsOption

    val vertretungsNotification =
            de.codecrops.vertretungsplangymwen.pushnotifications.AppNotificationManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        createNotificationChannel()
        setNavigationViewListener()
        addDrawerListener()

        nav_view.setNavigationItemSelectedListener(this)

        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        toolbar_title.text = getString(R.string.app_name)

        val aToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(aToggle)
        aToggle.syncState()

        setCompleteDataToday()

        addOnItemClickListener()
        addOnFabClickListener()

        val currentCalendar = Calendar.getInstance()
        val currentDate: Date = currentCalendar.time
        val day = SimpleDateFormat("EE", Locale.GERMAN).format(currentDate.time)

        val menu = nav_view.menu
        if(day == "Sa." || day == "So.") {
            menu.findItem(R.id.today).isVisible = false
            menu.findItem(R.id.today_complete).isVisible = false
        } else {
            menu.findItem(R.id.today).isVisible = true
            menu.findItem(R.id.today_complete).isVisible = true
        }
    }

    private fun setDataToday() {
        val extract = HttpGetRequest.extractToday(this)
        if(Utils.dateEqualsToday(extract.date)) {
            DBManager.clearVertretungsDB(this)
            for(v: VertretungData in extract.table) {
                DBManager.addVertretungsstunde(this, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, extract.date)
            }
            setFilteredData(Calendar.getInstance().time)
            setHeaderToday()
            vertretungsOption = VertretungsOption.TODAY_FILTERED
            vertretungs_list.visibility = View.VISIBLE
            no_data.visibility = View.INVISIBLE
        } else {
            setDataNextDay()
        }
    }

    private fun setDataNextDay() {
        val extract = HttpGetRequest.extractTomorrow(this)
        val nextDateReturn = Utils.dateEqualsNextDay(extract.date)
        if(nextDateReturn.isNextDay) {
            DBManager.clearVertretungsDB(this)
            for(v: VertretungData in extract.table) {
                DBManager.addVertretungsstunde(this, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, extract.date)
            }
            setFilteredData(nextDateReturn.date)
            setHeaderNextDay(nextDateReturn.date)
            vertretungsOption = VertretungsOption.NEXT_DAY_FILTERED
            vertretungs_list.visibility = View.VISIBLE
            no_data.visibility = View.INVISIBLE
        } else {
            //Keine Daten vorhanden
            header.text = getString(R.string.no_data)
            vertretungs_list.visibility = View.INVISIBLE
            no_data.visibility = View.VISIBLE
        }
    }

    private fun setCompleteDataToday() {
        val extract = HttpGetRequest.extractToday(this)
        if(Utils.dateEqualsToday(extract.date)) {
            DBManager.clearVertretungsDB(this)
            for(v: VertretungData in extract.table) {
                DBManager.addVertretungsstunde(this, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, extract.date)
            }
            setCompleteData(Calendar.getInstance().time)
            setHeaderToday()
            vertretungsOption = VertretungsOption.TODAY
            vertretungs_list.visibility = View.VISIBLE
            no_data.visibility = View.INVISIBLE
        } else {
            setCompleteDataNextDay()
        }
    }

    private fun setCompleteDataNextDay() {
        val extract = HttpGetRequest.extractTomorrow(this)
        val nextDateReturn = Utils.dateEqualsNextDay(extract.date)
        if(nextDateReturn.isNextDay) {
            DBManager.clearVertretungsDB(this)
            for(v: VertretungData in extract.table) {
                DBManager.addVertretungsstunde(this, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, extract.date)
            }
            setCompleteData(nextDateReturn.date)
            setHeaderNextDay(nextDateReturn.date)
            vertretungsOption = VertretungsOption.NEXT_DAY
            vertretungs_list.visibility = View.VISIBLE
            no_data.visibility = View.INVISIBLE
        } else {
            //Keine Daten vorhanden
            header.text = getString(R.string.no_data)
            vertretungs_list.visibility = View.INVISIBLE
            no_data.visibility = View.VISIBLE
        }
    }

    private fun setFilteredData(date: Date) {
        //TODO: Klassen aus DB bekommen
        val list = DBManager.getVertretungenByKlasse(this, "1m21", date)
        val adapter = VertretungsAdapter(list, this)
        vertretungs_list.adapter = adapter
    }

    private fun setCompleteData(date: Date) {
        val list = DBManager.getAllVertretungen(this, date)
        val adapter = VertretungsAdapter(list, this)
        vertretungs_list.adapter = adapter
    }

    private fun setHeaderToday() {
        val calendar = Calendar.getInstance()
        val currentDate: Date = calendar.time
        val day = SimpleDateFormat("EEEE", Locale.GERMAN).format(currentDate.time)
        toolbar.header.text = "$day der ${Utils.formGermanDate(calendar)}"
    }

    private fun setHeaderNextDay(nextDateReturnDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = nextDateReturnDate
        val currentDate: Date = calendar.time
        val day = SimpleDateFormat("EEEE", Locale.GERMAN).format(currentDate.time)
        toolbar.header.text = "$day der ${Utils.formGermanDate(calendar)}"
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
                val i = Intent(this, ContactActivity::class.java)
                drawer_layout.closeDrawer(GravityCompat.START)
                startActivity(i)
            }
            R.id.settings -> {
                val i = Intent(this, SettingsActivity::class.java)
                drawer_layout.closeDrawer(GravityCompat.START)
                startActivity(i)
            }
            R.id.today -> {
                setDataToday()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.next_day -> {
                setDataNextDay()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.next_day_complete -> {
                setCompleteDataNextDay()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.today_complete -> {
                setCompleteDataToday()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.info_activity -> {
                val intent = Intent(this, InfoActivity::class.java)
                drawer_layout.closeDrawer(GravityCompat.START)
                startActivity(intent)
            }
            R.id.klassenview -> {
                val intent = Intent(this, KlasseActivity::class.java)
                startActivity(intent)
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

    private fun addOnFabClickListener() {
        fab.setOnClickListener {
            when(vertretungsOption) {
                VertretungsOption.TODAY_FILTERED -> setDataToday()
                VertretungsOption.NEXT_DAY_FILTERED -> setDataNextDay()
                VertretungsOption.TODAY -> setCompleteDataToday()
                VertretungsOption.NEXT_DAY -> setCompleteDataNextDay()
            }
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
