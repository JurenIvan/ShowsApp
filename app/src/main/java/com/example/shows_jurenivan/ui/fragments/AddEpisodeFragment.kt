package com.example.shows_jurenivan.ui.fragments

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.TextInputEditText
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.data.dataStructures.Episode
import com.example.shows_jurenivan.data.viewModels.EpisodesViewModel
import kotlinx.android.synthetic.main.fragment_add_episode.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File

class AddEpisodeFragment : Fragment(), BackKeyInterface{


    companion object {

        const val MAX_SEASON_NUM = 20
        const val MIN_SEASON_NUM = 1
        const val MAX_EPISODE_NUM = 99
        const val MIN_EPISODE_NUM = 1

        const val TAKE_PHOTO = 80
        const val PICK_PHOTO = 81

        const val REQUEST_CAMERA_PERMISSION = 101
        const val REQUEST_READ_EXTERNAL_STORAGE = 102

        private const val SAVED_INSTANCE_SEASON = "SEASON"
        private const val SAVED_INSTANCE_EPISODE = "EPISODE"
        private const val SAVED_INSTANCE_FILEURI = "FILE"
    }

    private var seasonNum = 1
    private var episodeNum = 1
    private var fileURL: Uri? = null

    private var showId = 0
    private lateinit var viewModel: EpisodesViewModel
    

    fun setShow(showId: Int) {
        this.showId = showId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setHasOptionsMenu(true)
        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_light)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }


        image.setImageResource(R.drawable.ic_camera)

        viewModel = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)
        viewModel.setShow(showId)

        seasonEpisodeNumberSelector.setOnClickListener {
            seasonEpisodeNumberListener
        }

        pictureBackground.setOnClickListener { selectPictureDialog() }
        uploadPhoto.setOnClickListener { selectPictureDialog() }
        changePhoto.setOnClickListener { selectPictureDialog() }

        episodeTitle.addTextChangedListener(textWatcher)
        episodeDescription.addTextChangedListener(textWatcher)

        btnSave.setOnClickListener {

            val episode = Episode(
                fileURL.toString(),
                episodeNum,
                seasonNum,
                episodeDescription.text.toString(),
                episodeTitle.text.toString()
            )
            viewModel.addEpisode(episode)
            activity?.onBackPressed()
        }

        if (savedInstanceState != null) {
            seasonNum = savedInstanceState.getInt(SAVED_INSTANCE_SEASON)
            episodeNum = savedInstanceState.getInt(SAVED_INSTANCE_EPISODE)
            val uri = savedInstanceState.getString(SAVED_INSTANCE_FILEURI)
            if (uri != null)
                fileURL = Uri.parse(uri)

            seasonEpisodeNumberSelector.text = String.format("S%02d E%02d", seasonNum, episodeNum)
            image.setImageURI(fileURL)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            activity?.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private val seasonEpisodeNumberListener = {
        val builder = context?.let { AlertDialog.Builder(it) }
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

        builder?.setView(view)
        val dialog = builder?.create()

        btn.setOnClickListener {
            episodeNum = enp.value
            seasonNum = snp.value
            seasonEpisodeNumberSelector.text = String.format("S%02d E%02d", seasonNum, episodeNum)
            dialog?.dismiss()
        }
        dialog?.show()
    }


    private fun selectPictureDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireContext())
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
        val permissionsNeeded = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val requestPermissionList = generatePermissionList(permissionsNeeded)

        if (requestPermissionList.isEmpty()) {
            openCamera()
        }
        for (permission in requestPermissionList) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    requestPermissionList.toTypedArray(),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }
    }

    private fun pickPicture() {
        val permissionsNeeded = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        val requestPermissionList = generatePermissionList(permissionsNeeded)

        if (requestPermissionList.isEmpty()) {
            openGallery()
        }
        for (permission in requestPermissionList) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    requestPermissionList.toTypedArray(),
                    REQUEST_READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    private fun openGallery() {
        val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(
            pickImageIntent,
            PICK_PHOTO
        )
    }

    private fun generatePermissionList(permissionsNeeded: Array<String>): ArrayList<String> {
        val stillNeeded = ArrayList<String>()

        for (permission in permissionsNeeded) {
            if (ActivityCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                stillNeeded.add(permission)
            }
        }
        return stillNeeded
    }

    private fun openCamera() {
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile("image", ".jpg", storageDir)
        fileURL = FileProvider.getUriForFile(requireContext(), "com.example.shows_jurenivan", image)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity?.packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileURL)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(
                intent,
                TAKE_PHOTO
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty()) {
                    if (!hasUngrantedPermissions(grantResults)) {
                        openCamera()
                    } else {
                        explainEnablePermission("Camera and write on external storage")
                    }
                }
            }
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty())) {
                    if (!hasUngrantedPermissions(grantResults)) {
                        pickPicture()
                    } else {
                        explainEnablePermission("Read external storage")
                    }
                }
            }
        }
    }

    private fun hasUngrantedPermissions(grantResults: IntArray): Boolean {
        for (permission in grantResults) {
            if (permission != PackageManager.PERMISSION_GRANTED) return true
        }
        return false
    }

    private fun explainEnablePermission(whichPermission: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Permission needed!")
        builder.setMessage("$whichPermission permission is needed for this action, please enable it.")
        builder.setNeutralButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            btnSave.isEnabled =
                checkAllTextBoxConditions(episodeTitle) && checkAllTextBoxConditions(episodeDescription)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        private fun checkAllTextBoxConditions(textBox: TextInputEditText?): Boolean {
            return textBox?.text?.length?.let { len -> len >= 1 } ?: false
        }
    }


    override fun backKeyPressed() {
        if (episodeDescription.text.toString().isNotBlank() ||
            episodeTitle.text.toString().isNotBlank() ||
            fileURL != null ||
            seasonNum != MIN_SEASON_NUM ||
            episodeNum != MIN_EPISODE_NUM
        ) {

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirm back")
            builder.setMessage("Are you sure you want to discard all changes?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }

            builder.create().show()
        } else {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

}


interface BackKeyInterface {
    fun backKeyPressed()
}