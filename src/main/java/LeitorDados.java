import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorDados {

    private List<String> lerCSV(String caminho) {
        List<String> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linhas;
    }

    public Loja[] lerLojas(String caminhoCapacidade, String caminhoDemanda, String caminhoCusto) {
        Loja[] lojas = new Loja[10];
        List<String> linhasCap = lerCSV(caminhoCapacidade);
        List<String> linhasDemanda = lerCSV(caminhoDemanda);
        List<String> linhasCusto = lerCSV(caminhoCusto);

        // Processar a matriz de demanda: ignorar cabeçalho e primeira coluna
        int numLojas = linhasDemanda.get(0).split(",").length - 1; // assumindo primeira linha como cabeçalho
        int numProdutos = linhasDemanda.size() - 1; // ignorar cabeçalho

        int[][] demandasPorLoja = new int[numLojas][numProdutos];

        for (int i = 1; i <= numProdutos; i++) {
            String[] tokens = linhasDemanda.get(i).split(",");
            for (int j = 1; j < tokens.length; j++) {
                demandasPorLoja[j - 1][i - 1] = Integer.parseInt(tokens[j]);
            }
        }

        for (int i = 1; i <= 10; i++) {
            int capacidadeMaxima = Integer.parseInt(linhasCap.get(i).split(",")[1]);

            int[] demanda = demandasPorLoja[i - 1]; // já está na forma correta por loja

            String[] custoTokens = linhasCusto.get(i).split(",");
            int custo = Integer.parseInt(custoTokens[1]);

            lojas[i - 1] = new Loja(capacidadeMaxima, demanda, custo);
        }

        return lojas;
    }


    public CentroDistribuicao lerCentroDistribuicao(String caminhoEstoque) {
        List<String> linhas = lerCSV(caminhoEstoque);
        int[] estoque = new int[linhas.size() - 1];
        for (int i = 1; i < linhas.size(); i++) {
            String[] partes = linhas.get(i).split(",");
            estoque[i - 1] = Integer.parseInt(partes[1]);
        }
        return new CentroDistribuicao(estoque);
    }
}
