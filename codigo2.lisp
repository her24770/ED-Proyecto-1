;;prueba operadores aritmeticos 1
(+ 1 2 3)              
(* 4 5)               
(- 10 3)            
(/ 20 4)    

;;uso de quote 2
(quote (1 2 3))         
'(a b c)       

;;definicion de funciones 3
(defun suma (a b)
  (+ a b))              

;;uso de setq 4
(setq x 10)            
(setq y 20)            
(+ x y)  

;;predicados 5
(atom 5)              
(list 1 2 3)             
(equal 3 3)           
(< 2 3)                  
(> 5 4)      

;;condicionales cond 6
(defun es-positivo (n)
  (cond
    ((> n 0) "Es Positivo") 
    ((< n 0) "Es Negativo")  
    (t "Cero")))         

;;recursividad 7
(defun factorial (n)
  (cond
    ((<= n 1) 1)        
    (t (* n (factorial (- n 1)))))) 

;;expression compleja 8
(defun potencia (base exponente)
  (cond
    ((= exponente 0) 1)  
    (t (* base (potencia base (- exponente 1)))))) 

;;lisp y equal 9
(setq lista1 (list 1 2 3))  
(setq lista2 (list 1 2 3))  
(equal lista1 lista2)       