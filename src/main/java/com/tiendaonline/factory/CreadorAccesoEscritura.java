
package com.tiendaonline.factory;

import javax.sql.DataSource;

public class CreadorAccesoEscritura extends CreadorAccesoBD {

    private final DataSource dataSource;

    public CreadorAccesoEscritura(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    protected DataSource crearDataSource() {

        return dataSource;
    }

    @Override
    public String toString() {
        return "CreadorAccesoEscritura{" +
                "dataSource=" + dataSource +
                '}';
    }
}
