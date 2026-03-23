package com.example.valentinesgarage.data.repository

import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import androidx.room.Room
import com.example.valentinesgarage.data.local.dao.EmployeeDao
import com.example.valentinesgarage.data.local.dao.RepairTaskDao
import com.example.valentinesgarage.data.local.dao.TruckDao
import com.example.valentinesgarage.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "garage_db"
        ).build()
    }

    @Provides
    fun provideTruckDao(db: AppDatabase): TruckDao = db.truckDao()

    @Provides
    fun provideRepairTaskDao(db: AppDatabase): RepairTaskDao = db.repairTaskDao()

    @Provides
    fun provideEmployeeDao(db: AppDatabase): EmployeeDao = db.employeeDao()

    @Provides
    @Singleton
    fun provideRepository(
        truckDao: TruckDao,
        repairTaskDao: RepairTaskDao,
        employeeDao: EmployeeDao
    ): GarageRepository {
        return GarageRepository(truckDao, repairTaskDao, employeeDao)
    }
}