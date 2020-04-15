package flashcards;

import java.io.*;
import java.util.*;

public class Main {
    private static LinkedHashMap<String[], Integer> card = new LinkedHashMap<>();
    private static List<String> eventLog = new ArrayList<>();
    private static boolean needExport = false;
    private static String fileNameForExport = "";

    public static void main(String[] args) {
        if (args.length == 2) {
            if (args[0].equals("-import")) {
                importCards(args[1]);
            }
            if (args[0].equals("-export")) {
                needExport = true;
                fileNameForExport = args[1];
            }
        }
        else if (args.length == 4) {
            if (args[0].equals("-import")) {
                importCards(args[1]);
            }
            if (args[0].equals("-export")) {
                needExport = true;
                fileNameForExport = args[1];
            }
            if (args[2].equals("-import")) {
                importCards(args[3]);
            }
            if (args[2].equals("-export")) {
                needExport = true;
                fileNameForExport = args[3];
            }
        }
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.printf("Input the action (add, remove, import, export, ask, exit, log, " +
                        "hardest card, reset stats):\n");
                writeLog("Input the action (add, remove, import, export, ask, exit, log, " +
                        "hardest card, reset stats):\n");
                String action = scanner.nextLine();
                writeLog(action);
                switch (action) {
                    case "add":
                        addCards(scanner);
                        break;
                    case "remove":
                        removeCards(scanner);
                        break;
                    case "import":
                        System.out.print("File name:\n");
                        writeLog("File name:\n");
                        String fileNameImport = scanner.nextLine();
                        writeLog(fileNameImport);
                        importCards(fileNameImport);
                        break;
                    case "export":
                        System.out.print("File name:\n");
                        writeLog("File name:\n");
                        String fileNameExport = scanner.nextLine();
                        writeLog(fileNameExport);
                        exportCards(fileNameExport);
                        break;
                    case "ask":
                        ask(scanner);
                        break;
                    case "log":
                        saveLog(scanner);
                        break;
                    case "hardest card":
                        askHard();
                        break;
                    case "reset stats":
                        resetMistake();
                        break;
                    case "exit":
                        exit();
                        break;
                    default:
                        System.out.print("Command not found\n");
                        writeLog("Command not found\n");
                }
                if (action.equals("exit"))
                    break;
                System.out.println();
                writeLog("\n");
            }
        }
    }

    private static void saveLog(Scanner scanner) {
        System.out.print("File name:\n");
        writeLog("File name:\n");
        String fileName = scanner.nextLine();
        writeLog(fileName);
        try (PrintWriter writer = new PrintWriter(fileName)) {
            for (String entry : eventLog) {
                writer.println(entry);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getStackTrace());
        }
        System.out.printf("The log has been saved.\n");
        writeLog("The log has been saved.\n");
    }
    private static void askHard() {
        int max = 0;
        for (Integer entry : card.values()) {
            if (entry > max) {
                max = entry;
            }
        }
        if (max == 0) {
            System.out.println("There are no cards with errors.");
            writeLog("There are no cards with errors.");
        }
        else {
            int count = 0;
            for (Integer entry : card.values()) {
                if (entry == max) {
                    count++;
                }
            }
            StringBuilder forWriteLog = new StringBuilder("");
            if (count > 1) {
                System.out.print("The hardest cards are ");
                forWriteLog.append("The hardest cards are ");
            }
            else {
                System.out.print("The hardest card is ");
                forWriteLog.append("The hardest card is ");
            }
            count = 0;
            for (Map.Entry<String[], Integer> entry : card.entrySet()) {
                if (entry.getValue() == max) {
                    if (count == 0) {
                        System.out.printf("\"%s\"", entry.getKey()[0]);
                        forWriteLog.append(String.format("\"%s\"", entry.getKey()[0]));
                        count++;
                    }
                    else {
                        System.out.printf(", \"%s\"", entry.getKey()[0]);
                        forWriteLog.append(String.format(", \"%s\"", entry.getKey()[0]));
                    }
                }
            }
            if (count > 1) {
                System.out.printf(". You have %d errors answering them.\n", max);
                forWriteLog.append(String.format(". You have %d errors answering them.\n", max));
            } else {
                System.out.printf(". You have %d errors answering it.\n", max);
                forWriteLog.append(String.format(". You have %d errors answering it.\n", max));
            }
            writeLog(forWriteLog.toString());
        }
    }
    private static void resetMistake() {
        for (String[] entry : card.keySet()) {
            card.put(entry, 0);
        }
        System.out.print("Card statistics has been reset.\n");
        writeLog("Card statistics has been reset.\n");
    }

    private static void addCards(Scanner scanner) {
        while (true) {
            String[] forCard = new String[2];
            System.out.print("The card:\n");
            writeLog("The card:\n");
            String theCard = scanner.nextLine();
            writeLog(theCard);
            boolean contain = false;
            for (String[] entry : card.keySet()) {
                if (entry[0].equals(theCard)) {
                    System.out.printf("The card \"%s\" already exists.\n", theCard);
                    writeLog(String.format("The card \"%s\" already exists.\n", theCard));
                    contain = true;
                    break;
                }
            }
            if (contain)
                break;
            contain = false;
            System.out.printf("The definition of the card:\n");
            writeLog("The definition of the card:\n");
            String definition = scanner.nextLine();
            writeLog(definition);
            for (String[] entry : card.keySet()) {
                if (entry[1].equals(definition)) {
                    System.out.printf("The definition \"%s\" already exists.\n", definition);
                    writeLog(String.format("The definition \"%s\" already exists.\n", definition));
                    contain = true;
                    break;
                }
            }
            if (contain)
                break;
            else
            {
                forCard[0] = theCard;
                forCard[1] = definition;
                card.put(forCard, 0);
                System.out.printf("The pair (\"%s\":\"%s\") has been added.\n", theCard, definition);
                writeLog(String.format("The pair (\"%s\":\"%s\") has been added.\n", theCard, definition));
                break;
            }
        }
    }

    private static void removeCards(Scanner scanner) {
        System.out.print("The card:\n");
        writeLog("The card:\n");
        String theCard = scanner.nextLine();
        writeLog(theCard);
        boolean contain = true;
        for (String[] entry : card.keySet()) {
            if (entry[0].equals(theCard)) {
                card.remove(entry);
                System.out.printf("The card has been removed.\n");
                writeLog("The card has been removed.\n");
                contain = false;
                break;
            }
        }
        if (contain) {
            System.out.printf("Can't remove \"%s\": there is no such card.\n", theCard);
            writeLog(String.format("Can't remove \"%s\": there is no such card.\n", theCard));
        }
    }

    private static void importCards(String fileName) {
        File file = new File(fileName);
        if (file.isFile()) {
            try {
                int count = 0;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                    while (reader.ready()) {
                        String line = reader.readLine();
                        String[] arr = line.split(":");
                        String[] forCard = new String[]{arr[0], arr[1]};
                        Integer mistake = Integer.parseInt(arr[2]);
                        boolean isPut = false;
                        for (String[] temp : card.keySet()) {
                            if (arr[0].equals(temp[0])) {
                                temp[0] = arr[0];
                                temp[1] = arr[1];
                                card.put(temp, mistake);
                                isPut = true;
                            }
                        }
                        if (!isPut)
                            card.put(forCard, mistake);
                        count++;
                    }
                    System.out.printf("%d cards have been loaded.\n", count);
                    writeLog(String.format("%d cards have been loaded.\n", count));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.printf("File not found.\n");
            writeLog("File not found.\n");
        }
    }

    private static void exportCards(String fileNameExport) {
        String fileName = fileNameExport;
        int count = 0;
        try (PrintWriter writer = new PrintWriter(fileName)) {
            for (Map.Entry<String[], Integer> entry : card.entrySet()) {
                String[] forCard = entry.getKey();
                writer.println(forCard[0] + ":" + forCard[1] + ":" + entry.getValue());
                count++;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getStackTrace());
        }
        System.out.printf("%d cards have been saved.\n", count);
        writeLog(String.format("%d cards have been saved.\n", count));
    }

    private static void ask(Scanner scanner) {
        System.out.printf("How many times to ask?\n");
        writeLog("How many times to ask?\n");
        int n = Integer.parseInt(scanner.nextLine());
        writeLog(String.format("%d\n", n));
        int count = 0;
        while (count < n) {
            for (Map.Entry<String[], Integer> entry : card.entrySet()) {
                if (count == n)
                    break;
                String[] forCard = entry.getKey();
                System.out.printf("Print the definition of \"%s\":\n", forCard[0]);
                writeLog(String.format("Print the definition of \"%s\":\n", forCard[0]));
                String answer = scanner.nextLine();
                writeLog(answer);
                if (forCard[1].equals(answer)) {
                    System.out.print("Correct answer.\n");
                    writeLog("Correct answer.\n");
                    count++;
                    if (count == n)
                        break;
                    continue;
                } else {
                    boolean contain = false;
                    for (String[] entry2 : card.keySet()) {
                        if (entry2[1].equals(answer)) {
                            System.out.printf("Wrong answer. The correct one is \"%s\", " +
                                    "you've just written the definition " +
                                    "of \"%s\".\n", entry.getKey()[1], entry2[0]);
                            writeLog(String.format("Wrong answer. The correct one is \"%s\", " +
                                    "you've just written the definition " +
                                    "of \"%s\".\n", entry.getKey()[1], entry2[0]));
                            card.put(forCard, card.get(forCard) + 1);
                            count++;
                            contain = true;
                            break;
                        }
                    }
                    if (count == n)
                        break;
                    if (!contain) {
                        System.out.printf("Wrong answer. The correct " +
                                "one is \"%s\".\n", entry.getKey()[1]);
                        writeLog(String.format("Wrong answer. The correct " +
                                "one is \"%s\".\n", entry.getKey()[1]));
                        card.put(forCard, card.get(forCard) + 1);
                        count++;
                        if (count == n)
                            break;
                    }
                }
            }
        }
    }
    private static void exit() {
        System.out.print("Bye bye!\n");
        writeLog("Bye bye!\n");
        if (needExport == true) {
            exportCards(fileNameForExport);
        }
    }

    private static void writeLog(String log) {
        eventLog.add(log);
    }
}