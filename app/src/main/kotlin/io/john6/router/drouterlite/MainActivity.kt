package io.john6.router.drouterlite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.SparseArray
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import io.john6.router.drouterlite.api.DRouterLite
import io.john6.router.drouterlite.annotation.Router
import io.john6.router.drouterlite.annotation.Service
import io.john6.router.drouterlite.api.core.RouterCallback
import io.john6.router.drouterlite.api.core.RouterResult
import io.john6.router.drouterlite.databinding.CommonLayoutBinding
import kotlin.random.Random


@Router(path = "/main")
class MainActivity : AppCompatActivity() {

    private lateinit var mLauncher: ActivityResultLauncher<Intent>
    private lateinit var mBinding: CommonLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Toast.makeText(this, it.resultCode.toString(), Toast.LENGTH_SHORT).show()
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        mBinding = CommonLayoutBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()
    }

    private fun initView() {
        mBinding.content.text = intent.getStringExtra("content")
        mBinding.btnNext.setOnClickListener {
            val withCallback = mBinding.checkBoxNavWithCallback.isChecked
            val usingLauncher = mBinding.checkBoxNavUsingLauncher.isChecked
            if(withCallback){
                navigateWithCallback(usingLauncher)
            }else{
                justNavigate(usingLauncher)
            }
        }
        mBinding.btnService.setOnClickListener {
            DRouterLite.build(IMainService::class.java)?.test(this, CONTENT_SERVICE_MESSAGE)
        }
        mBinding.btnNext.setOnLongClickListener {
            DRouterLite.build("/library").start()
            true
        }
    }

    private fun justNavigate(usingLauncher:Boolean){
        val randomInt = Random.nextInt()
        val routerString = "Router-$randomInt"
        DRouterLite.build("/main")
            .withExtras {
                putAll(test_bundle)
                putString("content", routerString)
            }
            .start(this, if(usingLauncher) mLauncher else null)
    }

    private fun navigateWithCallback(usingLauncher:Boolean){
        DRouterLite.build("/main")
            .withExtras {
                putString("content", CONTENT_CALLBACK_TEST)
            }
            .start(this,
                if(usingLauncher) mLauncher else null,
                object : RouterCallback {
                override fun onResult(result: RouterResult) {
                    Toast.makeText(this@MainActivity, result.statusCode.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }

    companion object {

        const val CONTENT_CALLBACK_TEST = "callback test"
        const val CONTENT_SERVICE_MESSAGE = "Service Test"

        @VisibleForTesting
        val test_bundle = Bundle().apply {
            putByte("byte", 1)
            putByteArray("byteArray", byteArrayOf(1, 2, 3))
            putShort("short", 2)
            putShortArray("shortArray", shortArrayOf(1, 2, 3))
            putInt("int", 3)
            putIntArray("intArray", intArrayOf(1, 2, 3))
            putLong("long", 4)
            putLongArray("longArray", longArrayOf(1, 2, 3))
            putFloat("float", 5.0f)
            putFloatArray("floatArray", floatArrayOf(1.0f, 2.0f, 3.0f))
            putDouble("double", 6.0)
            putDoubleArray("doubleArray", doubleArrayOf(1.0, 2.0, 3.0))
            putBoolean("boolean", true)
            putBooleanArray("booleanArray", booleanArrayOf(true, false))
            putChar("char", 'a')
            putCharArray("charArray", charArrayOf('a', 'b'))
            putString("content", "test")
            putStringArray("stringArray", arrayOf("test1", "test2"))
            putStringArrayList("stringArrayList", arrayListOf("test1", "test2"))
            putParcelable("parcelable", Intent(Intent.ACTION_CALL_BUTTON))
            putParcelableArray("parcelableArray", arrayOf(Intent(Intent.ACTION_CALL_BUTTON)))
            putParcelableArrayList(
                "parcelableArrayList",
                arrayListOf(Intent(Intent.ACTION_CALL_BUTTON))
            )
            putSparseParcelableArray(
                "sparseParcelableArray",
                SparseArray<Parcelable>().apply { put(1, Intent(Intent.ACTION_CALL_BUTTON)) })
            putSerializable("serializable", "test" to "test")
        }
    }
}

@Service(clazz = IMainService::class)
class MainServiceImpl : IMainService {
    override fun test(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

interface IMainService {
    fun test(context: Context, msg: String)
}