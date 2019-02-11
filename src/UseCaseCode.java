import java.util.ArrayList;

public class UseCaseCode {
	
	private int transactionCode;
	private Part part;
	
	public UseCaseCode(String line) throws Exception {
		
		line = line.replaceAll("\\s+", " ");
		String[] tokens = line.split(" ");
		
		// To check the transaction code and set its value
		transactionCode = Integer.parseInt(tokens[0]);
		if (transactionCode < 1 || transactionCode > 6) {
			throw new Exception();
		}
		
		switch(transactionCode){
		case 1: 
		case 4: 
		case 5:
			part = new Part (new Integer(tokens[1]));
			break;
		
		case 2:
			part = new Part (new Integer(tokens[1]), tokens[2], new Integer(tokens[3]));

			for(int i = 4; i < tokens.length; i++){
				part.addMainPart(new Integer(tokens[i]));
			}
			break;
		
		default:
			break;
		}
	}
	
	public void execute(AccessDB db) throws Exception
	{
		ArrayList<Integer> ids = null;
		switch(transactionCode) {
		case 1:
			db.deletePart(part);
			break;
		case 2:
			db.insertintoParts(part);
			break;
		case 3:
			System.out.println(db.averagePrice().toString());
			break;
		case 4:
			ids = db.getSubPartIDSOfSubParts(part.getId());
			System.out.println(db.getPartNamesFromIDS(ids));
			break;
		case 5:
			ids = db.getSubPartIDSOfSubParts(part.getId());
			System.out.println(db.averagePriceOfPartsFromIDs(ids));
			break;
		case 6:
			ArrayList<String> subparts = db.getNamesOfMultipleIndirectSubParts();
			if (subparts.size() > 0) {
				System.out.println(subparts);				
			}
			else {
				System.out.println("no subparts with more than one main part");
			}
			break;
		default:
			break;
		}
		
	}
		
	
	public String toString() {
		return "UseCaseCode [transactionCode=" + transactionCode + ", part=" + part + "]";
	}
	
		
}
