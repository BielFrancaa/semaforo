package semaphore;

import java.util.concurrent.Semaphore;
import java.util.Random;

public class Aeroporto {

    public static void main(String[] args) {
        Aviao[] avioes = new Aviao[12];
        for (int i = 0; i < 12; i++) {
            avioes[i] = new Aviao("Avião " + (i + 1));
            avioes[i].start();
        }
    }
}

class Aviao extends Thread {
    private static final Semaphore pistaNorte = new Semaphore(1);
    private static final Semaphore pistaSul = new Semaphore(1);
    private static final Semaphore areaDecolagem = new Semaphore(2);  // Limita 2 aviões na área
    private String nome;

    public Aviao(String nome) {
        this.nome = nome;
    }

    @Override
    public void run() {
        try {
            // Escolhe aleatoriamente uma pista
            Random random = new Random();
            boolean usaPistaNorte = random.nextBoolean();

            // Tenta ocupar a pista
            if (usaPistaNorte) {
                pistaNorte.acquire();
                System.out.println(nome + " está usando a pista norte.");
            } else {
                pistaSul.acquire();
                System.out.println(nome + " está usando a pista sul.");
            }

            // Simula fases da decolagem
            areaDecolagem.acquire();
            fase("Manobra", 300, 700);
            fase("Taxiamento", 500, 1000);
            fase("Decolagem", 600, 800);
            fase("Afastamento", 300, 800);
            areaDecolagem.release();

            // Libera a pista
            if (usaPistaNorte) {
                pistaNorte.release();
                System.out.println(nome + " liberou a pista norte.");
            } else {
                pistaSul.release();
                System.out.println(nome + " liberou a pista sul.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fase(String nomeFase, int min, int max) throws InterruptedException {
        int duracao = new Random().nextInt(max - min + 1) + min;
        System.out.println(this.nome + " está na fase de " + nomeFase + " por " + duracao + " ms.");
        Thread.sleep(duracao);
    }
}