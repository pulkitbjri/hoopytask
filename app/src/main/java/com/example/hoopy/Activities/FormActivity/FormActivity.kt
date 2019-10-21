package com. example.hoopy.Activities.FormActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_form.*
import android.util.Patterns
import android.text.TextUtils
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.hoopy.models.User
import java.util.regex.Pattern
import java.io.File
import java.util.*
import android.os.Environment
import com.example.hoopy.R
import java.text.SimpleDateFormat
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.IOException
import android.app.Activity
import android.net.Uri
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.hoopy.Utils


class FormActivity : AppCompatActivity() {

    private var  isUpdate : Boolean =false
    private var pictureFilePath: String? =null
    private lateinit var viewModel: FormViewModel

    var user : User = User(null,null,null,null,null,null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProviders.of(this).get(FormViewModel::class.java)

        attachTextListners()
        attachListners()

        checkForIntentData()

    }

    private fun checkForIntentData() {
        if (intent.hasExtra("user"))
        {
            isUpdate=true
            viewModel.getUser(intent.getSerializableExtra("user") as User)
            save.setText("Update")
            select_image.setText("Update Image")
            toolbar.setTitle("Update User")
        }
    }

    private fun setUserData (user:User) {
        this.user=user
        name.setText(user.name)
        contact.setText(user.mobile.toString())
        email.setText(user.email)
        username.setText(user.username)
        Glide.with(this).load(user.imageUrl).into(image)
    }

    private fun attachListners() {
        select_image.setOnClickListener{
            cameraIntent()
        }
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        save.setOnClickListener{
            if (isUpdate)
            {
                if (user.name==null || user.email==null ||user.mobile==null || user.username==null )
                {
                    Toast.makeText(this@FormActivity,"All Values Are Mandatory",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                viewModel.update(user)
            }
            else{
                if (user.name==null || user.email==null ||user.mobile==null || user.username==null || pictureFilePath.isNullOrEmpty() )
                {
                    Toast.makeText(this@FormActivity,"All Values Are Mandatory",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                viewModel.save(user, pictureFilePath!!)

            }
        }
        viewModel.loadingData.observe(this,Observer {
            if (it) {
                Utils.showprogressdialog(this)
                setViewsEnabled(false)
            }
            else
            {
                Utils.dismissprogressdialog()
                setViewsEnabled(true)

            }
        })
        viewModel.toastData.observe(this,Observer {
            Toast.makeText(this@FormActivity,it,Toast.LENGTH_LONG).show()
        })
        viewModel.userAdded.observe(this,Observer {
            onBackPressed()
        })
        viewModel.userValue.observe(this,Observer {
            user=it
            setUserData(user)
        })

    }

    private fun setViewsEnabled(b: Boolean) {
        for (pos in 0 .. main.childCount)
        {
            main.getChildAt(pos)?.setEnabled(b);
        }
    }

    private fun attachTextListners() {
        username.addTextChangedListener( object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s!!.length == 0)
                {
                    contact.setError("Enter Valid Name")
                    user.username=null
                }
                else{
                    contact.setError(null)
                    user.username=username.text.toString()
                }
            }
        })
        email.addTextChangedListener( object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isValidEmail(s!!))
                    email.setError("Enter Valid Email")
                else
                    email.setError(null)
                if (s.length == 0)
                    user.email=null
                else
                    user.email=email.text.toString()
            }
        })
        contact.addTextChangedListener( object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isValidMobile(s.toString()) && s?.length!! == 0)
                {
                    user.mobile=null
                    contact.setError("Enter Valid mobile")
                }
                else {
                    contact.setError(null)
                    user.mobile=contact.text.toString().toLong()
                }
            }
        })
        name.addTextChangedListener( object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (!isValidName(s.toString()))
                    contact.setError("Enter Valid Name")
                else
                    contact.setError(null)
                if (s?.length!! == 0)
                    user.name=null
                else
                    user.name=name.text.toString()
            }
        })
    }
    private fun cameraIntent() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
//            startActivityForResult(cameraIntent, 1)

            var pictureFile: File? = null
            try {
                pictureFile = getFile()
            } catch (ex: IOException) {
                Toast.makeText(
                    this,
                    "Photo file can't be created, please try again",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            if (pictureFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    applicationContext,
                    "com.example.hoopy.fileprovider",
                    pictureFile
                )
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(cameraIntent, 1)
            }
        }
    }
    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
    private fun isValidMobile(phone: String): Boolean {
        val regx="^[0-9]*\$"
        return Pattern.matches(regx,phone)
    }
    private fun isValidName(name : String ) :Boolean{
        val regx = "^[\\p{L} .'-]+$"
        return Pattern.matches(regx,name)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1 && resultCode === Activity.RESULT_OK) {
            val imgFile = File(pictureFilePath)
            if (imgFile.exists()) {
                image.setImageURI(Uri.fromFile(imgFile))
            }
        }
        else
            pictureFilePath=""
    }
    private fun getFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Date())
        val pictureFile = "hoopy$timeStamp"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(pictureFile, ".jpg", storageDir)
        pictureFilePath = image.absolutePath
        return image
    }

}
