import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AlgoritmoGenetico {
    private int tamanhoPopulacao;
    private int numeroGeracoes;
    private double taxaMutacao;
    private int numCortes;

    public AlgoritmoGenetico(int tamanhoPopulacao, int numeroGeracoes, double taxaMutacao, int numCortes) {
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.numeroGeracoes = numeroGeracoes;
        this.taxaMutacao = taxaMutacao;
        this.numCortes = numCortes;
    }

    public AlgoritmoGenetico() {
        this.tamanhoPopulacao = 50;
        this.numeroGeracoes = 100;
        this.taxaMutacao = 0.25;
        this.numCortes = 10;
    }

    // Executa o loop evolutivo
    public void executar() {
        List<Cromossomo> populacao = new ArrayList<>();

        // Inicializa população
        for (int i = 0; i < tamanhoPopulacao; i++) {
            populacao.add(new Cromossomo());
        }

        // Evolui por N gerações
        for (int geracao = 0; geracao < numeroGeracoes; geracao++) {
            List<Cromossomo> novaPopulacao = new ArrayList<>();

            while (novaPopulacao.size() < tamanhoPopulacao) {
                // Seleção de cromossomos
                Cromossomo[] paisTorneio = selecaoTorneio(populacao);
                Cromossomo[] paisRoleta = selecaoRoleta(populacao);

                // // Log dos pais da seleção por torneio binário
                // System.out.println("Pais por seleção por torneio binário:");
                // System.out.println("Fitness 1: " + paisTorneio[0].getFitness());
                // System.out.println("Fitness 2: " + paisTorneio[1].getFitness());

                // // Log dos pais da seleção por roleta viciada
                // System.out.println("Pais por seleção por roleta viciada:");
                // System.out.println("Fitness 1: " + paisRoleta[0].getFitness());
                // System.out.println("Fitness 2: " + paisRoleta[1].getFitness());

                // Adiciona os pais a nova população
                novaPopulacao.add(paisTorneio[0]);
                novaPopulacao.add(paisTorneio[1]);
                novaPopulacao.add(paisRoleta[0]);
                novaPopulacao.add(paisRoleta[1]);

                // Cruzamento dos pais
                Cromossomo[] filhosBinTor = cruzamentoBinario(paisTorneio[0], paisTorneio[1]);
                Cromossomo[] filhosBinRol = cruzamentoBinario(paisRoleta[0], paisRoleta[1]);
                Cromossomo[] filhosMulTor = cruzamentoMultiplo(paisTorneio[0], paisTorneio[1]);
                Cromossomo[] filhosMulRol = cruzamentoMultiplo(paisRoleta[0], paisRoleta[1]);

                // // Log dos filhos de cruzamento binário entre pais de toneiro binário
                // System.out.println("Filhos por cruzamento binário de pais de seleção por torneio binário:");
                // System.out.println("Fitness 1: " + filhosBinTor[0].getFitness());
                // System.out.println("Fitness 2: " + filhosBinTor[1].getFitness());

                // // Log dos filhos de cruzamento binário entre pais de toneiro binário
                // System.out.println("Filhos por cruzamento binário de pais de seleção por roleta viciada:");
                // System.out.println("Fitness 1: " + filhosBinRol[0].getFitness());
                // System.out.println("Fitness 2: " + filhosBinRol[1].getFitness());

                // // Log dos filhos de cruzamento múltiplo entre pais de toneiro binário
                // System.out.println("Filhos por cruzamento múltiplo de pais de seleção por torneio binário:");
                // System.out.println("Fitness 1: " + filhosMulTor[0].getFitness());
                // System.out.println("Fitness 2: " + filhosMulTor[1].getFitness());

                // // Log dos filhos de cruzamento binário entre pais de toneiro binário
                // System.out.println("Filhos por cruzamento múltiplo de pais de seleção por roleta viciada:");
                // System.out.println("Fitness 1: " + filhosMulRol[0].getFitness());
                // System.out.println("Fitness 2: " + filhosMulRol[1].getFitness());

                // Adiciona os filhos a nova população
                novaPopulacao.add(filhosBinTor[0]);
                novaPopulacao.add(filhosBinTor[1]);
                novaPopulacao.add(filhosBinRol[0]);
                novaPopulacao.add(filhosBinRol[1]);
                novaPopulacao.add(filhosMulTor[0]);
                novaPopulacao.add(filhosMulTor[1]);
                novaPopulacao.add(filhosMulRol[0]);
                novaPopulacao.add(filhosMulRol[1]);

                // Mutação simples
                mutacaoSimples(filhosBinTor[0]);
                mutacaoSimples(filhosBinTor[1]);
                mutacaoSimples(filhosBinRol[0]);
                mutacaoSimples(filhosBinRol[1]);
                mutacaoSimples(filhosMulTor[0]);
                mutacaoSimples(filhosMulTor[1]);
                mutacaoSimples(filhosMulRol[0]);
                mutacaoSimples(filhosMulRol[1]);

                novaPopulacao.add(filhosBinTor[0]);
                novaPopulacao.add(filhosBinTor[1]);
                novaPopulacao.add(filhosBinRol[0]);
                novaPopulacao.add(filhosBinRol[1]);
                novaPopulacao.add(filhosMulTor[0]);
                novaPopulacao.add(filhosMulTor[1]);
                novaPopulacao.add(filhosMulRol[0]);
                novaPopulacao.add(filhosMulRol[1]);
                
                // Mutação múltipla
                mutacaoMultipla(filhosBinTor[0]);
                mutacaoMultipla(filhosBinTor[1]);
                mutacaoMultipla(filhosBinRol[0]);
                mutacaoMultipla(filhosBinRol[1]);
                mutacaoMultipla(filhosMulTor[0]);
                mutacaoMultipla(filhosMulTor[1]);
                mutacaoMultipla(filhosMulRol[0]);
                mutacaoMultipla(filhosMulRol[1]);

                novaPopulacao.add(filhosBinTor[0]);
                novaPopulacao.add(filhosBinTor[1]);
                novaPopulacao.add(filhosBinRol[0]);
                novaPopulacao.add(filhosBinRol[1]);
                novaPopulacao.add(filhosMulTor[0]);
                novaPopulacao.add(filhosMulTor[1]);
                novaPopulacao.add(filhosMulRol[0]);
                novaPopulacao.add(filhosMulRol[1]);

                // Mutação binária
                mutacaoBinaria(filhosBinTor[0]);
                mutacaoBinaria(filhosBinTor[1]);
                mutacaoBinaria(filhosBinRol[0]);
                mutacaoBinaria(filhosBinRol[1]);
                mutacaoBinaria(filhosMulTor[0]);
                mutacaoBinaria(filhosMulTor[1]);
                mutacaoBinaria(filhosMulRol[0]);
                mutacaoBinaria(filhosMulRol[1]);

                novaPopulacao.add(filhosBinTor[0]);
                novaPopulacao.add(filhosBinTor[1]);
                novaPopulacao.add(filhosBinRol[0]);
                novaPopulacao.add(filhosBinRol[1]);
                novaPopulacao.add(filhosMulTor[0]);
                novaPopulacao.add(filhosMulTor[1]);
                novaPopulacao.add(filhosMulRol[0]);
                novaPopulacao.add(filhosMulRol[1]);

                // Encontrar o cromossomo com maior fitness
                double melhorFitness = 0;
                
                for (Cromossomo cromossomo : novaPopulacao) {
                    if (cromossomo.getFitness() > melhorFitness) melhorFitness = cromossomo.getFitness();
                }

                // Mostrar o melhor cromossomo da geração
                System.out.println("Melhor Cromossomo da Geração " + (geracao+1) + ": " + melhorFitness);
                
            }
        }


    }
   
    // Seleciona dois cromossomos pais aleatórios da população
    public Cromossomo[] selecaoTorneio(List<Cromossomo> populacao) {
        Random rand = new Random();
        Cromossomo pai1 = populacao.get(rand.nextInt(populacao.size()));
        Cromossomo pai2 = populacao.get(rand.nextInt(populacao.size()));
        return (pai1.getFitness() < pai2.getFitness()) ? new Cromossomo[]{pai1, pai2} : new Cromossomo[]{pai2, pai1};
    }

    // Seleciona os dois cromossomos da população que tem o maior fitness
    public Cromossomo[] selecaoRoleta(List<Cromossomo> populacao) {
        double totalFitness = 0.0;

        for (Cromossomo c : populacao) {
            totalFitness += c.getFitness();
        }

        Cromossomo pai1 = roletaViciada(populacao, totalFitness);
        Cromossomo pai2;

        // Garante que pai2 seja diferente de pai1
        do {
            pai2 = roletaViciada(populacao, totalFitness);
        } while (pai1 == pai2 && populacao.size() > 1);

        return new Cromossomo[] { pai1, pai2 };
    }

    // Sorteia um cromossomo com base proporcional ao seu fitness
    private Cromossomo roletaViciada(List<Cromossomo> populacao, double totalFitness) {
        double ponto = Math.random() * totalFitness;
        double acumulado = 0.0;

        for (Cromossomo c : populacao) {
            acumulado += c.getFitness();
            if (acumulado >= ponto) {
                return c;
            }
        }

        return populacao.get(populacao.size() - 1); // fallback
    }

    // Faz o cruzamento de dois pais substituindo os dados a partir de um ponto de corte
    public Cromossomo[] cruzamentoBinario(Cromossomo pai1, Cromossomo pai2) {
        Random rand = new Random();
        int corte = rand.nextInt(pai1.getGenes().length);

        int[] genesFilho1 = new int[pai1.getGenes().length];
        int[] genesFilho2 = new int[pai1.getGenes().length];

        for (int i = 0; i < genesFilho1.length; i++) {
            if (i < corte) {
                genesFilho1[i] = pai1.getGenes()[i];
                genesFilho2[i] = pai2.getGenes()[i];
            } else {
                genesFilho1[i] = pai2.getGenes()[i];
                genesFilho2[i] = pai1.getGenes()[i];
            }
        }

        Cromossomo filho1 = new Cromossomo(genesFilho1);
        Cromossomo filho2 = new Cromossomo(genesFilho2);

        // System.out.println(filho1.getFitness());
        // System.out.println(filho2.getFitness());

        return new Cromossomo[]{filho1, filho2};
    }

    // Faz o cruzamento de dois pais substituindo os dados a partir de pontos múltiplos de corte
    public Cromossomo[] cruzamentoMultiplo(Cromossomo pai1, Cromossomo pai2) {
        Random rand = new Random();
        int tamanho = pai1.getGenes().length;
        int[] cortes = new int[numCortes];

        // Gerar cortes aleatórios
        for (int i = 0; i < numCortes; i++) {
            cortes[i] = rand.nextInt(tamanho);
        }
        Arrays.sort(cortes);

        int[] genesFilho1 = new int[tamanho];
        int[] genesFilho2 = new int[tamanho];

        int origem = 0; // começa com o pai1 como origem
        int corteAtual = 0;
        int proximoCorte = (corteAtual < numCortes) ? cortes[corteAtual] : tamanho;

        for (int i = 0; i < tamanho; i++) {
            if (i >= proximoCorte) {
                origem = 1 - origem; // alterna entre pai1 e pai2
                corteAtual++;
                proximoCorte = (corteAtual < numCortes) ? cortes[corteAtual] : tamanho;
            }

            genesFilho1[i] = (origem == 0) ? pai1.getGenes()[i] : pai2.getGenes()[i];
            genesFilho2[i] = (origem == 0) ? pai2.getGenes()[i] : pai1.getGenes()[i];
        }

        Cromossomo filho1 = new Cromossomo(genesFilho1);
        Cromossomo filho2 = new Cromossomo(genesFilho2);

        // System.out.println(filho1.getFitness());
        // System.out.println(filho2.getFitness());

        return new Cromossomo[]{filho1, filho2};
    }

    // Mutação simples: altera um gene aleatório
    public void mutacaoSimples(Cromossomo cromossomo) {
        Random rand = new Random();

        // if (rand.nextDouble() < taxaMutacao) {
            int[] genes = cromossomo.getGenes();

            // Escolhe um índice aleatório para mutar
            int indice = rand.nextInt(genes.length);

            // Decide se soma ou subtrai 20
            int delta = rand.nextBoolean() ? 20 : -20;

            int novoValor = genes[indice] + delta;

            // Garante que não seja menor que 0
            if (novoValor < 0) {
                novoValor = 0;
            }

            // Garante que seja múltiplo de 20
            novoValor = (novoValor / 20) * 20;

            genes[indice] = novoValor;

            // Atualiza o fitness
            // System.out.println(cromossomo.getFitness());
            cromossomo.calcularFitness();
        // }
    }

    // Mutação múltipla: altera mais de um gene
    public void mutacaoMultipla(Cromossomo cromossomo) {
        Random rand = new Random();
        int[] genes = cromossomo.getGenes();  // Obtém os genes do cromossomo

        // Itera sobre os genes do cromossomo
        for (int i = 0; i < genes.length; i++) {
            // Para cada gene, gera uma probabilidade para mutação
            if (rand.nextDouble() < taxaMutacao) {  // taxaMutacao é a probabilidade
                // Gera uma alteração aleatória para o gene
                int delta = (rand.nextBoolean() ? 20 : -20);  // A mutação pode ser de +20 ou -20

                // Calcula o novo valor do gene
                int novoValor = genes[i] + delta;

                // Garante que o novo valor do gene seja maior ou igual a 0
                novoValor = Math.max(0, (novoValor / 20) * 20);  // Mantém múltiplos de 20 e >= 0

                // Atualiza o gene no cromossomo
                genes[i] = novoValor;
            }
        }

        // Atualiza a avaliação do cromossomo após as mutações
        cromossomo.calcularFitness();
    }

    // Mutação binária: altera os genes de acordo com um vetor binário (0 = não muta; 1 = muta)
    public void mutacaoBinaria(Cromossomo cromossomo) {
        Random rand = new Random();
        int[] genes = cromossomo.getGenes();  // Obtém os genes do cromossomo
        int tamanho = genes.length;

        // Gera um vetor binário aleatório do mesmo tamanho do cromossomo (0 ou 1)
        int[] vetorBinario = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetorBinario[i] = rand.nextInt() < taxaMutacao ? 1 : 0;  // 1 com taxa de mutação, 0 caso contrário
        }

        // Aplica a mutação binária
        for (int i = 0; i < tamanho; i++) {
            if (vetorBinario[i] == 1) {  // Se o valor binário for 1, muta o gene correspondente
                int delta = (rand.nextBoolean() ? 20 : -20);  // Alteração de +20 ou -20

                // Calcula o novo valor do gene
                int novoValor = genes[i] + delta;

                // Garante que não seja menor que 0
                if (novoValor < 0) {
                    novoValor = 0;
                }

                // Garante que seja múltiplo de 20
                novoValor = (novoValor / 20) * 20;

                genes[i] = novoValor;
            }
        }

        // Atualiza a avaliação do cromossomo após a mutação
        cromossomo.calcularFitness();
    }

}