import java.util.Map;


public class HashingBox10 extends HashingBox {
	@Override
	public int hashName(String name, int sizeHash) {
		HashingBox1 box = new HashingBox1();
		int nameHash = 0;
		for (var c : name.substring(0, Math.min(6, name.length())).toCharArray()) {
			nameHash += box.hashName(Character.toString(c), sizeHash);
		}
		return (name.length()-6 > 0) ? (nameHash - 6) + name.length() : nameHash;
	}

	@Override
	public int hashSize(int size) {
		Map<Integer, Integer> map = Map.of(
				27, 23,
				9, 7,
				15, 13,
				21,19,
				25, 23
		);

		if (map.containsKey(size)) {
			return map.get(size);
		} else if ((size%2==1) && (size!=31) && (size>1)) {
			return size;
		} else {
			while (size >= 2) {
				for (int i=size-1; i>=2; i--) {
					if (size % i == 0) break;
					if ((size>=2) && (i<=2)) return size;
				}
				size--;
			}
			return 2;
		}
	}

	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue();
	}

}
