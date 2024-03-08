package com.example.budgetbuddy.Data

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetbuddy.R
import com.example.budgetbuddy.navigation.AppScreens
import com.example.budgetbuddy.utils.AppLanguage
import java.time.LocalDate
import java.util.UUID


/*******************************************************************
 **       Data Clase para definir nuevos tipos de datos          **
 ******************************************************************/

/**
 * Al estar organizada de forma temporal, la APP utilizará por defecto la clase
 * [GastoDia] con los datos necesarios que se recojan de [Gasto] (de forma que
 * no se tenga acceso a toda la información en todas partes). Lo mismo para [GastoTipo]
 * que se utilizará en el composable [Dashboards] únicamente.
 *
 * El DataClass [Diseño] vale para definir el diseño de la barra de navegación, sin
 * tener que definir los botones uno a uno.
 */


data class GastoDia(val cantidad: Double, val fecha: LocalDate)
data class GastoTipo(val cantidad: Double, val tipo: TipoGasto)
data class Diseño(val pantalla: AppScreens, val icono: Painter)

/**
 * Clase [Gasto], utilizada para almacenar toda la información de todos las entidades
 * tipo [Gasto] en la base de datos de Room. Si se quieren utilizar datos específicos
 * dependiendo de la pantalla (como es siempre el caso). Se recurren a las data-class
 * definidas arriba.
 *
 * Esta clase se utiliza con propósito de almacenaje.
 */

@Entity
data class Gasto(
    var nombre: String,
    var cantidad: Double,
    var fecha: LocalDate,
    var tipo: TipoGasto,
    @PrimaryKey val id: String = UUID.randomUUID().toString()
){
    //Cada Gasto viene con esta función definida de forma que no haya que implementar la consulta
    fun toString(idioma: AppLanguage): String {
        return "${nombre} (${obtenerTipoEnIdioma(tipo, idioma.code)}):\t\t${cantidad}€\n"
    }
}

