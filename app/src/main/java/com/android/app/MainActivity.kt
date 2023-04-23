package com.android.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.android.app.databinding.ActivityMainBinding
import com.android.photoselector.interfaces.PhotoCallBackListener
import com.android.photoselector.photo.PhotoUtil

class MainActivity : AppCompatActivity() {

    private val context: FragmentActivity by lazy {
        return@lazy this@MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflate = LayoutInflater.from(this)
            .inflate(R.layout.activity_main, null)
        val mBinding = ActivityMainBinding.bind(inflate)

        setContentView(mBinding.root)

        val photoUtil = PhotoUtil()
        mBinding.btnOpen.setOnClickListener {
            photoUtil.openPhotoCamera(context, object : PhotoCallBackListener {
                override fun onCallBack(result: List<String>) {
                    Log.e("Photo", "result: $result")
                }
            })
        }
    }
}