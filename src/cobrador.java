import java.util.*;

public class cobrador {
    static final Map<Integer, Servicio> SERVICIOS = new LinkedHashMap<>();
    static final int GUIA_KEY;
    static {
        SERVICIOS.put(1, new Servicio("Desayuno", 350));
        SERVICIOS.put(2, new Servicio("Almuerzo", 500));
        SERVICIOS.put(3, new Servicio("Cena", 300));
        SERVICIOS.put(4, new Servicio("Caballo", 1000));
        SERVICIOS.put(5, new Servicio("Guía turístico", 600)); // obligatorio
        SERVICIOS.put(6, new Servicio("Habitación simple", 2000));
        SERVICIOS.put(7, new Servicio("Habitación doble", 3000));
        SERVICIOS.put(8, new Servicio("Habitación familiar", 5000));
        int guia = -1;
        for (Map.Entry<Integer, Servicio> e : SERVICIOS.entrySet()) {
            String n = e.getValue().nombre.toLowerCase();
            if (n.contains("guía") || n.contains("guia")) { guia = e.getKey(); break; }
        }
        GUIA_KEY = guia;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Número de clientes: ");
        int nClientes = readInt(sc, 1);

        List<Double> totales = new ArrayList<>();
        Set<Integer> plantillaSeleccion = null;
        boolean usarPlantillaParaRestantes = false;

        for (int i = 1; i <= nClientes; i++) {
            System.out.println("\n--- Cliente " + i + " ---");
            System.out.print("¿Cuántos días tomará el servicio? ");
            int dias = readInt(sc, 1);

            Set<Integer> seleccion = new LinkedHashSet<>();

            if (usarPlantillaParaRestantes && plantillaSeleccion != null) {
                seleccion.addAll(plantillaSeleccion);
                System.out.println("Se aplicó la selección previa a este cliente.");
            } else {
                // Servicios generales (sin guía y sin tipos de habitación)
                System.out.println("Responde 's' para agregar cada servicio:");
                for (Map.Entry<Integer, Servicio> e : SERVICIOS.entrySet()) {
                    int key = e.getKey();
                    Servicio s = e.getValue();
                    if (key == GUIA_KEY || isRoomKey(key)) continue;
                    System.out.printf("%d) %s ($%.2f por día)%n", key, s.nombre, s.precio);
                }
                for (Map.Entry<Integer, Servicio> e : SERVICIOS.entrySet()) {
                    int key = e.getKey();
                    Servicio s = e.getValue();
                    if (key == GUIA_KEY || isRoomKey(key)) continue;
                    System.out.print("Agregar " + s.nombre + "? (s/n): ");
                    if (readYesNo(sc)) {
                        seleccion.add(key);
                    }
                }

                // Preguntar sobre habitación (si quiere o no)
                System.out.print("¿Desea habitación? (s/n): ");
                boolean quiereHabitacion = readYesNo(sc);
                if (quiereHabitacion) {
                    System.out.println("Elija tipo de habitación:");
                    System.out.println("1) Habitacion simple");
                    System.out.println("2) Habitacion doble");
                    System.out.println("3) Habitacion familiar");
                    System.out.print("Ingrese 1/2/3: ");
                    int tipo = readIntInRange(sc, 1, 3);
                    int roomKey = switch (tipo) {
                        case 1 -> 6;
                        case 2 -> 7;
                        default -> 8;
                    };
                    seleccion.add(roomKey);
                } else {
                    System.out.println("No se agregará habitación para este cliente.");
                }
            }

            // Añadir guía obligatorio siempre
            if (GUIA_KEY != -1) {
                seleccion.add(GUIA_KEY);
                System.out.println("Guía turístico (obligatorio) incluido.");
            }

            // Ofrecer aplicar la selección a los siguientes (plantilla) si no existe plantilla aún
            if (plantillaSeleccion == null && i < nClientes) {
                System.out.print("¿Aplicar la selección de este cliente a los siguientes clientes? (s/n): ");
                if (readYesNo(sc)) {
                    plantillaSeleccion = new LinkedHashSet<>(seleccion);
                    usarPlantillaParaRestantes = true;
                    System.out.println("La selección será aplicada a los clientes restantes.");
                }
            }

            double subtotal = 0.0;
            for (Integer key : seleccion) {
                Servicio s = SERVICIOS.get(key);
                subtotal += s.precio * dias;
            }

            totales.add(subtotal);
            System.out.printf("Total Cliente %d: $%.2f%n", i, subtotal);
        }

        double suma = totales.stream().mapToDouble(Double::doubleValue).sum();
        double pagoPorPersona = suma / nClientes;
        double adelantoTotal = suma * 0.40;
        double adelantoPorPersona = adelantoTotal / nClientes;

        System.out.println("\n--- RESUMEN ---");
        System.out.printf("TOTAL GENERAL para %d clientes: $%.2f%n", nClientes, suma);
        System.out.printf("Pago por persona (promedio): $%.2f%n", pagoPorPersona);
        System.out.printf("Adelanto (40%% del total): $%.2f%n", adelantoTotal);
        System.out.printf("Adelanto por persona: $%.2f%n", adelantoPorPersona);

        sc.close();
    }

    static boolean isRoomKey(int key) {
        return key == 6 || key == 7 || key == 8;
    }

    static int readInt(Scanner sc, int min) {
        while (true) {
            String line = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min) {
                    System.out.print("Valor inválido, debe ser >= " + min + ". Intenta de nuevo: ");
                    continue;
                }
                return v;
            } catch (NumberFormatException ex) {
                System.out.print("Número inválido, intenta de nuevo: ");
            }
        }
    }

    static int readIntInRange(Scanner sc, int lo, int hi) {
        while (true) {
            String line = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < lo || v > hi) {
                    System.out.print("Valor fuera de rango. Intenta de nuevo: ");
                    continue;
                }
                return v;
            } catch (NumberFormatException ex) {
                System.out.print("Número inválido, intenta de nuevo: ");
            }
        }
    }

    static boolean readYesNo(Scanner sc) {
        String line = sc.nextLine().trim().toLowerCase();
        return line.equals("s") || line.equals("si") || line.equals("sí") || line.equals("y") || line.equals("yes");
    }

    static class Servicio {
        String nombre;
        double precio;
        Servicio(String n, double p) { nombre = n; precio = p; }
    }
}