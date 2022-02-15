package com.imtmobileapps.cryptocompose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.Person
import com.imtmobileapps.cryptocompose.model.TotalValues
import com.imtmobileapps.cryptocompose.util.BigDecimalDoubleTypeConverter


@Database(entities = [CryptoValue::class, Person::class, TotalValues::class], version = 2, exportSchema = false)
@TypeConverters(BigDecimalDoubleTypeConverter::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun cryptoValueDao() : CryptoValuesDao
    abstract fun totalValuesDao() : TotalValuesDao
    abstract fun personDao() : PersonDao

    //SINGLETON
    companion object {
        @Volatile private var instance : AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }

        }

        // We may change this if we ever go to prod with this app
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "cryptodatabase"
        ).fallbackToDestructiveMigration().build()
    }

}