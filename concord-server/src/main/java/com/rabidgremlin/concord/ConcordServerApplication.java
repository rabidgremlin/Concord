package com.rabidgremlin.concord;

import java.io.UnsupportedEncodingException;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
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
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.rabidgremlin.concord.auth.ConcordServerAuthenticator;
import com.rabidgremlin.concord.auth.AuthorizeAllAuthorizer;
import com.rabidgremlin.concord.auth.Caller;
import com.rabidgremlin.concord.resources.PhrasesResource;
import com.rabidgremlin.concord.resources.RedirectResource;
import com.rabidgremlin.concord.resources.SessionsResource;

public class ConcordServerApplication
    extends Application<ConcordServerConfiguration>
{
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
    throws UnsupportedEncodingException
  {
    configureCors(environment);
    environment.jersey().setUrlPattern("/api/*");

    
    environment.jersey().register(new SessionsResource(configuration.getJwtTokenSecret()));    
    environment.jersey().register(new RedirectResource());
    
    environment.jersey().register(new PhrasesResource());

    setupJwtAuth(configuration, environment);
  }

}
