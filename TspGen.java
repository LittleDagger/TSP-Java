package TspGen;
import java.io.BufferedReader;
import java.util.Random;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TspGen {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String line;
        int size;
        int[][] distance;
        String[] substrings;
        int[][] population;
        int[] populationRating;
        Random rnd = new Random();
        int[][] tempPopulation;
        int minRating = 2000000000;
        int minID = 0;
        
        //control variables
        int amountOfIterations = 800000;
        int amountOfIndividualsInPopulation = 40;
        int selectivePressure = 2;
        int crossProbability = 95000; //100000
        int swapMutationProbability = 600; //100000
        int tournamentProbability = 100;
        /*notes
            mutation 110 cros 92 - 8200
            mutation 115 cros 92 - 7890
            mutation 120 cros 92 - 8262
            mutation 113 cros 95 - 7700 600k iter | 7957 430k iter
            mutation 113 cros 94 - 7860 800k iter
            mutation 113 cros 96 - 7818 600k iter
            mutation 113 cros 96 - 7500 520k iter
            9520 117
        */
        
        
        //loading from file
        FileReader fileReader = new FileReader("pr107.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        size = Integer.valueOf(bufferedReader.readLine());
        distance = new int[size][size];
        int lineId = 0;
        while((line = bufferedReader.readLine()) != null)
        {
        substrings = line.split(" ");

            for(int j =0; j<substrings.length; j++)
            {
                distance[lineId][j] = Integer.valueOf(substrings[j]);
                distance[j][lineId] = Integer.valueOf(substrings[j]);
            }
        lineId++;
        }

        bufferedReader.close();
        
        //Generating population
        population = Gen.Generator(amountOfIndividualsInPopulation, size);
        
        //Printing population
        /*for(int i = 0; i < amountOfIndividualsInPopulation; i++)
        {
            for(int j = 0; j< size; j++)
            {
                System.out.print(population[i][j] + " ");
            }
            System.out.println();
        }*/
        
        //Rating generated population
        populationRating = Gen.RatingPopulation(population, distance, amountOfIndividualsInPopulation, size);
        
        //Printing ratings
        /*for(int i = 0; i< amountOfIndividualsInPopulation; i++)
        {
            System.out.print(populationRating[i] + " ");
        }*/

        for (int a = 0; a < amountOfIterations; a++)
        {
            if (a % 100000 == 0)
            {
                System.out.println("\n------------------------------------------------------------ Iteration: " + a + "-------------------------------------------------------------");
            }
            
                                  
            //drawing selection
            int drawSelection = rnd.nextInt(100);

            if (drawSelection < tournamentProbability)
            {
                //Tournament selection
                tempPopulation = Gen.TournamentSelection(population, populationRating, amountOfIndividualsInPopulation, size, selectivePressure);
                
                //Printing population after tournament
                /*for(int i = 0; i < amountOfIndividualsInPopulation; i++)
                {
                    for(int j = 0; j< size; j++)
                    {
                        System.out.print(tempPopulation[i][j] + " ");
                    }
                System.out.println();
                }*/
            }
            else
            {
                //Roulete selection
                tempPopulation = Gen.RouleteSelection(population, populationRating, amountOfIndividualsInPopulation, size);
                
                //Printing population after roulete
                /*for(int i = 0; i < amountOfIndividualsInPopulation; i++)
                {
                    for(int j = 0; j< size; j++)
                    {
                        System.out.print(tempPopulation[i][j] + " ");
                    }
                System.out.println();
                }*/
            }
            
            //Crossing population
            population = Gen.PMXCrossover(tempPopulation, size, amountOfIndividualsInPopulation, crossProbability);
            
            //Mutating population
            Gen.SwapMutation(population, size, amountOfIndividualsInPopulation, swapMutationProbability);
            
            //Printing population after crossover and mutation
            /*for(int i = 0; i < amountOfIndividualsInPopulation; i++)
            {
                for(int j = 0; j< size; j++)
                {
                    System.out.print(population[i][j] + " ");
                }
                System.out.println();
            }*/
            
            //Rating population after mutation
            populationRating = Gen.RatingPopulation(population, distance, amountOfIndividualsInPopulation, size);
            
            //Checking for min distance
            if (minRating > Gen.Min(populationRating))
            {
                minRating = Gen.Min(populationRating);

                for (int k = 0; k < amountOfIndividualsInPopulation; k++)
                {
                    if (minRating == populationRating[k])
                    {
                        minID = k;
                    }
                }
                System.out.println("\nMin found: " + minRating + " Iteration: " + a);
                System.out.println("Best route: ");
                for (int k = 0; k < size; k++)
                {
                    System.out.print(population[minID][k] + "-");
                }
            }
        }
    }           
            
   

}
