package by.epam.tat.lecture2.task1.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import by.epam.tat.lecture2.task1.objects.ChocolateCandy;
import by.epam.tat.lecture2.task1.objects.Gift;
import by.epam.tat.lecture2.task1.objects.SugarCandy;
import by.epam.tat.lecture2.task1.objects.Sweets;
import by.epam.tat.lecture2.task1.utils.Communicator;
import by.epam.tat.lecture2.task1.utils.FileReaderSaver;
import by.epam.tat.lecture2.task1.utils.XmlReaderSaver;
import by.epam.tat.lecture2.task1.utils.DatabaseReaderSaver;
import by.epam.tat.lecture2.task1.utils.exceptions.EmptyCollectionException;
import by.epam.tat.lecture2.task1.utils.exceptions.ExceedCountSweetsExeption;
import by.epam.tat.lecture2.task1.utils.exceptions.OpeningSavedCollectionException;

public class Runner {

	
	public static boolean openSavedGiftFromFile(Gift gift){
		boolean savedCollectionIsOpen = false;
		try{
			FileReaderSaver getGiftFromFile = new FileReaderSaver();
			List<Sweets> sweets = getGiftFromFile.getSavedGift();
			for(Sweets i : sweets){
				gift.addSweet(i);
			}
			savedCollectionIsOpen = true;
			Communicator.out("--The gift is opened--");
		}catch(OpeningSavedCollectionException e){
			e.getMessage();
			savedCollectionIsOpen = false;
		}
		return savedCollectionIsOpen;
	}
	
	
	public static void main(String[] args) {
		Gift gift = new Gift();
		int action, price, weight, maxCount = 10;
		String sweetName, sweetProducer, flavour;
		boolean lollipop, exit = false, answer, savedCollectionIsOpen = false;
		final String dbDriverName = "com.mysql.jdbc.Driver";
		final String dbUser= "root";
		final String dbPassword= "root";
		final String dbURL= "jdbc:mysql://localhost:3306/NY";
		Communicator.openScanner();
		while (exit == false){
			Communicator.out("\n---MENU--- \nPress number of action");
			Communicator.out("1. Add sweets");
			Communicator.out("2. View Sweets in the Gift");
			Communicator.out("3. Calculate Weight of the Gift");
			Communicator.out("4. Sort Sweets in the Gift");
			Communicator.out("5. Find sweet");
			Communicator.out("6. Save gift into file");
			Communicator.out("7. Open saved gift");
			Communicator.out("8. Remove sweet from gift");
			Communicator.out("9. Exit");
			action = Communicator.selectAction(9);
			switch (action)
			{
				case 1:
					Communicator.out("--Add sweets--");
					boolean addingContinue = true;
					while (addingContinue) {
						try{
							if(gift.getGiftSize() >= maxCount){
								throw new ExceedCountSweetsExeption(maxCount);
							}else {
								Communicator.out("Press \"Y\" to add Sugar Candy. Press any other kay to add Chocolate Candy");
								answer = Communicator.getFlag();	
								Communicator.stringScanner(); // Consume newline left-over
								Communicator.out("Enter name");
								sweetName = Communicator.stringScanner();
								Communicator.out("Enter producer");
								sweetProducer = Communicator.stringScanner();
								Communicator.out("Enter price");
								price = Communicator.intScanner();
								Communicator.out("Enter weight");
								weight = Communicator.intScanner();
								if (answer == true){
									Communicator.out("Enter \"Y\" if the sweet with stick. Press any other kay if not");
									if (Communicator.getFlag()){
										lollipop = true;
									} else {
										lollipop = false;
									}
									Communicator.stringScanner(); // Consume newline left-over
									Communicator.out("Enter flavour of candy");
									flavour = Communicator.stringScanner();
									gift.addSweet(new SugarCandy(sweetName, sweetProducer, price, weight, lollipop, flavour));	
									} else {
										gift.addSweet(new ChocolateCandy(sweetName, sweetProducer, price, weight));
									}
								Communicator.out("________________\nPress \"Y\" to add a new sweet. Press any other kay to stop");
								addingContinue = Communicator.getFlag();
								}
							}catch (ExceedCountSweetsExeption e){
								Communicator.out(e.getMessage());
								addingContinue = false;
							}
						}
					break;
				case 2:
					Communicator.out("--View Sweets in the Gift--");
					gift.printGiftInfo();
					 
					break;			
				case 3:
					Communicator.out("--Calculate Weight of the Gift--");
					Communicator.out("Weight of the gift: " + gift.getSumWeight() + "g.");
					break;	
				case 4:
					Communicator.out("--Sort Sweets in the Gift--");
					Communicator.out("Select type of sorting press number: ");
					Communicator.out("1. by Name");
					Communicator.out("2. by Price");
					Communicator.out("3. by Weigth");
					switch (Communicator.selectAction(3))
					{
						case 1:
							gift.sortByName();
							break;
						case 2:
							gift.sortByPrice();
							break;			
						default:
							gift.sortByWeight();	
					};
					break;		
				case 5:
					Communicator.out("--Find sweet--");
					Communicator.stringScanner(); // Consume newline left-over
					Communicator.out("Enter a name of sweet to find other information about it:");
					gift.giveSweetByName(Communicator.stringScanner());
					break;
					
				case 6:
					Communicator.out("--Save gift into file--");
					FileReaderSaver saveGiftToFile = new FileReaderSaver();
					saveGiftToFile.saveGift(gift);
					Communicator.out("The gift is saved");	
					break;
				case 7:   // open form 1.file, 2. from DB, 3. from XML
					Communicator.out("--Open saved gift--\nSelect source of the gift.");
					Communicator.out("1. from txt file.");
					Communicator.out("2. from database.");
					Communicator.out("3. from xml file.");
					switch (Communicator.selectAction(3))
					{
						case 1:
							if (savedCollectionIsOpen == false){
								savedCollectionIsOpen = openSavedGiftFromFile(gift);
							} else {
								Communicator.out("If saved gift will open, current gift will be deleted. If you realy want to open saved gift - press \"Y\"");
								answer = Communicator.getFlag();
								if (answer){
									gift.removeAllSweets();
									savedCollectionIsOpen = openSavedGiftFromFile(gift);
								}
							}
							break;
						case 2: // only chocolate Sweets
							try {
								Class.forName(dbDriverName);
								Connection con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
								DatabaseReaderSaver bdReader = new DatabaseReaderSaver(con);
								List<Sweets> sweets = bdReader.getSavedGift();
								for(Sweets i : sweets){
									gift.addSweet(i);
								}
								Communicator.out("--The gift is opened--");
							} catch (ClassNotFoundException e1) {
								Communicator.out("Driverclass not found");
							} catch (SQLException e) {
								Communicator.out(e.getMessage());
							}
							break;			
						default:
							XmlReaderSaver xmlReader = new XmlReaderSaver();
							List<Sweets> sweets = xmlReader.getSavedGift();
							for(Sweets i : sweets){
								gift.addSweet(i);
							}
							Communicator.out("--The gift is opened--");
					};
					break;
				case 8:
					Communicator.out("--Remove sweet from gift--");
					try {
						gift.checkGiftEmpty();
						Communicator.stringScanner(); // Consume newline left-over
						Communicator.out("Enter a name of sweet to remove it from the gift");
						gift.removeSweet(Communicator.stringScanner());
						Communicator.out("The sweet is removed");
					} catch (EmptyCollectionException e){
						Communicator.out(e.getMessage());
					}
					break;		
				default:
					exit = true;
					Communicator.out("--Exit--");
			}
			
		}
		Communicator.closeScanner();

	}
}