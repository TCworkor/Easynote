package com.example.myapplication.part

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.launch

// ViewModel工厂类，用于创建ViewModel实例
// ViewModel factory class, used for creating ViewModel instances
class ViewModelFactory(private val userRepository: UserRepository, private val textWithDateRepository: TextWithDateRepository) : ViewModelProvider.Factory {
    // 创建ViewModel的方法，根据传入的ViewModel类的类型来实例化对应的ViewModel
    // Method to create ViewModel, instantiates a corresponding ViewModel based on the passed ViewModel class type
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInputViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserInputViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(TextWithDateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TextWithDateViewModel(textWithDateRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// 用于处理带日期文本数据的ViewModel
// ViewModel for handling text data with dates
class TextWithDateViewModel(private val repository: TextWithDateRepository) : ViewModel() {
    // 获取所有带日期的文本数据
    // Retrieve all text data with dates
    val allTextWithDates: LiveData<List<TextWithDate>> = repository.allTextWithDates

    // 根据ID获取UserInput对象的方法
    // Method to get UserInput object by ID
    fun getUserInputById(id: Int): LiveData<TextWithDate?> {
        return repository.getUserInputById(id)
    }

    // 插入文本数据的方法
    // Method to insert text data
    fun insert(text: String, year: Int, month: Int, day: Int) = viewModelScope.launch {
        repository.insert(TextWithDate(text = text, year = year, month = month, day = day))
    }

    // 根据ID删除文本数据的方法
    // Method to delete text data by ID
    fun deleteById(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }
}

// 用于处理用户输入数据的ViewModel
// ViewModel for handling user input data
class UserInputViewModel(private val repository: UserRepository) : ViewModel() {
    // 获取所有用户输入数据
    // Retrieve all user input data
    val allInputs: LiveData<List<UserInput>> = repository.allInputs

    // 根据ID获取UserInput对象的方法
    // Method to get UserInput object by ID
    fun getUserInputById(id: Int): LiveData<UserInput?> {
        return repository.getUserInputById(id)
    }

    // 插入UserInput对象的方法
    // Method to insert a UserInput object
    fun insert(userInput: UserInput) = viewModelScope.launch {
        repository.insert(userInput)
    }

    // 根据ID删除用户输入的方法
    // Method to delete user input by ID
    fun deleteById(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }
}

// 用于处理带日期文本数据的Repository
// Repository for handling text data with dates
class TextWithDateRepository(private val textWithDateDao: TextWithDateDao) {
    // 获取所有带日期的文本数据
    // Retrieve all text data with dates
    val allTextWithDates: LiveData<List<TextWithDate>> = textWithDateDao.getAllTextWithDates()

    // 插入带日期的文本数据的方法
    // Method to insert text data with a date
    suspend fun insert(textWithDate: TextWithDate) {
        textWithDateDao.insert(textWithDate)
    }

    // 根据ID获取文本数据的方法
    // Method to get text data by ID
    fun getUserInputById(id: Int): LiveData<TextWithDate?> {
        return textWithDateDao.getUserInputById(id)
    }

    // 根据ID删除文本数据的方法
    // Method to delete text data by ID
    suspend fun deleteById(id: Int) {
        textWithDateDao.deleteById(id)
    }
}


// 用户输入数据的Repository，处理用户输入数据的业务逻辑
// Repository for user input data, handling business logic for user input
class UserRepository(private val userInputDao: UserInputDao) {
    // 获取所有用户输入
    // Retrieve all user inputs
    val allInputs: LiveData<List<UserInput>> = userInputDao.getAllInputs()

    // 插入用户输入数据
    // Insert user input data
    suspend fun insert(userInput: UserInput) {
        userInputDao.insert(userInput)
    }

    // 根据ID获取用户输入
    // Get user input by ID
    fun getUserInputById(id: Int): LiveData<UserInput?> {
        return userInputDao.getUserInputById(id)
    }

    // 根据ID删除用户输入
    // Delete user input by ID
    suspend fun deleteById(id: Int) {
        userInputDao.deleteById(id)
    }
}

// 数据库类，定义数据库配置和DAOs
// Database class, defining the database configuration and DAOs
@Database(entities = [UserInput::class, TextWithDate::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // 获取UserInputDao的实例
    // Get an instance of UserInputDao
    abstract fun userInputDao(): UserInputDao

    // 获取TextWithDateDao的实例
    // Get an instance of TextWithDateDao
    abstract fun textWithDateDao(): TextWithDateDao

    companion object {
        // 单例模式，保证全局只有一个数据库实例
        // Singleton pattern to ensure only one instance of the database globally
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 获取数据库的实例，如果不存在则创建
        // Get the database instance, create it if it doesn't exist
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_input_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// DAO接口，定义操作UserInput表的方法
// DAO interface, defining methods for operating on the UserInput table
@Dao
interface UserInputDao {
    @Insert
    suspend fun insert(userInput: UserInput)

    @Query("SELECT * FROM user_input ORDER BY id DESC")
    fun getAllInputs(): LiveData<List<UserInput>>

    // 删除指定ID的用户输入
    // Delete user input with the specified ID
    @Query("DELETE FROM user_input WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM user_input WHERE id = :id")
    fun getUserInputById(id: Int): LiveData<UserInput?>
}

// DAO接口，定义操作TextWithDate表的方法
// DAO interface, defining methods for operating on the TextWithDate table
@Dao
interface TextWithDateDao {
    @Insert
    suspend fun insert(textWithDate: TextWithDate)

    @Query("SELECT * FROM text_with_date ORDER BY id DESC")
    fun getAllTextWithDates(): LiveData<List<TextWithDate>>

    @Query("DELETE FROM text_with_date WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM text_with_date WHERE id = :id")
    fun getUserInputById(id: Int): LiveData<TextWithDate?>
}

// UserInput实体，定义用户输入的数据结构
// UserInput entity, defining the data structure for user input
@Entity(tableName = "user_input")
data class UserInput(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var text: String = "空",
    var hours: Int = 0,
    var minutes: Int = 0,
    var seconds: Int = 0,
    var alarmTriggerType: String = "每天",
    var weekDaysState: String = "false,false,false,false,false,false,false", // 使用字符串存储周天状态
    var requestcode: Int = 0
)

// 带日期的文本数据实体
// Entity for text data with a date
@Entity(tableName = "text_with_date")
data class TextWithDate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 主键，自动生成
    var text: String = "空", // 文本内容，默认值为"空"
    var year: Int = 2100, // 年份，默认值为2100
    var month: Int = 1, // 月份，默认值为1
    var day: Int = 30 // 日，默认值为30
)
