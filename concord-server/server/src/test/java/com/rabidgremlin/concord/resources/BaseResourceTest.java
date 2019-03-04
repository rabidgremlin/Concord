package com.rabidgremlin.concord.resources;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static org.mockito.Mockito.mock;

import javax.ws.rs.core.UriInfo;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import com.rabidgremlin.concord.ConcordServerApplication;
import com.rabidgremlin.concord.dao.LabelsDao;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;

public class BaseResourceTest
{
  // TODO maybe find a better way then using a static here ?
  private static EmbeddedMysql mysqld;

  protected Jdbi jdbi;

  private void startDatabase()
  {
    // TODO set proxy for download...

    MysqldConfig config = aMysqldConfig(v5_7_latest)
        .withCharset(UTF8)
        .withPort(2215)
        // .withUser("concorduser", "concordpwd")
        .build();

    mysqld = anEmbeddedMysql(config)
        .addSchema("concorddb")// , classPathScript("db/001_init.sql"))
        .start();
  }

  private void resetDatabase()
    throws Exception
  {
    // if database hasn't start been started then start it
    if (mysqld == null)
    {
      startDatabase();
    }

    // override properties from config file
    System.setProperty("dw.database.url", "jdbc:mysql://localhost:2215/concorddb?nullNamePatternMatchesAll=true");
    System.setProperty("dw.database.user", "root");
    System.setProperty("dw.database.password", "");

    // use build in dropwizard logic/liquibase to drop the database
    String[] args = { "db", "drop-all", "--confirm-delete-everything", "src/main/yml/server.yml" };
    ConcordServerApplication.main(args);

    // use build in dropwizard logic/liquibase to create a clean empty database
    args = new String[]{ "db", "migrate", "src/main/yml/server.yml" };
    ConcordServerApplication.main(args);
  }

  public void setUpDatabase()
    throws Exception
  {
    resetDatabase(); // create a fresh empty database

    // create a jdbi object to access the database
    jdbi = Jdbi.create(System.getProperty("dw.database.url"), System.getProperty("dw.database.user"), System.getProperty("dw.database.password"));
    jdbi.installPlugin(new SqlObjectPlugin());
  }

  public LabelsResource createLabelsResource()
  {
    LabelsResource labelsResource = new LabelsResource(jdbi.onDemand(LabelsDao.class));
    labelsResource.uriInfo = mock(UriInfo.class);

    return labelsResource;
  }

}
