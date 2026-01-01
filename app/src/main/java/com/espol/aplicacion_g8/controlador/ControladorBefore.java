package com.espol.aplicacion_g8.controlador;
/**
 * EN ESTE CONTROLADOR PUEDEN ENCONTRAR EL CONTROL QUE TENIAMOS EN JAVA
 * ANTES DE EMPEZAR A TRABAJAR, CREEN SU PROPIO CONTROLADOR POR SEPARADO Y EXTRAIGAN SUS METODOS
 * 1RO ELIMINEN LOS SCANNERS Y SOUTS DE SU CONTROLADOR
 * 2DO HAGAN LOS METODOS LO MAS SENCILLOS POSIBLES Y QUE CONSERVEN LA LOGICA QUE TIENEN
 * 3RO GUIENSEN CON EL "ControladorActividades" y "ControladorSesionEnfoque"
 */


/*import java.util.Arrays;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.espol.modelo.actividad.Actividad;
import com.espol.modelo.actividad.ActividadAcademica;
import com.espol.modelo.actividad.ActividadPersonal;
import com.espol.modelo.actividad.Prioridad;
import com.espol.modelo.actividad.SesionEnfoque;
import com.espol.modelo.actividad.SesionEnfoque.Tecnicas;
import com.espol.modelo.actividad.TipoAcademico;
import com.espol.modelo.actividad.TipoActividad;
import com.espol.modelo.actividad.TipoPersonal;
import com.espol.modelo.hidratacion.ControlHidratacion;
import com.espol.modelo.hidratacion.RegistroHidratacion;
import com.espol.modelo.juego.Carta;
import com.espol.modelo.juego.JuegoDeMemoria;
import com.espol.modelo.sostenibilidad.AccionSostenible;
import com.espol.modelo.sostenibilidad.RegistroSostenibilidad;
import com.espol.visualizador.Visualizer1;
import com.espol.visualizador.Visualizer2;
import com.espol.visualizador.Visualizer3;
import com.espol.visualizador.Visualizer4;
import com.espol.visualizador.visualizerEnfoque;
import com.espol.modelo.actividad.TipoPersonal;
import com.espol.modelo.actividad.Prioridad;

public class ControladorBefore {
    private Scanner sc;
    private Visualizer1 vista;
    private Visualizer2 vista2;
    private final ControlHidratacion hidratacion = new ControlHidratacion();
    private Visualizer3 vista3 = new Visualizer3();
    private Visualizer4 vista4 = new Visualizer4();
    private RegistroSostenibilidad registroSostenibilidad;
    private visualizerEnfoque vistaEnfoque = new visualizerEnfoque();
    private JuegoDeMemoria juegoDeMemoria;
    private boolean juegoMemoriaInciado = false;


    public Controlador(){
        this.sc = new Scanner(System.in);
        this.vista2 = new Visualizer2();
        this.vista = new Visualizer1();
        this.vista3 =new Visualizer3();
        this.registroSostenibilidad = new RegistroSostenibilidad(null);
        inicializarSostenibilidad();
    }
    public void iniciarApp(){
        // aqui se van a inicializar las apps
        ActividadAcademica actividad1 = new ActividadAcademica(TipoActividad.ACADEMICA, TipoAcademico.PROYECTO.getNombre(), "Proyecto POO", "POO","Relizarlo", Prioridad.ALTA, "30/11/2025", 2);
        actividad1.agregarSesion(new SesionEnfoque(Tecnicas.POMODORO, 25));
        actividad1.agregarSesion(new SesionEnfoque(Tecnicas.POMODORO, 25));
        anadirActividad(actividad1);
        anadirActividad(new ActividadPersonal(TipoActividad.PERSONAL, TipoPersonal.CITAS.getNombre(), "Cita medica", "ir con el Dr.Gutierrez", Prioridad.ALTA, "30/11/2025", 45, "En el IESS"));
        anadirActividad(new ActividadAcademica(TipoActividad.ACADEMICA, TipoAcademico.TAREA.getNombre(), "Tares calculo vectorial", "Calculo Vectorial","Hacerlo con calma", Prioridad.MEDIA, "03/12/2025", 0));
        anadirActividad(new ActividadAcademica(TipoActividad.ACADEMICA, TipoAcademico.EXAMEN.getNombre(), "Examen de POO", "POO","Estudiar a full", Prioridad.ALTA, "10/12/2025", 0));
        hidratacion.registrarHidratacion(500,LocalDate.of(2024, 11, 23),LocalTime.of(9, 0) );
        hidratacion.registrarHidratacion(750,LocalDate.of(2024, 11, 27),LocalTime.of(22, 10));

        // el resto de la aplicación
        int opcion = 0;
        while (opcion != 6){
            vista.menu();

            // Evitador de errores a la hora de que el usuario ingrese algo que no sea un int
            if (sc.hasNextInt()){
                opcion = sc.nextInt();
                sc.nextLine();
            } else {
                System.out.println("¡Debe de ingrese un valor valido!\n");
                sc.nextLine();
                continue;
            }

            switch (opcion) {
                // Gestión de Actividades
                case 1:
                    int opcionActividades= 0;
                    while(opcionActividades != 5){
                        System.out.println("=== <GESTION DE ACTIVIDADES> ===");
                        vista.menuActividades();
                        System.out.print("Eliga una opción: ");
                        if (sc.hasNextInt()){
                            opcionActividades = sc.nextInt();
                            sc.nextLine();
                        } else {
                            System.out.println("¡Ingrese un valor valido!\n");
                            sc.nextLine();
                        }
                        switch (opcionActividades){
                            case 1: // Detalles de actividad
                                visualizarActividades(listaActividades);
                                break;
                            case 2: // Crear actividad
                                creacionActividades(opcionActividades);
                                break;
                            case 3: // Modificar avance
                                modificadorAvance(listaActividades);
                                break;
                            case 4: // Eliminar actividad
                                procesoEliminacion(listaActividades);
                                break;
                        }
                    }
                    break;
                case 2:
                    sesionEnfoque(); // Sesion de enfoque
                    break;

                case 3:{    //Control de hidratacion
                    int primeropi= 0;
                    while(primeropi !=4){
                        vista3.menuControlHidratacion();
                        if(sc.hasNextInt()) {
                            primeropi = sc.nextInt();
                            sc.nextLine();
                        }
                        else {
                            System.out.println("Opción no válida. Ingrese un número.");
                            sc.nextLine();
                            continue;
                        }
                        switch (primeropi){
                            case 1:
                                c3_registrarIngesta();
                                break;
                            case 2:
                                c3_establecerMeta();
                                break;
                            case 3:
                                c3_verProgreso();
                                break;
                            case 4:
                                System.out.println("Volviendo al menú principal...");
                                break;
                            default:
                                System.out.println("Opcion no valida. Intente de nuevo");
                        }
                    }
                }
                break;
                case 4:     // Opción 4: Registro diario de Sostenibilidad
                    gestionarSostenibilidad();
                    break;

                case 5:     // Juego de memoria
                    jugarMemoria();
                    break;

            }
        }
    }
    // Todo lo relacionado con ACTIVIDADES
    // Lista con actividades =============================================
    static ArrayList<Actividad> listaActividades = new ArrayList<>();
    public static ArrayList<Actividad> getListaActividades(){
        return listaActividades;
    }

    static ArrayList<SesionEnfoque> listaSesionesEnfoques = new ArrayList<>();
    public static ArrayList<SesionEnfoque> getListaSesionesEnfoques(){
        return listaSesionesEnfoques;
    }
    //====================================================================
    //Visualiza las actividades
    private void visualizarActividades(ArrayList<Actividad> listaActividades){
        if (listaActividades.isEmpty()){
            System.out.println("¡ No se encuentran Actividades Registradas !");
            System.out.println("Registre al menos una actividad para poder visualizarla.\n");
        } else {
            int opcionActividadesDetalladas;
            do {
                vista.visualizarActividades(listaActividades);
                vista.finaLineaVisualizadorActividades();
                System.out.print("Ingrese el ID para ver *DETALLES* o '0' para volver: ");
                opcionActividadesDetalladas = sc.nextInt();
                sc.nextLine();
                if (opcionActividadesDetalladas != 0) {
                    verificarTipoDeActividad(opcionActividadesDetalladas, listaActividades);
                    sc.nextLine();
                }
            } while (opcionActividadesDetalladas != 0);
        }
    }
    // CREACION DE ACTIVIDADES VERSION OPTIMIZADA (Mantenimiento)
    public void creacionActividades(int opcion){
        vista.mostrarEncabezado();
        vista.mostrarPasoCategoria();
        while(!(Controlador.controladorOpcion(opcion))){
            System.out.print("Ingrese una opción valida: ");
            opcion = sc.nextInt();
        }
        if (opcion == 1){
            sc.nextLine();
            TipoAcademico[] tipos = TipoAcademico.values();

            int opcionAcademica = sc.nextInt();
            sc.nextLine();

            while (opcionAcademica < 1 || opcionAcademica > tipos.length) {
                System.out.println("Opción inválida!");
                System.out.print("Ingrese una opcion valida (1-3): ");
                opcionAcademica = sc.nextInt();
                sc.nextLine();
            }

            TipoAcademico tipoSeleccionado = tipos[opcionAcademica - 1];
            String tipSelecString = tipoSeleccionado.getNombre();
            System.out.print("Ingrese Nombre de la Actividad Academica: ");
            String nombre = sc.nextLine();
            System.out.print("Ingrese la Asignatura: ");
            String asignatura = sc.nextLine();
            System.out.print("Ingrese una descripcion: ");
            String descripcion = sc.nextLine();
            System.out.print("Ingrese la Fecha de Vencimiento (DD/MM/AAAA): ");
            String fechaLimite= sc.nextLine();
            System.out.print("Ingrese Prioridad (1.ALTA, 2.MEDIA, 3.BAJA): ");
            int opcionPrioridad = sc.nextInt();
            Prioridad[] prioridades = Prioridad.values();
            while ((opcionPrioridad <0) || (opcionPrioridad >3)){
                System.out.println("Eliga una de las 3 prioridades indiciadas, por favor");
                System.out.print("Ingrese Prioridad (1.ALTA, 2.MEDIA, 3.BAJA): ");
                opcionPrioridad = sc.nextInt();
            }
            Prioridad prioridad = prioridades[opcion-1];
            System.out.print("Ingrese Tiempo Estimado (en horas): ");
            double tiempoEstimado = sc.nextDouble();
            sc.nextLine();
            Actividad actividad = new ActividadAcademica(TipoActividad.ACADEMICA, tipSelecString, nombre, asignatura ,descripcion, prioridad, fechaLimite, tiempoEstimado);
            vista.mostrarConfirmacion(actividad);
            anadirActividad(actividad);


        } else {
            TipoPersonal[] tipos = TipoPersonal.values();

            int opcionPersonal = sc.nextInt();
            sc.nextLine();

            while (opcionPersonal < 1 || opcionPersonal > tipos.length) {
                System.out.println("Opción inválida!");
                System.out.print("Ingrese una opcion valida (1-3): ");
                opcionPersonal = sc.nextInt();
                sc.nextLine();
            }

            TipoPersonal tipoSeleccionado = tipos[opcionPersonal - 1];
            String TipselecString = tipoSeleccionado.getNombre();


            System.out.print("Ingrese el Nombre de la Actividad Personal: ");
            String nombre = sc.nextLine();
            System.out.print("Ingrese Descripcion: ");
            String detalles = sc.nextLine();
            System.out.print("Ingrese la Fecha de Vencimiento (DD/MM/AAAA):  ");
            String fechaLimite = sc.nextLine();
            System.out.print("Ingrese Prioridad (1.ALTA, 2.MEDIA, 3.BAJA): ");
            int opcionPrioridad = sc.nextInt();
            Prioridad[] prioridades = Prioridad.values();
            while ((opcionPrioridad <0) || (opcionPrioridad >3)){
                System.out.println("Eliga una de las 3 prioridades indiciadas, por favor");
                System.out.print("Ingrese Prioridad (1.ALTA, 2.MEDIA, 3.BAJA): ");
                opcionPrioridad = sc.nextInt();
            }
            Prioridad prioridad = prioridades[opcion-1];
            System.out.print("Ingrese Tiempo Estimado (en minutos): ");
            double tiempoEstimado = sc.nextDouble();
            sc.nextLine();
            System.out.print("Ingrese Lugar (Opcional): ");
            String lugar = sc.nextLine();
            Actividad actividad = new ActividadPersonal(TipoActividad.PERSONAL, TipselecString, nombre, detalles, prioridad, fechaLimite, tiempoEstimado, lugar);
            vista.mostrarConfirmacion(actividad);
            anadirActividad(actividad);
        }
    }

    // Metodo para anadir nuevas actividades
    public void anadirActividad(Actividad actividad){
        listaActividades.add(actividad);
    }


    // Metodo para eliminar una actividad
    public void eliminarActividad(int ID){
        listaActividades.removeIf(actividad -> ID == actividad.getId());
    }

    // metodo donde procesa la eliminacion
    public void procesoEliminacion(ArrayList<Actividad> listaActividades){
        vista.mostrarActividadesAEliminar(listaActividades);
        int id = sc.nextInt();
        sc.nextLine();
        Actividad actividad = encuentraActividad(listaActividades, id);
        if (actividad != null){
            vista.confirmacionEliminacion(id, actividad);
            String confirmacion = sc.nextLine();
            if (confirmacion.toUpperCase().equals("S")){
                eliminarActividad(id);
                Actividad.reducirContadorId();
                for (Actividad actividadIteracion :listaActividades ){
                    if (actividadIteracion.getId()> id){
                        actividadIteracion.setId(actividadIteracion.getId()-1);;
                    }
                }
            } else {
                vista.actividadNoEncontrada(id);
            }
        }
    }

    //Metodo que encuentra la actividad
    public static Actividad encuentraActividad(ArrayList<Actividad> actividades, int id){
        Actividad actividadEncontrada = null;
        for (Actividad actividad: actividades){
            if (actividad.getId() == id){
                actividadEncontrada = actividad;
                return actividadEncontrada;
            }
        }
        return actividadEncontrada;
    }

    // Metodo para actualizar el avance de una actividad
    public void actualizarEstado(int ID, int avance){
        for (int i = 0; i < listaActividades.size();i++){
            if (ID == listaActividades.get(i).getId()){
                listaActividades.get(i).setAvance(avance);
            }
        }
    }

    // Retorna falso o verdadero para la opcion 2 del menu
    public static boolean controladorOpcion(int numero){
        if ((numero == 1) || (numero == 2 )){
            return true;
        }
        return false;
    }

    // Metodo que sirve para identificar si la actividad seleccionada es tipo Academica o Personal
    public void verificarTipoDeActividad(int ID, ArrayList<Actividad> listaActividad){
        Actividad actividad = null;
        for (Actividad actividadSeleccionada: listaActividad){
            if(actividadSeleccionada.getId() == ID){
                actividad = actividadSeleccionada;
            }
        }
        if (actividad == null){
            System.out.println("=== EL ID de la actividad ingresada no existe ===");
        } else {
            if (actividad instanceof ActividadAcademica) {
                ActividadAcademica actividadAcademica = (ActividadAcademica) actividad;
                vista.visualizarActividadesDetalladasAcademicas(actividadAcademica);
            } else {
                ActividadPersonal actividadPersonal = (ActividadPersonal) actividad;
                vista.visualizarActividadesDetalladasPersonales(actividadPersonal);
            }
        }
    }

    // verifica y le pone etiqueta al avance
    public static String verificarAvance(double avance){
        if(avance != 100){
            return "En curso";
        } else {
            return "Finalizado";
        }
    }

    // Modificador de avance
    public void modificadorAvance(ArrayList<Actividad> listaActividades){
        vista.mostrarAvances(listaActividades);
        int id = sc.nextInt();
        sc.nextLine(); // limpia el buffer
        while(id != 0){
            Actividad actividad = encuentraActividad(listaActividades,id);
            if (actividad != null){
                int nuevoAvance = vista.mostrarDetallesDelAvance(actividad);
                actividad.setAvance(nuevoAvance);
                id = 0; // aqui el id se tiene que poner 0 para que no exista un bucle infinito :(
            } else {
                vista.actividadNoEncontrada(id);
                break;
            }

        }
    }


    // TODO LO RELACIONADO CON SESION DE ENFOQUE
    public void sesionEnfoque(){
        vistaEnfoque.menuTenicaEnfoque();
        int opcion = sc.nextInt();
        opcion = verificaOpcionEnfoque(opcion);
        sc.nextLine();
        switch (opcion){
            //METODO POMODORO
            case 1:
                vistaEnfoque.inicioPomodoro();
                int id = sc.nextInt();
                Actividad actividad = encuentraActividad(listaActividades, id);
                if (actividad!=null){
                    SesionEnfoque sesion = new SesionEnfoque(SesionEnfoque.Tecnicas.POMODORO, 25);
                    vistaEnfoque.desarrolloPomodoro(actividad, sesion);
                    actividad.agregarSesion(sesion);
                    actividad.setAvance(100);
                } else {
                    vista.actividadNoEncontrada(id);
                }
                break;
            case 2:
                vistaEnfoque.inicioDeepwork();
                id = sc.nextInt();
                actividad = encuentraActividad(listaActividades, id);
                if (actividad!=null){
                    SesionEnfoque sesion = new SesionEnfoque(SesionEnfoque.Tecnicas.DEEPWORK, 90);
                    vistaEnfoque.desarrolloDeepwork(actividad, sesion);
                    actividad.agregarSesion(sesion);
                    actividad.setAvance(100);
                } else {
                    vista.actividadNoEncontrada(id);
                }
                break;
            case 3:
                break;
        }

    }

    public int verificaOpcionEnfoque(int opcion){
        while((opcion > 3) || (opcion < 0)){
            System.out.print("Ingrese una opcion valida (0-3): ");
            opcion = sc.nextInt();
        }
        return opcion;
    }




    //  SOSTENIBILIDAD

    // Carga inicial: 2 días con acciones (23 y 24 de noviembre)
    private void inicializarSostenibilidad() {
        // Tomamos el año anterior al actual
        int anioAnterior = LocalDate.now().minusYears(1).getYear();

        // 1er registro: 23 de noviembre (acción 1)
        registroSostenibilidad.registrarAcciones(
                crearAccionesDesdeOpciones(
                        Arrays.asList(1),                       // solo acción 1
                        LocalDate.of(anioAnterior, 11, 23)      // 23 de nov del año anterior
                )
        );

        // 2do registro: 24 de noviembre (acción 2)
        registroSostenibilidad.registrarAcciones(
                crearAccionesDesdeOpciones(
                        Arrays.asList(2),                       // solo acción 2
                        LocalDate.of(anioAnterior, 11, 24)      // 24 de nov del año anterior
                )
        );
    }

    private void gestionarSostenibilidad() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Permitir simular la fecha del registro
        System.out.print("Ingrese la fecha del registro (dd/MM/yyyy) o ENTER para usar hoy: ");
        String fechaStr = sc.nextLine().trim();

        LocalDate hoy;
        if (fechaStr.isEmpty()) {
            hoy = LocalDate.now();
        } else {
            hoy = LocalDate.parse(fechaStr, formato);
        }

        System.out.println("\n--- REGISTRO DIARIO DE SOSTENIBILIDAD (" + hoy.format(formato) + ") ---");
        System.out.println("Marque las acciones que realizó hoy (ingrese los números separados por coma, ej: 1, 3):");
        System.out.println();
        System.out.println("1. Usé transporte público, bicicleta o caminé.");
        System.out.println("2. No realicé impresiones.");
        System.out.println("3. No utilicé envases descartables (usé mi termo/taza).");
        System.out.println("4. Separé y reciclé materiales (vidrio, plástico, papel).");
        System.out.print("\nIngrese sus selecciones: ");

        String entrada = sc.nextLine();
        if (entrada.trim().isEmpty()) {
            System.out.println("No se ingreso ninguna opcion. Volviendo al menu...");
            return;
        }

        String[] seleccion = entrada.split(",");

        int puntos = 0;
        ArrayList<AccionSostenible> accionesHoy = new ArrayList<>();

        for (String opcionStr : seleccion) {
            try {
                int opcion = Integer.parseInt(opcionStr.trim());
                AccionSostenible nuevaAccion = crearAccionDesdeOpcion(opcion, hoy);
                if (nuevaAccion == null) {
                    System.out.println("Opcion no valida: " + opcion);
                    continue;
                }
                accionesHoy.add(nuevaAccion);
                puntos += nuevaAccion.getPuntosSostenibilidad();
            } catch (NumberFormatException e) {
                System.out.println("Opcion no valida: " + opcionStr.trim());
            }
        }

        registroSostenibilidad.registrarAcciones(accionesHoy);

        System.out.println("----------------------------------------");
        System.out.println("Acciones de sostenibilidad registradas:");
        for (AccionSostenible a : accionesHoy) {
            System.out.println("- " + a.getAccion());
        }

        System.out.println();
        System.out.println("¡Excelente contribución al planeta hoy!");
        System.out.println("Puntos de Sostenibilidad Acumulados: +" + puntos);
        System.out.println("Presione [ENTER] para ver el resumen semanal");
        sc.nextLine();

        registroSostenibilidad.mostrarEstadisticas();

        System.out.println("Presione [ENTER] para volver al menu de Sostenibilidad...");
        sc.nextLine();
    }


    private AccionSostenible crearAccionDesdeOpcion(int opcion, LocalDate fecha) {
        switch (opcion) {
            case 1:
                return new AccionSostenible("Usé transporte público, bicicleta o caminé.", 1, "¡Gran Movilidad!", fecha);
            case 2:
                return new AccionSostenible("No realicé impresiones.", 1, "Excelente", fecha);
            case 3:
                return new AccionSostenible("No utilicé envases descartables (usé mi termo/taza).", 1, "Necesita mejorar", fecha);
            case 4:
                return new AccionSostenible("Separé y reciclé materiales (vidrio, plástico, papel).", 1, "Muy bien", fecha);
            default:
                return null;
        }
    }

    private List<AccionSostenible> crearAccionesDesdeOpciones(List<Integer> opciones, LocalDate fecha) {
        List<AccionSostenible> acciones = new ArrayList<>();
        if (opciones == null) {
            return acciones;
        }
        for (Integer opcion : opciones) {
            AccionSostenible accion = crearAccionDesdeOpcion(opcion, fecha);
            if (accion != null) {
                acciones.add(accion);
            }
        }
        return acciones;
    }

    public void registrarAccion(String accion, int puntos, int vecesRealizada, String logro) {
        AccionSostenible nuevaAccion = new AccionSostenible(accion, puntos, logro, LocalDate.now());
        nuevaAccion.setVecesRealizada(vecesRealizada);
        registroSostenibilidad.registrarAccion(nuevaAccion);
        vista4.mostrarConfirmacion(nuevaAccion);
    }

    public void mostrarAcciones() {
        registroSostenibilidad.mostrarAcciones();
    }

    public void mostrarEstadisticas() {
        registroSostenibilidad.mostrarEstadisticas();
    }

    //Todo lo relacionado con Hidratacion
    //verificador de entero
    private Integer leerEntero() {
        if (!sc.hasNextInt()) {
            sc.nextLine();
            System.out.println("Valor inválido. Ingrese un número entero.");
            return null;
        }
        int vlor = sc.nextInt();
        sc.nextLine();
        return vlor;
    }

    private void c3_registrarIngesta() {
        vista3.registrarAgua();
        System.out.print("Ingrese la cantidad de agua que ha tomado (en **mililitros**): ");
        Integer cantidad= leerEntero();
        int antes= hidratacion.getAcumuladoHoy();
        if (cantidad == null) {
            return;
        }
        if(hidratacion.registrarHidratacion(cantidad)){
            int totalHoy = hidratacion.getAcumuladoHoy();
            int meta = hidratacion.getMetaDiaria();
            int porcentaje = (int) Math.round(hidratacion.getProgreso());

            System.out.println();
            System.out.println("Registro de " + cantidad + " ml añadido.\n");
            System.out.println("--- PROGRESO RÁPIDO ---");
            System.out.println("Meta Diaria: " + meta + " ml");
            System.out.println("Acumulado Hoy: " + totalHoy + " ml (Antes eran " + antes + " ml)");
            vista3.barrita(porcentaje, meta, totalHoy);

            System.out.println("\nPresione [ENTER] para continuar...");
            sc.nextLine();
        }
        else {
            System.out.println("La cantidad debe ser mayor que 0 ml.");
            System.out.println("Presione [ENTER] para continuar...");
            sc.nextLine();
        }
    }
    private void c3_establecerMeta() {
        int metaActual = hidratacion.getMetaDiaria();

        vista3.estableMeta();
        System.out.println("Meta diaria actual: " + metaActual + " ml.\n");
        System.out.print("Ingrese la nueva meta diaria de hidratación (en mililitros): ");

        int nuevaMeta = leerEntero();

        System.out.print("\n¿Confirma que la nueva meta diaria es " + nuevaMeta + " ml? (S/N): ");
        String resp = sc.nextLine().trim().toUpperCase();

        if (!resp.equals("S")) {
            System.out.println("\nNo se modificó la meta diaria.");
            System.out.println("Presione [ENTER] para volver al menú...");
            sc.nextLine();
            return;
        }

        if (hidratacion.establecerMetaDiaria(nuevaMeta)) {
            int totalHoy = hidratacion.getAcumuladoHoy();
            int porcentaje = (int) Math.round(hidratacion.getProgreso());

            System.out.println("\nMeta diaria de hidratación actualizada a " + nuevaMeta + " ml con éxito.\n");
            System.out.println("--- PROGRESO ACTUALIZADO ---");
            System.out.println("Acumulado Hoy: " + totalHoy + " ml");
            System.out.println("Nueva Meta: " + nuevaMeta + " ml");
            vista3.barrita(porcentaje, nuevaMeta, totalHoy);
        } else {
            System.out.println("\nLa meta debe ser mayor que 0 ml.");
        }

        System.out.println("\nPresione [ENTER] para volver al menú...");
        sc.nextLine();
    }
    private void c3_verProgreso() {
        int meta = hidratacion.getMetaDiaria();
        int totalHoy = hidratacion.getAcumuladoHoy();
        int falta = Math.max(meta - totalHoy, 0);
        int porcentaje = (int) Math.round(hidratacion.getProgreso());


        java.util.List<RegistroHidratacion> registrosHoy = hidratacion.getRegistrosHoy();

        vista3.mostrarProgresoDiario(java.time.LocalDate.now(),meta,totalHoy, falta,porcentaje,registrosHoy);
        sc.nextLine();
    }


    //Todo lo relacionado a Juego de Memoria

    private void jugarMemoria(){
        if(!juegoMemoriaInciado){
            juegoDeMemoria = new JuegoDeMemoria();
            inicializarCartasEnControlador();
            juegoMemoriaInciado = true;
        }

        int turno = 1;

        while(!juegoDeMemoria.isJuegoTerminado()){
            vista2.limpiarPantalla();
            vista2.mostrarInicio();
            vista2.mostrarTabla(juegoDeMemoria.getCartas());
            vista2.mostrarIntentos(juegoDeMemoria.getIntentos(), juegoDeMemoria.getParesEncontrados());
            vista2.mostrarInicioTurno(turno);

            //Primera seleccion
            int seleccion1 = solicitarSeleccionMemoria("PRIMERA");

            int seleccion2 = solicitarSeleccionMemoria("SEGUNDA");

            if (seleccion1 == seleccion2){
                vista2.mostrarErrorMismaCarta();
                juegoDeMemoria.getCartas().get(seleccion1).setDescubierta(false);
                juegoDeMemoria.setPrimeraCarta(null);
                esperarEnterMemoria();
                continue;
            }

            vista2.limpiarPantalla();
            vista2.mostrarInicio();
            vista2.mostrarVolteandoCartas();
            vista2.mostrarTabla(juegoDeMemoria.getCartas());
            vista2.mostrarIntentos(juegoDeMemoria.getIntentos(), juegoDeMemoria.getParesEncontrados());

            // Verificar pareja
            if (verificarParejaMemoria(seleccion1, seleccion2)) {
                vista2.mostrarParejaEncontrada(juegoDeMemoria.getCartas().get(seleccion1).getContenido(), seleccion1 + 1, seleccion2 + 1);
            } else {
                vista2.mostrarNoEsPareja(juegoDeMemoria.getCartas().get(seleccion1).getContenido(), juegoDeMemoria.getCartas().get(seleccion2).getContenido(), seleccion1 + 1, seleccion2 + 1);
                esperar(2000);
                voltearCartasNoEmparejadas();
            }

            vista2.mostrarPresioneEnter();
            esperarEnterMemoria();
            turno++;
        }

        // Mostrar fin del juego
        vista2.limpiarPantalla();
        vista2.mostrarInicio();
        vista2.mostrarTabla(juegoDeMemoria.getCartas());
        vista2.mostrarIntentos(juegoDeMemoria.getIntentos(), juegoDeMemoria.getParesEncontrados());
        vista2.mostrarJuegoTerminado(juegoDeMemoria.getIntentos());
        esperarEnterMemoria();

        // Reiniciar juego para próxima partida
        juegoMemoriaInciado =false;
    }

    private int solicitarSeleccionMemoria(String tipo) {
        while (true) {
            if (tipo.equals("PRIMERA")) {
                vista2.mostrarSolicitudPrimeraCarta();
            } else {
                vista2.mostrarSolicitudSegundaCarta();
            }

            try {
                int seleccion = sc.nextInt() - 1; // Convertir a índice 0-based

                if (seleccion < 0 || seleccion >= juegoDeMemoria.getTotalCartas()) {
                    vista2.mostrarErrorSeleccionInvalida();
                    continue;
                }

                Carta carta = juegoDeMemoria.getCartas().get(seleccion);
                if (carta.isDescubierta() || carta.isEmparejada()) {
                    vista2.mostrarErrorSeleccionInvalida();
                    continue;
                }

                carta.setDescubierta(true);
                if (tipo.equals("PRIMERA")){
                    juegoDeMemoria.setPrimeraCarta(carta);
                } else {
                    juegoDeMemoria.setSegundaCarta(carta);
                    juegoDeMemoria.setIntentos(juegoDeMemoria.getIntentos() + 1);
                }

                return seleccion;

            } catch (Exception e) {
                vista2.mostrarErrorSeleccionInvalida();
                sc.nextLine(); // Limpiar buffer
            }
        }
    }


    private boolean verificarParejaMemoria(int indice1, int indice2) {
        Carta carta1 = juegoDeMemoria.getCartas().get(indice1);
        Carta carta2 = juegoDeMemoria.getCartas().get(indice2);

        if (carta1.getContenido().equals(carta2.getContenido()) && carta1.getId() != carta2.getId()) {
            // Son pareja
            carta1.setEmparejada(true);
            carta2.setEmparejada(true);
            juegoDeMemoria.setParesEncontrados(juegoDeMemoria.getParesEncontrados() + 1);

            if (juegoDeMemoria.getParesEncontrados() == JuegoDeMemoria.getTOTAL_PARES()) {
                juegoDeMemoria.setJuegoTerminado(true);
            }
            return true;
        }
        return false;
    }


    private void voltearCartasNoEmparejadas() {
        if (juegoDeMemoria.getPrimeraCarta() != null && !juegoDeMemoria.getPrimeraCarta().isEmparejada()) {
            juegoDeMemoria.getPrimeraCarta().setDescubierta(false);
        }
        if (juegoDeMemoria.getSegundaCarta() != null && !juegoDeMemoria.getSegundaCarta().isEmparejada()) {
            juegoDeMemoria.getSegundaCarta().setDescubierta(false);
        }
        juegoDeMemoria.setPrimeraCarta(null);
        juegoDeMemoria.setSegundaCarta(null);
    }


    private void esperarEnterMemoria() {
        sc.nextLine(); // Limpiar buffer anterior
        sc.nextLine(); // Esperar Enter
    }


    private void esperar(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void inicializarCartasEnControlador() {
        ArrayList<Carta> cartas = new ArrayList<>();
        String[] valoresEcologicos = {"RECIC", "BICI", "AGUA", "SOL", "ARBOl", "HOJA", "TIERr", "VIEN"};
        int id = 0;

        // Crear 8 pares de cartas
        for(String valor : valoresEcologicos) {
            cartas.add(new Carta(id++, valor));
            cartas.add(new Carta(id++, valor));
        }

        // Mezclar las cartas
        Collections.shuffle(cartas);

        // Asignar al modelo
        juegoDeMemoria.setCartas(cartas);
    }

}
*/