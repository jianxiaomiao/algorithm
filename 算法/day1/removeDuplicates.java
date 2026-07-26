import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class removeDuplicates {

    public static class Solution {
        /**
         * 解法 1：双指针 (In-place 原地修改)
         * 时间复杂度: O(N) - 仅需遍历一遍快指针
         * 空间复杂度: O(1) - 满足题目原地修改的死命令
         */
        public int removeDuplicates(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }
            
            // slow 游标：指向当前最新一个“唯一元素”的存放位置
            int slow = 0;
            
            // fast 游标：快指针，从第 2 个数开始向后扫描
            for (int fast = 1; fast < nums.length; fast++) {
                // 如果快指针遇到了一个与慢指针不同的数（说明找到了新数字）
                if (nums[fast] != nums[slow]) {
                    slow++;                  // 慢指针往前走一步，空出一个新坑
                    nums[slow] = nums[fast]; // 把新数字填入这个新坑
                }
            }
            
            // 最终去重后的数组长度即为 slow 指针下标 + 1
            return slow + 1;
        }

        /**
         * 解法 2：使用 ArrayList（仅供你学习 Java 集合 API 参考）
         * 时间复杂度: O(N)
         * 空间复杂度: O(N) - 占用了额外内存
         */
        public int removeDuplicatesWithList(int[] nums) {
            // 1. 初始化一个 ArrayList。Java 21 推荐使用接口 List 声明，后面用实现类 ArrayList 初始化
            List<Integer> list = new ArrayList<>();
            
            // 2. 遍历原数组，利用 ArrayList 去重
            for (int num : nums) {
                // 如果 list 是空的，或者 list 的最后一个元素不等于当前数字（因为数组是有序的，重复的一定相邻）
                // list.size() 可以获取当前 List 的长度
                // list.get(index) 可以获取指定下标的元素
                if (list.isEmpty() || list.get(list.size() - 1) != num) {
                    list.add(num); // list.add(element) 向列表末尾添加一个元素
                }
            }
            
            // 3. 把 ArrayList 里的排好序的不重复值，重新写回原数组 nums 里
            for (int i = 0; i < list.size(); i++) {
                nums[i] = list.get(i); // 把 list 里的数依次覆盖 nums 前面的位置
            }
            
            // 4. 返回最新的长度
            return list.size();
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums1 = {1, 1, 2, 2, 3};
        int[] nums2 = {1, 1, 2, 2, 3};

        System.out.println("========== 1. 运行双指针原地解法 ==========");
        System.out.println("运行前数组: " + Arrays.toString(nums1));
        int k1 = solution.removeDuplicates(nums1);
        System.out.println("去重后长度: " + k1);
        System.out.println("去重后数组（前 " + k1 + " 个有效）: " + Arrays.toString(nums1));

        System.out.println("\n========== 2. 运行 ArrayList 辅助解法 ==========");
        System.out.println("运行前数组: " + Arrays.toString(nums2));
        int k2 = solution.removeDuplicatesWithList(nums2);
        System.out.println("去重后长度: " + k2);
        System.out.println("去重后数组（前 " + k2 + " 个有效）: " + Arrays.toString(nums2));
    }
}