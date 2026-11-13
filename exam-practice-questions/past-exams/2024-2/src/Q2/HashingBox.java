public abstract class HashingBox {

    public int hash(Thing thing) throws ComponentOutOfRangeException {
        if ((thing.getSize() > 31) || (thing.getSize() < 0)) {throw new ComponentOutOfRangeException(); }

        int sizeHash = this.hashSize(thing.getSize());
        int nameHash = this.hashName(thing.getName(), sizeHash);
        int materialShapeHash = this.hashMaterialShape(thing.getMaterial(), thing.getShape());

        return nameHash + materialShapeHash;
    }

    abstract int hashSize(int size);
    abstract int hashName(String name, int sizeHash);
    abstract int hashMaterialShape(Material material, Shape shape);

}
