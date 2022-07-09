package com.example.android.strikingarts.mediaplayer

internal interface Observer {
    fun onUpdateComponent1()
    fun onUpdateComponent2()
}

internal interface Subject {
    var observer: Observer?

    fun onComponent1Change()
    fun onComponent2Change()
}