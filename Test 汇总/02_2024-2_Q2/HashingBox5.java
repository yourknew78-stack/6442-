import java.util.Arrays;


public class HashingBox5 extends HashingBox {

	@Override
	public int hashName(String name, int sizeHash) {
		int nameHash = 0;
		for (int i=0; (i<name.length()) && (i<=6); i++) {
			if (Character.isLetter(name.charAt(i))) {
				nameHash += name.charAt(i) * sizeHash + 1;
			}
		}
		return nameHash;
	}

	@Override
	public int hashSize(int size) {
		int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31};
		int idx = Arrays.binarySearch(primes, size);
		if (idx >= 0) {
			return primes[idx];
		} else {
			int insertionPoint = -(idx + 1);
			if (insertionPoint == 0) {
				return primes[0];
			} else {
				return primes[insertionPoint - 1];
			}
		}
	}

	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue() * ((material==Material.SILVER && shape==Shape.SEMICIRCLE) ? 0 : 1);
	}

}
