class Solution {
    public int[] twoSum(int[] numbers, int target) {
        int x1 = 0; int x2 = numbers.length - 1;
        while (x1 < x2) {
            if (numbers[x1] + numbers[x2] == target) {
                return new int[] {x1 + 1, x2 + 1};
            } else if (numbers[x1] + numbers[x2] < target) {
                x1++;
            } else {
                x2--;
            }
        }
        return new int[] {-1, -1};
    }
}