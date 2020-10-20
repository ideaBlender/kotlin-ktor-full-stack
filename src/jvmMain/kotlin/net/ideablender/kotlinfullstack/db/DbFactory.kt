package net.ideablender.kotlinfullstack.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.ideablender.kotlinfullstack.helpers.PropsHelper
import net.ideablender.kotlinfullstack.models.Players
import net.ideablender.kotlinfullstack.models.Teams
import net.ideablender.kotlinfullstack.pojos.DBInitMode
import net.ideablender.kotlinfullstack.statics.CLASS_NAME_POSTGRES
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DbFactory {
    val logger: Logger = LoggerFactory.getLogger("DbFactory.kt")
    fun init(){
        Database.connect(startHikari())
        if(PropsHelper.getDbInitMode() == DBInitMode.NEW){
            transaction {
                SchemaUtils.drop(Players)
                SchemaUtils.drop(Teams)

                SchemaUtils.create(Teams)
                SchemaUtils.create(Players)
            }
        }
        logger.info("Database is connected...")
    }

    private fun startHikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = CLASS_NAME_POSTGRES
        config.jdbcUrl = PropsHelper.getDbUrl()
        config.username = PropsHelper.getDbUser()
        config.password = PropsHelper.getDbPassword()
        config.maximumPoolSize = PropsHelper.getDbMaxPoolSize()
        config.isAutoCommit = PropsHelper.getDbIsAutoCommit()
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {transaction { block() }   }

}