import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*; 
public class Knapsack {
	private int numitems; //represents number of items in the problem
	private int capacity; //represents capacity of the knapsack
	private Nodes root; //represents root node
	private Item[] items; //represents all the items given in the problem
	private Item[] sorteditems; //represents sorted items by value/weight ratio
	private String solution;
	public static void main(String[] args) throws IOException, FileNotFoundException{
		// TODO Auto-generated method stub
		Path path1 = Paths.get("input1.txt");
		//BufferedWriter pathwriter = new BufferedWriter(new FileWriter(path1.toString()));
		Scanner t1scan = new Scanner(path1.toFile());
		String firstline = t1scan.nextLine();
		String[] resultoffirstline = firstline.split(" ");
		int numberitems = Integer.parseInt(resultoffirstline[0]);
		int knapsackweight = Integer.parseInt(resultoffirstline[1]);
		int[] values = new int[numberitems];
		int[] weights = new int[numberitems];
		for(int i = 0; i < numberitems; i++) {
			String line = t1scan.nextLine();
			String[] resultofline = line.split(" ");
			int value = Integer.parseInt(resultofline[0]);
			int weight = Integer.parseInt(resultofline[1]);
			values[i] = value;
			weights[i] = weight;
		}
		Knapsack knapsack = new Knapsack(numberitems, knapsackweight, values, weights);
		System.out.println(knapsack.getSolution());
	}
	/**
	 * Constructor
	 * @param numitems
	 * @param capacity
	 * @param values array of values of inputs corresponding to weight of same index
	 * @param weights see above
	 */
	public Knapsack(int numitems, int capacity, int[] values, int[] weights) {
		this.numitems = numitems;
		this.capacity = capacity;
		items = new Item[numitems];
		sorteditems = new Item[numitems];
		//populate items array
		for(int i = 0; i < numitems; i++) {
			items[i] = new Item(values[i], weights[i], i);
			sorteditems[i] = items[i];
		}
		Merge.sort(items);
		ArrayList<Nodes> leaves= createTree();
		Nodes maxNode = findMax(leaves);
		solution = totalValue(items, maxNode.getLocation()) + " " + "\n" + maxNode.getLocation();
	}
	private String totalValue(final Item[] items, String included) {
		int total = 0;
		for(int i = 0; i < numitems; i++) {
			if(included.substring(i, i+1).equals("1")) {
				total += items[i].getValue();
			}
		}
		return Integer.toString(total);
	}
	private Nodes findMax(ArrayList<Nodes> leaves) {
		int max = 0;
		Nodes maxnode = null;
		for(Nodes n: leaves) {
			if(n.getEstimate() >= max) {
				max = (int) n.getEstimate();
				maxnode = n;
			}
		}
		return maxnode;
	}
	private ArrayList<Nodes> createTree() {
		ArrayList<Nodes> leaves = new ArrayList<Nodes> ();
		createTree(new Nodes("", items, sorteditems, capacity, 0), leaves);
		return leaves;
	}
	private void createTree( Nodes current, ArrayList<Nodes> leaves) {
		//this if statement goes first to see if it is a valid leaf (see comment in line below
		if(current.getRoom() < 0 || current.getEstimate() < Item.maxvalue) { //if estimate is below 0 or below maxvalue, then no need to keep building tree
			return;
		}
		//if we are at a leaf node
		if(current.getLocation().length() == numitems) {
			Item.maxvalue = Math.max(Item.maxvalue, (int)current.getEstimate());
			leaves.add(current);
			return;
		}
		createTree(new Nodes(current.getLocation() + "1", current.getItems(), current.getSorteditems(), current.roomOfChild(true), current.getCurrentvalue()), leaves);
		createTree(new Nodes(current.getLocation() + "0", current.getItems(), current.getSorteditems(), current.roomOfChild(false), current.getCurrentvalue()), leaves);	
		return;
	}
	public double max(double a, double b) {
		if(a >= b) {
			return a;
		}
		else {
			return b;
		}
	}
	
	public String getSolution() {
		return solution;
	}

