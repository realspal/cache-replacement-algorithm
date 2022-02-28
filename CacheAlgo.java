/* Topic     : Cache Replacement Algorithm
 *
 * Objective : Implementing a suitable cache replacement algorithm
 *             and calculating the corresponding cache hit ratio.
 *
 * Input     : The program will accept variable number of arguments, where
 *             1st argument = Size of cache (in multiple of cache block),
 *             2nd argument = Size of main memory (in multiple of cache block),
 *             3rd argument = Type of cache replacement algorithm (FIFO or LRU),
 *             4th argument onwards = Sequence of memory block references.
 *
 *             For the input to be valid,
 *             (1) 1st argument should not be more than 1/4th of 2nd argument.
 *             (2) 2nd argument should be one of 32 / 64 / 128.
 *             (3) 3rd argument should be either F (for FIFO) or L (for LRU).
 *             (4) 4th argument onwards should be numbers upto 31 / 63 / 127.
 *
 * Output    : Cache Hit Ratio (number of hits / total number of memory references)
 */
  
/*
 * Compilation command :    javac CacheAlgo.java
 * Execution sequence  :    java CacheAlgo <arguments>
 */
 
/*
 * Sample Input 1  :         java CacheAlgo 8 32 F 1 2 3 1 4 2 12 5 3 6 8 11 9 12 10 7 1 9 5 7
 * Sample Output 1 :         Cache Hit Ratio = 10/20 = 0.500
 *
 * Sample Input 2  :         java CacheAlgo 8 32 L 1 2 3 1 4 2 12 5 3 6 8 11 9 12 10 7 1 9 5 7
 * Sample Output 2 :         Cache Hit Ratio = 11/20 = 0.550
 */

// importing library files
import java.io.*;
class CacheAlgo
{
    // declaring global variables
    static int sizeOfCache,sizeOfMainMemory,hit,numberOfReferences;
    static char algoType;
    static int references[];
    static double cacheHitRatio;
    
    // implementing FIFO (First-In-First-Out) using circular queue

    // declaring global variables needed for FIFO
    static int cq_size,cq_front,cq_rear;
    static int cq_items[];
    
    // dequeue function for removing element from circular queue
    public static void cq_dequeue()
    {
        if(cq_front==cq_rear)
        {
            cq_front=-1;
            cq_rear=-1;
        }
        else
        cq_front=(cq_front+1)%cq_size;
    }
    
    // enqueue function for inserting element into circular queue
    public static void cq_enqueue(int element)
    {
        if((cq_front==0 && cq_rear==cq_size-1) || cq_front==cq_rear+1)
        cq_dequeue();
        if(cq_front==-1)
        cq_front=0;
        cq_rear=(cq_rear+1)%cq_size;
        cq_items[cq_rear]=element;
    }
    
    // function for running FIFO algorithm
    public static void runFIFO()
    {
        cq_front=-1;
        cq_rear=-1;
        cq_size=sizeOfCache;
        cq_items=new int[sizeOfCache];
        for(int i=0;i<sizeOfCache;i++)
        cq_items[i]=-1;
        hit=0;
        for(int j=0;j<numberOfReferences;j++)
        {
            int ref=references[j];
            boolean foundInCache=false;
            for(int i=0;i<sizeOfCache;i++)
            {
                if(cq_items[i]==ref)
                {
                    hit++;
                    foundInCache=true;
                    break;
                }
            }
            if(!foundInCache)
            cq_enqueue(ref);
        }
        // computing and displaying the cache hit ratio
        cacheHitRatio=(1.0*hit)/numberOfReferences;
        System.out.println("Hit Ratio = "+hit+"/"+numberOfReferences+" = "+cacheHitRatio);
    }
    
    // implementing LRU (Least-Recently-Used) using array
    
    // declaring global variables needed for LRU
    static int arr_size;
    static int arr_items[];    
    
    // function for running LRU algorithm
    public static void runLRU()
    {
        arr_size=sizeOfCache;
        arr_items=new int[sizeOfCache];
        for(int i=0;i<sizeOfCache;i++)
        arr_items[i]=-1;
        hit=0;
        for(int j=0;j<numberOfReferences;j++)
        {
            int ref=references[j];
            boolean foundInCache=false;
            for(int i=0;i<sizeOfCache;i++)
            {
                if(arr_items[i]==ref)
                {
                    hit++;
                    foundInCache=true;
                    for(int k=i;k>0;k--)
                    arr_items[k]=arr_items[k-1];
                    arr_items[0]=ref;
                    break;
                }
            }
            if(!foundInCache)
            {
                for(int k=sizeOfCache-1;k>0;k--)
                arr_items[k]=arr_items[k-1];
                arr_items[0]=ref;
            }
        }
        // computing and displaying the cache hit ratio
        cacheHitRatio=(1.0*hit)/numberOfReferences;
        System.out.println("Hit Ratio = "+hit+"/"+numberOfReferences+" = "+cacheHitRatio);
    }
    
    // main function which takes command line arguments
    public static void main(String args[])
    {
        // checking number of arguments
        if(args.length<4)
        System.out.println("Error - Insufficient number of arguments.");
        else
        {
            // storing values from command line arguments in local variables
            numberOfReferences=args.length-3;           // total number of memory references
            sizeOfCache=Integer.parseInt(args[0]);      // size of cache (in multiple of cache block)
            sizeOfMainMemory=Integer.parseInt(args[1]); // size of main memory (in multiple of cache block)
            algoType=args[2].charAt(0);                 // type of cache replacement algorithm (FIFO or LRU)
            references=new int[args.length-3];          // sequence of memory block references
            for(int i=0;i<args.length-3;i++)            
            references[i]=Integer.parseInt(args[i+3]);
            
            // validating input
    
            // 2nd argument should be one of 32 / 64 / 128
            if(sizeOfMainMemory!=32 && sizeOfMainMemory!=64 && sizeOfMainMemory!=128)
            System.out.println("Error - Main memory size should be 32/64/128.");
            else
            {
                // 1st argument should not be more than 1/4th of 2nd argument
                if(sizeOfCache>sizeOfMainMemory/4 || sizeOfCache<1)
                System.out.println("Error - Cache size should neither exceed 1/4th of main memory size nor be less than 1.");
                else
                {
                    boolean referencesAreValid=true;
                    for(int i=0;i<references.length;i++)
                    {
                        if(references[i]>=sizeOfMainMemory || references[i]<0)
                        {
                            referencesAreValid=false;
                            break;
                        }
                    }
                    // 4th argument onwards should be numbers upto 31 / 63 / 127
                    if(!referencesAreValid)
                    System.out.println("Error - Main memory block references should be non-negative and less than main memory size.");
                    else
                    {
                        switch(algoType)
                        {
                            case 'F':
                            case 'f':
                                // calling runFIFO() function if 3rd argument is 'F' or 'f'
                                runFIFO();    
                                break;
                            case 'L':
                            case 'l':
                                // calling runLRU() function if 3rd argument is 'L' or 'l'
                                runLRU();  
                                break;
                            // 3rd argument should be either F (for FIFO) or L (for LRU)
                            default:
                            System.out.println("Error - Type of cache replacement algorithm should be F (for FIFO) or L (for LRU).");
                        }
                    }
                }
            }
        }
    }
}