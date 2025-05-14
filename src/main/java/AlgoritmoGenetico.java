import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AlgoritmoGenetico {
    private int tamanhoPopulacao;
    private int numeroGeracoes;
    private int tipoMutacao;
    private Double taxaMutacao;
    private int numLoops;
    private int numCortes;

    public AlgoritmoGenetico(int tamanhoPopulacao, int numeroGeracoes, int tipoMutacao, Double taxaMutacao, int numCortes) {
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.numeroGeracoes = numeroGeracoes;
        this.tipoMutacao = tipoMutacao;
        this.taxaMutacao = taxaMutacao;
        this.numCortes = numCortes;
    }

    public AlgoritmoGenetico(int tamanhoPopulacao, int numeroGeracoes, int tipoMutacao, int numLoops, int numCortes) {
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.numeroGeracoes = numeroGeracoes;
        this.tipoMutacao = tipoMutacao;
        this.numLoops = numLoops;
        this.numCortes = numCortes;
    }

    public AlgoritmoGenetico() {
        this.tamanhoPopulacao = 1000;
        this.numeroGeracoes = 100;
        this.taxaMutacao = 0.25;
        this.numCortes = 10;
        this.tipoMutacao = 1;
        this.numLoops = 10000;
    }

    // Executa o loop evolutivo
    public void executar() {
        List<Cromossomo> populacao = new ArrayList<>();
        List<Cromossomo> populacaoFinal = new ArrayList<>();

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

                // Cruzamento dos pais
                Cromossomo[] filhosBinTor = cruzamentoBinario(paisTorneio[0], paisTorneio[1]);
                Cromossomo[] filhosBinRol = cruzamentoBinario(paisRoleta[0], paisRoleta[1]);
                Cromossomo[] filhosMulTor = cruzamentoMultiplo(paisTorneio[0], paisTorneio[1]);
                Cromossomo[] filhosMulRol = cruzamentoMultiplo(paisRoleta[0], paisRoleta[1]);

                // Adiciona os filhos a nova população
                novaPopulacao.add(filhosBinTor[0]);
                novaPopulacao.add(filhosBinTor[1]);
                novaPopulacao.add(filhosBinRol[0]);
                novaPopulacao.add(filhosBinRol[1]);
                novaPopulacao.add(filhosMulTor[0]);
                novaPopulacao.add(filhosMulTor[1]);
                novaPopulacao.add(filhosMulRol[0]);
                novaPopulacao.add(filhosMulRol[1]);
            }

            // Encontrar o cromossomo com maior fitness
            Double melhorFitness = 0.0;
            
            for (Cromossomo cromossomo : novaPopulacao) {
                if (cromossomo.getFitness() > melhorFitness) melhorFitness = cromossomo.getFitness();
            }

            // Mostrar o melhor cromossomo da geração
            System.out.println("Melhor Cromossomo da Geração " + (geracao+1) + ": " + melhorFitness);

            populacaoFinal.addAll(novaPopulacao);
        }
        
        Double melhorFitness = 0.0;
        Cromossomo melhor = new Cromossomo();
        for (Cromossomo cromossomo : populacaoFinal) {
            if (cromossomo.getFitness() > melhorFitness) {
                melhor = cromossomo;
                melhorFitness = melhor.getFitness();
            }
        }

        // Mostrar o melhor cromossomo de todas as gerações
        System.out.println("Melhor Cromossomo Final de Todas as Gerações: " + melhor.getFitness());

        // System.out.println("Efetuando mutação 1 (simples) com o melhor cromossomo...");
        // Cromossomo novoMelhor = new Cromossomo(melhor.getGenes(), this.taxaMutacao, 1);
        // System.out.println("Fitness do cromossomo após a mutação: " + novoMelhor.getFitness());
        
        // System.out.println("Efetuando mutação 2 (por viagem) com o melhor cromossomo...");
        // novoMelhor = new Cromossomo(melhor.getGenes(), this.taxaMutacao, 2);
        // System.out.println("Fitness do cromossomo após a mutação: " + novoMelhor.getFitness());
        
        // System.out.println("Efetuando mutação 3 (múltipla) com o melhor cromossomo...");
        // novoMelhor = new Cromossomo(melhor.getGenes(), this.taxaMutacao, 3);
        // System.out.println("Fitness do cromossomo após a mutação: " + novoMelhor.getFitness());

        System.out.println("Efetuando loop de mutações com o melhor cromossomo...");
        Cromossomo novoMelhor = new Cromossomo(melhor.getGenes(), this.numLoops, this.tipoMutacao);
        System.out.println("Fitness do cromossomo após o loop de mutações: " + novoMelhor.getFitness());
        System.out.println("Melhor Cromossomo:");
        System.out.println(novoMelhor.toString());
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
        Double totalFitness = 0.0;

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
    private Cromossomo roletaViciada(List<Cromossomo> populacao, Double totalFitness) {
        Double ponto = Math.random() * totalFitness;
        Double acumulado = 0.0;

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
        // Define o corte como sendo em uma das viagens
        int corte = ((rand.nextInt(99) + 1) * 50) - 1;

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

        // Gerar cortes aleatórios entre as viagens
        for (int i = 0; i < numCortes; i++) {
            cortes[i] = rand.nextInt(((rand.nextInt(99) + 1) * 50) - 1);
            if (i > 0 && cortes[i-1] == cortes[i]) cortes[i] = rand.nextInt(((rand.nextInt(99) + 1) * 50) - 1);
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

}