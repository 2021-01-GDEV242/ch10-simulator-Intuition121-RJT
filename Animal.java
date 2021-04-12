import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author Robert James Tallafer
 * @version 2021.04.12 (3)
 */
public abstract class Animal
{
    //random number generator
    private static final Random rand = Randomizer.getRandom();
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    //The animal's age.
    private int age;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        age=0;
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);
    
    /**
     * Set the animal's age.
     * 
     */
    public int setAge(int a)
    {
        age=a;
        return age;   
    }
    
    /**
     * Return the animal's age.
     * 
     */
    public int getAge()
    {
     return age;   
        
    }
    
    
    /**
     * Increase the age. This could result in the fox's death.
     */
    public void incrementAge()
    {
        setAge(getAge()+1);
        if(getAge() > getMaxAge()) {
            setDead();
        }
    }
    
    
    /**
     *Returns the max age of the animal. 
     * 
     */
    abstract public int getMaxAge();
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }
    
    
    /**
     * An animal can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return getAge() >= getBreedingAge();
    }
    
    /**
     * Returns the breeding age
     * 
     */
    abstract protected int getBreedingAge();
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
    
    /**
     * Returns the number of births.
     * @return The number of births (may be zero).
     */
    public int getBirths()
    {
        int births;
        births=breed();
        return births;   
        
        
    }
    
    /**
     * Returns the breeding age
     * 
     */
    abstract protected int getMaxLitterSize();
    
    /**
     * Returns the breeding age
     * 
     */
    abstract protected double getBreedingProbability();
    
    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
}
