package com.example.cardictionary

import android.app.Application
import cn.leancloud.LeanCloud
import com.example.skin.SkinManager
import com.tencent.tauth.Tencent

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LeanCloud.initialize(
            this,
            "nMBBbObDFZ9gum6tfrTuPI7d-gzGzoHsz",
            "SZ8ojm7bkt8X3TTCkqy5LF9G",
            "https://nmbbbobd.lc-cn-n1-shared.com"
        )
        SkinManager.getInstance().setSkinManager(this)
    }
}