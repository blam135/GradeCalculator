# Grade Calculator Program
**What is it?**

A Java Program that calculates what marks you need in the exam to get a certain grade. It works for both Australian and NZ Universities i.e. Pass, Credit, Distinction, High Distinction for Australian WAMs and A+,A,A-,B+,B,B-,C+,C,C-,D+,D,D- for NZ GPA Universities

**Why did you make it?**

Well I got bored during the holidays and also I always calculated how many marks I needed to get a certain grade every time during exam periods and also always been
interested in learning how to make GUI Applications, so why not make this the first one? 

## Guidelines on how to use it
PC: 
Simply download the file "GradeCalculator.jar" 

MAC: So apparently Apple doesn't allow you to run jar files because I am not a vErIfIeD dEvElOpEr. So sorry, you are going to have to do it the long way: 

1. Download Java if you haven't done so (link here: https://www.java.com/en/download/)
2. Download the classes folder 
3. Go to the folder where the classes folder is 
4. Right click on the folder
5. Go to services
6. Click "New Terminal Tab at Folder" 
7. Type "java GradeCalculator" in the terminal


and you will see a program that looks like this:
![image](https://github.com/blam135/GradeCalculator/blob/master/Capture.PNG)

All fields except "UOS" must be filled, in order for the program to work

### Fields:
#### UOS - The name of your subject/Unit of study. 
#### Internal Composition - Maximum internal mark you can receive.  
#### Assessment Title - The name of the assessment that you have received.
#### Mark Received - The mark you have received in plain numbers (i.e. not %). 
#### Max Mark - The maximum mark you can be awarded in this assessment. 
#### Weighting - How much the assessment is worth for the whole course 

### Buttons:
#### Calculate - Calculates how many marks you need to get in the exam for specific grades
#### Reset - Resets all assessment values on the table 
#### ADD - Adds the assessment details to the table. Must have all required fields to be filled in. It will also not work if the total weighting becomes more than the internal composition   
#### REMOVE - Removes the current selected row on the table (you have to click on the row)
#### UPDATE - Removes the current selected row on the table (you have to click on the row) 


### Text: 
#### Accumulated Percentage - How many marks in % have you received so far 
#### Max Percentage Accumulated - How many marks in % max you could've received

© Brendon Lam 2018
