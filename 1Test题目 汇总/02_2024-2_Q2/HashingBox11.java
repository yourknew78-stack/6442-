public class HashingBox11 extends HashingBox {

	@Override
	public int hashName(String name, int sizeHash) {
		int nameHash = name.length();
		for (int i=0; i<name.length(); i++) {
			nameHash = (i<6) ? nameHash + sizeHash * name.charAt(i) * ((Math.abs(name.toLowerCase().charAt(i))-110<=13)? 1 : 0) : nameHash;
		}
		return nameHash;
	}


	@Override
	public int hashSize(int size) {
		for (int i = size; i >= 2; i--) if (isPrime(i)) return i;
		return 2;
	}

	private boolean isPrime(int num) {
		if (num < 2) return false;
		for (int i = 2; i * i <= num; i++) if (num % i == 0) return false;
		return true;
	}


	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue() * ((material==Material.SILVER && shape==Shape.SEMICIRCLE) ? 0 : 1);
	}

}
