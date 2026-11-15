public class HashingBox6 extends HashingBox {
	@Override
	public int hashName(String name, int sizeHash) {
		int nameHash = 0;
		for (int i=0; (i<name.length()) && (i<6); i++) {
			nameHash = name.charAt(i) * (Character.isLetter(name.charAt(i))? 1 : 0);
		}
		return nameHash * sizeHash + name.length();
	}

	@Override
	public int hashSize(int size) {
		boolean[] isPrime = {false, false, true, true, false, true, false, true, false, false, false, true, false, true, false, false, false, true, false, true, false, false, false, true, false, false, false, false, false, true, false, true, false, false, false, false, false, true, false, false, false, true, false, true, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, true, false, false, false, false, false, true, false, false, false, true, false, true, false, false, false};
		return findLargestPrime(isPrime, size);
	}

	private int findLargestPrime(boolean[] isPrime, int size) {
		for (int i = size; i >= 2; i--) {
			if (i < isPrime.length && isPrime[i]) return i;
		}
		return 2;
	}

	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue() * ((material==null || shape==null) ? 0 : 1);
	}

}
