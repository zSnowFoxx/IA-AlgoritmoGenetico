import java.util.Random;


public class Cromossomo {
    // Constantes do problema
    private static final int NUM_LOJAS = 10;                  // Número total de lojas
    private static final int NUM_PRODUTOS = 50;               // Número total de produtos
    private static final int VIAGENS_POR_LOJA = 10;           // Máximo de viagens por loja
    private static final int TAM_CROMOSSOMO = NUM_LOJAS * VIAGENS_POR_LOJA * NUM_PRODUTOS; // Total de genes
    private static final int PESO_ALTO = 3;                   // Peso para penalidades mais graves
    private static final int PESO_MEDIO = 2;                  // Peso intermediário
    private static final int PESO_BAIXO = 1;                  // Peso para penalidades leves

    private int[] genes;      // Vetor que representa as quantidades enviadas de produtos
    private double fitness;   // Valor da aptidão (fitness) do cromossomo

    public Cromossomo(int[] genes) {
        this.genes = genes;
        calcularFitness();
    }

    public Cromossomo() {
        this.genes = new int[TAM_CROMOSSOMO];
        gerarGenesAleatorios();
        calcularFitness();
    }

    private void gerarGenesAleatorios() {
        int[] estoqueDisponivel = Config.centroDistribuicao.getEstoqueDisponivelPorProduto().clone();
        Random rand = new Random();

        int[] indicesLojas = new int[NUM_LOJAS];
        for (int i = 0; i < NUM_LOJAS; i++) {
            indicesLojas[i] = i;
        }
        for (int i = NUM_LOJAS - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = indicesLojas[i];
            indicesLojas[i] = indicesLojas[j];
            indicesLojas[j] = temp;
        }

        for (int l = 0; l < NUM_LOJAS; l++) {
            int lojaIdx = indicesLojas[l];
            Loja loja = Config.lojas[lojaIdx];

            // Inserir chance de loja ruim
            boolean lojaRuim = rand.nextDouble() < 0.2; // 20% de chance

            int[] demandaLoja;
            int capacidadeRestante;

            if (lojaRuim) {
                // Capacidade muito baixa ou muito alta
                // Capacidade inconsistente para loja ruim
                capacidadeRestante = rand.nextBoolean()
                        ? 1000 + rand.nextInt(2000)  // 1000 a 3000
                        : 12000 + rand.nextInt(8000); // 12000 a 20000


                // Demanda inconsistente (zero ou muito alta)
                demandaLoja = new int[NUM_PRODUTOS];
                for (int d = 0; d < NUM_PRODUTOS; d++) {
                    demandaLoja[d] = rand.nextBoolean()
                            ? 0
                            : 500 + rand.nextInt(1501); // 500 a 2000
                }
            } else {
                // Dados reais da loja
                demandaLoja = loja.getDemandaPorProduto().clone();
                capacidadeRestante = loja.getCapacidadeMaxima();
            }

            for (int viagem = 0; viagem < VIAGENS_POR_LOJA; viagem++) {
                int totalViagem = 0;

                int[] indicesProdutos = new int[NUM_PRODUTOS];
                for (int p = 0; p < NUM_PRODUTOS; p++) {
                    indicesProdutos[p] = p;
                }
                for (int p = NUM_PRODUTOS - 1; p > 0; p--) {
                    int j = rand.nextInt(p + 1);
                    int temp = indicesProdutos[p];
                    indicesProdutos[p] = indicesProdutos[j];
                    indicesProdutos[j] = temp;
                }

                for (int p = 0; p < NUM_PRODUTOS; p++) {
                    int produto = indicesProdutos[p];
                    int geneIndex = lojaIdx * 500 + viagem * 50 + produto;
                    int demandaRestante = demandaLoja[produto];

                    if (demandaRestante <= 0 || estoqueDisponivel[produto] <= 0) {
                        genes[geneIndex] = 0;
                        continue;
                    }

                    int maxEnviar = Math.min(estoqueDisponivel[produto], demandaRestante);
                    maxEnviar = Math.min(maxEnviar, 1000 - totalViagem);
                    maxEnviar = Math.min(maxEnviar, capacidadeRestante);
                    maxEnviar = (maxEnviar / 20) * 20;

                    int enviar = (maxEnviar >= 20) ? rand.nextInt((maxEnviar / 20) + 1) * 20 : 0;

                    genes[geneIndex] = enviar;

                    estoqueDisponivel[produto] -= enviar;
                    demandaLoja[produto] -= enviar;
                    totalViagem += enviar;
                    capacidadeRestante -= enviar;

                    if (totalViagem >= 1000) break;
                }
            }
        }
    }


