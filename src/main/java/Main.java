import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Runnable logic = () -> {

                int valueCommandCounter;
                valueCommandCounter = robotCommandCounter(generateRoute("RLRFR", 100), "R");

                synchronized (sizeToFreq) {

                    if (sizeToFreq.containsKey(valueCommandCounter)) {
                        sizeToFreq.replace(valueCommandCounter, sizeToFreq.get(valueCommandCounter) + 1);
                    } else {
                        sizeToFreq.put(valueCommandCounter, 1);
                    }
                }
            };

            Thread thread = new Thread(logic);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Map.Entry<Integer, Integer> maxEntry = maxEntryInMap(sizeToFreq);

        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)", maxEntry.getKey(),
                maxEntry.getValue());
        System.out.println();

        System.out.println("Другие размеры:");

        int sum = 0;
        for (Map.Entry<Integer, Integer> mapCommandCount : sizeToFreq.entrySet()) {

            if (maxEntry.getKey() != mapCommandCount.getKey()) {
                System.out.println(" - " + mapCommandCount.getKey() + " " + "(" + mapCommandCount.getValue() +
                        " раз)");

                sum += mapCommandCount.getValue();

            }
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int robotCommandCounter(String text, String sub) {
        int count = 0;
        int pos = 0;
        while ((pos = text.indexOf(sub, pos)) != -1) {
            count++;
            pos += sub.length();
        }
        return count;
    }

    public static Map.Entry<Integer, Integer> maxEntryInMap(Map<Integer, Integer> sizeToFreq) {
        Map.Entry<Integer, Integer> maxEntry = sizeToFreq.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElse(null);
        return maxEntry;
    }
}