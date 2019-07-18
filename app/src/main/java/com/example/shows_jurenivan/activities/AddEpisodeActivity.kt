package com.example.shows_jurenivan.activities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.TextInputEditText
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.checkSelfPermission
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.dataStructures.Episode
import kotlinx.android.synthetic.main.activity_add_episode.*
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment
import android.support.v4.content.FileProvider
import java.io.File


const val RESULT = "episode"


const val MAX_SEASON_NUM = 20
const val MIN_SEASON_NUM = 1
const val MAX_EPISODE_NUM = 99
const val MIN_EPISODE_NUM = 1

const val RESULT_SHOW_NUM = "resultShowNum"
const val TAKE_PHOTO = 80
const val PICK_PHOTO = 81


const val REQUEST_CAMERA_PERMISSION = 101
const val REQUEST_READ_EXTERNAL_STORAGE = 102


class AddEpisodeActivity : AppCompatActivity() {

    private var seasonNum = 1
    private var episodeNum = 1
    private lateinit var fileURL: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnSave.isEnabled =
                    checkAllTextBoxConditions(episodeTitle) && checkAllTextBoxConditions(episodeDescription)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }

        episodeTitle.addTextChangedListener(textWatcher)
        episodeDescription.addTextChangedListener(textWatcher)

        uploadPhoto.setOnClickListener {
            selectPictureDialog()
        }
        changePhoto.setOnClickListener {
            selectPictureDialog()
        }



        seasonEpisodeNumberSelector.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.number_picker, null)

            val snp = view.findViewById(R.id.seasonNumberPicker) as NumberPicker
            val enp = view.findViewById(R.id.episodeNumberPicker) as NumberPicker
            val btn = view.findViewById(R.id.saveButton) as Button

            snp.maxValue = MAX_SEASON_NUM
            snp.minValue = MIN_SEASON_NUM
            enp.maxValue = MAX_EPISODE_NUM
            enp.minValue = MIN_EPISODE_NUM

            if (seasonNum != 1) snp.value = seasonNum
            if (episodeNum != 1) enp.value = episodeNum

            builder.setView(view)
            val dialog = builder.create()

            btn.setOnClickListener {
                episodeNum = enp.value
                seasonNum = snp.value
                seasonEpisodeNumberSelector.text = String.format("S%d E%d", seasonNum, episodeNum)
                dialog.dismiss()
            }
            dialog.show()
        }


        btnSave.setOnClickListener {
            val resultIntent = Intent()
            val episode = Episode(episodeNum, seasonNum, episodeDescription.text.toString(), episodeTitle.text.toString())
            val pos = intent.getIntExtra(RESULT_SHOW_NUM, -1)
            resultIntent.putExtra(RESULT_SHOW_NUM, pos)
            resultIntent.putExtra(RESULT, episode)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun checkAllTextBoxConditions(textBox: TextInputEditText?): Boolean {
        return textBox?.text?.length?.let { len -> len >= 1 } ?: false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectPictureDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setItems(options) { dialog, position ->
            when (position) {
                0 -> takePicture()
                1 -> pickPicture()
            }
            dialog.dismiss()
        }
        builder.show()


    }


    private fun takePicture() {
        if (checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this)
                    .setTitle("We'll need a permission for camera to take a photo.")
                    .setNeutralButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION
                        )
                    }.create().show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
    }

    private fun openCamera() {

//        val values = ContentValues(1)
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
//        fileURL = contentResolver
//            .insert(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                values
//            )

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            "image", /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        fileURL = FileProvider.getUriForFile(this,
            "com.example.shows_jurenivan",
            image);

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileURL)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, TAKE_PHOTO)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_DENIED) {
                    openCamera()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_CAMERA_PERMISSION
                    )
                }
            }
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    pickPicture()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_EXTERNAL_STORAGE
                    )
                }
                return
            }
        }
    }

    private fun pickPicture() {
        if (checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //show why
            }
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
        } else {
            val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickImageIntent, PICK_PHOTO)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TAKE_PHOTO -> image.setImageURI(fileURL)

                PICK_PHOTO -> {
                    fileURL = data!!.data
                    image.setImageURI(fileURL)
                }

            }
            changeVisibility()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun changeVisibility() {
        uploadPhoto.visibility = View.GONE
        changePhoto.visibility = View.VISIBLE
    }

}
