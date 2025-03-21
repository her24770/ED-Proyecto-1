;;prueba operadores aritmeticos 1
(+ 1 2 3)              
(* 4 5)               
(- 10 3)            
(/ 20 4)    

;;uso de quote 2
(quote (1 2 3))         
'(a b c)  

(+ 1 2 3)

;;definicion de funciones 3
(defun suma (a b c d f s)
  (+ a b)
  (setq y (+ 1 2)))
  (setq x 20)
  (+ y x a b)

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
(+ 2 2 + ( 3 * 1))   


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
    (t (* list1 (potencia base (- exponente 1)))))) 

;;lisp y equal 9
(setq lista1 (list 1 2 3))  
(setq lista2 (list 1 2 3))  
(equal lista1 lista2)



(cond
  ((> x 10) (- 10 x) (setq x 0))
  ((< x 5) (+ 5 x) (setq x 5))
  ((= x 7) (+ 7 x) (setq x (+ 5 1)))
  (t (> 3 4) (setq x 999)))




(defun fibonacci (n)
  (cond
    ((equal n 0) 0)           
    ((equal n 1) 1)             
    (t (+ (fibonacci (- n 1))
          (fibonacci (- n 2))
      )
    )
  )
) 

(fibonacci 5);

(defun factorial (n)
  (cond
    ((equal n 0) 1)      
    (t (* n (factorial (- n 1))))
  )
)  


(factorial 6)

