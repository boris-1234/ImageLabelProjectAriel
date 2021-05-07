package com.example.imagelabel

import android.R.attr
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val SELECT_PICTURE: Int = 200;
    private lateinit var uri: Uri
    private lateinit var image: ImageView
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
    }

    //ImageView, Detect Button, Select Image Button, TextView with text.
    //Choose Image from phone
    fun selectImage(view: View) {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        // pass the constant to compare it
        // with the returned requestCode

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)


    }

    //Analyze Image
    fun analyzeImage(view: View) {
        var image: InputImage? = null
        try {
            image = InputImage.fromFilePath(this, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // To use default options:
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        if (image != null)
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Task completed successfully
                    // ...
                    //Put text in TextView
                    textView.text = labels.get(0).text
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        val index = label.index
                    }
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode === SELECT_PICTURE) {
                // Get the url of the image from data
                val selectedImageUri: Uri? = data?.data
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    image.setImageURI(selectedImageUri)
                    uri=selectedImageUri
                }
            }
        }

    }
}