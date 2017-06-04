package threadsObserver;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import randomperson.RandomUser;
import randomperson.RandomUserGenerator;

/**
 * Assignment asks to implement the Oberserver Pattern, so we have to extend
 * this class with Observable.
 *
 * @author bear
 */
public class RandomUserControl extends Observable {

    /**
     * Method to fetch a random user.
     *
     * @return a RandomUser through the user-variable.
     */
    public void fetchRandomUser() {

        new Thread(() -> {

            RandomUser user = null;

            try {

                user = RandomUserGenerator.getRandomUser();

            } catch (InterruptedException ex) {

                System.out.println(ex.getStackTrace().toString());;

            }

            //No longer needed, hence this class extends Observable. 
            //return user;
            //Marks this Observable object as having been changed; the hasChanged
            //method will now return true. 
            setChanged();

            //If this object has changed, as indicated by the
            //hasChanged method, then notify all of its observers
            //and then call the clearChanged method to
            //indicate that this object has no longer changed.
            notifyObservers(user);

        }).start();

//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//
//                RandomUser user = null;
//
//                try {
//
//                    user = RandomUserGenerator.getRandomUser();
//
//                } catch (InterruptedException ex) {
//
//                    System.out.println(ex.getStackTrace().toString());;
//
//                }
//
//                //No longer needed, hence this class extends Observable. 
//                //return user;
//                //Marks this Observable object as having been changed; the hasChanged
//                //method will now return true. 
//                setChanged();
//
//                //If this object has changed, as indicated by the
//                //hasChanged method, then notify all of its observers
//                //and then call the clearChanged method to
//                //indicate that this object has no longer changed.
//                notifyObservers(user);
//
//            }
//
//        };
//
//        Thread t = new Thread(r);
//        t.start();

    }

}
