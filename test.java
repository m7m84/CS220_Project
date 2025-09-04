import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter number of students: ");
        int student_num = input.nextInt();
        
        double highestGPA = 0;
        
        for (int i = 1; i <= student_num; i++) {
            System.out.print("Enter GPA for student number " + i + ": ");        double gpa = input.nextDouble();
            
            // Example logic to find the highest GPA
    if (gpa > highestGPA) {
                highestGPA = gpa;
            }
        }
        
        System.out.println("Highest GPA entered: " + highestGPA);
        input.close();
    }
}
