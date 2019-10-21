package com.example.hoopy.Activities.FormActivity

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import com.example.hoopy.Network.NetworkClient
import com.example.hoopy.Network.Service
import com.example.hoopy.NetworkResponse.FileUploadResponse
import com.example.hoopy.NetworkResponse.UserAddResponse
import com.example.hoopy.database.Database
import com.example.hoopy.models.User

import java.io.File

import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

import androidx.constraintlayout.widget.Constraints.TAG
import com.example.hoopy.NetworkResponse.UserGetResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FormViewModel(val applicationContext: Application) : AndroidViewModel(applicationContext) {

    val loadingData = MutableLiveData<Boolean>()
    var userValue = MutableLiveData<User>()
    val toastData = MutableLiveData<String>()
    val userAdded = MutableLiveData<Boolean>()
    private val service: Service

    init {
        loadingData.value = false
        service = NetworkClient.getClient().create(Service::class.java)
    }

    fun save(user: User, pictureFilePath: String) {
        loadingData.value = true
        val file = File(pictureFilePath)
        val fbody = RequestBody.create(MediaType.parse("file/*"), file)
        val part = MultipartBody.Part.createFormData("upload", file.name, fbody)

        service.addPhoto(part)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<Response<FileUploadResponse>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(fileUploadResponseResponse: Response<FileUploadResponse>) {
                    Log.i(TAG, "onSuccess: ")
                    if (fileUploadResponseResponse.isSuccessful && fileUploadResponseResponse.body()!!.metadata.response_code == 200) {
                        user.imageUrl = fileUploadResponseResponse.body()!!.urls[0]
                        insertData(user)
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, "onError: ")
                    toastData.value = "Error Adding User"
                    loadingData.value = false
                }
            })

    }

    public fun insertData(user: User) {
        service.addUser(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<Response<UserAddResponse>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(fileUploadResponseResponse: Response<UserAddResponse>) {
                    Log.i(TAG, "onSuccess: ")
                    loadingData.value = false

                    if (fileUploadResponseResponse.isSuccessful && fileUploadResponseResponse.body()!!.metadata.response_code == 200) {
                        toastData.value = fileUploadResponseResponse.body()!!.metadata.response_text

                        GlobalScope.async{
                            Database.getDatabase(applicationContext).Dao().saveUser(user)

                        }
                        userAdded.value=true
                    }

                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, "onError: ")
                    loadingData.value = false
                    toastData.value = "Error Adding User"
                }
            })
    }
    public fun getUser(username: User) {
        loadingData.value=true
        service.getUser(username.username)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<Response<UserGetResponse>>{
                override fun onSuccess(fileUploadResponseResponse: Response<UserGetResponse>) {
                    loadingData.value=false
                    if (fileUploadResponseResponse.isSuccessful && fileUploadResponseResponse.body()!!.metadata.response_code == 200) {
                        toastData.value = fileUploadResponseResponse.body()!!.metadata.response_text

                        GlobalScope.async{
                            Database.getDatabase(applicationContext).Dao().saveUser(
                                fileUploadResponseResponse.body()!!.data.get(0))

                        }
                        userValue.value= fileUploadResponseResponse.body()!!.data.get(0)
                    }
                    else
                    {
                        toastData.value=fileUploadResponseResponse.body()!!.metadata.response_text
                        userAdded.value=true
                    }
                }

                override fun onSubscribe(d: Disposable) {
                }


                override fun onError(e: Throwable) {
                    loadingData.value=true
                    toastData.value = "Error Getting User"

                }

            })

    }

    fun update(user: User) {
        loadingData.value=true
        service.updateUser(user.id,user.name,user.username,user.mobile,user.email)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<Response<UserAddResponse>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(fileUploadResponseResponse: Response<UserAddResponse>) {
                    Log.i(TAG, "onSuccess: ")
                    loadingData.value = false

                    if (fileUploadResponseResponse.isSuccessful && fileUploadResponseResponse.body()!!.metadata.response_code == 200) {
                        toastData.value = fileUploadResponseResponse.body()!!.metadata.response_text

                        GlobalScope.async{
                            Database.getDatabase(applicationContext).Dao().saveUser(user)

                        }
                        userValue.value= user
                    }

                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, "onError: ")
                    loadingData.value = false
                    toastData.value = "Error Updating User"
                }
            })
    }

}
