package com.iqoid.pairgoth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.iqoid.pairgoth.client.model.Tournament
import com.iqoid.pairgoth.client.network.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class TournamentActivity : AppCompatActivity() {
    private lateinit var tournamentListView: ListView
    private lateinit var tournaments: Map<String, Tournament>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament)

        tournamentListView = findViewById(R.id.tournamentListView)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Initialize the base URL from shared preferences
        NetworkManager.initializeBaseUrl(this)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NetworkManager.pairGothApiService.getTours()
                if (response.isSuccessful) {
                    tournaments = response.body() ?: emptyMap() // Store the map directly
                    // log all the tournaments
                    tournaments.forEach { (key, value) ->
                        Log.d("TournamentActivity", "Key: $key, Value: $value")
                    }
                    runOnUiThread {
                        updateTournamentList()
                    }
                } else {
                    Log.e("TournamentActivity", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("TournamentActivity", "Exception: ${e.message}")
            }
        }

        tournamentListView.setOnItemClickListener { _, _, position, _ ->
            val selectedTournamentEntry = tournaments.entries.toList()[position] // Get the selected entry
            val tournamentKey = selectedTournamentEntry.key // Get the key
            val selectedTournament = selectedTournamentEntry.value // Get the tournament object
            val intent = Intent(this, TournamentDetailsActivity::class.java).apply { // Change to TournamentDetailsActivity
                putExtra("tournamentName", selectedTournament.name)
                putExtra("tournamentId", tournamentKey) // Pass the key
                Log.d("TournamentActivity", "Key passed to TournamentDetailsActivity: $tournamentKey")
            }
            startActivity(intent)
        }

        // Add logic to scan a QR code and save the base URL in the app preferences
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                val newBaseUrl = result.contents
                sharedPreferences.edit().putString("api-base-url", newBaseUrl).apply()
                NetworkManager.updateBaseUrl(newBaseUrl)
                Toast.makeText(this, "New base URL saved and Retrofit client updated", Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateTournamentList() {
        val tournamentNames = tournaments.values.map { it.name } // Get names from values
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tournamentNames)
        tournamentListView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
