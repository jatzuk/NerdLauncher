package com.example.jatzuk.nerdlauncher

class NerdLauncherActivity : SingleFragmentActivity() {
    override fun createFragment() = NerdLauncherFragment.newInstance()
}