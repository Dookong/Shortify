package dk.shortify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import dk.shortify.api.gd.GdServiceImpl
import dk.shortify.api.me2.Me2Service
import dk.shortify.api.me2.Me2ServiceImpl
import dk.shortify.api.shrtcode.ShrtCodeServiceImpl
import dk.shortify.model.GD
import dk.shortify.model.ME2
import dk.shortify.model.ShrtCode
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import splitties.toast.toast

class MainActivity : AppCompatActivity() {

    var flag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                flag = p2
            }
        }

        btn_shortify.setOnClickListener {
            val url = et_base.text.toString().trim()

            if(url.isBlank())
                toast(R.string.blank)
            else
                shorrtify(url)
        }
    }

    private fun shorrtify(url: String){
        val cb = object : Callback<GD> {
            override fun onFailure(call: Call<GD>, t: Throwable) {
                toast("err: $t")
            }

            override fun onResponse(call: Call<GD>, response: Response<GD>) {
                applyShorten(response.takeIf { it.isSuccessful }?.body()?.shorturl)
            }

        }

        when(flag){
            0 -> GdServiceImpl.vGd.getShorten("json", url).enqueue(cb)
            1-> GdServiceImpl.isGd.getShorten("json", url).enqueue(cb)
            2 -> {
                Me2ServiceImpl.service.getShorten(url).enqueue(object : Callback<ME2>{
                    override fun onFailure(call: Call<ME2>, t: Throwable) {
                        toast("err: $t")
                    }

                    override fun onResponse(call: Call<ME2>, response: Response<ME2>) {
                        applyShorten(response.takeIf { it.isSuccessful }?.body()?.result?.url)
                    }
                })
            }
            3 -> {
                ShrtCodeServiceImpl.service.getShorten(url).enqueue(object : Callback<ShrtCode>{
                    override fun onFailure(call: Call<ShrtCode>, t: Throwable) {
                        toast("err: $t")
                    }

                    override fun onResponse(call: Call<ShrtCode>, response: Response<ShrtCode>) {
                        applyShorten(response.takeIf { it.isSuccessful }?.body()?.result?.shortLink)
                    }
                })
            }
        }
    }

    private fun applyShorten(shorten: String?) {
        shorten?.
            let { tv_shorten.text = it } ?:
            run { toast("Unsupported format:(") }
    }
}
