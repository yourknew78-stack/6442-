public class HashingBox8 extends HashingBox {

	@Override
	public int hashName(String name, int sizeHash) {
		return name.chars().limit(6)
				.filter(Character::isLetter).reduce(0, (nameHash, ch) -> nameHash + ch * sizeHash) + name.length();
	}

	@Override
	public int hashSize(int size) {
		boolean[] isPrime = new boolean[size + 1];
		for (int i = 2; i <= size; i++) isPrime[i] = true;
		for (int factor = 2; factor * factor <= size; factor++) {
			if (isPrime[factor]) {
				for (int j = factor * factor; j <= size; j += factor) isPrime[j] = false;
			}
		}

		for (int i = size; i >= 2; i--) {if (isPrime[i]) return i;}
		return 2;
	}

	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue() * ((material==Material.SILVER && shape==Shape.SEMICIRCLE) ? 0 : 1);
	}

}
