package yardspoon.greengeofence

import android.app.Application
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

val appEventBus: Subject<String> = PublishSubject.create()

class TheApp : Application() {
    override fun onCreate() {
        super.onCreate()

        appEventBus.onNext("App Created")
    }
}