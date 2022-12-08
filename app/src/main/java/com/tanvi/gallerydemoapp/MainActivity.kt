package com.tanvi.gallerydemoapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    lateinit var ivImageView: ImageView
    lateinit var chooseImage:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ivImageView=findViewById(R.id.ivImageView)
        chooseImage = findViewById(R.id.chooseImage)
        chooseImage.setOnClickListener {
            //here we are taking the software permissions 
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //requesting for the permissions
                    // yha per hum array m kon kon si permission chaiye ek bar m mangte hai
                    // jse hume yha bs read extrnal storage ki permission chaiye to ek hi mange
                    // is tarah or permison bi mang sakte h
                    //yha permission array ka size ky hai?

                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    pickImageFromGallery();
                }
            } else {
                pickImageFromGallery();
            }
        }
    }
    private fun pickImageFromGallery() {
        // yha intent use kiya gya h ACTION_PICK wala
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*" //intent type m bataya ki image load karni hai
        //startActivityForResult or startActivity m farak y ki startActivityForResult m action khtm hone k bad koi na koi value militi hai,
        // jabki..startActivity m koi value nhi miltie  -> y value hume onActivityResult m milti hai
        // yha per hum ek permission code bhjte h..and  nhi suno phle..y code hum kuch bi d sakte h id ki tarah smjlo
        //ok ..
        // y s image pick kri...image tumnse seleck kr k ok kiya
        //uske bad
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
//
        companion object {
            private val IMAGE_PICK_CODE = 1000; //this will be set by us
            private val PERMISSION_CODE = 1001;// this will also be set by us
        }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                // ni samajh ara 53 line ka
                // to yha pr whi check hora hai, ki khi array empty(mtlb size  0 to nhi hai)
                // agar zero nhi mtlb koi na koi permission mangi gai hai...smji?yes
                //grantResults[0] == PackageManager.PERMISSION_GRANTED yha check krra h ki user n allow kiya h ki nhi, agar granted hai mtlb kiya h
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   // yha hume permission mil chuki hai..or hum gallery khol sakte hai
                    pickImageFromGallery()
                }
                else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    // hum yha ate h..mtlb code is fulnction ko call krta hai
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // resultCode mtlb muser n image select krli ya cancel kr diya
        //or request code whi code h jo humne startactivityforresult m pass kiya tha
        // to whi check hora h ki jo hmne request send ki thi image k liye use permission code k sath ky y yhi h
        //
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            // yha pr hume wo data mil jata h
            //but from the gallery image is not uploading
            // ba ->bo hora h  logic m soch..dekhliya h..shi kro
           // ivImageView -> y imageview humari activity m nhi hai islye app crash hori h//
            Log.v("locationOfImage",data?.data?.path ?:"")
            // tumne imageview laga to diya but error dekho
            ivImageView.setImageURI(data?.data)
        }

    }
}



