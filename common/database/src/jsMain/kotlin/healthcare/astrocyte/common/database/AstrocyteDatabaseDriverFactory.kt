package healthcare.astrocyte.common.database

import com.badoo.reaktive.promise.asSingle
import com.badoo.reaktive.single.Single
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import healthcare.astrocyte.database.AstrocyteDatabase

fun astrocyteDatabaseDriver(): Single<SqlDriver> =
    initSqlDriver(AstrocyteDatabase.Schema).asSingle()
