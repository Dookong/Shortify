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

    class SettingFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(dk.shortify.R.xml.settings_preference)

            val historyClear: PreferenceScreen? = findPreference("history_clear")
            val homepage: PreferenceScreen? = findPreference("homepage")
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

            version?.summary = getAppVersionName()
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
