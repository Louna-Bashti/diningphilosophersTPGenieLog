package diningphilosophers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChopStick {
    private final Lock verrou = new ReentrantLock();
    private final Condition deuxDispo = verrou.newCondition();
    private static int stickCount = 0;
    private boolean iAmFree = true;
    private final int myNumber;

    public ChopStick() {
        myNumber = ++stickCount;
    }

    synchronized public boolean tryTake(long delay) throws InterruptedException {
        verrou.lock();
        try{
            if (!iAmFree) {
            deuxDispo.await(delay, TimeUnit.MILLISECONDS);
            if (!iAmFree) // Toujours pas libre, on abandonne
            {
                return false; // Echec
            }
        }

            iAmFree = false;
            // Pas utile de faire notifyAll ici, personne n'attend qu'elle soit occupée
            return true; // Succès

    } finally
        {verrou.unlock();}

    }

    synchronized public void release() {
        verrou.lock();
        try{
            iAmFree = true;
            deuxDispo.signalAll();
            System.out.println("Stick " + myNumber + " Released");
        }
        finally {verrou.unlock();}

        }

    @Override
    public String toString() {
        return "Stick#" + myNumber;
    }
}
