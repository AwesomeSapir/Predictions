package engine.world.value.generator.random.string;

import engine.world.value.generator.random.RandomValueGenerator;

public class RandomStringValueGenerator extends RandomValueGenerator<String> {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !?,_-().";

    @Override
    public String generateValue() {
        int maxStringSize = 50;
        int size = random.nextInt(maxStringSize + 1); // Random size up to 50
        StringBuilder randomString = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            randomString.append(randomChar);
        }
        return randomString.toString();
    }
}
