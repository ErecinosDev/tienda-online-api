
package com.tiendaonline.strategy;

import com.tiendaonline.entity.Compra;
import com.tiendaonline.entity.MetodoPago;

public interface EstrategiaPago {

    MetodoPago getMetodoPago();

    ResultadoPago procesar(Compra c);


}
