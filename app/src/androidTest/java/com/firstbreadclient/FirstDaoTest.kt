package com.firstbreadclient

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.room.FirstDao
import com.firstbreadclient.room.FirstRoomDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FirstDaoTest {
    private lateinit var firstDao: FirstDao
    private lateinit var db: FirstRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, FirstRoomDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        firstDao = db.firstDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetFirst() = runBlocking {
        val auth = Auth("1", "1234", "ИП Иванов Александр Александрович",
                "ул. Сталеваров,  д.68","25d55ad283aa400af464c76d713c07ad")
        firstDao.insertAuth(auth)
        val allAuths = firstDao.getAlphabetizedAuths().first()
        assertEquals(allAuths[0], auth)
    }

    @Test
    @Throws(Exception::class)
    fun getAllFirsts() = runBlocking {
        val auth = Auth("1", "1234", "ИП Иванов Александр Александрович",
                "ул. Сталеваров,  д.68","25d55ad283aa400af464c76d713c07ad")
        firstDao.insertAuth(auth)
        val auth2 = Auth("2", "4321", "ИП Петров Эдуард Викторович",
                "ул.Энтузиастов, д.12, м-н \"Золотая подкова\"","25d55ad283aa400af464c76d713c07ad")
        firstDao.insertAuth(auth2)
        val allAuths = firstDao.getAlphabetizedAuths().first()
        assertEquals(allAuths[0], auth)
        assertEquals(allAuths[1], auth2)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllFirsts() = runBlocking {
        val auth = Auth("1", "1234", "ИП Иванов Александр Александрович",
                "ул. Сталеваров,  д.68","25d55ad283aa400af464c76d713c07ad")
        firstDao.insertAuth(auth)
        val auth2 = Auth("2", "4321", "ИП Петров Эдуард Викторович",
                "ул.Энтузиастов, д.12, м-н \"Золотая подкова\"","25d55ad283aa400af464c76d713c07ad")
        firstDao.insertAuth(auth2)
        firstDao.deleteAllAuths()
        val allAuths = firstDao.getAlphabetizedAuths().first()
        assertTrue(allAuths.isEmpty())
    }
}