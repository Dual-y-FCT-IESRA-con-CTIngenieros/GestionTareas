|                   | **Respuestas**                          |
|-------------------|-----------------------------------------|
|**Nombre**         | CambiarContraseña.vb      |
|**Descripción**    | Ventana que permite el cambio de contraseñas de los usuarios. Si el usuario tiene rol básico, solo puede cambiar su propia contraseña. Un responsable puede cambiar cualquier contraseña. |
|**Funcionalidad**  | Icono verde: Verifica que los datos ingresados sean correctos y actualiza la base de datos con la bueva contraseña. <br>Icono rojo: Cancela la operación y vuelve a la pantalla anterior. <br>Pantalla: Muestra información sobre los usuarios a los que tiene acceso el usuario actual. |
|**Otros**          | Restricciones: Un usuario básico no puede modificar la contraseña de otro usuario. <br>Validaciones: <br>La nueva contraseña debe cumplir con las políticas de seguridad establecidas. <br>Se debe solicitar confirmación antes de cambiar la contraseña de otro usuario (en caso de un responsable). |
|**Acceso a BD**    | ✅                                |
|*Tabla1*           | Usuarios |
|*Consulta*         | ✅ |
|*Modificación*     | ✅ |
|*Inserción*        | ❌ |
|*Borrado*          | ❌ |
|**Imagen**           | ![Nombre_Imagen](CambiarContraseña_img.jpg)|