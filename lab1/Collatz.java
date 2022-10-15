

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Class that prints the Collatz sequence starting from a given number.
 *  @author YOUR NAME HERE
 */
public class Collatz {

    /** Buggy implementation of nextNumber! */
    public static int nextNumber(int n)
    {
       if(n == 1)
        {
            return n;
        }

        else if (n % 2 == 0)
        {
            return n / 2;
        }
        else
        {
            return n = 3 * n + 1;

        }

    }

    public static void main(String[] args)
    {
        int n = 5;
        int terminate = 1;
        List<Integer> collatz = new ArrayList<>();


        while(terminate!=0)
        {

            collatz.add(n);
            n = nextNumber(n);
            if(n == 1)
            {
                collatz.add(n);
                terminate = 0;
            }

        }
        collatz.forEach(result -> System.out.print(result + " "));






    }
}

