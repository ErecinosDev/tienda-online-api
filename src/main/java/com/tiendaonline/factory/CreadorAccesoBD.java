package com.tiendaonline.factory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class CreadorAccesoBD {
    protected abstract DataSource crearDataSource();

    public Connection obtenerConexion() throws SQLException {
        return crearDataSource().getConnection();
    }

    @Override
    public String toString() {
        return "CreadorAccesoBD{}";
    }
}
