package com.tiendaonline.factory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
class PermisosMySqlIntegrationTest {

    private static Properties properties;

    private int idTemporal;

    @BeforeAll
    static void cargarConfiguracion() throws Exception {

        properties = new Properties();

        try (InputStream input = new ClassPathResource(
                "application-test-local.properties"
        ).getInputStream()) {

            properties.load(input);
        }
    }

    @Test
    void lecturaPuedeConsultar() throws Exception {

        assertTrue(
                new VerificadorPermisosBD(
                        new CreadorAccesoLectura(
                                dataSource("lectura")
                        )
                ).verificarLectura()
        );
    }

    @Test
    void escrituraPuedeInsertarActualizarYEliminar()
            throws Exception {

        VerificadorPermisosBD verificador =
                new VerificadorPermisosBD(
                        new CreadorAccesoEscritura(
                                dataSource("escritura")
                        )
                );

        idTemporal = verificador.verificarEscritura(
                "__TEST_PERMISOS__",
                1
        );

        assertTrue(
                verificador.verificarLecturaEscritura(
                        idTemporal,
                        "__TEST_PERMISOS_ACTUALIZADO__",
                        2
                )
        );

        assertTrue(
                verificador.verificarRestricciones(idTemporal)
        );

        idTemporal = 0;
    }

    @AfterEach
    void limpiar() throws SQLException {

        if (idTemporal == 0) {
            return;
        }

        try (
                Connection c = dataSource("escritura").getConnection();

                PreparedStatement s = c.prepareStatement(
                        "DELETE FROM prueba_permisos WHERE id_prueba=?"
                )
        ) {

            s.setInt(1, idTemporal);
            s.executeUpdate();
        }
    }

    private static DataSource dataSource(String tipo) {

        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setUrl(
                properties.getProperty(
                        "test.datasource." + tipo + ".url"
                )
        );

        ds.setUsername(
                properties.getProperty(
                        "test.datasource." + tipo + ".username"
                )
        );

        ds.setPassword(
                properties.getProperty(
                        "test.datasource." + tipo + ".password"
                )
        );

        return ds;
    }
}
