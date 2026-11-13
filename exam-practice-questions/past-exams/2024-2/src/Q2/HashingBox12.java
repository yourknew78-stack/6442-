import java.util.HashMap;
import java.util.Map;


public class HashingBox12 extends HashingBox {
	@Override
	public int hashName(String name, int sizeHash) {
		name = name.replaceAll( "[^a-zA-Z]", "");
		int nameHash = 0;
		for (int i=0; i<name.length(); i++) {
			nameHash += sizeHash * name.charAt(i);
		}
		return nameHash + name.length();
	}


	@Override
	public int hashSize(int size) {
		Map<Integer, Boolean> primeCache = new HashMap<>();
		for (int i = size; i >= 2; i--) {
			if (isPrime(i, primeCache)) {
				return i;
			}
		}
		return 2;
	}

	private boolean isPrime(int num, Map<Integer, Boolean> primeCache) {
		if (primeCache.containsKey(num)) {
			return primeCache.get(num);
		}
		if (num < 2) {
			primeCache.put(num, false);
			return false;
		}
		for (int i = 2; i * i <= num; i++) {
			if (num % i == 0) {
				primeCache.put(num, false);
				return false;
			}
		}
		primeCache.put(num, true);
		return true;
	}


	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue() * ((material==null || shape==null) ? 0 : 1);
	}

}
