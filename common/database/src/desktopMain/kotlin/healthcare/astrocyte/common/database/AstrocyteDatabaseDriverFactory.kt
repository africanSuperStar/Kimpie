package healthcare.astrocyte.common.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import healthcare.astrocyte.database.AstrocyteDatabase
import java.io.File

@Suppress("FunctionName") // FactoryFunction
fun AstrocyteDatabaseDriver(): SqlDriver {
    val databasePath = File(System.getProperty("java.io.tmpdir"), "ComposeAstrocyteDatabase.db")
    val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePath}")
    AstrocyteDatabase.Schema.create(driver)

    return driver
}
