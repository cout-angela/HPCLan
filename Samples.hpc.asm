move SP FP  
pushr FP 
move SP AL 
pushr AL 
subi SP 4
storei A0 0
pushr A0 
push function0
pushr FP 
pushr AL 
move SP FP 
addi FP 2
move FP AL 
subi AL 1 
storei A0 4 
divi A0 2 
pushr A0 
b label4
label4:
storei T1 0 
blt A0 T1 label5
move AL T1 
subi T1 1
store A0 0(T1) 
move AL T1 
store T1 0(T1) 
subi T1 1
sub T1 A0 
pushr FP 
move AL T1
store T1 0(T1) 
pushr T1 
move AL T1 
subi T1 1
store A0 0(T1) 
pushr A0
move SP FP 
addi FP 1
addi FP 2
move FP AL 
subi AL 1 
jsub function0
popr T1 
load A0 0(T1) 
move AL T1 
subi T1 1
store A0 0(T1) 
subi A0 1 
move AL T1 
subi T1 1
load A0 0(T1)
b label4
label5:
storei A0 4 
divi A0 2 
addi A0 1 
move AL T1 
subi T1 1
load A0 0(T1) 
b label6
label6:
storei A0 4 
store T1 0(T1) 
bleq A0 T1 label7
move AL T1 
subi T1 1
store A0 0(T1) 
move AL T1 
store T1 0(T1) 
subi T1 1
sub T1 A0 
pushr FP 
move AL T1
store T1 0(T1) 
pushr T1 
move AL T1 
subi T1 1
store A0 0(T1) 
pushr A0
move SP FP 
addi FP 1
addi FP 2
move FP AL 
subi AL 1 
jsub function0
popr T1 
load A0 0(T1) 
move AL T1 
subi T1 1
store A0 0(T1) 
addi A0 1 
move AL T1 
subi T1 1
load A0 0(T1) 
b label6
label7:
addi SP 1 
pop 
store FP 0(FP) 
move FP AL 
subi AL 1 
pop 
b label8
label8:
move AL T1 
subi T1 5
store A0 0(T1) 
pushr A0 
storei A0 4 
popr T1 
blt T1 A0 label10
storei A0 0 
b label11
label10:
storei A0 1
label11:
storei T1 0 
beq A0 T1 label9
storei A0 0
move AL T1 
subi T1 1
sub T1 A0 
storei A0 0
move AL T1 
subi T1 1
sub T1 A0 
popr T1 
store A0 0(T1) 
pushr A0 
move AL T1 
subi T1 5
store A0 0(T1) 
move AL T1 
subi T1 1
sub T1 A0 
popr T1 
store A0 0(T1) 
popr T1 
add A0 T1 
popr A0 
popr T1 
load A0 0(T1) 
move AL T1 
subi T1 5
store A0 0(T1) 
pushr A0 
storei A0 1
popr T1 
add A0 T1 
popr A0 
move AL T1 
subi T1 5
load A0 0(T1) 
b label8
label9:
storei A0 0
move AL T1 
subi T1 1
sub T1 A0 
popr T1 
store A0 0(T1) 
halt

function0:
pushr RA 
move AL T1 
subi T1 1
store A0 0(T1) 
pushr A0 
storei A0 0
popr T1 
beq A0 T1 label2
storei A0 0
b label3
label2:
storei A0 1
label3:
storei T1 1 
beq A0 T1 label0
move AL T1 
subi T1 1
store A0 0(T1) 
pushr A0 
pushr FP 
move AL T1
store T1 0(T1) 
pushr T1 
move AL T1 
subi T1 1
store A0 0(T1) 
pushr A0 
storei A0 1
popr T1 
sub T1 A0 
popr A0 
pushr A0
move SP FP 
addi FP 1
addi FP 2
move FP AL 
subi AL 1 
jsub function0
popr T1 
mul A0 T1 
popr A0 
b label1
label0:
storei A0 1
label1:
addi SP 0
popr RA 
addi SP 1
pop 
store FP 0(FP) 
move FP AL 
subi AL 1 
pop 
rsub RA 
