package com.gresikdev.sihipomobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_sihipo_c.*
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.URL

class SihipoCActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sihipo_c)

        val url = intent?.getBundleExtra("bundle")?.getString("url")

        toast("url $url")

        url.let {
            btn1on?.setOnClickListener { cekData("$url/1?p=0") }
            btn2on?.setOnClickListener { cekData("$url/1?p=1") }
            btn3on?.setOnClickListener { cekData("$url/1?p=2") }
            btn4on?.setOnClickListener { cekData("$url/1?p=3") }
            btn5on?.setOnClickListener { cekData("$url/1?p=4") }
            btn6on?.setOnClickListener { cekData("$url/1?p=5") }
            btn7on?.setOnClickListener { cekData("$url/1?p=6") }
            btn8on?.setOnClickListener { cekData("$url/1?p=7") }
            btn1off?.setOnClickListener { cekData("$url/0?p=0") }
            btn2off?.setOnClickListener { cekData("$url/0?p=1") }
            btn3off?.setOnClickListener { cekData("$url/0?p=2") }
            btn4off?.setOnClickListener { cekData("$url/0?p=3") }
            btn5off?.setOnClickListener { cekData("$url/0?p=4") }
            btn6off?.setOnClickListener { cekData("$url/0?p=5") }
            btn7off?.setOnClickListener { cekData("$url/0?p=6") }
            btn8off?.setOnClickListener { cekData("$url/0?p=7") }
            btn1toggle?.setOnClickListener { cekData("$url/2?p=0") }
            btn2toggle?.setOnClickListener { cekData("$url/2?p=1") }
            btn3toggle?.setOnClickListener { cekData("$url/2?p=2") }
            btn4toggle?.setOnClickListener { cekData("$url/2?p=3") }
            btn5toggle?.setOnClickListener { cekData("$url/2?p=4") }
            btn6toggle?.setOnClickListener { cekData("$url/2?p=5") }
            btn7toggle?.setOnClickListener { cekData("$url/2?p=6") }
            btn8toggle?.setOnClickListener { cekData("$url/2?p=7") }

            doAsync {
                var hasil = ""
                runBlocking {
                    hasil = URL(url).readText()
                }
                uiThread {
                    cobaKonversi(hasil)
                }
            }
        }
    }

    fun cobaKonversi(hasil: String) {
        val parser = JsonParser()
        val o = parser.parse(hasil).asJsonObject
        if (o.has("value")) {
            val gson = Gson()
            val listType = object : TypeToken<List<Int>>() { }.type
            cekButton(gson.fromJson<List<Int>>(o["value"], listType))
        }
    }

    fun cekButton(arr: List<Int>) {
        btn1toggle?.text = if (arr[0] == 0) "1 off" else "1 on"
        btn2toggle?.text = if (arr[1] == 0) "2 off" else "2 on"
        btn3toggle?.text = if (arr[2] == 0) "3 off" else "3 on"
        btn4toggle?.text = if (arr[3] == 0) "4 off" else "4 on"
        btn5toggle?.text = if (arr[4] == 0) "5 off" else "5 on"
        btn6toggle?.text = if (arr[5] == 0) "6 off" else "6 on"
        btn7toggle?.text = if (arr[6] == 0) "7 off" else "7 on"
        btn8toggle?.text = if (arr[7] == 0) "8 off" else "8 on"
    }

    fun cekData(alamat: String) {
        doAsync {
            var hasil = ""
            runBlocking {
                hasil = URL(alamat).readText()
            }
            uiThread {
                cobaKonversi(hasil)
            }
        }
    }

}
