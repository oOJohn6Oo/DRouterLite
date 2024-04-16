package com.example.mylibrary

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.button.MaterialButton
import io.john6.router.drouterlite.annotation.Router
import io.john6.router.drouterlite.annotation.Service
import io.john6.router.drouterlite.api.DRouterLite


@Router(path = "/library")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            addView(MaterialButton(this@MainActivity).apply {
                text = "Hello Library"
                setOnClickListener {
                    DRouterLite.build(ILibraryService::class.java)
                        ?.test(this@MainActivity, "Hello Library")
                }
            })
        })
    }
}

@Service(clazz = ILibraryService::class)
class LibraryServiceImpl : ILibraryService {
    override fun test(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

interface ILibraryService {
    fun test(context: Context, msg: String)
}