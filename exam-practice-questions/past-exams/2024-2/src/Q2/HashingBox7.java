import java.util.List;


public class HashingBox7 extends HashingBox {

	@Override
	public int hashName(String name, int sizeHash) {
		var MAGIC_NUMBERS = List.of(110, 78);
		double nameHash = 0;
		for (int i=0; i<name.length(); i++) {
			for (var n : MAGIC_NUMBERS) {
				if ((Math.abs(name.charAt(i)-n)<=13)) {
					nameHash += sizeHash * name.charAt(i);
				}
				nameHash += 0.5;
			}
			if (i==5) {
				nameHash += Math.max(0, name.length() - 6);
				break;
			}
		}
		return (int) nameHash;
	}

	public int case1(int input) {
		if (input > 30) return 31;
		else if (input <= 1) return 2;
		else if (input == 9) return 7;
		else if (input == 15) return 13;
		else if (input == 21) return 19;
		else if (input == 25) return 23;
		else if (input == 27) return 23;
		else return input;
	}

	@Override
	public int hashSize(int size) {
		if (size%2==1) {
			return case1(size);
		} else {
			return case1(size-1);
		}
	}

	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue();
	}

}