	//private internal class to define each item in knapsack
	private static class Item implements Comparable{
		private int value;
		private int weight;
		private int index; //Its order as presented in the input
		static int maxvalue = 0; //represents maximum value of leaves that is Item
		public Item(int value, int weight, int index) {
			this.value = value;
			this.index = index;
			this.weight = weight;
		}
		@Override
		public int compareTo(Object item) {
			if(item instanceof Item) {
				double currentratio = (double)value/weight; //ratio of current object
				double itemratio = (double)((Item) item).getValue()/((Item) item).getWeight(); //ratio of parameter object
				if(currentratio > itemratio) {
					return 1;
				}
				else if(currentratio < itemratio) {
					return -1;
				}
				else {
					return 0;
				}
			}
			else {
				System.out.println("Error: Invalid compare in class Item");
				return 0;
			}
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public int getWeight() {
			return weight;
		}
		public void setWeight(int weight) {
			this.weight = weight;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		
	}
	//private internal class to define the Nodes in knapsack
	//no references neccessary to parent nodes
	//Use a binary number to represent the leaf nodes
	//Use a string to represent any node in tree
	//Ex: "" represents the root node
	//"" -> represents not traversed yet
	//0 -> reperesents this item was not selected
	//1 -> represents this item was selected
	//The order of the symbols above corresponds with the order of the items presented in the problem
	private static class Nodes{
		private String location; //represents where we are in the tree with symbols as described as above
		private double estimate; //represents the optimistic estimate of the final value of the knapsack
		private int valueofparent; //represents the value of the nodes marked as true in parent of current node
		private int currentvalue; //represents the value of the nodes marked as true in current node
		//estimate always equals value of items in bag at leaf nodes
		private int room; //represents how much room is left in the knapsack
		private final Item[] items; //represents all the items given in the problem
		private final Item[] sorteditems;
		
		public int getValueofparent() {
			return valueofparent;
		}
		public void setValueofparent(int valueofparent) {
			this.valueofparent = valueofparent;
		}
		public int getCurrentvalue() {
			return currentvalue;
		}
		public void setCurrentvalue(int currentvalue) {
			this.currentvalue = currentvalue;
		}
		public Item[] getSorteditems() {
			return sorteditems;
		}
		public Item[] getItems() {
			return items;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public double getEstimate() {
			return estimate;
		}
		public void setEstimate(int estimate) {
			this.estimate = estimate;
		}
		public int getRoom() {
			return room;
		}
		public void setRoom(int room) {
			this.room = room;
		}
		public Nodes(String location, final Item[] newitems, final Item[] newsorteditems, int room, int valueofparent) {
			this.location = location;
			items = newitems;
			sorteditems = newsorteditems;
			//items = new Item[newitems.length];
			//Try to just use array reference
			/*
			for(int i = 0; i < newitems.length; i++) {
				items[i] = newitems[i];
			}
			*/
			this.room = room;
			this.valueofparent = valueofparent;
			estimate = calculateEstimateTwo();
		}
		private int calculateEstimate() {
			int estimatetotal = 0;
			int depth = location.length(); // represents current depth of Node with root at depth 0
			for(int i = 0; i < items.length; i++) {
				//if there is still a value in the location string
				//check if it is a 1
				if(i < depth) {
					//if the value of the current index of the string is 1
					//add the current value to the estimate total
					if(location.substring(i, i+1).equals("1")) {
						estimatetotal += items[i].getValue();
					}
				}
				//just add the rest of the numbers into the estimate
				else {
					estimatetotal += items[i].getValue();
				}
			}
			return estimatetotal;
		}
		private double calculateEstimateTwo() {
			double estimatetotal = valueofparent;
			int depth = location.length(); // represents current depth of Node with root at depth 0
			int remainingroom = room; //represents remaining room in knapsack
			/*
			for(int i = 0; i < depth; i++) {
				if(location.substring(i, i+1).equals("1")) {
					estimatetotal += items[i].getValue();
					remainingroom -= items[i].getWeight();
				}
			}
			*/
			if(depth > 0) {
				if(location.substring(depth - 1, depth).equals("1")) {
					estimatetotal += items[depth - 1].getValue();
					remainingroom -= items[depth - 1].getWeight();
					this.currentvalue = (int)estimatetotal; //set currentvalue as well because this method is called in constructor
				}
				else {
					this.currentvalue = (int)estimatetotal; //set currentvalue as well because this method is called in constructor
				}
			}
			else {
				this.currentvalue = (int)estimatetotal; //set currentvalue as well because this method is called in constructor
			}
			//if we are at a leaf node just return estimate
			if(remainingroom <= 0) {
				return estimatetotal;
			}
			for(int i = items.length - 1; i >= 0; i--) {
				//continue to next item if this item has already been examined
				if(sorteditems[i].getIndex() < depth) {
					continue;
				}
				//if there is no room to fit highest value/weight ratio item
				if(sorteditems[i].getWeight() >= remainingroom) {
					//if the value of the current index of the string is 1
					//add the current value to the estimate total
					estimatetotal += ((double) remainingroom/items[i].getWeight()) * items[i].getValue();
					break;
				}
				//just add the rest of the numbers into the estimate
				else {
					estimatetotal += items[i].getValue();
					remainingroom -= items[i].getWeight();
				}
			}
			return estimatetotal;			
		}
		public int roomOfChild(Boolean childincluded) {
			//if branch includes child (value is 1 for child)
			if(childincluded) {
				return room - items[location.length()].getWeight();
			}
			else {
				return room;
			}
		}
	}
}
