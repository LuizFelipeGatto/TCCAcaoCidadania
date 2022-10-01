package br.org.ifsuldeminas.acaocidadania;

import br.org.ifsuldeminas.acaocidadania.AcaoCidadaniaApp;
import br.org.ifsuldeminas.acaocidadania.config.AsyncSyncConfiguration;
import br.org.ifsuldeminas.acaocidadania.config.EmbeddedSQL;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { AcaoCidadaniaApp.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public @interface IntegrationTest {
}
