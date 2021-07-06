package healthcare.astrocyte.common.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import healthcare.astrocyte.database.AstrocyteDatabase

@Suppress("FunctionName") // FactoryFunction
fun AstrocyteDatabaseDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = AstrocyteDatabase.Schema,
        context = context,
        name = "AstrocyteDatabase.db"
    )
