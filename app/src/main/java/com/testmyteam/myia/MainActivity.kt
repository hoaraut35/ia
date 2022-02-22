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
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark

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
                    Toast.makeText(
                        this,
                        "Extract barcode " + rawValue.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    textView.text = "Data : " + rawValue.toString()

                }

            }.addOnFailureListener {
                Toast.makeText(this, "Can't extract barcode " + it.message, Toast.LENGTH_SHORT)
                    .show()
            }


        }


        //setup face detection...
        val highAccuracyOpts = FaceDetectorOptions.Builder()
             //select better mode
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
             //identitify eyes ears, noze , etc
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
             //get data
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        // Real-time contour detection
        val realTimeOpts = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()

        //setup image ...
        val photo = BitmapFactory.decodeResource(resources, R.drawable.photo)
        val photoTerminator = BitmapFactory.decodeResource(resources, R.drawable.terminator)


        val imageViewPhoto = findViewById<ImageView>(R.id.imageViewPhoto)
        val imageViewPhotoTerminator = findViewById<ImageView>(R.id.imageViewPhoto2)

        imageViewPhoto.setImageResource(R.drawable.photo)
        imageViewPhotoTerminator.setImageResource(R.drawable.terminator)


        //setup inputImage ...
        val photoOfMe = InputImage.fromBitmap(photo, 0)
        val photoOfTerminator = InputImage.fromBitmap(photoTerminator, 0)

        val detector = FaceDetection.getClient(highAccuracyOpts)

        //setup the button ...
        val buttonPhoto = findViewById<Button>(R.id.btnphoto)

        val textViewPhoto = findViewById<TextView>(R.id.textphoto)
        val textViewPhoto2 = findViewById<TextView>(R.id.textphoto2)

        buttonPhoto.setOnClickListener {

            val result2 = detector.process(photoOfTerminator)

                .addOnSuccessListener{faces ->

                    for (face in faces) {
                        val bounds = face.boundingBox
                        val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                        val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                        textViewPhoto2.text =""
                        textViewPhoto2.append("Emplacement : " +  bounds.toString() + "\n")
                        textViewPhoto2.append("Rotation tête horizontale X : "+ rotY.toString() + "°" + "\n" )
                        textViewPhoto2.append("Inclinaison tête axe Z : "+ rotZ.toString() + "°" + "\n" )




                        // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                        // nose available):
                        val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                        leftEar?.let {
                            val leftEarPos = leftEar.position
                        }

                        // If contour detection was enabled:
                        val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                        val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                        // If classification was enabled:
                        if (face.smilingProbability != null) {
                            val smileProb = face.smilingProbability
                            textViewPhoto2.append("Evaluation du sourire [0.0 - 1.0] : "+ smileProb.toString()  )
                        }
                        if (face.rightEyeOpenProbability != null) {
                            val rightEyeOpenProb = face.rightEyeOpenProbability
                        }

                        // If face tracking was enabled:
                        if (face.trackingId != null) {
                            val id = face.trackingId
                        }
                    }
            }


            val result = detector.process(photoOfMe)

                .addOnSuccessListener { faces ->

                //    val faceGraphic :FaceContour = FaceContourGraphic(photo2)

                    for (face in faces) {
                        val bounds = face.boundingBox
                        val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                        val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                        textViewPhoto.text =""
                        textViewPhoto.append("Emplacement : " +  bounds.toString() + "\n")
                        textViewPhoto.append("Rotation tête horizontale X : "+ rotY.toString() + "°" + "\n" )
                        textViewPhoto.append("Inclinaison tête axe Z : "+ rotZ.toString() + "°" + "\n" )




                        // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                        // nose available):
                        val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                        leftEar?.let {
                            val leftEarPos = leftEar.position
                        }

                        // If contour detection was enabled:
                        val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                        val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                        // If classification was enabled:
                        if (face.smilingProbability != null) {
                            val smileProb = face.smilingProbability
                            textViewPhoto.append("Evaluation du sourire [0.0 - 1.0] : "+ smileProb.toString()  )
                        }
                        if (face.rightEyeOpenProbability != null) {
                            val rightEyeOpenProb = face.rightEyeOpenProbability
                        }

                        // If face tracking was enabled:
                        if (face.trackingId != null) {
                            val id = face.trackingId
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Can't extract photo " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
        }

    }

}