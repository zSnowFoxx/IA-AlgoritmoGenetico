import java.net.URL;

public class LeitorTest {
    public static void main(String[] args) {
        LeitorDados leitor = new LeitorDados();

        String caminhoCapacidade = getPathFromResources("capacidade_lojas.csv");
        String caminhoDemanda = getPathFromResources("demanda.csv");
        String caminhoCusto = getPathFromResources("custo_por_caminhao.csv");
        String caminhoEstoque = getPathFromResources("estoque_cd.csv");

        Loja[] lojas = leitor.lerLojas(caminhoCapacidade, caminhoDemanda, caminhoCusto);
        CentroDistribuicao centro = leitor.lerCentroDistribuicao(caminhoEstoque);

        System.out.println("==== LOJAS ====");
        for (int i = 0; i < lojas.length; i++) {
            Loja loja = lojas[i];
            System.out.println("Loja " + (i + 1));
            System.out.println("Capacidade Máxima: " + loja.getCapacidadeMaxima());
            System.out.print("Demanda: ");
            for (int d : loja.getDemandaPorProduto()) {
                System.out.print(d + " ");
            }
            System.out.println("\nCusto: " + loja.getCustoViagem());
            System.out.println();
        }

        System.out.println("==== CENTRO DE DISTRIBUIÇÃO ====");
        System.out.print("Estoque: ");
        for (int e : centro.getEstoqueDisponivelPorProduto()) {
            System.out.print(e + " ");
        }
    }

    private static String getPathFromResources(String fileName) {
        URL resource = LeitorTest.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("Arquivo não encontrado: " + fileName);
        }
        return resource.getPath();
    }
}
