package com.rabidgremlin.concord;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import com.rabidgremlin.concord.dao.UploadDao;
import com.rabidgremlin.concord.plugin.InvalidConfigPropertiesException;
import nz.co.airnz.convlabel.ConvLabelSuggester;
import org.apache.http.auth.Credentials;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

import com.amazonaws.services.logs.AWSLogs;
import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.binout.jaxrs.csv.CsvMessageBodyProvider;

import com.rabidgremlin.concord.api.Label;
import com.rabidgremlin.concord.auth.ConcordServerAuthenticator;
import com.rabidgremlin.concord.auth.AuthorizeAllAuthorizer;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.config.ConcordServerConfiguration;
import com.rabidgremlin.concord.dao.LabelsDao;
import com.rabidgremlin.concord.dao.PhrasesDao;
import com.rabidgremlin.concord.dao.VotesDao;
import com.rabidgremlin.concord.plugin.CredentialsValidator;
import com.rabidgremlin.concord.plugin.LabelSuggester;
import com.rabidgremlin.concord.plugin.SystemLabel;
import com.rabidgremlin.concord.plugin.SystemLabelStore;
import com.rabidgremlin.concord.plugin.labelsuggesters.AllLabelsSuggester;
import com.rabidgremlin.concord.resources.LabelsResource;
import com.rabidgremlin.concord.resources.PhrasesResource;
import com.rabidgremlin.concord.resources.RedirectResource;
import com.rabidgremlin.concord.resources.SessionsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcordServerApplication
    extends Application<ConcordServerConfiguration>
{

  private Logger log = LoggerFactory.getLogger(ConcordServerApplication.class);

  public static void main(String[] args)
    throws Exception
  {
    new ConcordServerApplication().run(args);
  }

  @Override
  public String getName()
  {
    return "concord-server";
  }

  @Override
  public void initialize(Bootstrap<ConcordServerConfiguration> bootstrap)
  {
    // serve up swagger stuff as assets
    bootstrap.addBundle(new AssetsBundle("/ui", "/", "index.html"));
    
    bootstrap.addBundle(new MigrationsBundle<ConcordServerConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(ConcordServerConfiguration configuration) {
            return configuration.getDatabase();
        }
    });
  }

  private void configureCors(Environment environment)
  {
    Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
    filter.setInitParameter("allowCredentials", "true");
  }

  /**
   * Add recommended security headers to content.
   * 
   * @param environment Environment to configure.
   */
  private void configureAssetsSecurityHeaders(Environment environment)
  {
    Dynamic filter = environment.servlets().addFilter("AssetsSecurityHeaders", AssetsSecurityHeadersFilter.class);
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/");
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "*.html");
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "*.css");
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "*.js");
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "*.png");
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "*.ttf");
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "*.ico");
  }

  private void setupJwtAuth(ConcordServerConfiguration configuration,
    Environment environment)
    throws UnsupportedEncodingException
  {
    final JwtConsumer consumer = new JwtConsumerBuilder()
        .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
        .setRequireExpirationTime() // the JWT must have an expiration time
        .setRequireSubject() // the JWT must have a subject claim
        .setVerificationKey(new HmacKey(configuration.getJwtTokenSecret())) // verify the signature with the public key
        .setJwsAlgorithmConstraints(new AlgorithmConstraints(ConstraintType.WHITELIST, org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256)) // we only expect HMAC_SHA256 alg 
        .setRelaxVerificationKeyValidation() // relaxes key length requirement
        .build(); // create the JwtConsumer instance

    environment.jersey().register(new AuthDynamicFeature(
        new JwtAuthFilter.Builder<Caller>()
            .setJwtConsumer(consumer)
            .setCookieName("JWT_COOKIE") // set cookie name so we can get JWT back for file downloads...
            .setRealm("Concord Server")
            .setPrefix("Bearer")
            .setAuthenticator(new ConcordServerAuthenticator())
            .setAuthorizer(new AuthorizeAllAuthorizer())         
            .buildAuthFilter()));
       

    environment.jersey().register(new AuthValueFactoryProvider.Binder<>(Caller.class));
    environment.jersey().register(RolesAllowedDynamicFeature.class);
  }

  @Override
  public void run(ConcordServerConfiguration configuration,
    Environment environment)
    throws UnsupportedEncodingException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    configureCors(environment);
    environment.jersey().setUrlPattern("/api/*");

    environment.jersey().register(CsvMessageBodyProvider.class);


	// TODO: Clean this up with type system and wrap exceptions
	Class credentialsValidatorClass = Class.forName(configuration.getCredentialsValidator().getClassName());
	Constructor credentialsValidatorConstructor = credentialsValidatorClass.getConstructor(HashMap.class);
	CredentialsValidator credentialsValidator = (CredentialsValidator)credentialsValidatorConstructor.newInstance(configuration.getCredentialsValidator().getConfigProperties());


    environment.jersey().register(new SessionsResource(configuration.getJwtTokenSecret(),credentialsValidator));
    environment.jersey().register(new RedirectResource());

    final JdbiFactory factory = new JdbiFactory();
    final Jdbi jdbi = factory.build(environment, configuration.getDatabase(), "mysql");


    // TODO: Very ugly needs to be refactored out
    SystemLabelStore systemLabelStore = new SystemLabelStore() {

		@Override
		public List<SystemLabel> getSystemLabels() {
			LabelsDao dao = jdbi.onDemand(LabelsDao.class);

			List<Label> labels = dao.getLabels();
			ArrayList<SystemLabel> systemLabels = new ArrayList<SystemLabel>();

			for (Label label: labels)
			{
				systemLabels.add(new SystemLabel(label.getLabel(), label.getShortDescription(), label.getLongDescription()));
			}

			return systemLabels;
		}
	};

    LabelSuggester labelsSuggester = null;

    try
    {
      Class labelSuggesterClass = Class.forName(configuration.getLabelSuggester().getClassName());
      Constructor labelSuggesterConstructor = labelSuggesterClass.getConstructor(SystemLabelStore.class, HashMap.class);
      labelsSuggester = (LabelSuggester) labelSuggesterConstructor.newInstance(systemLabelStore, configuration.getLabelSuggester().getConfigProperties());
    }
    catch (InvocationTargetException e)
    {
      log.error("Error loading label suggester", e);
    }

    LabelsResource labelsResource = new LabelsResource(jdbi.onDemand(LabelsDao.class));
    PhrasesResource phrasesResource = new PhrasesResource(jdbi.onDemand(PhrasesDao.class),jdbi.onDemand(VotesDao.class),
            jdbi.onDemand(UploadDao.class), labelsSuggester);

    environment.jersey().register(labelsResource);
    environment.jersey().register(phrasesResource);

    setupJwtAuth(configuration, environment);
  }

}
