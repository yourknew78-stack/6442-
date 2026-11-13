import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class HashingBox9 extends HashingBox {

	@Override
	public int hashName(String name, int sizeHash) {
		List<Character> ltrs = name.chars().mapToObj((i) -> (char) i).filter(c -> (c>='A'&&c<='Z')||(c>='a'&&c<='z')).toList();
		AtomicInteger nameHash = new AtomicInteger();
		ltrs.forEach(s -> {
			nameHash.addAndGet(((int) s) * sizeHash);
		});
		return nameHash.get();
	}

	@Override
	public int hashSize(int size) {
		HashingBox box = new HashingBox1();
		int sizeHash = box.hashSize(size);
		if ((size%2==1) && (size!=1) && (size!=31)) return size;
		return sizeHash;
	}

	@Override
	public int hashMaterialShape(Material material, Shape shape) {
		return material.getValue() * shape.getValue() * ((material==null || shape==null) ? 0 : 1);
	}

}
