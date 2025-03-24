## NUEVO Desarrollo de la aplicación (app móvil + web)

### Notas reunión CTIngenieros (24/03/25)

#### Tema: Nueva Base de datos

Se plantea la simplificación de la base de datos original. Debido al uso actual de la aplicación, es necesario una actualización de las tablas, tanto a nivel de nomenclatura, como a su contenido y las relaciones entre ellas.

En principio, vamos a mantener un gestor de base de datos tipo SQL en la nube. Los alumnos están investigando varias opciones, aunque la que nos ha parecido más adecuada y realizaremos las primeras pruebas será Supabase *([supabase.com](https://supabase.com))*, basada en Postgre SQL y de tipo Opensource.

#### Tablas:

**Project**  
Contiene los diferentes proyectos en los que está trabajando CTIngenieros.

- Campos
   * idProject
   * desc
   * idManager

**WorkOrder**  
Contiene las órdenes de trabajo de cada proyecto.  
NOTA: *Confirmar con CTI si es una por cada trabajador que imputará horas en la orden de trabajo*

- Campos:
   * idWorkOrder
   * desc
   * projectManager *(FK Manager)*
   * idProject *(FK Project)*

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
   * idArea
   * idRol
 
**Area**
Diferentes áreas o departamentees de trabajo a los que pertenecen los empleados.

- Campos:
   * idArea
   * desc

**EmployeeActivity**
Registro de horas en actividades de cada empleado.

- Campos:
   * idEmployee
   * idWorkOrder
   * idTimeCode
   * idActivity
   * time
   * comment

**Config**
Parámetros comunes del Sistema.

- Campos:
   * clave
   * valor

