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
    //Initializer.init();
    if(app == null) {


      Map<String, String> env = System.getenv();

      entityManagerFactory =  createEntityManagerFactory();
      System.out.println("Se creo el entity manager ok");
      System.out.println("Se creo el entity manager ok");
      System.out.println("Se creo el entity manager ok");
      String strport = System.getenv("PORT");
      if (strport == null){
        strport = "8080";
      }


      PrettyProperties.getInstance();
      Integer port = Integer.parseInt(System.getenv("PORT"));
      app = Javalin.create(config()).start(Integer.parseInt("8080"));
      initTemplateEngine();
      AppHandlers.applyHandlers(app);
      Router.init();
/*
      if(Boolean.parseBoolean(PrettyProperties.getInstance().propertyFromName("dev_mode"))) {
        Initializer.init();
      }
      */

    }

  }



  public static EntityManagerFactory createEntityManagerFactory() throws Exception {
    // https://stackoverflow.com/questions/8836834/read-environment-variables-in-persistence-xml-file
    Map<String, String> env = System.getenv();
    Map<String, Object> configOverrides = new HashMap<String, Object>();


    configOverrides.put("javax.persistence.jdbc.url",  env.get("javax.persistence.jdbc.url"));
    configOverrides.put("javax.persistence.jdbc.user", env.get("javax.persistence.jdbc.user"));
    configOverrides.put("javax.persistence.jdbc.password", env.get("javax.persistence.jdbc.password"));
    configOverrides.put("javax.persistence.jdbc.driver", env.get("javax.persistence.jdbc.driver"));
    configOverrides.put("hibernate.hbm2ddl.auto", env.get("hibernate.hbm2ddl.auto"));
    return Persistence.createEntityManagerFactory("dpg-cljr3sh8mmjc73dbl5dg-a", configOverrides);
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