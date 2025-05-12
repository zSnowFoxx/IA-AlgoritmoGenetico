import java.net.URL;

public class Config {
    // Instâncias globais para armazenar os dados
    public static Loja[] lojas;
    public static CentroDistribuicao centroDistribuicao;

    // Caminhos dos arquivos
    private static String caminhoCapacidade;
    private static String caminhoDemanda;
    private static String caminhoCusto;
    private static String caminhoEstoque;

    // Metodo para inicializar os dados
    public static void inicializar() {
        // Inicializa os caminhos
        caminhoCapacidade = getPathFromResources("capacidade_lojas.csv");
        caminhoDemanda = getPathFromResources("demanda.csv");
        caminhoCusto = getPathFromResources("custo_por_caminhao.csv");
        caminhoEstoque = getPathFromResources("estoque_cd.csv");

        // Leitura dos arquivos CSV
        LeitorDados leitor = new LeitorDados();

        // Carregando dados
        lojas = leitor.lerLojas(caminhoCapacidade, caminhoDemanda, caminhoCusto);
        centroDistribuicao = leitor.lerCentroDistribuicao(caminhoEstoque);
    }

    // Metodo para obter o caminho dos arquivos dentro dos recursos
    public static String getPathFromResources(String fileName) {
        URL resource = Config.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("Arquivo não encontrado: " + fileName);
        }
        return resource.getPath();
    }
}
