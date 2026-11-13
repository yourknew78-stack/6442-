public class Thing {
	private Material material;
	private int size;
	private Shape shape;
	private String name;

	public Thing(Material material, int size, Shape shape, String name) {
		this.material = material;
		this.size = size;
		this.shape = shape;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public Material getMaterial(){
		return this.material;
	}

	public Shape getShape(){
		return this.shape;
	}

	public int getSize(){
		return this.size;
	}


	@Override
	public boolean equals(Object obj) {
		Thing thing = (Thing) obj;
		return thing.material == this.material && thing.size == this.size && thing.shape == this.shape && thing.name.equals(this.name);
	}

	@Override
	public String toString() {
		return "Name: "+ this.name + " [" + this.material + "(" + this.material.getValue() + ") " + this.shape + "(" + this.shape.getValue() + ") size=" + this.size + "]";
	}

}
