public class Loja {
    int capacidadeMaxima;
    int[] demandaPorProduto;
    int custoViagem;

    public Loja(int capacidadeMaxima, int[] demandaPorProduto, int custoViagem) {
        this.capacidadeMaxima = capacidadeMaxima;
        this.demandaPorProduto = demandaPorProduto;
        this.custoViagem = custoViagem;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public int[] getDemandaPorProduto() {
        return demandaPorProduto;
    }

    public int getCustoViagem() {
        return custoViagem;
    }
}
