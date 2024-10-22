import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import objetos.Quarto;
import objetos.Reserva;

public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static List<Quarto> quartos = new ArrayList<>();
    public static List<Reserva> reservas = new ArrayList<>();

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        System.out.println("\n-- Menu --\n" +
                "1 - Cadastrar Quarto\n" +
                "2 - Fazer uma Reserva\n" +
                "3 - Relatório de Ocupação\n" +
                "4 - Histórico de Reservas por Hóspede\n" +
                "5 - Check-in\n" +
                "6 - Check-out\n" +
                "7 - Sair\n");

        executar();
    }

    public static void executar() {
        System.out.print("Digite a opção desejada: ");
        int entrada = scanner.nextInt();
        scanner.nextLine();

        switch (entrada) {
            case 1:
                cadastrarQuarto();
                break;
            case 2:
                reservar();
                break;
            case 3:
                relatorioDeOcupacao();
                break;
            case 4:
                historicoDeReservas();
                break;
            case 5:
                checkIn();
                break;
            case 6:
                checkOut();
                break;
            case 7:
                System.out.println("Saindo...");
                break;
            default:
                System.out.println("Opção inválida!\n");
                break;
        }
        if (entrada != 7) {
            menu();
        }
    }

    public static void cadastrarQuarto() {
        System.out.println("\n-- Cadastro de Quarto --");
        System.out.print("Digite o número do quarto: ");
        int numero = scanner.nextInt();

        for (Quarto quartoVerificado : quartos) {
            if (numero == quartoVerificado.numero) {
                System.out.println("O quarto já existe!");
                return;
            }
        }

        String tipo = "";
        while (tipo.equals("")) {
            System.out.print("Digite o tipo do quarto: (1 - Solteiro, 2 - Casal, 3 - Suite): ");
            int escolha = scanner.nextInt();

            switch (escolha) {
                case 1:
                    tipo = "Solteiro";
                    break;
                case 2:
                    tipo = "Casal";
                    break;
                case 3:
                    tipo = "Suite";
                    break;
                default:
                    System.out.println("Opção inválida. Escolha 1, 2 ou 3.");
                    break;
            }
        }

        System.out.print("Digite o preço da diária do quarto: ");
        double preco = scanner.nextDouble();

        Quarto quarto = new Quarto(numero, tipo, preco);
        quartos.add(quarto);

        System.out.println("Quarto cadastrado com sucesso!");
    }

    public static void reservar() {
        System.out.println("\n-- Reserva de Quarto --");
        System.out.print("Digite o nome do hóspede: ");
        String nome = scanner.nextLine();

        LocalDate dataCheckin = lerData("Digite a data de check-in (dd/MM/yyyy): ");
        LocalDate dataCheckout = lerData("Digite a data de check-out (dd/MM/yyyy): ");

        if (dataCheckout.isBefore(dataCheckin)) {
            System.out.println("Erro: A data de check-out deve ser posterior à data de check-in.");
            return;
        }

        System.out.println("\n-- Lista de quartos disponíveis --");
        List<Quarto> quartosDisponiveis = new ArrayList<>();

        for (Quarto quarto : quartos) {
            if (quarto.disponivel) {
                System.out.print("Número: " + quarto.numero + " | Tipo: " + quarto.tipo + " | Preço: " + quarto.preco + "\n");
                quartosDisponiveis.add(quarto);
            }
        }

        if (quartosDisponiveis.isEmpty()) {
            System.out.println("Não há quartos disponíveis.");
            return;
        }

        List<Quarto> quartosReservados = new ArrayList<>();
        int entrada = 1;

        while (entrada != 0) {
            System.out.print("Digite o número do quarto que deseja reservar (Digite 0 para sair): ");
            entrada = scanner.nextInt();
            for (Quarto quarto : quartosDisponiveis) {
                if (entrada == quarto.numero && quarto.disponivel) {
                    quartosReservados.add(quarto);
                    System.out.println("Quarto " + quarto.numero + " reservado!");
                    break;
                }
            }
        }

        Reserva reserva = new Reserva(nome, dataCheckin, dataCheckout, quartosReservados);
        reservas.add(reserva);

        System.out.println("Reserva realizada com sucesso!");
    }

    public static void relatorioDeOcupacao() {
        System.out.println("\n-- Relatório de Ocupação de Quartos --");
        int quartosOcupados = 0;

        for (Quarto quarto : quartos) {
            String status = quarto.disponivel ? "Disponível" : "Ocupado";
            System.out.print("Número: " + quarto.numero + " | Tipo: " + quarto.tipo + " | Status: " + status);

            if (!quarto.disponivel) {
                quartosOcupados++;
                for (Reserva reserva : reservas) {
                    if (reserva.quartos.contains(quarto)) {
                        System.out.print(" | Ocupado de " + reserva.dataEntrada + " até " + reserva.dataSaida);
                        break;
                    }
                }
            }
            System.out.println();
        }

        System.out.println("\nTotal de quartos ocupados: " + quartosOcupados);
    }

    public static void historicoDeReservas() {
        System.out.print("Digite o nome do hóspede para ver o histórico: ");
        String nome = scanner.nextLine();

        System.out.println("\n-- Histórico de Reservas --");
        boolean encontrou = false;

        for (Reserva reserva : reservas) {
            if (reserva.hospede.equalsIgnoreCase(nome)) {
                System.out.println(reserva);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma reserva encontrada para o hóspede " + nome);
        }
    }

    public static void checkIn() {
        System.out.print("Digite o nome do hóspede para realizar o check-in: ");
        String nome = scanner.nextLine();

        for (Reserva reserva : reservas) {
            if (reserva.hospede.equalsIgnoreCase(nome)) {
                for (Quarto quarto : reserva.quartos) {
                    if (!quarto.disponivel) {
                        System.out.println("O quarto " + quarto.numero + " já está ocupado!");
                        return;
                    }
                    quarto.disponivel = false;
                }
                System.out.println("Check-in realizado para " + nome);
                return;
            }
        }

        System.out.println("Reserva não encontrada para o hóspede " + nome + " com check-in hoje.");
    }

    public static void checkOut() {
        System.out.print("Digite o nome do hóspede para realizar o check-out: ");
        String nome = scanner.nextLine();

        for (Reserva reserva : reservas) {
            if (reserva.hospede.equalsIgnoreCase(nome)) {
                for (Quarto quarto : reserva.quartos) {
                    quarto.disponivel = true;
                }
                System.out.println("Check-out realizado para " + nome);
                return;
            }
        }

        System.out.println("Reserva não encontrada para o hóspede " + nome);
    }

    public static LocalDate lerData(String mensagem) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = null;

        while (data == null) {
            System.out.print(mensagem);
            String input = scanner.nextLine();
            try {
                data = LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use dd/MM/yyyy.");
            }
        }

        return data;
    }
}