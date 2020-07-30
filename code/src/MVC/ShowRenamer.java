package MVC;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowRenamer
{
    public static void main(String[] args)
    {
        Scanner s = new Scanner(System.in);

        System.out.println("Enter Directory:");
        String DIR = s.nextLine();

        System.out.println("Enter Delimiter");
        String delim = s.nextLine();

        File dir = new File(DIR);

        // get list of files
        File[] files = dir.listFiles();

        if (files != null)
        {
            for (File item : files)
            {
                if (item.isFile())
                {
                    // get name of file
                    String name = item.getName();
                    int lastIndex = name.lastIndexOf('.');
                    String ext = name.substring(lastIndex);
                    name = name.substring(0, lastIndex);

                    // rename and format to: Title SXXEXXX.EXT
                    String newName = formatTVShow(name, ".");
                    File file = new File(dir + "\\" + newName + ext);
                    item.renameTo(file);
                }
            }
        }
    }

    /**
     * Formats a TV Show file to format: TITLE SXXEXXX
     *
     * @param name      File name
     * @param delimiter delimiter to split name by
     * @return Name in format: TITLE SXXEXXX
     */
    private static String formatTVShow(String name, String delimiter)
    {
        Pattern pattern = Pattern.compile("^[sS][0-9]{2}[Ee][0-9]{2,3}");
        Matcher matcher;

        StringBuilder title = new StringBuilder();

        if (delimiter.equals("."))
            delimiter = "\\.";

        String[] split = name.split(delimiter);

        boolean titleFound = false;

        // loop until title is found then append S and E to name
        for (int i = 0; i < split.length && !titleFound; i++)
        {
            matcher = pattern.matcher(split[i]);

            // check if the item matches format: SXXEXX or SXXEXXX or SXXEXX;
            if (!matcher.find() && !titleFound)
            {
                // append title and " " to title
                title.append(split[i]).append(" ");
            }
            else
            {
                // append season and episode numbers to title
                titleFound = true;
                title.append(split[i]);
            }
        }

        return title.toString();
    }
}
