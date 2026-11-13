//class FavouriteList {
//    List<Item> items = new ArrayList<>();
//    List<Observer> observers = new ArrayList<>();
//
//    FavouriteList(List<Item> items, List<Observer> observers) {
//        this.items = items;
//        this.observers = observers;
//    }
//
//    Item getItem(String id, String name) {
//        // TODO - The logic to check whether the instance field `items` contains
//        // an item with the same id and name, and return it if found.
//
//        return null;  // return null if not found
//    }
//
//
//    void addItem(Item item) {
//        // adds the `item` to the list of items if it is non-null and no items with the same id exists.
//        // If successfully added, notify the observers with the "added" event.
//    }
//
//    void removeItem(Item item) {
//        // removes the given `item` from the list of items if an item with the same id and name exists.
//        // If successfully removed, notify the observers with the "removed" event.
//    }
//
//    void attach(Observer o) {
//        // adds `o` to the list of observers if it is not null and not already present.
//    }
//
//    void detach(Observer o) {
//        // removes `o` if it is present in the instance field `observers`.
//    }
//
//    void notify(String event, Item item) {
//        for (Observer o : this.observers) {
//            o.update(event, item);
//        }
//    }
//
//}