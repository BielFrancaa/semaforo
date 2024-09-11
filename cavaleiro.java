package semaphore;
import java.util.concurrent.Semaphore;
import java.util.Random;

public class cavaleiro {

    public static void main(String[] args) {
        Cavaleiro[] cavaleiros = new Cavaleiro[4];
        for (int i = 0; i < 4; i++) {
            cavaleiros[i] = new Cavaleiro("Cavaleiro " + (i + 1));
            cavaleiros[i].start();
        }
    }
}

class Cavaleiro extends Thread {
    private static final int DIST_CORREDOR = 2000;
    private static final int TOCHA_DIST = 500;
    private static final int PEDRA_DIST = 1500;
    private static Semaphore semaforoTocha = new Semaphore(1);
    private static Semaphore semaforoPedra = new Semaphore(1);
    private int velocidade;
    private String nome;

    public Cavaleiro(String nome) {
        this.nome = nome;
        this.velocidade = new Random().nextInt(3) + 2; // de 2 a 4 metros por 50ms
    }

    @Override
    public void run() {
        int distanciaPercorrida = 0;

        while (distanciaPercorrida < DIST_CORREDOR) {
            try {
                Thread.sleep(50);  // 50ms de pausa entre os incrementos
                distanciaPercorrida += velocidade;

                // Verifica se chegou na tocha
                if (distanciaPercorrida >= TOCHA_DIST && semaforoTocha.tryAcquire()) {
                    System.out.println(nome + " pegou a tocha!");
                    velocidade += 2;  // Aumenta a velocidade
                }

                // Verifica se chegou na pedra
                if (distanciaPercorrida >= PEDRA_DIST && semaforoPedra.tryAcquire()) {
                    System.out.println(nome + " pegou a pedra!");
                    velocidade += 2;  // Aumenta a velocidade
                }

                System.out.println(nome + " percorreu " + distanciaPercorrida + " metros.");
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Chegando ao final, escolha de porta aleatória
        escolherPorta();
    }

    private void escolherPorta() {
        Random random = new Random();
        int portaEscolhida = random.nextInt(4) + 1;
        System.out.println(nome + " escolheu a porta " + portaEscolhida);
        if (portaEscolhida == 1) {
            System.out.println(nome + " encontrou a saída!");
        } else {
            System.out.println(nome + " foi devorado por monstros.");
        }
    }
}