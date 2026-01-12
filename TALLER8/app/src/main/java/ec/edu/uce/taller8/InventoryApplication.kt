package ec.edu.uce.taller8

import android.app.Application
import ec.edu.uce.taller8.di.AppContainer
import ec.edu.uce.taller8.di.AppDataContainer

class InventoryApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}