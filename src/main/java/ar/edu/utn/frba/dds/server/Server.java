package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.server.handlers.AppHandlers;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.JavalinRenderer;
import ar.edu.utn.frba.dds.server.authmiddleware.AuthMiddleware;
import ar.edu.utn.frba.dds.server.utils.PrettyProperties;


import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Server {
  private static Javalin app = null;

  public static Javalin app() {
    if(app == null)
      throw new RuntimeException("App no inicializada");
    return app;
  }

  public static void init() throws Exception {
     EntityManagerFactory entityManagerFactory;
    Initializer.init();
    if(app == null) {


      Map<String, String> env = System.getenv();

      entityManagerFactory =  createEntityManagerFactory();
      String strport = System.getenv("PORT");
      if (strport == null){
        strport = "8080";
      }


      PrettyProperties.getInstance();
      Integer port = Integer.parseInt(System.getenv("PORT"));
      app = Javalin.create(config()).start(port);
      initTemplateEngine();
      AppHandlers.applyHandlers(app);
      Router.init();

      if(Boolean.parseBoolean(PrettyProperties.getInstance().propertyFromName("dev_mode"))) {
        Initializer.init();
      }
    }

  }



  public static EntityManagerFactory createEntityManagerFactory() throws Exception {
    // https://stackoverflow.com/questions/8836834/read-environment-variables-in-persistence-xml-file
    Map<String, String> env = System.getenv();
    Map<String, Object> configOverrides = new HashMap<String, Object>();

    String[] keys = new String[] {
        "DATABASE_URL",
        "javax__persistence__jdbc__driver",
        "javax__persistence__jdbc__password",
        "javax__persistence__jdbc__url",
        "javax__persistence__jdbc__user",
        "hibernate__hbm2ddl__auto",
        "hibernate__connection__pool_size",
        "hibernate__show_sql" };

    for (String key : keys) {

      try{
        if (key.equals("DATABASE_URL")) {

          // https://devcenter.heroku.com/articles/connecting-heroku-postgres#connecting-in-java
          String value = env.get(key);
          URI dbUri = new URI(value);
          String username = dbUri.getUserInfo().split(":")[0];
          String password = dbUri.getUserInfo().split(":")[1];
          //javax.persistence.jdbc.url=jdbc:postgresql://localhost/dblibros
          value = "jdbc:mysql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();// + "?sslmode=require";
          configOverrides.put("javax.persistence.jdbc.url", value);
          configOverrides.put("javax.persistence.jdbc.user", username);
          configOverrides.put("javax.persistence.jdbc.password", password);
          configOverrides.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");

          configOverrides.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        }
        // no se pueden poner variables de entorno con "." en la key
        String key2 = key.replace("__",".");
        if (env.containsKey(key)) {
          String value = env.get(key);
          configOverrides.put(key2, value);
        }
      } catch(Exception ex){
        System.out.println("Error configurando " + key);
      }
    }
    System.out.println("Config overrides ----------------------");
    for (String key : configOverrides.keySet()) {
      System.out.println(key + ": " + configOverrides.get(key));
    }
    return Persistence.createEntityManagerFactory("db", configOverrides);
  }



  private static Consumer<JavalinConfig> config() {
    return config -> {
      config.staticFiles.add(staticFiles -> {
        staticFiles.hostedPath = "/";
        staticFiles.directory = "/public";
      });
      AuthMiddleware.apply(config);
    };
  }


  private static void initTemplateEngine() {
    JavalinRenderer.register(
        (path, model, context) -> { // Función que renderiza el template
          Handlebars handlebars = new Handlebars();
          Template template = null;
          try {
            template = handlebars.compile(
                "templates/" + path.replace(".hbs",""));
            return template.apply(model);
          } catch (IOException e) {
            e.printStackTrace();
            context.status(HttpStatus.NOT_FOUND);
            return "No se encuentra la página indicada...";
          }
        }, ".hbs" // Extensión del archivo de template
    );
  }
}