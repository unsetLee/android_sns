package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri // url 상위 개념이라는 듯, 링크뿐 아니라 사진 등도 전달하는 것인듯?
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {

    val PICK_IMAGE_FROM_ALBUM = 0
    var storage : FirebaseStorage? = null
    var photoUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        storage = FirebaseStorage.getInstance() // initialize

        //var photoPickerIntent = Intent(Intent.ACTION_PICK)
        //photoPickerIntent.type = "image/*"
        //startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        image_add.setOnClickListener{
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }

        addphoto_btn_upload.setOnClickListener {
            contentUpload()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_FROM_ALBUM){
            if(resultCode == Activity.RESULT_OK){
                photoUri = data?.data
                addphoto_image.setImageURI(data?.data)
            }
        }
    }

    fun contentUpload(){
        val timeStamp = SimpleDateFormat("yyyy_HHmmss").format(Date())
        val imageFileName ="PNG_" + timeStamp + "_.png"

        val storageRef = storage?.reference?.child("images")?.child(imageFileName)

        storageRef?.putFile(photoUri!!)?.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(this, "업로드 성공", Toast.LENGTH_LONG).show()
        }
    }
}
