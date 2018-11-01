package com.gresikdev.sihipomobile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import com.google.gson.Gson
import com.gresikdev.sihipomobile.hotspotmanager.WifiApManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread
import java.net.URL
import com.google.gson.JsonObject
import com.google.gson.JsonParser



class MainActivity : AppCompatActivity() {

    var wifiApManager: WifiApManager? = null
    //SSID: SIHIPO
    //Pass: sistemhidroponik
    var urlSihipoC: String? = null
    var urlSihipoS: String? = null
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wifiApManager = WifiApManager(this)
        wifiApManager?.showWritePermissionSettings(true)

        btnWiFi?.setOnClickListener {
            result = ""
            scan()
        }

        btnSihipoC?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", urlSihipoC)
            val page = Intent(this@MainActivity, SihipoCActivity::class.java)
            page.putExtra("bundle", bundle)
            startActivity(page)
        }

        scan()
    }

    fun scan() {
        wifiApManager?.getClientList(false) {
            var status = "WifiApState: " + wifiApManager?.getWifiApState() + "\n\n"
            status = "${status}Clients: \n"
            for (clientScanResult in it) {
                status = ("${status}####################\n")
                status = ("${status}IpAddr: " + clientScanResult.getIpAddr() + "\n")
                status = ("${status}Device: " + clientScanResult.getDevice() + "\n")
                status = ("${status}HWAddr: " + clientScanResult.getHWAddr() + "\n")
                status = ("${status}isReachable: " + clientScanResult.isReachable() + "\n")
                cekData(clientScanResult.getIpAddr())
            }
            tvStatus?.text = status
        }
    }

    fun cekButton() {
        if (urlSihipoC?.isEmpty() == true) btnSihipoC?.gone() else btnSihipoC?.visible()
        if (urlSihipoS?.isEmpty() == true) btnSihipoS?.gone() else btnSihipoS?.visible()
    }

    var result: String = ""

    fun cekData(alamat: String) {
        doAsync {
            runBlocking {
                try {
                    val url = "http://$alamat"
                    val hasil = URL(url).readText()
                    val parser = JsonParser()
                    val o = parser.parse(hasil).asJsonObject
                    if (o.has("type") && o.get("type").asString.equals("SIHIPO_C")) {
                        urlSihipoC = url
                    } else if (o.has("type") && o.get("type").asString.equals("SIHIPO_S")) {
                        urlSihipoS = url
                    }
                    result = "$result\n$alamat\n$hasil"
                } catch (e: java.lang.Exception) {

                }
            }
            uiThread {
                tvGetResponse?.text = result
                Log.d("Request", result)
                cekButton()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 0, 0, "Get Clients");
        menu?.add(0, 1, 0, "Open AP");
        menu?.add(0, 2, 0, "Close AP");
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            0 -> scan()
            1 -> wifiApManager?.setWifiApEnabled(null, true)
            2 -> wifiApManager?.setWifiApEnabled(null, false)
        }
        return super.onOptionsItemSelected(item)
    }
}
