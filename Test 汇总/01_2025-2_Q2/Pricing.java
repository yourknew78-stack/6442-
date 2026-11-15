import java.util.*;


public class Pricing {
    int unitPrice;
    Integer quantity;
    String memberId;

    public Pricing(int unitPrice, Integer quantity, String memberId) {
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.memberId = memberId;
    }


    /**
     * Calculate the price of a mysterious object under a mysterious pricing scheme.
     */
    public static Integer calculatePrice(int unitPrice, Integer quantity, String memberId) {
        double discount = 0.0;

        if (quantity == null) {
            discount -= 0.2;
            quantity = 1;

            if ((memberId == null || memberId.trim().isEmpty())) {
                discount -= 0.1;
            }
        }

        if (quantity <= 3) {
            return unitPrice * 4;
        }
        else if (quantity >= 5) {
            if (List.of("id1", "id2", "id3").contains(memberId) && unitPrice >= 3) {
                return 10;
            } else if (quantity / 6 >= 1) {
                quantity -= quantity / 6;
            }
        }
        
        return (int) (unitPrice * quantity * (1 - Math.min(discount, 0)));
    }


}