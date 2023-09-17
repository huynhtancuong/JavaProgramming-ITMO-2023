package client.utility;

import client.App;
import common.data.Chapter;
import common.data.Coordinates;
import common.data.MeleeWeapon;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;
import common.utility.Outputer;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Asks a user a marine's value.
 */
public class MarineAsker {
    private final int MAX_Y = 267;
    private final double MIN_HEALTH = 0;
    private final long MIN_MARINES = 1;
    private final long MAX_MARINES = 1000;

    private Scanner userScanner;
    private boolean fileMode;
    
    public MarineAsker(Scanner userScanner) {
        this.userScanner = userScanner;
        fileMode = false;
    }

    /**
     * Sets a scanner to scan user input.
     * @param userScanner Scanner to set.
     */
    public void setUserScanner(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * @return Scanner, which uses for user input.
     */
    public Scanner getUserScanner() {
        return userScanner;
    }

    /**
     * Sets marine asker mode to 'File Mode'.
     */
    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Sets marine asker mode to 'User Mode'.
     */
    public void setUserMode() {
        fileMode = false;
    }

    /**
     * Asks a user the marine's name.
     * @return Marine's name.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public String askName() throws IncorrectInputInScriptException {
        String name;
        while (true) {
            try {
                Outputer.println("Введите имя:");
                Outputer.print(App.PS2);
                name = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(name);
                if (name.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Имя не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Имя не может быть пустым!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return name;
    }

    /**
     * Asks a user the marine's X coordinate.
     * @return Marine's X coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public double askX() throws IncorrectInputInScriptException {
        String strX;
        double x;
        while (true) {
            try {
                Outputer.println("Введите координату X:");
                Outputer.print(App.PS2);
                strX = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strX);
                x = Double.parseDouble(strX);
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Координата X не распознана!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Координата X должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Asks a user the marine's Y coordinate.
     * @return Marine's Y coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Float askY() throws IncorrectInputInScriptException {
        String strY;
        Float y;
        while (true) {
            try {
                Outputer.println("Введите координату Y < " + (MAX_Y+1) + ":");
                Outputer.print(App.PS2);
                strY = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strY);
                y = Float.parseFloat(strY);
                if (y > MAX_Y) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Координата Y не распознана!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Координата Y не может превышать " + MAX_Y + "!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Координата Y должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return y;
    }

    /**
     * Asks a user the marine's coordinates.
     * @return Marine's coordinates.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        double x;
        Float y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the marine's health.
     * @return Marine's health.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public double askHealth() throws IncorrectInputInScriptException {
        String strHealth;
        double health;
        while (true) {
            try {
                Outputer.println("Введите здоровье:");
                Outputer.print(App.PS2);
                strHealth = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strHealth);
                health = Double.parseDouble(strHealth);
                if (health < MIN_HEALTH) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Здоровье не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Здоровье должно быть больше нуля!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Здоровье должно быть представлено числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return health;
    }

    public float askHeight() throws IncorrectInputInScriptException {
        String strHeight;
        float height;
        while (true) {
            try {
                Outputer.println("Enter height:");
                Outputer.print(App.PS2);
                strHeight = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strHeight);
                height = Float.parseFloat(strHeight);
                if (height < MIN_HEALTH) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Height не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Height должно быть больше нуля!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Height должно быть представлено числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return height;
    }

    /**
     * Asks a user the marine's melee weapon.
     * @return Marine's melee weapon.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public MeleeWeapon askMeleeWeapon() throws IncorrectInputInScriptException {
        String strMeleeWeapon;
        MeleeWeapon meleeWeapon;
        while (true) {
            try {
                Outputer.println("Список оружия ближнего боя - " + MeleeWeapon.nameList());
                Outputer.println("Введите оружие ближнего боя:");
                Outputer.print(App.PS2);
                strMeleeWeapon = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strMeleeWeapon);
                meleeWeapon = MeleeWeapon.valueOf(strMeleeWeapon.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Оружие не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                Outputer.printerror("Оружия нет в списке!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return meleeWeapon;
    }

    /**
     * Asks a user the marine chapter's name.
     * @return Chapter's name.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public String askChapterName() throws IncorrectInputInScriptException {
        String chapterName;
        while (true) {
            try {
                Outputer.println("Введите имя ордена:");
                Outputer.print(App.PS2);
                chapterName = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(chapterName);
                if (chapterName.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Имя ордена не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Имя ордена не может быть пустым!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return chapterName;
    }

    /**
     * Asks a user the marine chapter's number of soldiers.
     * @return Number of soldiers.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public int askChapterMarinesCount() throws IncorrectInputInScriptException {
        String strMarinesCount;
        int marinesCount;
        while (true) {
            try {
                Outputer.println("Введите количество солдат в ордене < " + (MAX_MARINES+1) + ":");
                Outputer.print(App.PS2);
                strMarinesCount = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strMarinesCount);
                marinesCount = Integer.parseInt(strMarinesCount);
                if (marinesCount < MIN_MARINES || marinesCount > MAX_MARINES) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Количество солдат в ордене не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Количество солдат в ордене должно быть положительным и не превышать " + MAX_MARINES + "!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Количество солдат в ордене должно быть представлено числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return marinesCount;
    }

    /**
     * Asks a user the marine chapter's Parent Legion.
     * @return Chapter's Parent Legion.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public String askChapterParentLegion() throws IncorrectInputInScriptException {
        String chapterParentLegion;
        while (true) {
            try {
                Outputer.println("Enter Parent Legion:");
                Outputer.print(App.PS2);
                chapterParentLegion = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(chapterParentLegion);
                if (chapterParentLegion.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Parent Legion не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Parent Legion не может быть пустым!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return chapterParentLegion;
    }

    /**
     * Asks a user the marine chapter's World.
     * @return Chapter's World.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public String askChapterWorld() throws IncorrectInputInScriptException {
        String chapterWorld;
        while (true) {
            try {
                Outputer.println("Enter World:");
                Outputer.print(App.PS2);
                chapterWorld = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(chapterWorld);
                if (chapterWorld.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("World не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("World не может быть пустым!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return chapterWorld;
    }

    /**
     * Asks a user the marine's chapter.
     * @return Marine's chapter.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Chapter askChapter() throws IncorrectInputInScriptException {
        String name, parentLegion, world;
        int marinesCount;
        name = askChapterName();
        marinesCount = askChapterMarinesCount();
        parentLegion = askChapterParentLegion();
        world = askChapterWorld();
        return new Chapter(name, parentLegion, marinesCount, world);
    }

    /**
     * Asks a user a question.
     * @return Answer (true/false).
     * @param question A question.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Outputer.println(finalQuestion);
                Outputer.print(App.PS2);
                answer = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Ответ не распознан!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Ответ должен быть представлен знаками '+' или '-'!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return (answer.equals("+")) ? true : false;
    }

    /**
     * Asks a user a question.
     * @return Answer (true/false).
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public boolean askLoyal() throws IncorrectInputInScriptException {
        String answer;
        while (true) {
            try {
                Outputer.println("Enter Loyal:");
                Outputer.print(App.PS2);
                answer = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Ответ не распознан!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Ответ должен быть представлен знаками '+' или '-'!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return (answer.equals("+")) ? true : false;
    }

    @Override
    public String toString() {
        return "MarineAsker (вспомогательный класс для запросов пользователю)";
    }
}