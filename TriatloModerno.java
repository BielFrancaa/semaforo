package semaphore;
import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.Arrays;

public class TriatloModerno {

    public static void main(String[] args) {
        Atleta[] atletas = new Atleta[25];
        for (int i = 0; i < 25; i++) {
            atletas[i] = new Atleta("Atleta " + (i + 1));
            atletas[i].start();
        }
        
        // Aguarda a finalização de todos os atletas
        for (Atleta atleta : atletas) {
            try {
                atleta.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Exibe ranking final
        Arrays.sort(Atleta.pontuacoesFinais);
        System.out.println("Ranking final:");
        for (int i = 0; i < Atleta.pontuacoesFinais.length; i++) {
            System.out.println("Atleta " + (i + 1) + ": " + Atleta.pontuacoesFinais[i] + " pontos");
        }
    }
}

class Atleta extends Thread {
    private static final Semaphore armas = new Semaphore(5); // Limita 5 armas disponíveis
    private static final Random random = new Random();
    private static int ranking = 250;  // Pontuação decrescente
    private static final int[] pontuacoesFinais = new int[25];
    private String nome;
    private int pontuacao;
    private int posicao;

    public Atleta(String nome) {
        this.nome = nome;
    }

    @Override
    public void run() {
        try {
            // Corrida de 3km
            int distanciaCorrida = 0;
            while (distanciaCorrida < 3000) {
                Thread.sleep(30);
                distanciaCorrida += random.nextInt(6) + 20;
            }
            System.out.println(nome + " terminou a corrida!");

            // Pegando arma para os tiros
            armas.acquire();
            System.out.println(nome + " pegou uma arma.");
            int pontosTiros = 0;
            for (int i = 0; i < 3; i++) {
                int tempoTiro = random.nextInt(2500) + 500;
                Thread.sleep(tempoTiro);
                int pontuacaoTiro = random.nextInt(11);
                pontosTiros += pontuacaoTiro;
                System.out.println(nome + " fez " + pontuacaoTiro + " no tiro " + (i + 1));
            }
            armas.release();

            // Ciclismo de 5km
            int distanciaCiclismo = 0;
            while (distanciaCiclismo < 5000) {
                Thread.sleep(40);
                distanciaCiclismo += random.nextInt(11) + 30;
            }
            System.out.println(nome + " terminou o ciclismo!");

            // Calcula a pontuação final
            synchronized (Atleta.class) {
                posicao = ranking;
                pontuacao = posicao + pontosTiros;
                pontuacoesFinais[Integer.parseInt(nome.split(" ")[1]) - 1] = pontuacao;
                ranking -= 10;
            }
            System.out.println(nome + " terminou a prova com " + pontuacao + " pontos.");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}