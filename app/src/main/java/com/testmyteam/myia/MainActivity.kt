package com.testmyteam.myia

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setup the barcode scanner...
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC,
                Barcode.FORMAT_EAN_13
            )
            .build()

        //setup image ...
        val image = BitmapFactory.decodeResource(resources, R.drawable.codebarre)
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageResource(R.drawable.codebarre)


        //setup inputImage ...
        val image2 = InputImage.fromBitmap(image, 0)

        //setup instance of scanner
        val scanner = BarcodeScanning.getClient()

        //setup the button ...
        val button = findViewById<Button>(R.id.btn)
        val textView = findViewById<TextView>(R.id.textView)

        button.setOnClickListener {

            val result = scanner.process(image2).addOnSuccessListener { barcodelist ->
                for (barcode in barcodelist) {
                    val bounds = barcode.boundingBox
                    val corners = barcode.cornerPoints
                    val rawValue = barcode.rawValue
                    val valueType = barcode.valueType
                    Toast.makeText(this, "Extract barcode " + rawValue.toString(), Toast.LENGTH_SHORT).show()
                    textView.text = "Data : " + rawValue.toString()

                }

            }.addOnFailureListener {
                Toast.makeText(this, "Can't extract barcode " + it.message, Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

}