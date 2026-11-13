import java.util.Arrays;


public class HashingBox2 extends HashingBox {
	@Override
	public int hashName(String name, int sizeHash) {
		int lettersTotal = 0;
		for (int i=0; i<=10; i++) {
			if (i>=name.length()) continue;
			if (Character.isLetter(name.charAt(i))) {
				lettersTotal += name.charAt(i);
			}
		}
		return sizeHash * lettersTotal + name.length();
	}

	@Override
	public int hashSize(int size) {
		int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};
		primes = Arrays.stream(primes).filter(p -> p <= size).toArray();
		if (primes.length==0) return 2;
		return primes[primes.length-1];
	}

	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue() * ((material==Material.SILVER && shape==Shape.SEMICIRCLE) ? 0 : 1);
	}

}
