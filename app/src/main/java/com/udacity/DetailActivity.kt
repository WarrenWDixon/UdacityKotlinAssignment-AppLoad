package com.udacity

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        tv_repo_name.text = repoName
        tv_download_status.text = downloadStatus
        btn_return.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        animateButton()
    }

        fun animateButton() {
            tv_download_status.alpha = 0f
            tv_download_status.visibility = View.VISIBLE
            val alphaAnimator = ValueAnimator.ofFloat(0f, 1f)
            alphaAnimator.duration = 2000

            alphaAnimator.addUpdateListener {
                val animationAlpha = it.animatedValue as Float
                tv_download_status.alpha = animationAlpha
            }
            alphaAnimator.start()
        }
}
