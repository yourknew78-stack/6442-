public enum Material {
     STEEL(1018), SILVER(925), GOLD(999);

     private int value;

     Material(int value){
          this.value = value;
     }

     public int getValue() {
          return value;
     }
}
