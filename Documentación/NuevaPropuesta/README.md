## NUEVO Desarrollo de la aplicación (app móvil + web)

### Notas reunión CTIngenieros (24/03/25)

#### Tema 1: Nueva Base de datos

Se plantea la simplificación de la base de datos original. Debido al uso actual de la aplicación, es necesario una actualización de las tablas, tanto a nivel de nomenclatura, como a su contenido y las relaciones entre ellas.

En principio, vamos a mantener un gestor de base de datos tipo SQL en la nube. Los alumnos están investigando varias opciones, aunque la que nos ha parecido más adecuada y realizaremos las primeras pruebas será Supabase *([supabase.com](https://supabase.com))*, basada en Postgre SQL y de tipo Opensource.

#### Tablas:

**Project**  
Contiene los diferentes proyectos en los que está trabajando CTIngenieros.

- Campos
   * idProject
   * desc
   * idCliente *(FK Cliente)*
   * idArea *(FK Area)*
   * idAircraft *(FK Aircraft)*

**WorkOrder**  
Contiene las órdenes de trabajo de cada proyecto.  
NOTA: *Confirmar con CTI si es una por cada trabajador que imputará horas en la orden de trabajo*

- Campos:
   * idWorkOrder
   * desc
   * idProject *(FK Project)*

**EmployWorkOrder**  
Contiene las órdenes de trabajo de cada Empleado.  

- Campos:
   * idEmployWorkOrder
   * desc
   * projectManager *(FK Manager)*
   * idAircraft *(FK AirCraft)*
   * idWorkOrder *(FK WorkOrder)*

**Manager**
Responsables de las `Work Orders`.

- Campos:
   * idManager
   * nombre
   * apellidos

**TimeCode**
Códigos resumen parar imputar en horas. Cada entrada de esta tabla estará directamente relacionada con una o varias entradas de la tabla `Activity`.

- Campos:
   * idTimeCode
   * desc
   * color
   * chkProd

**Activity**
Detalle de las actividades que cada empleado debe utilizar al imputar horas.

- Campos:
   * idActivity
   * idTimeCode *(FK TimeCode)*
   * desc
   * dateFrom
   * dateTo

**ProjectTimeCode**
Tabla que relaciona la info de las tablas `Project` y `TimeCode`, es decir, los códigos que se pueden imputar para cada proyecto.

- Campos:
   * idProject
   * idTimeCode
 
**Employee**
Empleados que van a utilizar la aplicación y registrar la información.

- Campos:
   * idEmployee
   * nombre
   * apellidos
   * email
   * dateFrom
   * dateTo *(null o 2999-12-01)*
   * idArea *(FK Area)*
   * idRol *(FK Rol)*

 
**Cliente**
Nombre de los clientes con los que se van a trabajar.

- Campos:
   * idCliente
   * nombre
 
**Area**
Diferentes áreas o departamentees de trabajo a los que pertenecen los empleados.

- Campos:
   * idArea
   * desc

**EmployeeActivity**
Registro de horas en actividades de cada empleado.

- Campos:
   * idEmployee *(FK Employee)*
   * idWorkOrder *(FK WorkOrder)*
   * idTimeCode *(FK Activity)*
   * idActivity *(FK Activity)*
   * time
   * date
   * comment

**Config**
Parámetros comunes del Sistema.

- Campos:
   * clave
   * valor

**Aircraft**

- Campos:
    * idAircraft
    * desc

**Calendar**

- Campos:
    * idCalendar
    * date

**Rol**

- Campos:
     * idRol
     * rol

**Employee WorkHours**

- Campos:
    * dateFrom
    * dateTo
    * idEmployee *(FK Employee)*
    * hours

#### Tema 2: Pantallas App Móvil

Después de analizar la nueva base de datos y la funcionalidad de la aplicación original, a modo de resumen debemos realizar las siguientes pantallas:

**IMPORTANTE:** ***Todas las pantallas con elementos muy visuales***

- **Login**
Autenticación del empleado.

- **ResumenInicial**
Resumen horas hasta el día actual:
   * Horas por códigos de TimeCode/Activity
   * Horas positivas/negativas
   * ...
   * Menú, Botones y/o elementos de acceso al resto de pantallas de la app.

- **DetalleHorasMes**
   * Calendario por mes y codificación de color diaria.
   * Gráfico de colores *(barra horizontal apilada o similar... al hacer clic en ella nos podría llevar a otra pantalla con la info desglosada)*

- **ResumenHorasAnio**
  * Mostrar por meses un gráfico de colores de las horas realizadas.
  * Contador anual de horas realizadas en los distintos conceptos.
  * Teórico de horas productivas y las horas previstas a realizar según la info hasta el día actual.

- **GeneradorInformes** *(ADMIN)*
   * Ver con el responsable de CTIngenieros Cádiz los informes que necesita y el formato en el que generarlos.

- **GestionUsuarios** *(ADMIN)*
  * Mantenimiento de usuarios.

- **GestionTablasBase** *(ADMIN)*
   * Mantenimiento de tablas base (Project, WorkOrder, ...)

