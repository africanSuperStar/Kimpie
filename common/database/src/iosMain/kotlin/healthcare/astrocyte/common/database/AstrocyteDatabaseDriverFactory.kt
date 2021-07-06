package healthcare.astrocyte.common.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import healthcare.astrocyte.database.AstrocyteDatabase

@Suppress("FunctionName") // Factory function
fun AstrocyteDatabaseDriver(): SqlDriver =
    NativeSqliteDriver(AstrocyteDatabase.Schema, "AstrocyteDatabase.db")
