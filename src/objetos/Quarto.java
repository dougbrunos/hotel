package objetos;

public class Quarto {

    public int numero;
    public String tipo;
    public double preco;
    public boolean disponivel;

    public Quarto(int numero, String tipo, double preco) {
        this.numero = numero;
        this.tipo = tipo;
        this.preco = preco;
        this.disponivel = true;
    }

    @Override
    public String toString() {
        return "\nNúmero: " + numero + " | Tipo: " + tipo + " | Preço: R$" + preco + " | Disponível: " + disponivel;
    }
}