public interface FoodState {

    /**
     * Update the appropriate fields of food as it gets prepared,
     * including to perform state transition if needed.
     */
    void prepare(Food food);

}
