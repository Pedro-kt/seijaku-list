package com.example.seijakulist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Interruptor principal de Hilt, le dice a Hilt donde empezar a inyectar las cosas que fabrico AppModule

@HiltAndroidApp
class SeijakuListApp() : Application() {

}