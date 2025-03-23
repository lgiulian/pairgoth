package com.iqoid.pairgoth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.iqoid.pairgoth.client.model.Tournament
import com.iqoid.pairgoth.client.network.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class TournamentActivity : AppCompatActivity() {
    private lateinit var tournamentListView: ListView
    private lateinit var tournaments: Map<String, Tournament>

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView
    private var camera: Camera? = null
    private var isScanning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament)

        tournamentListView = findViewById(R.id.tournamentListView)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val scanQrButton: Button = findViewById(R.id.scanQrButton)
        previewView = findViewById(R.id.previewView)
        previewView.visibility = View.GONE

        scanQrButton.setOnClickListener {
            if (allPermissionsGranted()) {
                if (!isScanning) {
                    startCamera()
                    previewView.visibility = View.VISIBLE
                    isScanning = true
                }
            } else {
                requestPermissions()
            }
        }

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

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA

            // Preview
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            // Image Analysis
            val imageAnalyzer = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                    processImage(imageProxy)
                }
            }

            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer) // Bind both preview and image analysis
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImage(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        val scanner = BarcodeScanning.getClient()
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { scannedUrl ->
                        saveBaseUrl(scannedUrl)
                        Toast.makeText(this, "Tournament QR Code saved!", Toast.LENGTH_SHORT).show()
                        stopCamera()
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("QR Scanner", "Error scanning QR", e)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun stopCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
            previewView.visibility = View.GONE
            isScanning = false
        }, ContextCompat.getMainExecutor(this))
    }

    private fun saveBaseUrl(url: String) {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        prefs.edit().putString("api_base_url", url).apply()
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
    }
}