package dk.shortify.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import androidx.preference.PreferenceScreen
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.model.Notice
import de.psdev.licensesdialog.model.Notices
import dk.shortify.R
import dk.shortify.db.DB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import splitties.toast.toast


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, SettingFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.not_move, R.anim.leftout)
    }

    class SettingFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(dk.shortify.R.xml.settings_preference)

            val historyClear: PreferenceScreen? = findPreference("history_clear")

            val homepage: PreferenceScreen? = findPreference("homepage")
            val other: PreferenceScreen? = findPreference("other_app")

            val license: PreferenceScreen? = findPreference("license")
            val version: PreferenceScreen? = findPreference("version")

            historyClear?.setOnPreferenceClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    DB.get(context!!).historyDao().clear()
                }
                toast(R.string.clearComplete)
                true
            }

            homepage?.setOnPreferenceClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://developmentdk.ivyro.net/service")
                    )
                )
                true
            }

            other?.setOnPreferenceClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/dev?id=5704360550255011002")
                    )
                )
                true
            }

            license?.setOnPreferenceClickListener {
                showLicense()
            }

            version?.summary = getAppVersionName()
        }

        private fun showLicense(): Boolean{
            val notices = Notices().apply {
                addNotice(Notice("Retrofit", "https://square.github.io/retrofit", "Copyright 2013 Square, Inc.", ApacheSoftwareLicense20()))
                addNotice(Notice("Gson", "https://github.com/google/gson", "Copyright 2008 Google Inc.", ApacheSoftwareLicense20()))
                addNotice(Notice("Splitties", "https://github.com/LouisCAD/Splitties", "", ApacheSoftwareLicense20()))
                addNotice(Notice("no-internet-layout", "https://github.com/MohamedWessam/no-internet-layout", "Copyright 2019 Mohamed Wessam.", ApacheSoftwareLicense20()))
                addNotice(Notice("TedAdHelper", "https://github.com/ParkSangGwon/TedAdHelper", "Copyright 2017 Ted Park.", ApacheSoftwareLicense20()))
            }

            LicensesDialog.Builder(context)
                .setNotices(notices)
                .setIncludeOwnLicense(true)
                .build()
                .show()

            return true
        }

        private fun getAppVersionName(): String {
            val packageInfo: PackageInfo

            try {
                packageInfo = context!!.packageManager.getPackageInfo(context!!.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                return ""
            }

            return packageInfo.versionName
        }
    }
}
