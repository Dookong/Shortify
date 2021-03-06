package dk.shortify.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import dk.shortify.api.gd.GdServiceImpl
import dk.shortify.api.me2.Me2ServiceImpl
import dk.shortify.api.shrtcode.ShrtCodeServiceImpl
import dk.shortify.model.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import splitties.toast.toast
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import com.facebook.ads.AdView
import com.wessam.library.LayoutImage
import com.wessam.library.NetworkChecker
import com.wessam.library.NoInternetLayout
import dk.shortify.AdsKey
import dk.shortify.R
import dk.shortify.db.DB
import dk.shortify.db.History
import gun0912.tedadhelper.TedAdHelper
import gun0912.tedadhelper.banner.OnBannerAdListener
import gun0912.tedadhelper.banner.TedAdBanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var flag = 0

    lateinit var imm: InputMethodManager

    @Suppress("DEPRECATION") lateinit var pd: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!NetworkChecker.isNetworkConnected(this)){
            NoInternetLayout.Builder(this, R.layout.activity_main)
                .setImage(LayoutImage.SHELL)
                .mainTitle(R.string.internet_main)
                .secondaryText(R.string.internet_sub)
                .buttonText(R.string.internet_btn)
                .animate()
        }

        else
            initView()
    }

    private fun initView() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                flag = p2
            }
        }

        btn_shortify.setOnClickListener {
            val url = et_base.text.toString().trim()

            if (url.isBlank())
                toast(R.string.blank)
            else
                shorrtify(url)
        }

        fab_send.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, tv_shorten.text.toString())
            }

            startActivity(Intent.createChooser(shareIntent, getString(R.string.send)))
        }

        @Suppress("DEPRECATION")
        pd = ProgressDialog(this).apply {
            isIndeterminate = true
            setCancelable(false)
            setMessage(getString(R.string.doing))
        }

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        showBannerAd()
        runIntentAction()
    }

    private fun runIntentAction() {
        with(intent) {
            if (type == "text/plain") {
                when (action) {
                    Intent.ACTION_SEND ->
                        et_base.setText(intent.getStringExtra(Intent.EXTRA_TEXT))

                    Intent.ACTION_PROCESS_TEXT ->
                        et_base.setText(intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT))
                }
                btn_shortify.performClick()
            }
        }
    }

    private fun shorrtify(url: String){
        imm.hideSoftInputFromWindow(et_base.windowToken, 0)
        pd.show()

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
                        applyShorten(response.takeIf { it.isSuccessful }?.body()?.result?.fullShortLink)
                    }
                })
            }
        }
    }

    private fun applyShorten(shorten: String?) {
        shorten?.
            let {
                tv_shorten.text = it
                fab_send.show()

                lifecycleScope.launch(Dispatchers.IO) {
                    DB.get(this@MainActivity).historyDao()
                        .insert(History(flag, et_base.text.toString(), it))
                }

                toast(R.string.clearComplete)

            } ?:
            run {
                tv_shorten.text = ""
                fab_send.hide()
                toast(R.string.unsupported)
            }
        pd.dismiss()
    }

    private var facebookBanner: AdView? = null
    private fun showBannerAd(){
        TedAdBanner.showBanner(bannerMain, AdsKey.FACEBOOK, AdsKey.ADMOB, TedAdHelper.AD_FACEBOOK,
            object : OnBannerAdListener{
                override fun onAdClicked(adType: Int) {
                }

                override fun onLoaded(adType: Int) {
                }

                override fun onError(errorMessage: String?) {
                }

                override fun onFacebookAdCreated(facebookBanner: AdView?) {
                    this@MainActivity.facebookBanner = facebookBanner
                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                overridePendingTransition(R.anim.rightin, R.anim.not_move)
            }
            else -> {
                startActivity(Intent(this, SettingActivity::class.java))
                overridePendingTransition(R.anim.rightin, R.anim.not_move)
            }
        }

        return true
    }

    override fun onDestroy() {
        facebookBanner?.destroy()
        super.onDestroy()
    }
}
