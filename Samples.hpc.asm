move SP FP  
pushr FP 
move SP AL 
pushr AL 
storei A0 0
pushr A0 
storei A0 2
pushr A0 
move AL T1 
subi T1 1
store A0 0(T1) 
pushr A0 
storei A0 1
popr T1 
add A0 T1 
popr A0 
move AL T1 
subi T1 1
load A0 1(T1) 
move AL T1 
subi T1 1
store A0 0(T1) 
move AL T1 
subi T1 2
load A0 2(T1) 
move AL T1 
subi T1 2
store A0 0(T1) 
halt
