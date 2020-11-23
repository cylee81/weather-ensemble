package edu.cs371m.weather.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_settings.*

class SettingsManager : AppCompatActivity() {
    companion object {
        val doLoopKey = "doLoop"
        val songsPlayedKey = "songsPlayed"
    }

    // XXX probably want a member variable
    private var isLoop: Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        setSupportActionBar(settingsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val songCount =intent.getStringExtra("click_counts")
        isLoop = intent.getBooleanExtra("is_loop", false)
        songsPlayed.text = songCount
        loopSwitch.isChecked = isLoop
        // XXX Write me
        cancel.setOnClickListener{
            cancelButton()
        }
        ok.setOnClickListener{
            okButton()
        }
    }

    private fun cancelButton() {
        // XXX Write me
        doFinish(isLoop)
    }

    private fun okButton() {
        // XXX Write me
        doFinish(loopSwitch.isChecked)
    }

    // Return to MainActivity
    private fun doFinish(loop: Boolean) {
        // XXX Write me.  This function contains most of the "code" in this activity
        val returnIntent = Intent().apply {
            putExtra(doLoopKey, loop)
        }
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == android.R.id.home) {
            // If user clicks "up", then it it as if they clicked OK.  We commit
            // changes and return to parent
            okButton()
            true
        } else super.onOptionsItemSelected(item)

    }
}