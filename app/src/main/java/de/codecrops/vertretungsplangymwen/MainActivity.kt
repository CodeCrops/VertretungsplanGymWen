package de.codecrops.vertretungsplangymwen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
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
import de.codecrops.vertretungsplangymwen.service.AllVertretungService
import de.codecrops.vertretungsplangymwen.service.NewVertretungService
import de.codecrops.vertretungsplangymwen.service.ScheduleManager
import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlinx.android.synthetic.main.header_layout.*
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

/**
 * @author K1TR1K
 */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private enum class VertretungsOption {
        TODAY_COMPLETE, TODAY_FILTERED, NEXT_DAY_COMPLETE, NEXT_DAY_FILTERED
    }

    private lateinit var vertretungsOption: VertretungsOption

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        createNotificationChannel()

        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        val aToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(aToggle)
        aToggle.syncState()

        val currentCalendar = Calendar.getInstance()
        val currentDate: Date = currentCalendar.time
        val day = SimpleDateFormat("EE", Locale.GERMAN).format(currentDate.time)
        val menu = nav_view.menu
        if(day == Utils.DAY.SATURDAY || day == Utils.DAY.SUNDAY) {
            menu.findItem(R.id.today).isVisible = false
            menu.findItem(R.id.today_complete).isVisible = false
            vertretungsOption = VertretungsOption.NEXT_DAY_COMPLETE
        } else {
            menu.findItem(R.id.today).isVisible = true
            menu.findItem(R.id.today_complete).isVisible = true
            vertretungsOption = VertretungsOption.TODAY_COMPLETE
        }
        addListeners()

        Utils.fillDatabase(this)
        update()

        ScheduleManager.scheduleAllVertretungJob(this, 15)
    }

    private fun update() {
        if(HttpGetRequest.getResponseCodeForPasswordCheck(this) == HttpURLConnection.HTTP_OK) {
            no_internet_icon.visibility = View.INVISIBLE
        } else {
            no_internet_icon.visibility = View.VISIBLE
        }
        when(vertretungsOption) {
            VertretungsOption.TODAY_FILTERED -> {
                setListData(Calendar.getInstance().time, true)
                setHeaderToday()
            }
            VertretungsOption.TODAY_COMPLETE -> {
                setListData(Calendar.getInstance().time, false)
                setHeaderToday()
            }
            VertretungsOption.NEXT_DAY_FILTERED -> {
                val date = loadNextDayFromDatabase(true)
                setHeaderNextDay(date)
            }
            VertretungsOption.NEXT_DAY_COMPLETE -> {
                val date = loadNextDayFromDatabase(false)
                setHeaderNextDay(date)
            }
        }
    }

    private fun setHeaderToday() {
        val calendar = Calendar.getInstance()
        val currentDate: Date = calendar.time
        val day = SimpleDateFormat("EEEE", Locale.GERMAN).format(currentDate.time)
        header.text = "$day der ${Utils.formGermanDate(calendar)}"
        header_icon.setImageResource(R.drawable.ic_date_black)
    }

    private fun setHeaderNextDay(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val day = SimpleDateFormat("EEEE", Locale.GERMAN).format(date.time)
        header.text = "$day der ${Utils.formGermanDate(calendar)}"
        header_icon.setImageResource(R.drawable.ic_date_black)
    }

    private fun addListeners() {
        setNavigationViewListener()
        addDrawerListener()
        nav_view.setNavigationItemSelectedListener(this)
        addOnItemClickListener()
        addOnFabClickListener()
        addOnNoInternetIconClickListener()
    }

    //WICHTIG
    private fun loadNextDayFromDatabase(filtered: Boolean) : Date {
        val currentCalendar = Calendar.getInstance()
        val currentDate: Date = currentCalendar.time
        val day = SimpleDateFormat("EE", Locale.GERMAN).format(currentDate.time)
        when(day) {
            Utils.DAY.FRIDAY -> {
                currentCalendar.add(Calendar.DATE, 3)
            }
            Utils.DAY.SATURDAY -> {
                currentCalendar.add(Calendar.DATE, 2)
            }
            Utils.DAY.SUNDAY -> {
                currentCalendar.add(Calendar.DATE, 1)
            }
            else -> {
                currentCalendar.add(Calendar.DATE, 1)
            }
        }
        setListData(currentCalendar.time, filtered)
        return currentCalendar.time
    }

    //WICHTIG
    private fun setListData(date: Date, filtered: Boolean) {
        var list: ArrayList<VertretungData> = arrayListOf()
        if(filtered) {
            for(p in DBManager.getAllPreferences(this)) {
                list.addAll(DBManager.getVertretungenByKlasse(this, p.course, date))
            }
        } else {
            list = DBManager.getAllVertretungen(this, date)
        }
        val adapter = VertretungsAdapter(list, this)
        vertretungs_list.adapter = adapter

        if(list.isEmpty()) {
            vertretungs_list.visibility = View.INVISIBLE
            no_vertretung.visibility = View.VISIBLE
        } else {
            vertretungs_list.visibility = View.VISIBLE
            no_vertretung.visibility = View.INVISIBLE
            no_data.visibility = View.INVISIBLE
        }
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
                vertretungsOption = VertretungsOption.TODAY_FILTERED
                update()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.next_day -> {
                vertretungsOption = VertretungsOption.NEXT_DAY_FILTERED
                update()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.next_day_complete -> {
                vertretungsOption = VertretungsOption.NEXT_DAY_COMPLETE
                update()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.today_complete -> {
                vertretungsOption = VertretungsOption.TODAY_COMPLETE
                update()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.info_activity -> {
                val intent = Intent(this, InfoActivity::class.java)
                drawer_layout.closeDrawer(GravityCompat.START)
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
            Utils.fillDatabase(this)
            update()
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
}
