
package com.tiendaonline.factory;

import javax.sql.DataSource;

public class CreadorAccesoLectura extends CreadorAccesoBD {

    private final DataSource dataSource;

    public CreadorAccesoLectura(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    protected DataSource crearDataSource() {

        return dataSource;
    }

    @Override
    public String toString() {
        return "CreadorAccesoLectura{" +
                "dataSource=" + dataSource +
                '}';
    }
}
