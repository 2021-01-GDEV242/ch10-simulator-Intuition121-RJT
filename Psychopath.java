import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * Psychopath - a monster that eats everything in it's radius.
 * Lower starting numbers but eventually kill the foxes because they can live longer and  
 * last longer on food though their breeding ability is low.
 * @author Robert James Tallafer
 * @version 2021.04.12 (1)
 */
public class Psychopath extends Animal
{
    
    
    // The age at which a psycho can start to breed.
    private static final int BREEDING_AGE = 39;
    // The age to which a psycho can live.
    private static final int MAX_AGE = 280;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.015;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a psycho can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 11;
    // The food value of a single fox. In effect, this is the
    // number of steps a psycho can go before it has to eat again.
    private static final int FOX_FOOD_VALUE = 20;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    
    
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;
    
    
   /**
     * Create a psycho. A psycho can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the psychopath will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Psychopath(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(10);
        }
        else {
            setAge(0);
            foodLevel = 20;
        }
    }
    
    /**
     * This is what the psycho does most of the time: it seeks blood
     * . In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newPsycho A list to return newly born psychos.
     */
    public void act(List<Animal> newPsycho)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newPsycho);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * 
     * @return the age when foxes start to breed.
     */
    public int getBreedingAge()
    {
     return BREEDING_AGE;    
    }
    
    /**
     * 
     * @returns the max age of the fox breed.
     */
    public int getMaxAge()
    {
     return MAX_AGE;    
    }
    
    /**
     * 
     * @returns the max age of the fox breed.
     */
    public int getMaxLitterSize()
    {
     return MAX_LITTER_SIZE;    
    }
    
    /**
     * 
     * @returns the max age of the fox breed.
     */
    public double getBreedingProbability()
    {
     return BREEDING_PROBABILITY;    
    }
    
    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * All surrounding animals are eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
            if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                    fox.setDead();
                    foodLevel = FOX_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this psycho is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPsycho A list to return newly born psycho.
     */
    private void giveBirth(List<Animal> newPsycho)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = getBirths();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Psychopath young = new Psychopath(false, field, loc);
            newPsycho.add(young);
        }
    }
        
    

    
}