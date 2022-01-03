package uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper

import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.ScriptUtils
import uk.gov.justice.digital.hmpps.nomisprisonerdeletionservice.helper.SqlScriptHelper.Constants.SQL_SCRIPTS_PATH
import javax.sql.DataSource

class SqlScriptHelper {

  object Constants {
    const val SQL_SCRIPTS_PATH = "/uk/gov/justice/digital/hmpps/nomisprisonerdeletionservice/repository/jpa/"
  }
}

fun executeScripts(dataSource: DataSource, vararg scripts: String) {
  dataSource.connection.use { conn ->
    scripts.forEach {
      ScriptUtils.executeSqlScript(conn, ClassPathResource(SQL_SCRIPTS_PATH.plus(it)))
    }
  }
}
