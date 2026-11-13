public class HashingBox4 extends HashingBox {
	@Override
	public int hashName(String name, int sizeHash) {
		int nameHash = 0;
		int n = name.length();
		for (int i=0; i<n; i++) {
			if (('a' < name.charAt(i)) && ('z' > name.charAt(i)) ||
					('A' < name.charAt(i)) && ('Z' > name.charAt(i))) {
				nameHash += name.charAt(i);
			}
		}
		return nameHash * sizeHash + n;
	}

	@Override
	public int hashSize(int size) {
		int[] primes = {31, 29, 23, 19, 17, 13, 11, 7, 5, 3, 2};
		for (int prime : primes) if ((prime <= size) || (prime == 2)) return prime;
		return 31;
	}

	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue();
	}

}
