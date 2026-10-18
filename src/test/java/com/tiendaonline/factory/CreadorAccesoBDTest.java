package com.tiendaonline.factory;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreadorAccesoBDTest {

 @Test
 void obtieneConexionDelDataSourceInyectado() throws Exception {

  DataSource ds = mock(DataSource.class);
  Connection c = mock(Connection.class);

  when(ds.getConnection()).thenReturn(c);

  assertSame(
          c,
          new CreadorAccesoLectura(ds).obtenerConexion()
  );

  verify(ds).getConnection();
 }

 @Test
 void cadaCreadorConservaElDataSource() throws Exception {

  DataSource ds = mock(DataSource.class);

  when(ds.getConnection())
          .thenReturn(mock(Connection.class));

  assertNotNull(
          new CreadorAccesoEscritura(ds).obtenerConexion()
  );

  assertNotNull(
          new CreadorAccesoLecturaEscritura(ds).obtenerConexion()
  );
 }
}
