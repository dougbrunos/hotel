package objetos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Reserva {

    public String hospede;
    public LocalDate dataEntrada;
    public LocalDate dataSaida;
    public List<Quarto> quartos;

    public Reserva(String hospede, LocalDate dataEntrada, LocalDate dataSaida, List<Quarto> quartos) {
        this.hospede = hospede;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.quartos = quartos;
    }

    public long calcularDias() {
        return ChronoUnit.DAYS.between(dataEntrada, dataSaida);
    }

    public double calcularPrecoTotal() {
        long dias = calcularDias();
        double total = 0;
        for (Quarto quarto : quartos) {
            total += quarto.preco * dias;
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nNome: ").append(hospede);
        sb.append("\nData de Entrada: ").append(dataEntrada);
        sb.append("\nData de Saída: ").append(dataSaida);
        sb.append("\nDias: ").append(calcularDias());
        sb.append("\nQuartos Reservados: ");
        for (Quarto quarto : quartos) {
            String status = quarto.disponivel ? "Disponível" : "Ocupado";
            sb.append("\nNúmero: " + quarto.numero + " | Tipo: " + quarto.tipo + " | Preço: R$" + quarto.preco + " | Status: " + status);
        }
        sb.append("\nTotal da Reserva: R$").append(calcularPrecoTotal());
        return sb.toString();
    }
}