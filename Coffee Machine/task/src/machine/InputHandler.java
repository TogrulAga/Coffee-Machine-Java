package machine;

public class InputHandler {
    static int money = 550;
    static int waterSupply = 400;
    static int milkSupply = 540;
    static int beanSupply = 120;
    static int cupSupply = 9;
    static MainState state;
    static BuyState buyState = BuyState.menu;
    static FillState fillState = FillState.water_print;
    static String lastInput;

    enum MainState {
         idle, buy, fill, take, remaining, exit
    }

    enum FillState {
        water_print, water_fill, milk_print, milk_fill, beans_print, beans_fill, cups_print, cups_fill
    }

    enum BuyState {
        menu, takeInput
    }

    public static void execute(String input) {
        if (input == null) {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            state = MainState.idle;
            return;
        }
        lastInput = input;
        stateMachine();
    }

    private static void stateMachine() {
        switch (state) {
            case idle:
                idle(lastInput);
                break;
            case buy:
                sellCoffee();
                break;
            case fill:
                fillSupplies();
                break;
            case take:
                takeMoney();
                break;
            case remaining:
                printSupplies();
                break;
            case exit:
                System.exit(0);
                break;
            default:
        }
    }

    private static void idle(String input) {
        state = MainState.valueOf(input);
        stateMachine();
    }

    private static void fillSupplies() {
        switch (fillState) {
            case water_print:
                System.out.println("Write how many ml of water you want to add:");
                fillState = FillState.water_fill;
                break;
            case water_fill:
                waterSupply += Integer.parseInt(lastInput);
                fillState = FillState.milk_print;
                stateMachine();
                break;
            case milk_print:
                System.out.println("Write how many ml of milk you want to add:");
                fillState = FillState.milk_fill;
                break;
            case milk_fill:
                milkSupply += Integer.parseInt(lastInput);
                fillState = FillState.beans_print;
                stateMachine();
                break;
            case beans_print:
                System.out.println("Write how many grams of coffee beans you want to add:");
                fillState = FillState.beans_fill;
                break;
            case beans_fill:
                beanSupply += Integer.parseInt(lastInput);
                fillState = FillState.cups_print;
                stateMachine();
                break;
            case cups_print:
                System.out.println("Write how many disposable cups of coffee you want to add:");
                fillState = FillState.cups_fill;
                break;
            case cups_fill:
                cupSupply += Integer.parseInt(lastInput);
                fillState = FillState.water_print;
                state = MainState.idle;
                System.out.println();
                execute(null);
                break;
        }
    }

    private static void takeMoney() {
        System.out.printf("I gave you $%d%n%n", money);
        money = 0;
        execute(null);
    }

    private static void sellCoffee() {
        switch (buyState) {
            case menu:
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:");
                buyState = BuyState.takeInput;
                break;
            case takeInput:
                switch (lastInput) {
                    case "1":
                        makeCoffee(250, null, 16, 4);
                        break;
                    case "2":
                        makeCoffee(350, 75, 20, 7);
                        break;
                    case "3":
                        makeCoffee(200, 100, 12, 6);
                        break;
                    case "back":
                        buyState = BuyState.menu;
                        execute(null);
                        break;
                }
                buyState = BuyState.menu;
                execute(null);
                break;
        }
    }

    static void makeCoffee(Integer water, Integer milk, Integer beans, Integer price) {
        if (waterSupply < water) {
            System.out.println("Sorry, not enough water!");
        } else if (milk != null && milkSupply < milk) {
            System.out.println("Sorry, not enough milk!");
        } else if (beanSupply < beans) {
            System.out.println("Sorry, not enough coffee beans!");
        } else if (cupSupply <= 0) {
            System.out.println("Sorry, not enough disposable cups!");
        } else {
            System.out.println("I have enough resources, making you a coffee!");
            waterSupply -= water;
            milkSupply -= milk != null ? milk : 0;
            beanSupply -= beans;
            cupSupply--;
            money += price;
        }
    }

    private static void printSupplies() {
        String builder = "The coffee machine has:\n" +
                String.format("%d ml of water%n", waterSupply) +
                String.format("%d ml of milk%n", milkSupply) +
                String.format("%d g of coffee beans%n", beanSupply) +
                String.format("%d disposable cups%n", cupSupply) +
                String.format("$%d of money%n%n", money);

        System.out.println(builder);
        execute(null);
    }
}
