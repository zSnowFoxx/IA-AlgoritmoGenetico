// import java.util.ArrayList;
// import java.util.List;


public class CromossomoTest {

    public static void main(String[] args) {

        Config.inicializar();
        // List<Cromossomo> populacao = new ArrayList<>();
        AlgoritmoGenetico ag = new AlgoritmoGenetico();

        // for (int i = 0; i < 50; i++) {
        //     Cromossomo cromossomo = new Cromossomo();
        //     System.out.println(cromossomo.getFitness());
        //     populacao.add(cromossomo);
        // }

        ag.executar();
        // System.out.println(populacao.get(3));

    //     // Criando o cromossomo
    //     Cromossomo cromossomo = new Cromossomo();

    //     // Imprimindo as informações do cromossomo
    //     System.out.println(cromossomo);

    //    // teste dados
    //    Loja[] lojas = Config.lojas;

    //    for (int i = 0; i < lojas.length; i++) {
    //        int somaDemandas = 0;
    //        for (int j = 0; j < 50; j++) {
    //            somaDemandas += lojas[i].getDemandaPorProduto()[j];
    //        }
    //        System.out.println("Loja " + i + ":" + somaDemandas);
    //    }

    }

}
