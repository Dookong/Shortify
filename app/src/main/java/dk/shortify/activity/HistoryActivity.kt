package dk.shortify.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.ads.AdView
import dk.shortify.AdsKey
import dk.shortify.R
import dk.shortify.adapter.HistoryAdapter
import dk.shortify.db.DB
import gun0912.tedadhelper.TedAdHelper
import gun0912.tedadhelper.banner.OnBannerAdListener
import gun0912.tedadhelper.banner.TedAdBanner
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_main.*

class HistoryActivity : AppCompatActivity() {

    lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = HistoryAdapter(this, lifecycleScope)

        rv_history.adapter = this.adapter
        rv_history.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_history.addItemDecoration(DividerItemDecoration(this, 1))

        DB.get(this@HistoryActivity).historyDao().getAll().observe(this, Observer {
                history ->
            this@HistoryActivity.adapter.apply {
                data = history.reversed()
                notifyDataSetChanged()
            }
        })

        showBannerAd()
    }

    private var facebookBanner: AdView? = null
    private fun showBannerAd(){
        TedAdBanner.showBanner(bannerMain, AdsKey.FACEBOOK2, AdsKey.ADMOB, TedAdHelper.AD_FACEBOOK,
            object : OnBannerAdListener {
                override fun onAdClicked(adType: Int) {
                }

                override fun onLoaded(adType: Int) {
                }

                override fun onError(errorMessage: String?) {
                }

                override fun onFacebookAdCreated(facebookBanner: AdView?) {
                    this@HistoryActivity.facebookBanner = facebookBanner
                }

            })
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.not_move, R.anim.leftout)
    }

    override fun onDestroy() {
        facebookBanner?.destroy()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }
}