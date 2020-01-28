package dk.shortify.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import androidx.preference.PreferenceScreen
import android.content.Intent
import android.net.Uri


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, SettingFragment())
            .commit()
    }

    class SettingFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(dk.shortify.R.xml.settings_preference)

            val historyClear: PreferenceScreen? = findPreference("history_clear")
            val homepage: PreferenceScreen? = findPreference("homepage")
            val version: PreferenceScreen? = findPreference("version")

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
