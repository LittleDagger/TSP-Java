/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TspGen;
import java.util.Random;
/**
 *
 * @author Czarny
 */
public final class Gen {
    public static int[][] Generator(int amountOfIndividualsInPopulation, int size)
    {
        Random rnd = new Random();
        int[][] array = new int[amountOfIndividualsInPopulation] [size];
        int anc;
        //Filling array with -1s
        for (int i = 0; i < amountOfIndividualsInPopulation; i++)
        {
            for (int j = 0; j < size; j++)

                array[i][j] = -1;
        }
        //Filling array with random routes
        for (int i = 0; i < amountOfIndividualsInPopulation; i++)
        {
            for (int j = 0; j < size; j++)
            {
                anc = rnd.nextInt(size);
                while (Contain(array, anc, i, size))
                {
                    anc = rnd.nextInt(size);
                }

                array[i][j] = anc;
            }
        }
        return array;
    }
    
    public static boolean Contain(int[][] array, int value, int i, int size)
    {
        for (int j = 0; j < size; j++)
        {
            if (array[i][j] == value)
                return true;
        }
        return false;
    }
    
    public static int[] RatingPopulation(int[][] population, int[][] distance, int amountOfIndividualsInPopulation, int size)
    {
        int[] populationRating = new int[amountOfIndividualsInPopulation];
        int sum = 0;
        for (int i = 0; i < amountOfIndividualsInPopulation; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (j != size - 1)
                {
                    sum += distance[population[i][j]] [population[i][j + 1]];
                }
                else
                {
                    sum += distance[population[i][j]] [population[i][0]];
                }
            }
            populationRating[i] = sum;
            sum = 0;
        }
        return populationRating;
    }
    
    public static int[][] TournamentSelection(int[][] population, int[] populationRating, int amountOfIndividualsInPopulation, int size, int selectivePressure)
    {
        int[][] tournamentPopulation = new int[amountOfIndividualsInPopulation][size];
        int[] singleBattleArray = new int[selectivePressure];
        Random rnd = new Random();
        int anc;
        int minDistance;
        int minDistanceIndividualId;


        for (int j = 0; j < amountOfIndividualsInPopulation; j++)
        {
            //drawing individuals to tournament
            for (int i = 0; i < selectivePressure; i++)
            {
                anc = rnd.nextInt(amountOfIndividualsInPopulation);
                singleBattleArray[i] = anc;
            }

            //chosing best individual
            minDistanceIndividualId = singleBattleArray[0];
            minDistance = populationRating[singleBattleArray[0]];


            for (int i = 1; i < selectivePressure; i++)
            {
                if (minDistance > populationRating[singleBattleArray[i]])
                {
                    minDistance = populationRating[singleBattleArray[i]];
                    minDistanceIndividualId = singleBattleArray[i];
                }
            }

            for(int i = 0; i < size; i++)
            {
                tournamentPopulation[j][i] = population[minDistanceIndividualId] [i];
            }
        }
        return tournamentPopulation;
    }
    
    public static int Max (int[] array)
    {
        int max = array[0];
        for(int i = 1; i<array.length; i++)
        {
            if(max<array[i])
            {
                max = array[i];
            }
        }
        return max;
    }
    
    public static int Min (int[] array)
    {
        int min = array[0];
        for(int i = 1; i<array.length; i++)
        {
            if(array[i]<min)
            {
                min = array[i];
            }
        }
        return min;
    }
    
    public static int[][] RouleteSelection(int[][] population, int[] populationRating, int amountOfIndividualsInPopulation, int size)
    {
        int[][] rouletePopulation = new int[amountOfIndividualsInPopulation][size];
        int[] reversedRating = new int[amountOfIndividualsInPopulation];
        int maxRating = Max(populationRating);
        int sumReversedRating = 0;
        int partitionMin = 0;
        int anc;
        int counter;
        Random rnd = new Random();

        //Reversing rating max - xi + 1
        for (int i = 0; i < amountOfIndividualsInPopulation; i++)
        {
            reversedRating[i] = maxRating - populationRating[i] + 1;
        }

        //Summing reversed ratings
        for (int i = 0; i < amountOfIndividualsInPopulation; i++)
        {
            sumReversedRating += reversedRating[i];
        }

        for (int i = 0; i < amountOfIndividualsInPopulation; i++)
        {
            counter = 0;
            partitionMin = 0;
            anc = rnd.nextInt(sumReversedRating);

            while (anc >= (partitionMin += reversedRating[counter]) && counter != amountOfIndividualsInPopulation - 1)
            {
                counter++;
            }
            for (int j = 0; j < size; j++)
            {
                rouletePopulation[i][j] = population[counter][j];
            }
        }
        return rouletePopulation;
    }
    
    public static int[][] PMXCrossover(int[][] tempPopulation, int size, int amountOfIndividualsInPopulation, int crossProbability)
    {
        int[][] crossPopulation = new int [amountOfIndividualsInPopulation][size];
        Random rnd = new Random();
        int crossingPointMin;
        int crossingPointMax;
        int containValue;
        int containID;

        //filling with -1s
        for(int i = 0; i < amountOfIndividualsInPopulation; i++)
        {
            for(int j = 0; j < size; j++)
            {
                crossPopulation[i][j] = -1;
            }
        }

        for (int i = 0; i < amountOfIndividualsInPopulation; i = i+2)
        {
            int tryCrossing = rnd.nextInt(100000);

            if (tryCrossing < crossProbability)
            {
                //drawing crossing points
                crossingPointMin = rnd.nextInt(size - 2);
                crossingPointMax = rnd.nextInt(size-crossingPointMin) + crossingPointMin;

                //swaping middles
                for (int j = crossingPointMin; j <= crossingPointMax; j++)
                {
                    crossPopulation[i][j] = tempPopulation[i + 1][j];
                    crossPopulation[i + 1][j] = tempPopulation[i][j];
                }

                //first individual crossing
                for (int j = 0; j < size; j++)
                {
                    if (j < crossingPointMin || j > crossingPointMax)
                    {
                        containValue = tempPopulation[i][j];
                        while (ContainPMX(crossPopulation, containValue, i, size) != -1)
                        {
                            containID = ContainPMX(crossPopulation, containValue, i, size);
                            containValue = crossPopulation[i + 1][containID];
                        }

                        crossPopulation[i][j] = containValue;
                    }
                }

                //second individual crossing
                for (int j = 0; j < size; j++)
                {
                    if (j < crossingPointMin || j > crossingPointMax)
                    {
                        containValue = tempPopulation[i + 1][j];
                        
                        while (ContainPMX(crossPopulation, containValue, i + 1, size) != -1)
                        {
                            containID = ContainPMX(crossPopulation, containValue, i + 1, size);
                            containValue = crossPopulation[i][containID];
                        }

                        crossPopulation[i + 1][j] = containValue;
                    }
                }
            }
            else
            {
                //do not change individual
                for (int j = 0; j < size; j++)
                {
                    crossPopulation[i][j] = tempPopulation[i][j];
                    crossPopulation[i + 1][j] = tempPopulation[i + 1][j];
                }
            }
        }
        return crossPopulation;
    }
    
    public static int ContainPMX(int[][] array, int value, int i, int size)
    {
        for (int j = 0; j < size; j++)
        {
            if (array[i][j] == value)
                return j;
        }
        return -1;
    }
    
    public static void SwapMutation(int[][] tempPopulation, int size, int amountOfIndividualsInPopulation, int mutationProbability)
    {
        int temp;
        int tryMutating;
        Random rnd = new Random();

        for(int i = 0; i < amountOfIndividualsInPopulation; i++)
        {
            for(int j = 0; j<size; j++)
            {
                tryMutating = rnd.nextInt(100000);
                if(tryMutating < mutationProbability)
                {
                    temp = tempPopulation[i][j];
                    tempPopulation[i][j] = rnd.nextInt(size);
                    for(int k = 0; k<size; k++)
                    {
                        if(tempPopulation[i][k] == tempPopulation[i][j] && k!=j)
                        {
                            tempPopulation[i][k] = temp;
                        }
                    }
                }

            }
        }
    }
}
