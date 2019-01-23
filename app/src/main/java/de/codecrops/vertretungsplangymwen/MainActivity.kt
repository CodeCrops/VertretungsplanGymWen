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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import de.codecrops.vertretungsplangymwen.R.layout.activity_main
import de.codecrops.vertretungsplangymwen.credentials.CredentialsManager
import de.codecrops.vertretungsplangymwen.data.VertretungData
import de.codecrops.vertretungsplangymwen.gui.VertretungsAdapter
import de.codecrops.vertretungsplangymwen.network.HttpGetRequest
import de.codecrops.vertretungsplangymwen.pushnotifications.AppNotificationManager
import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlinx.android.synthetic.main.header_layout.*
import java.text.SimpleDateFormat

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

        val aToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(aToggle)
        aToggle.syncState()

        setCompleteDataToday()

        addOnItemClickListener()
        addOnFabClickListener()
        addOnNoInternetIconClickListener()

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
        addTodayToDatabase(true)
        addNextDayToDatabase(false)
        setFilteredData(Calendar.getInstance().time)
        setHeaderToday()
        vertretungsOption = VertretungsOption.TODAY_FILTERED
    }

    private fun setDataNextDay() {
        val date = addNextDayToDatabase(true)
        addTodayToDatabase(false)
        if(date!=null) {
            setFilteredData(date)
            setHeaderNextDay(date)
            no_data.visibility = View.INVISIBLE
            no_internet_icon.visibility = View.INVISIBLE
        } else {
            val currentCalendar = Calendar.getInstance()
            val currentDate: Date = currentCalendar.time
            val day = SimpleDateFormat("EE", Locale.GERMAN).format(currentDate.time)
            when(day) {
                "Fr." -> {
                    checkDatabaseForEntriesFiltered(3)
                }
                "Sa." -> {
                    checkDatabaseForEntriesFiltered(2)
                }
                "So." -> {
                    checkDatabaseForEntriesFiltered(1)
                }
                else -> {
                    checkDatabaseForEntriesFiltered(1)
                }
            }
        }
        vertretungsOption = VertretungsOption.NEXT_DAY_FILTERED
    }

    private fun setCompleteDataToday() {
        addTodayToDatabase(true)
        addNextDayToDatabase(false)
        setCompleteData(Calendar.getInstance().time)
        setHeaderToday()
        vertretungsOption = VertretungsOption.TODAY
    }

    private fun setCompleteDataNextDay() {
        val date = addNextDayToDatabase(true)
        addTodayToDatabase(false)
        if(date!=null) {
            setCompleteData(date)
            setHeaderNextDay(date)
            no_data.visibility = View.INVISIBLE
            no_internet_icon.visibility = View.INVISIBLE
        } else {
            val currentCalendar = Calendar.getInstance()
            val currentDate: Date = currentCalendar.time
            val day = SimpleDateFormat("EE", Locale.GERMAN).format(currentDate.time)
            when(day) {
                "Fr." -> {
                    checkDatabaseForEntries(3)
                }
                "Sa." -> {
                    checkDatabaseForEntries(2)
                }
                "So." -> {
                    checkDatabaseForEntries(1)
                }
                else -> {
                    checkDatabaseForEntries(1)
                }
            }
        }
        vertretungsOption = VertretungsOption.NEXT_DAY
    }

    private fun checkDatabaseForEntriesFiltered(daysToSkip: Int) {
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, daysToSkip)
        //TODO: nach klasse filtern mit dp
        if(!DBManager.getVertretungenByKlasse(this, "1m21", c.time).isEmpty()) {
            setFilteredData(c.time)
            setHeaderNextDay(c.time)
            no_internet_icon.visibility = View.VISIBLE
            vertretungs_list.visibility = View.VISIBLE
            no_data.visibility = View.INVISIBLE
            no_vertretung.visibility = View.INVISIBLE
        } else {
            vertretungs_list.visibility = View.INVISIBLE
            no_vertretung.visibility = View.VISIBLE
            header.text = getString(R.string.no_data)
            header_icon.setImageResource(R.drawable.ic_error_black)
        }
    }

    private fun checkDatabaseForEntries(daysToSkip: Int) {
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, daysToSkip)
        if(!DBManager.getAllVertretungen(this, c.time).isEmpty()) {
            setCompleteData(c.time)
            setHeaderNextDay(c.time)
            no_internet_icon.visibility = View.VISIBLE
            vertretungs_list.visibility = View.VISIBLE
            no_data.visibility = View.INVISIBLE
            no_vertretung.visibility = View.INVISIBLE
        } else {
            vertretungs_list.visibility = View.INVISIBLE
            no_vertretung.visibility = View.VISIBLE
            header.text = getString(R.string.no_data)
            header_icon.setImageResource(R.drawable.ic_error_black)
        }
    }

    private fun addTodayToDatabase(clear: Boolean) {
        val extract = HttpGetRequest.extractToday(this)
        if(!(extract.unauthorized || extract.networkError)) {
            if(Utils.dateEqualsToday(extract.date)) {
                if(clear)   DBManager.clearVertretungsDB(this)
                for(v: VertretungData in extract.table) {
                    DBManager.addVertretungsstunde(this, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, extract.date)
                }
                no_internet_icon.visibility = View.INVISIBLE
            } else {
                setDataNextDay()
            }
        }
        no_internet_icon.visibility = View.VISIBLE
    }

    private fun addNextDayToDatabase(clear: Boolean) : Date? {
        val extract = HttpGetRequest.extractTomorrow(this)
        if(!(extract.unauthorized || extract.networkError)) {
            val nextDateReturn = Utils.dateEqualsNextDay(extract.date)
            if (nextDateReturn.isNextDay) {
                if(clear)   DBManager.clearVertretungsDB(this)
                for (v: VertretungData in extract.table) {
                    DBManager.addVertretungsstunde(this, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, nextDateReturn.date)
                }
                no_internet_icon.visibility = View.INVISIBLE
            } else {
                setNoData()
            }
            return nextDateReturn.date
        }
        no_internet_icon.visibility = View.VISIBLE
        return null
    }


    private fun setNoData() {
        //Keine Daten vorhanden
        header.text = getString(R.string.no_data)
        header_icon.setImageResource(R.drawable.ic_error_black)
        vertretungs_list.visibility = View.INVISIBLE
        no_data.visibility = View.VISIBLE
    }

    private fun setFilteredData(date: Date) {
        //TODO: Klassen aus DB bekommen
        val list = DBManager.getVertretungenByKlasse(this, "1m21", date)
        val adapter = VertretungsAdapter(list, this)
        vertretungs_list.adapter = adapter

        if(list.isEmpty()) {
            vertretungs_list.visibility = View.INVISIBLE
            no_vertretung.visibility = View.VISIBLE
        } else {
            vertretungs_list.visibility = View.VISIBLE
            no_vertretung.visibility = View.INVISIBLE
        }
    }

    private fun setCompleteData(date: Date) {
        val list = DBManager.getAllVertretungen(this, date)
        val adapter = VertretungsAdapter(list, this)
        vertretungs_list.adapter = adapter

        if(list.isEmpty()) {
            vertretungs_list.visibility = View.INVISIBLE
            no_vertretung.visibility = View.VISIBLE
        } else {
            vertretungs_list.visibility = View.VISIBLE
            no_vertretung.visibility = View.INVISIBLE
        }
    }

    private fun setHeaderToday() {
        val calendar = Calendar.getInstance()
        val currentDate: Date = calendar.time
        val day = SimpleDateFormat("EEEE", Locale.GERMAN).format(currentDate.time)
        header.text = "$day der ${Utils.formGermanDate(calendar)}"
        header_icon.setImageResource(R.drawable.ic_date_black)
    }

    private fun setHeaderNextDay(nextDateReturnDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = nextDateReturnDate
        val currentDate: Date = calendar.time
        val day = SimpleDateFormat("EEEE", Locale.GERMAN).format(currentDate.time)
        header.text = "$day der ${Utils.formGermanDate(calendar)}"
        header_icon.setImageResource(R.drawable.ic_date_black)
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
        //TODO: FIX, wenn man zu schnell öffnet und schließt
        drawer_layout.addDrawerListener(
                object : DrawerLayout.DrawerListener {
                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
                    override fun onDrawerOpened(drawerView: View) {}
                    override fun onDrawerClosed(drawerView: View) {}
                    override fun onDrawerStateChanged(newState: Int) {}
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

    private fun addOnNoInternetIconClickListener() {
        no_internet_icon.setOnClickListener {
            val s = Snackbar.make(drawer_layout,
                    getString(R.string.old_data),
                    Snackbar.LENGTH_INDEFINITE)
            val textView = s.view.findViewById(android.support.design.R.id.snackbar_text) as TextView
            textView.maxLines = 5
            s.setAction("OK", { s.dismiss() })
            s.show()
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
