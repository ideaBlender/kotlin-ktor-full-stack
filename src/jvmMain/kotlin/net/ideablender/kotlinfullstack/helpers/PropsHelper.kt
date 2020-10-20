package net.ideablender.kotlinfullstack.helpers

import net.ideablender.kotlinfullstack.pojos.DBInitMode
import net.ideablender.kotlinfullstack.statics.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.*

object PropsHelper {
    val logger: Logger = LoggerFactory.getLogger("PropsHelper.kt")
    private var dbName:String? = null
    private var dbUser:String? = null
    private var dbPassword:String? = null
    private var dbUrlTemplate:String? = null
    private var dbPort:Int? = null
    private var dbMaxPoolSize:Int? = null
    private var dbIsAutoCommit:Boolean? = null
    private var dbInitMode: DBInitMode? = null
    private var dbTransactionIsolation:String? = null //transactionIsolation

    fun getDbName():String = dbName ?: throw Exception(propsNotLoadedFor("dbName"))
    fun getDbUser():String = dbUser ?: throw Exception(propsNotLoadedFor("dbUser"))
    fun getDbPassword():String = dbPassword ?: throw Exception(propsNotLoadedFor("dbPassword"))
    fun getDbTransactionIsolation():String = dbTransactionIsolation ?: throw Exception(propsNotLoadedFor("dbTransactionIsolation"))
    fun getDbInitMode(): DBInitMode = dbInitMode ?: throw Exception(propsNotLoadedFor("dbInitMode"))
    fun getDbPort():Int = dbPort ?: throw Exception(propsNotLoadedFor("dbPort"))
    fun getDbMaxPoolSize():Int = dbMaxPoolSize ?: throw Exception(propsNotLoadedFor("dbMaxPoolSize"))
    fun getDbUrl():String = dbUrlTemplate?.replace("PORT", getDbPort().toString())?.replace("NAME", getDbName()) ?: throw Exception(propsNotLoadedFor("getDbUrl"))
    fun getDbIsAutoCommit():Boolean = dbIsAutoCommit ?: throw Exception(propsNotLoadedFor("dbIsAutoCommit"))

    private fun propsNotLoadedFor(ent:String) : String = "Could not find $ent, properties did not load correctly."

    fun loadProps(){
        val p = Properties()
        val propsFile = File("${Paths.get("").toAbsolutePath()}/$FILE_NAME_PROPS")
        FileInputStream(propsFile).use{
            p.load(it)
        }
        dbName = p.getProperty(KEY_PROPS_DB_NAME).trim()
        dbUser = p.getProperty(KEY_PROPS_DB_USER).trim()
        dbPassword = p.getProperty(KEY_PROPS_DB_PASSWORD).trim()
        dbUrlTemplate = p.getProperty(KEY_PROPS_DB_URL).trim()
        dbTransactionIsolation = p.getProperty(KEY_PROPS_DB_TRANSACTION_ISOLATION).trim()
        dbPort = p.getProperty(KEY_PROPS_DB_PORT).trim().toInt()
        dbInitMode = DBInitMode.valueOf(p.getProperty(KEY_PROPS_DB_INIT_MODE).trim())
        dbMaxPoolSize = p.getProperty(KEY_PROPS_DB_MAX_POOL_SIZE).trim().toInt()
        dbIsAutoCommit = p.getProperty(KEY_PROPS_DB_IS_AUTO_COMMIT).trim().toBoolean()
    }

    fun logProps(){
        logger.info(
            """
                ${System.lineSeparator()}
                =================================================================================
                =========================== Kotlin Full Stack Started ===========================
                =================================================================================
                == Db Name: ${getDbName()}
                == Db User: ${getDbUser()}
                == Db Password: ${getDbPassword()}
                == Db Port: ${getDbPort()}
                == Db Url: ${getDbUser()}
                == Db Init Mode: ${getDbInitMode()}
                == Db Max Pool Size: ${getDbMaxPoolSize()}
                == Db Auto Commit: ${getDbIsAutoCommit()}
                == Db Transaction Isolation: ${getDbTransactionIsolation()}
                =================================================================================
                ${System.lineSeparator()}
            """.trimIndent()
        )
    }
}