class Solution {
    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1; // 1. 注意索引是 length() - 1

        while (left < right) {
            // 2. 移动 left 指针，直到指向字母或数字（注意带上 left < right 防止越界）
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            // 3. 移动 right 指针，直到指向字母或数字
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }

            // 4. 统一转小写后比较
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }

            left++;
            right--;
        }

        return true;
    }
}
