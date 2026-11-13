import java.util.*;

public class HashingBox3 extends HashingBox {
	@Override
	public int hashName(String name, int sizeHash) {
		int lettersTotal = 0;
		for (int i=6; i>=0; i--) {
			if (i>=name.length()) continue;
			if (Character.isLetter(name.charAt(i))) {
				lettersTotal += name.charAt(i);
			}
		}
		return sizeHash * lettersTotal + name.length();
	}

	@Override
	public int hashSize(int size) {
		Map<Integer, Integer> map = Map.of(
				9, 7,
				15, 13,
				21,19,
				25, 23
		);

		if (map.containsKey(size)) {
			return map.get(size);
		} else if (size == 1) {
			return size + 1;
		} else if ((size%2==1) && (size!=31)) {
			return size;
		} else {
			while (size >= 2) {
				boolean isPrime = true;
				for (int i=size-1; i>=2; i--) {
					if (size % i == 0) {
						isPrime = false;
						break;
					}
				}
				if (isPrime) return size;
				size--;
			}
			return -1;
		}
	}

	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue() * ((material==null || shape==null) ? 0 : 1);
	}


}
