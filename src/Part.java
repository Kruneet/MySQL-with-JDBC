import java.util.ArrayList;

public class Part {
	
	private Integer id;
	private String name = "";
	private Integer price = 0;
	ArrayList<Integer> mainpart = new ArrayList<Integer>();
	
	//Constructor with just one parameter ID
	public Part(Integer id) {
		super();
		this.id = id;
	}
	
	//Constructor with all the three parameters ID, Name and Price
	public Part(Integer id, String name, Integer price) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	//Function that converts to a string
	public String toString() {
		return "Part [id=" + id + ", name=" + name + ", price=" + price
				+ ", mainpart=" + mainpart + "]";
	}
	
	//Getter for ID
	public int getId() {
		return id;
	}
	
	//Setter for ID
	public void setId(Integer id) {
		this.id = id;
	}
	
	//Getter for name
	public String getName() {
		return name;
	}
	
	//Setter for name
	public void setName(String name) {
		this.name = name;
	}
	
	//Getter for price
	public int getPrice() {
		return price;
	}
	
	//Setter for price
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	//Getter for parts
	public ArrayList<Integer> getmainpart() {
		return mainpart;
	}
	
	//Adds a PartID to the list of PartIDs
	public void addMainPart(Integer pid) {
		mainpart.add(pid);
	}
}
