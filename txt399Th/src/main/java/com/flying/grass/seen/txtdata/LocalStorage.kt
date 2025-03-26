package com.flying.grass.seen.txtdata

import android.content.Context
import androidx.annotation.Keep
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Keep
@Entity
data class AppData(
    @PrimaryKey val id: Int = 0,

    var firstPoint: Boolean = false,
    var adOrgPoint: Boolean = false,
    var getlimit: Boolean = false,
    var fcmState: Boolean = false,
    var admindata: String = "",
    var refdata: String = "",
    var appiddata: String = "",
    var IS_INT_JSON: String = "",

    var h5HourShowNum: Int = 0,
    var h5HourShowDate: String = "",
    var h5DayShowNum: Int = 0,
    var h5DayShowDate: String = "",

    var adClickNum: Int = 0,

    var adDayShowNum: Int = 0,
    var adDayData: String = "",

    var adHShowNum: Int = 0,
    var adHData: String = "",

    var isAdFailCount: Int = 0,
    var lastReportTime: Long = 0L,

    var hourlyShowCount: Int? = null,    // 当前小时展示次数
    var currentHour: Int? = null,     // 当前记录的小时
    var dailyShowCount: Int? = null,     // 当日总展示次数
    var clickCount: Int? = null,        // 当日点击次数
    var lastDate: String? = null,        // 最后记录日期（yyyyMMdd）

    var hourlyShowCountH5: Int? = null,    // 当前小时展示次数
    var currentHourH5: Int? = null,     // 当前记录的小时
    var dailyShowCountH5: Int? = null,     // 当日总展示次数
    var lastDateH5: String? = null,        // 最后记录日期（yyyyMMdd）
    var isFirstInApp: Boolean = false
)

@Keep
@Dao
interface AppDataDao {
    @Query("SELECT * FROM AppData WHERE id = 0")
    fun getData(): AppData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveData(data: AppData)
}

@Keep
@Database(entities = [AppData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDataDao(): AppDataDao
}

@Keep
class LocalStorage(context: Context) {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "app-database"
    ).allowMainThreadQueries().build()

    var firstPoint: Boolean
        get() = db.appDataDao().getData()?.firstPoint ?: false
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(firstPoint = value))
        }

    var adOrgPoint: Boolean
        get() = db.appDataDao().getData()?.adOrgPoint ?: false
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(adOrgPoint = value))
        }

    var getlimit: Boolean
        get() = db.appDataDao().getData()?.getlimit ?: false
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(getlimit = value))
        }

    var fcmState: Boolean
        get() = db.appDataDao().getData()?.fcmState ?: false
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(fcmState = value))
        }

    var admindata: String
        get() = db.appDataDao().getData()?.admindata ?: ""
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(admindata = value))
        }

    var refdata: String
        get() = db.appDataDao().getData()?.refdata ?: ""
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(refdata = value))
        }

    var appiddata: String
        get() = db.appDataDao().getData()?.appiddata ?: ""
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(appiddata = value))
        }

    var IS_INT_JSON: String
        get() = db.appDataDao().getData()?.IS_INT_JSON ?: ""
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(IS_INT_JSON = value))
        }

    var isAdFailCount: Int
        get() = db.appDataDao().getData()?.isAdFailCount ?: 0
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(isAdFailCount = value))
        }

    var lastReportTime: Long
        get() = db.appDataDao().getData()?.lastReportTime ?: 0L
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(lastReportTime = value))
        }
    var hourlyShowCount: Int
        get() = db.appDataDao().getData()?.hourlyShowCount ?: 0
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(hourlyShowCount = value))
        }

    var currentHour: Int
        get() = db.appDataDao().getData()?.currentHour ?: 0
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(currentHour = value))
        }

    var dailyShowCount: Int
        get() = db.appDataDao().getData()?.dailyShowCount ?: 0
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(dailyShowCount = value))
        }

    var clickCount: Int
        get() = db.appDataDao().getData()?.clickCount ?: 0
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(clickCount = value))
        }

    var lastDate: String
        get() = db.appDataDao().getData()?.lastDate ?: ""
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(lastDate = value))
        }




    var hourlyShowCountH5: Int
        get() = db.appDataDao().getData()?.hourlyShowCountH5 ?: 0
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(hourlyShowCountH5 = value))
        }
    var currentHourH5: Int
        get() = db.appDataDao().getData()?.currentHourH5 ?: 0
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(currentHourH5 = value))
        }
    var dailyShowCountH5: Int
        get() = db.appDataDao().getData()?.dailyShowCountH5 ?: 0
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(dailyShowCountH5 = value))
        }
    var lastDateH5: String
        get() = db.appDataDao().getData()?.lastDateH5 ?: ""
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(lastDateH5 = value))
        }

    var isFirstInApp: Boolean
        get() = db.appDataDao().getData()?.isFirstInApp ?: false
        set(value) {
            val currentData = db.appDataDao().getData() ?: AppData()
            db.appDataDao().saveData(currentData.copy(isFirstInApp = value))
        }
}

