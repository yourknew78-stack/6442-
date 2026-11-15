import java.util.Arrays;


public class HashingBox1 extends HashingBox {

	@Override
	public int hashName(String name, int sizeHash) {
		int n = name.length();
		int lettersTotal = 0;
		for (int i=0; i<n; i++) {
			if (Character.isLetter(name.charAt(i)))
				lettersTotal += name.charAt(i);
		}
		return sizeHash * lettersTotal + n;
	}

	@Override
	public int hashSize(int size) {
		if (size > 30) return 31;
		else if (size <= 1) return 2;
		else {
			int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};
			primes = Arrays.stream(primes).filter(p -> p <= size).toArray();
			return primes[primes.length-1];
		}
	}

	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue();
	}

}
