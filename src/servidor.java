import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.List;

public class servidor {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("C:/Users/Starlin/Proyectos DEV/Interfaz de cobrador de servicios", Location.EXTERNAL);
            config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()));
        }).start(7070);

        app.post("/api/calcular", ctx -> {
            DatosEntrada entrada = ctx.bodyAsClass(DatosEntrada.class);
            DatosSalida salida = procesarCobro(entrada);
            ctx.json(salida);
        });

        System.out.println(">>> Servidor Java corriendo en http://localhost:7070 <<<");
    }

    public static DatosSalida procesarCobro(DatosEntrada entrada) {
        double totalGeneral = 0.0;

        if (entrada.clientes != null && !entrada.clientes.isEmpty()) {
            for (Cliente c : entrada.clientes) {
                totalGeneral += calcularTotalCliente(c, entrada.dias);
            }
        }

        int numClientes = (entrada.clientes != null && !entrada.clientes.isEmpty())
                ? entrada.clientes.size()
                : 1;

        double pagoPromedioPorPersona = totalGeneral / numClientes;
        double adelantoTotal = totalGeneral * 0.40;
        double adelantoPorPersona = adelantoTotal / numClientes;

        return new DatosSalida(totalGeneral, pagoPromedioPorPersona, adelantoTotal, adelantoPorPersona);
    }

    private static double calcularTotalCliente(Cliente cliente, int dias) {
        double subtotal = 0.0;

        if (cliente.serviciosSeleccionados != null) {
            for (String s : cliente.serviciosSeleccionados) {
                subtotal += obtenerPrecio(s) * dias;
            }
        }

        // Sumar el extra personalizado ingresado
        subtotal += cliente.montoExtra * dias;

        // Guía turístico obligatorio ($600/día)
        if (cliente.serviciosSeleccionados == null || !cliente.serviciosSeleccionados.contains("Guía turístico")) {
            subtotal += 600.0 * dias;
        }

        return subtotal;
    }

    private static double obtenerPrecio(String nombre) {
        return switch (nombre) {
            case "Desayuno" -> 350.0;
            case "Almuerzo" -> 500.0;
            case "Cena" -> 300.0;
            case "Caballo" -> 1000.0;
            case "Habitacion simple" -> 2000.0;
            case "Habitacion doble" -> 3000.0;
            case "Habitacion familiar" -> 5000.0;
            default -> 0.0;
        };
    }

    public static class Cliente {
        public List<String> serviciosSeleccionados;
        public double montoExtra; // Campo para recibir el monto customizado
    }

    public static class DatosEntrada {
        public int dias;
        public List<Cliente> clientes;
    }

    public static class DatosSalida {
        public double totalGeneral;
        public double pagoPorPersona;
        public double adelantoTotal;
        public double adelantoPorPersona;

        public DatosSalida(double tg, double pp, double at, double ap) {
            this.totalGeneral = tg;
            this.pagoPorPersona = pp;
            this.adelantoTotal = at;
            this.adelantoPorPersona = ap;
        }
    }
}