package org.github.kotlinizer.kospa.app

import kotlinx.browser.document
import kotlinx.html.body
import kotlinx.html.dom.create
import org.github.kotlinizer.kospa.content.Intent
import org.github.kotlinizer.kospa.context.ContextWrapper
import org.github.kotlinizer.kospa.os.Bundle
import org.github.kotlinizer.kospa.service.ActivityManager
import org.github.kotlinizer.kospa.view.View

abstract class Activity : ContextWrapper() {

    companion object {
        const val RESULT_CANCELED = -1
        const val RESULT_OK = 0

        private val emptyBodyElement by lazy {
            document.create.body()
        }
    }

    lateinit var intent: Intent

    internal var id: String = ""

    internal var resultForActivity: ResultForActivity? = null

    private var contentView: View? = null

    private val activityManager by lazy {
        getSystemService(ActivityManager::class)
    }

    open fun onCreate(savedInstanceState: Bundle?) {}

    open fun onStart() {
        document.body = contentView?.bodyHtmlElement ?: emptyBodyElement
    }

    open fun onResume() {}

    open fun onPause() {}

    open fun onStop() {
        document.body = emptyBodyElement
    }

    open fun onDestroy() {}

    fun startActivityForResult(intent: Intent, requestCode: Int) {
        activityManager.startActivityForResult(
            intent, ResultForActivity(activityId = id, requestCode = requestCode)
        )
    }

    fun setResult(resultCode: Int, data: Intent? = null) {
        resultForActivity = resultForActivity?.copy(
            resultCode = resultCode, data = data
        )
    }

    fun finish() {
        activityManager.finishActivity(this)
    }

    internal fun dispatchResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResult(requestCode, resultCode, data)
    }

    protected open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    protected fun setContentView(view: View) {
        contentView = view
    }

    internal data class ResultForActivity(
        val activityId: String,
        val requestCode: Int,
        val resultCode: Int? = null,
        val data: Intent? = null
    )
}
