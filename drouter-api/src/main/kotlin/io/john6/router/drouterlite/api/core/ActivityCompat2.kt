package io.john6.router.drouterlite.api.core

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Pair
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.john6.router.drouterlite.api.utils.Extend
import io.john6.router.drouterlite.api.core.RouterCallback.ActivityCallback
import io.john6.router.drouterlite.api.utils.DRouterLiteLogger
import io.john6.router.drouterlite.api.utils.DRouterLiteLogger.CORE_TAG
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by gaowei on 2018/9/12
 */
class ActivityCompat2 private constructor(private val active: Active) {
    // start index, will not change when rotation or recycle
    private var cur = 0
    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            cur = savedInstanceState.getInt(CbTag)
        }
        active.startActivity()
        //        RouterLogger.coreLogger.d("HoldFragment onCreate cur:" + cur);
    }

    private fun onActivityResult(activity: Activity?, resultCode: Int, data: Intent?) {
        var cb: ActivityCallback? = null
        val pair = sCallbackMap[cur]
        if (pair != null && pair.second.also { cb = it } != null) {
            DRouterLiteLogger.d(CORE_TAG, "HoldFragment ActivityResult callback success")
            cb?.onActivityResult(resultCode, data)
        }
        if (pair?.first == null || pair.first?.get() !== activity) {
            DRouterLiteLogger.e(CORE_TAG,
                "HoldFragment onActivityResult warn, " +
                        "for host activity changed, but still callback last host"
            )
        }
        DRouterLiteLogger.d(CORE_TAG, "HoldFragment remove %s callback and page", cur)
        sCallbackMap.remove(cur)
        active.remove()
    }

    private fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CbTag, cur)
    }

    private fun onDestroy() {
//        RouterLogger.coreLogger.d("HoldFragment onDestroy");
    }

    class HolderFragmentV4 : Fragment(), Active {
        override val compat: ActivityCompat2 = ActivityCompat2(this)
        private var intent: Intent? = null
        private var requestCode = 0

        override fun startActivity() {
            if (intent == null) return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivityForResult(
                    intent,
                    requestCode,
                    intent!!.getBundleExtra(Extend.START_ACTIVITY_OPTIONS)
                )
            } else {
                startActivityForResult(intent, requestCode)
            }
        }

        override fun attach(activity: Activity, intent: Intent?, requestCode: Int) {
            this.intent = intent
            this.requestCode = requestCode
            val fragmentManager = (activity as FragmentActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.add(this, TAG)
            transaction.commit()
        }

        override fun remove() {
            val fragmentManager = fragmentManager
            if (fragmentManager != null) {
                val transaction = fragmentManager.beginTransaction()
                transaction.remove(this)
                transaction.commit()
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            compat.onCreate(savedInstanceState)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            compat.onActivityResult(activity, resultCode, data)
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            compat.onSaveInstanceState(outState)
        }

        override fun onDestroy() {
            super.onDestroy()
            compat.onDestroy()
        }
    }

    @Suppress("deprecation")
    class HolderFragment : android.app.Fragment(), Active {
        override val compat: ActivityCompat2 = ActivityCompat2(this)
        private var intent: Intent? = null
        private var requestCode = 0

        override fun startActivity() {
            if (intent == null) return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivityForResult(
                    intent,
                    requestCode,
                    intent!!.getBundleExtra(Extend.START_ACTIVITY_OPTIONS)
                )
            } else {
                startActivityForResult(intent, requestCode)
            }
        }

        override fun attach(activity: Activity, intent: Intent?, requestCode: Int) {
            this.intent = intent
            this.requestCode = requestCode
            val fragmentManager = activity.fragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.add(this, TAG)
            transaction.commit()
        }

        override fun remove() {
            val fragmentManager = fragmentManager
            if (fragmentManager != null) {
                val transaction = fragmentManager.beginTransaction()
                transaction.remove(this)
                transaction.commit()
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            compat.onCreate(savedInstanceState)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
            super.onActivityResult(requestCode, resultCode, data)
            compat.onActivityResult(activity, resultCode, data)
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            compat.onSaveInstanceState(outState)
        }

        override fun onDestroy() {
            super.onDestroy()
            compat.onDestroy()
        }
    }

    internal interface Active {
        val compat: ActivityCompat2
        fun attach(activity: Activity, intent: Intent?, requestCode: Int)
        fun startActivity()
        fun remove()
    }

    companion object {
        private const val TAG = "DRouterEmptyFragment"
        private const val CbTag = "router_cb_tag"
        private val sCount = AtomicInteger(0)
        private val sCallbackMap = SparseArray<Pair<WeakReference<Activity>?, ActivityCallback>>()
        fun startActivityForResult(
            activity: Activity,
            intent: Intent, requestCode: Int,
            callback: ActivityCallback
        ) {
            val cur = sCount.incrementAndGet()
            sCallbackMap.put(cur, Pair(WeakReference(activity), callback))
            val active: Active = if (activity is FragmentActivity) {
                HolderFragmentV4()
            } else {
                HolderFragment()
            }
            DRouterLiteLogger.d(
                CORE_TAG,
                "HoldFragment start, put %s callback and page | isV4: %s",
                cur, active is HolderFragmentV4
            )
            active.compat.cur = cur
            active.attach(activity, intent, requestCode)
        }
    }
}
