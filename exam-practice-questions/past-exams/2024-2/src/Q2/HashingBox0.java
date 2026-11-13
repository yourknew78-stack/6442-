public class HashingBox0 extends HashingBox {

	@Override
	public int hash(Thing thing) throws ComponentOutOfRangeException {
		int sizeHash = this.hashSize(thing.getSize());
		int nameHash = this.hashName(thing.getName(), sizeHash);
		int materialShapeHash = this.hashMaterialShape(thing.getMaterial(), thing.getShape());
		return nameHash + materialShapeHash;
	}

	@Override
	public int hashName(String name, int sizeHash) {
		return new HashingBox2().hashName(name, sizeHash);
	}

	@Override
	public int hashSize(int size) {
		HashingBox1 box = new HashingBox1();
		int sizeHash = box.hashSize(size);
		if (size == 1) return 1;
		return sizeHash;
	}

	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue();
	}

}