    /**
     * Avalia a aptidão (fitness) do cromossomo com base em penalidades e custos.
     */
    public void calcularFitness() {
        int penalidadeTotal = 0;
        double custoTotal = 0.0;

        // 1. Penalidade por atender menos ou mais do que a demanda
        for (int lojaIdx = 0; lojaIdx < NUM_LOJAS; lojaIdx++) {
            int[] recebidosPorProduto = new int[NUM_PRODUTOS];

            for (int viagem = 0; viagem < VIAGENS_POR_LOJA; viagem++) {
                int base = lojaIdx * VIAGENS_POR_LOJA * NUM_PRODUTOS + viagem * NUM_PRODUTOS;
                for (int produto = 0; produto < NUM_PRODUTOS; produto++) {
                    recebidosPorProduto[produto] += genes[base + produto];
                }
            }

            int[] demanda = Config.lojas[lojaIdx].getDemandaPorProduto();

            for (int produto = 0; produto < NUM_PRODUTOS; produto++) {
                int diferenca = recebidosPorProduto[produto] - demanda[produto];
                int penalidade = penalidadeSimples(Math.abs(diferenca), diferenca > 0 ? PESO_MEDIO : PESO_BAIXO);
                if (penalidade > 0) {
                    //System.out.printf("Penalidade 1 [Loja %d, Produto %d]: Diferenca=%d, Penalidade=%d%n",lojaIdx, produto, diferenca, penalidade);
                }
                penalidadeTotal += penalidade;
            }
        }

        // 2. Penalidade por excesso de envio acima do estoque disponível
        int[] enviadosPorProduto = new int[NUM_PRODUTOS];
        for (int i = 0; i < genes.length; i++) {
            int produto = i % NUM_PRODUTOS;
            enviadosPorProduto[produto] += genes[i];
        }

        int[] estoque = Config.centroDistribuicao.getEstoqueDisponivelPorProduto();
        for (int produto = 0; produto < NUM_PRODUTOS; produto++) {
            int excesso = enviadosPorProduto[produto] - estoque[produto];
            if (excesso > 0) {
                int penalidade = penalidadeSimples(excesso, PESO_ALTO);
                //System.out.printf("Penalidade 2 [Produto %d]: Excesso=%d, Penalidade=%d%n", produto, excesso, penalidade);
                penalidadeTotal += penalidade;
            }
        }

        // 3. Penalidade por exceder a capacidade total da loja
        for (int lojaIdx = 0; lojaIdx < NUM_LOJAS; lojaIdx++) {
            int totalRecebido = 0;
            for (int viagem = 0; viagem < VIAGENS_POR_LOJA; viagem++) {
                int base = lojaIdx * VIAGENS_POR_LOJA * NUM_PRODUTOS + viagem * NUM_PRODUTOS;
                for (int produto = 0; produto < NUM_PRODUTOS; produto++) {
                    totalRecebido += genes[base + produto];
                }
            }

            int excesso = totalRecebido - Config.lojas[lojaIdx].getCapacidadeMaxima();
            if (excesso > 0) {
                int penalidade = penalidadeSimples(excesso, PESO_ALTO);
                //System.out.printf("Penalidade 3 [Loja %d]: Excesso total recebido=%d, Penalidade=%d%n", lojaIdx, excesso, penalidade);
                penalidadeTotal += penalidade;
            }
        }

        // 4. Penalidade por viagens pouco eficientes (com carga entre 1 e 599)
        for (int lojaIdx = 0; lojaIdx < NUM_LOJAS; lojaIdx++) {
            double custoViagemLoja = Config.lojas[lojaIdx].getCustoViagem();

            for (int viagem = 0; viagem < VIAGENS_POR_LOJA; viagem++) {
                int base = lojaIdx * VIAGENS_POR_LOJA * NUM_PRODUTOS + viagem * NUM_PRODUTOS;
                int carga = 0;
                for (int produto = 0; produto < NUM_PRODUTOS; produto++) {
                    carga += genes[base + produto];
                }

                if (carga > 0 && carga < 600) {
                    int deficit = (600 - carga) / 20;
                    int penalidade = penalidadeSimples(deficit, PESO_MEDIO);
                    //System.out.printf("Penalidade 4 [Loja %d, Viagem %d]: Carga=%d, Deficit=%d, Penalidade=%d%n", lojaIdx, viagem, carga, deficit, penalidade);
                    penalidadeTotal += penalidade;
                }

                custoTotal += custoViagemLoja * (carga > 0 ? 1 : 0);
            }
        }

        int penalidadeMaxima = 200000;
        double custoPeso = 1.0;
        double penalidadeComCusto = penalidadeTotal + (custoTotal * custoPeso);

        this.fitness = Math.max(0.0, 100.0 - ((penalidadeComCusto * 100.0) / penalidadeMaxima));

        //System.out.printf("Fitness final calculado: %.2f (Penalidade Total: %d, Custo Total: %.2f)%n", this.fitness, penalidadeTotal, custoTotal);
    }

    private int penalidadeSimples(int valor, int peso) {
        return valor * peso;
    }

    public double getFitness() {
        return fitness;
    }

    public int[] getGenes() {
        return genes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Genes (por viagem):\n");

        for (int lojaIdx = 0; lojaIdx < NUM_LOJAS; lojaIdx++) {
            sb.append("Loja ").append(lojaIdx).append(":\n");
            for (int viagem = 0; viagem < VIAGENS_POR_LOJA; viagem++) {
                int baseIndex = lojaIdx * VIAGENS_POR_LOJA * NUM_PRODUTOS + viagem * NUM_PRODUTOS;
                sb.append("  Viagem ").append(viagem).append(": ");
                for (int produto = 0; produto < NUM_PRODUTOS; produto++) {
                    sb.append(genes[baseIndex + produto]).append(" ");
                }
                sb.append("\n");
            }
        }

        sb.append("Fitness: ").append(fitness);
        return sb.toString();
    }
}
