package pl.javasolutions.apps;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import pl.javasolutions.apps.repository.BicycleRepository;
import pl.javasolutions.apps.service.mechanic.CreateMechanicCommand;
import pl.javasolutions.apps.service.mechanic.Mechanic;
import pl.javasolutions.apps.service.mechanic.MechanicService;
import pl.javasolutions.apps.service.repair.order.CreateRepairOrderCommand;
import pl.javasolutions.apps.service.repair.order.RepairOrder;
import pl.javasolutions.apps.service.repair.order.RepairOrderService;
import pl.javasolutions.apps.service.repair.order.RepairOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class BikeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeServiceApplication.class, args);
    }

     @Bean
     @Profile("!test")
     CommandLineRunner testData(MechanicService mechanicService,
                                RepairOrderService orderService,
                                BicycleRepository bicycleRepo) {
         return args -> {
             System.out.println("=== Mechanicy (przed dodaniem) ===");
             mechanicService.findAll().forEach(m ->
                 System.out.println(m.firstName() + " " + m.lastName()));

             System.out.println("=== Mechanicy (dodanie) ===");
             // --- Nowy mechanik ---
             Mechanic nowyMechanik = mechanicService.save(new CreateMechanicCommand(
                     "Tomasz",
                     "Kowalski",
                     "Naprawy elektryczne",
                     LocalDate.of(2026, 1, 15),
                     "tomasz.kowalski@bikeservice.pl"
             ));
             System.out.println("=== Dodano mechanika: " + nowyMechanik.firstName() + " " + nowyMechanik.lastName()
                     + " (id=" + nowyMechanik.id() + ")");

             System.out.println("=== Zlecenie (dodanie) ===");
             // Zakładamy, że rower o id=1 istnieje (wstawiony przez Liquibase)
             RepairOrder noweZlecenie = orderService.save(new CreateRepairOrderCommand(
                     "Wymiana opon i regulacja hamulców",
                     1L,
                     nowyMechanik.id(),
                     new BigDecimal("250.00")
             ));
             System.out.println("=== Dodano zlecenie: " + noweZlecenie.description()
                     + " [" + noweZlecenie.status() + "] (id=" + noweZlecenie.id() + ")");

             System.out.println("=== Wszystkie zlecenia ===");
             orderService.findAll().forEach(o ->
                 System.out.println(o.description() + " [" + o.status() + "]"));

             System.out.println("=== Liczba rowerow: " + bicycleRepo.count());

             System.out.println("=== Update statusu zlecenia id=" + noweZlecenie.id() + " na IN_PROGRESS ===");
             orderService.updateStatus(noweZlecenie.id(), RepairOrderStatus.IN_PROGRESS);

             System.out.println("=== Zlecenia po aktualizacji ===");
             orderService.findAll().forEach(o ->
                     System.out.println(o.description() + " [" + o.status() + "]"));

         };
     }
}
