
package com.tiendaonline.factory;

import javax.sql.DataSource;

public class CreadorAccesoLecturaEscritura extends CreadorAccesoBD {

    private final DataSource dataSource;

    public CreadorAccesoLecturaEscritura(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    protected DataSource crearDataSource() {

        return dataSource;
    }

    @Override
    public String toString() {
        return "CreadorAccesoLecturaEscritura{" +
                "dataSource=" + dataSource +
                '}';
    }
}
