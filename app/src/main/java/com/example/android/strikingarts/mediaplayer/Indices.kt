package com.example.android.strikingarts.mediaplayer

internal class Indices : Subject {
    override var observer: Observer? = null
    var index1 = 0
        set(value) {
            field = value
            onComponent1Change()
        }

    var index2 = 1
        set(value) {
            field = value
            onComponent2Change()
        }

    override fun onComponent1Change() {
        observer?.onUpdateComponent1()
    }

    override fun onComponent2Change() {
        observer?.onUpdateComponent2()
    }
}