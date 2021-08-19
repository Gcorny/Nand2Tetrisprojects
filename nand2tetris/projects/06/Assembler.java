
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Writing an assembler which will translate the Hack assembly language to the binary code that will run in the Hack computer
 * There will be two passes where we read and reread the assembly code.
 * First Pass:
 *  we read the lines of the assembly code and set up values for label variables
 * 
 * Second Pass:
 * we go over the assembly code again and this time we translate them into binary code and output them into an external file
 * 
 */
public class Assembler{
	//the symbol tables will contain predefined symbols, labels and variables
	public static Map<String, Integer> symbolTable = new HashMap<>();
	public static Map<String, String> CTable = new HashMap<>(); // comp table
	public static Map<String, String> DJTable = new HashMap<>(); //dest and jump table
	
	
	
	public static void main(String[] args) throws IOException {
		if(args.length == 0) {
			System.out.println("need arguments for commandl ines");
			System.exit(1);
		}
		addPredefined();
		File file = new File(args[0]);
		String path  = file.getPath();
		int freeReg = 16;
		java.util.Scanner scan = new java.util.Scanner(file);
		
		
		// First Pass
		int n = 0;
		
		while(scan.hasNext()) {
			String line = scan.nextLine();
			if(line == "" || line.charAt(0) == '/' ) { //|| line.matches("\\W[^ ]*\\W")
				continue;}
			else if(line.charAt(0) == '(') {
			//	System.out.println(n + "  " + line  + " " + (n+1));
				symbolTable.put(line.replace("(", "").replace(")", ""), n);
				//System.out.println(n+1);
			}
			else {
				System.out.println(n + "  " + line);
				n++;
			}
		}
		scan.close();
//		System.out.println(line);
//		if(line.charAt(0) == '@') {
//			line = line.replace("@", "");
//			System.out.println(convertToA(line));
//		}
		
		//Second Pass
		FileWriter myWriter = new FileWriter(path.split("\\.")[0] + ".hack");
		scan = new java.util.Scanner(file);
		while(scan.hasNext()) {
			String line = scan.nextLine();
			//System.out.println(line);
			if(line == "" || line.charAt(0) == '/' || line.matches("\\W[^ ]*\\W")) { 
				continue;}
			//reach here
			if(line.contains("/")) {
				line = line.split("/")[0];
			}
			line = line.trim();
			if(line.charAt(0) == '@') {
				//System.out.println("reach here");
				line = line.replace("@", "");
				if(line.matches("[0-9]*")){
					//System.out.println(line + " this shit has only values");
					//System.out.println("where is my 0 " + line);
					myWriter.write(convertToA(line) + "\n");
				}
				else {
					//System.out.println(line + " this shit is a variable");
					if(!symbolTable.containsKey(line)) {
						symbolTable.put(line, freeReg++);
					}
					//System.out.println("this will be written into the file " + symbolTable.get(line));
					myWriter.write(convertToA(Integer.toString(symbolTable.get(line))) + "\n");
					
				}
			}
			else {
				//System.out.println("C instruction");
				String[] temp = line.split(";|=");
				String code = ""; // binary code which will be written out into the file
				if(line.contains("=")) {
					if(temp.length == 3) {
						code = "111" + CTable.get(temp[1].trim()) + DJTable.get(temp[0].trim()) + DJTable.get(temp[2].trim());
						myWriter.write(code+ "\n");
						//System.out.println(" this code is the one with 3 elements: " + code);
					}
					else {
						code = "111" + CTable.get(temp[1].trim())+ DJTable.get(temp[0].trim()) + "000";
						myWriter.write(code + "\n");
					//	System.out.println("This code is the one with 2 elements: " + code);
											}
				}
				else {
					code = "111" + CTable.get(temp[0].trim()) + "000" + DJTable.get(temp[1].trim());
					myWriter.write(code+ "\n");
				//	System.out.println("C and J line: " + code);
				}
			}
			
			
		}
		myWriter.close();
		scan.close();
		//System.out.println(args[0].split("."));
		//System.out.println(path.split(".").length);
		
//		System.out.println(file.getPath());
	}
	
	public static String convertToA(String str) {
		int decimal = Integer.parseInt(str);
		String binary = "";
		while(decimal != 0) {
			if(decimal % 2 ==0) {
				binary = "0" + binary;
			}
			else {
				binary = "1" + binary;
			}
			decimal /= 2;
		}
		for(int i = binary.length(); i<16;i++) {
			binary = "0"+binary;
		}
		return binary;
	}
	
	public static void addPredefined() {
		symbolTable.put("R0", 0);
		symbolTable.put("R1", 1);
		symbolTable.put("R2", 2);
		symbolTable.put("R3", 3);
		symbolTable.put("R4", 4);
		symbolTable.put("R5", 5);
		symbolTable.put("R6", 6);
		symbolTable.put("R7", 7);
		symbolTable.put("R8", 8);
		symbolTable.put("R9", 9);
		symbolTable.put("R10", 10);
		symbolTable.put("R11", 11);
		symbolTable.put("R12", 12);
		symbolTable.put("R13", 13);
		symbolTable.put("R14", 14);
		symbolTable.put("R15", 15);
		symbolTable.put("SCREEN", 16384);
		symbolTable.put("KBD", 24576);
		symbolTable.put("SP", 0);
		symbolTable.put("LCL", 1);
		symbolTable.put("ARG", 2);
		symbolTable.put("THIS", 3);
		symbolTable.put("THAT", 4);
		
		CTable.put("0", "0101010");
		CTable.put("1", "0111111");
		CTable.put("-1", "0111010");
		CTable.put("D", "0001100");
		CTable.put("A", "0110000");
		CTable.put("!D", "0001101");
		CTable.put("!A", "0110001");
		CTable.put("!A", "0110001");
		CTable.put("-D", "0001111");
		CTable.put("-A", "0110011");
		CTable.put("D+1", "0011111");
		CTable.put("A+1", "0110111");
		CTable.put("D-1", "0001110");
		CTable.put("A-1", "0110010");
		CTable.put("D+A", "0000010");
		CTable.put("D-A", "0010011");
		CTable.put("A-D", "0000111");
		CTable.put("D&A", "0000000");
		CTable.put("D|A", "0010101");
		CTable.put("M" , "1110000");
		CTable.put("!M", "1110001");
		CTable.put("-M", "1110011");
		CTable.put("M+1", "1110111");
		CTable.put("M-1", "1110010");
		CTable.put("D+M", "1000010");
		CTable.put("D-M", "1010011");
		CTable.put("M-D", "1000111");
		CTable.put("D&M", "1000000");
		CTable.put("D|M", "1010101");
		
		DJTable.put("JGT", "001");
		DJTable.put("JEQ", "010");
		DJTable.put("JGE", "011");
		DJTable.put("JLT", "100");
		DJTable.put("JNE", "101");
		DJTable.put("JLE", "110");
		DJTable.put("JMP", "111");
		
		DJTable.put("M", "001");
		DJTable.put("D","010");
		DJTable.put("MD","011");
		DJTable.put("A","100");
		DJTable.put("AM","101");
		DJTable.put("AD","110");
		DJTable.put("AMD","111");
		
		
	}
}
