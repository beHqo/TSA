package com.example.android.strikingarts.hilt.module//package com.example.android.strikingarts.hilt.di
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.core.DataStoreFactory
//import androidx.datastore.dataStoreFile
//import com.example.android.strikingarts.UserPreferences
//import com.example.android.strikingarts.database.userprefs.UserPreferencesSerializer
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
////private const val TAG = "DataStoreModule"
//private const val DATA_STORE_FILE_NAME = "user_prefs.pb"
////private const val USER_PREFERENCES_NAME = "user_preferences"
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DataStoreModule {
//
//    @Singleton
//    @Provides
//    fun provideUserPreferencesStore(
//        @ApplicationContext appContext: Context
//    ): DataStore<UserPreferences> {
//        return DataStoreFactory.create(
//            serializer = UserPreferencesSerializer,
//            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_NAME) },
////            corruptionHandler = ReplaceFileCorruptionHandler { corruptionException ->
////                Log.e(TAG, "user_prefs.proto is corrupted", corruptionException)
////                UserPreferences.getDefaultInstance()
////            }
//        )
//    }
//}