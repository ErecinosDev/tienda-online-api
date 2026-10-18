
package com.tiendaonline.factory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VerificadorPermisosBD {

    private final CreadorAccesoBD creador;

    public VerificadorPermisosBD(CreadorAccesoBD creador) {

        this.creador = creador;
    }

    public boolean verificarLectura() throws SQLException {

        try (
                Connection c = creador.obtenerConexion();

                PreparedStatement s = c.prepareStatement(
                        "SELECT id_prueba, descripcion, valor FROM prueba_permisos"
                );

                ResultSet r = s.executeQuery()
        ) {

            return r != null;
        }
    }

    public int verificarEscritura(
            String descripcion,
            int valor) throws SQLException {

        try (
                Connection c = creador.obtenerConexion();

                PreparedStatement s = c.prepareStatement(
                        "INSERT INTO prueba_permisos(descripcion, valor) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            s.setString(1, descripcion);
            s.setInt(2, valor);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {

                if (!r.next()) {
                    throw new SQLException("No se generó identificador");
                }

                return r.getInt(1);
            }
        }
    }

    public boolean verificarLecturaEscritura(
            int id,
            String descripcion,
            int valor) throws SQLException {

        try (
                Connection c = creador.obtenerConexion();

                PreparedStatement s = c.prepareStatement(
                        "UPDATE prueba_permisos SET descripcion=?, valor=? WHERE id_prueba=?"
                )
        ) {

            s.setString(1, descripcion);
            s.setInt(2, valor);
            s.setInt(3, id);

            return s.executeUpdate() == 1;
        }
    }

    public boolean verificarRestricciones(int id)
            throws SQLException {

        try (
                Connection c = creador.obtenerConexion();

                PreparedStatement s = c.prepareStatement(
                        "DELETE FROM prueba_permisos WHERE id_prueba=?"
                )
        ) {

            s.setInt(1, id);

            return s.executeUpdate() == 1;
        }
    }
}
