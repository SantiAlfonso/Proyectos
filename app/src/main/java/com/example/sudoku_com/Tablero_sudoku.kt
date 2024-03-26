package com.example.sudoku_com

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast


/*Esta clase recibe una variable "context" que se usa para acceder a recursos del sistema android*/
/*AttributeSet este parámetro generalmente se utiliza para contener atributos XML que se infla para crear la vista*/
/*: View(context, attributeSet) Indica que la clase 'Tablero Sudoku' hereda de la clase 'View', ademas tambien le mandamos los parametros 'context' y 'attributeSet'*/
class Tablero_sudoku(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var sqrtSize = 3
    private var Size = 9
    private var pixeles =0F


    private var columnaSeleccionada=-1
    private var filaSeleccionada=-1

    /*Paint es una clase utilizada para especificcar cómo se deben dibujar los graficos en android */
    private val Pintura = Paint().apply{

        style = Paint.Style.STROKE/*Establece el estilo de la pintura en 'STROKE' lo que significa que solo se dibujara el contorno de la forma sin rellenar*/
        color = Color.BLACK/*Esto establece el color de la pintura en negro*/
        strokeWidth = 6F/*Establece el ancho de la linea en 4 pixeles(La letra al final indica que es un numero tipo Float)*/

    }

    /*Paint es una clase utilizada para especificcar cómo se deben dibujar los graficos en android */
    private val Pintura_p = Paint().apply{

        style = Paint.Style.STROKE/*Establece el estilo de la pintura en 'STROKE' lo que significa que solo se dibujara el contorno de la forma sin rellenar*/
        color = Color.BLACK/*Esto establece el color de la pintura en negro*/
        strokeWidth = 2F/*Establece el ancho de la linea en 4 pixeles(La letra al final indica que es un numero tipo Float)*/

    }

    /*METODO QUE CONVIERTE LA VISTA CUADRADA*/
    /*Este metodo(onMeasure) se llama automaticamente cuando es necesario medir las dimensiones de la vista*/
    /*Los parametros que recibe son numeros enteros que representan la medida tanto de alto como de ancho*/
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)/*Llamamos al metodo 'onMeasure() de la clase para realizar la medicion inicial'*/
        val sizePixels = Math.min(widthMeasureSpec,heightMeasureSpec)/*Aqui se calcula el tamaño en pixeles para la vista, obteniendo el valor mas pequeño entre ancho y altura para que elejir el mas pequeño y que la vista asi quede cuadrada*/
        setMeasuredDimension(sizePixels,sizePixels)/*Se establece el tamaño medido de la vista utilizando 'setMeasureSpec' para aseugurar que la vista sea cuadrada*/

    }

    /*Sobreescribimos el metodo 'onDraw' de la clase 'View' para permitirnos personalizar el comportamiento del metodo en nuestra clase tablero_sudoku*/
    /*Canvas proporciona metodos para dibujar sobre un lienzo*/
    override fun onDraw(canvas: Canvas){
        pixeles = (width/Size).toFloat()/*proporciona el ancho de cada "celda" en la cuadrícula.*/
        PintarCaudrosSeleccionado(canvas)
        dibujar_lineas(canvas)

    }

    private val CuadroSeleccionado= Paint().apply {

        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#90CAF9")/**/

    }

    private val CuadroConflicto = Paint().apply {

        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#E3F2FD")

    }

    private fun PintarCaudrosSeleccionado(canvas: Canvas) {
        if (filaSeleccionada == -1 || columnaSeleccionada == -1){//Verificamos que el usarios haya seleccionado un cuadro
            return Unit
        }else{
            for (i in 0..Size) {//Recorre cada fila
                for (j in 0..Size) {//Recorre cada columna
                    if (i == filaSeleccionada && j == columnaSeleccionada) {/*Caso en el que la fila y la columa coincidan lon las seleccionadas*/
                        MedidaCelda(canvas, i, j, CuadroSeleccionado)
                    } else if (i == filaSeleccionada || j == columnaSeleccionada) {/*Caso en el que un cuadro se encuentre en la misma fila o culomna que el seleccionado*/
                        MedidaCelda(canvas, i, j, CuadroConflicto)
                    } else if (i / sqrtSize == filaSeleccionada / sqrtSize && j / sqrtSize == columnaSeleccionada / sqrtSize){/*Caso para pintar todo el cuadro 3x3 en el que se encuentra el cuadro seleccionado*/
                        MedidaCelda(canvas, i, j, CuadroConflicto)
                    }
                }
            }
        }
    }

    private fun MedidaCelda(canvas:Canvas, fila:Int,columna:Int,paint: Paint){
        canvas.drawRect(columna * pixeles, fila * pixeles, (columna+1)*pixeles,(fila+1)*pixeles,paint)//Calcula las posiciones para rellenar el cuadro
    }

    /*METODO PARA DIBUJAR LAS LINEAS EN LA CUADRICULA*/
    private fun dibujar_lineas(canvas: Canvas){
        canvas.drawRect(0F,0F,width.toFloat(),height.toFloat(),Pintura)/*Se dibuja el borde del tablero*/
        /*Se dibuja un rectangulo. Los parametors que recibe especifican las cordenadas, asi como la pintura(estilo de dibujo) a usar*/
        /*0f, 0f, son las cordenadas en (X,Y), Height y width son las dimensiones convertidas a float debido a que drawRect() espero datos tipo flotantes, Pintura es la incstancia painte que se utilizara para dibujar el rectangulo*/

        for (i in 1 until Size){/*El bucle va a recorrer las 9 posiciones que tiene la cuadricula dibujando lineas en las posiciones correspondientes*/

            /*Obtenemos el tipo de linea que vamos a utilizar dependiendo de la posición(como anteriormente el lienzo)*/
            val pintura_uso = when(i % sqrtSize){/*La condición para que cada vez que i sea divisible por 3 la linea dibujada sera mas ancha*/
                0 -> Pintura
                else -> Pintura_p
            }

            canvas.drawLine(i * pixeles,/*Se encarga de indicar la posicion inicial en x de la linea vertical*/
                            0F,/*Como la linea es vertical siempre en su posición inicial en Y sera igual a 0*/
                            i*pixeles,/*Le indica que la posición final en x sera el mismo que el incio, esto para que la linea sea recta(evitando cosas como pendientes en la linea)*/
                            height.toFloat(),/*Le indica que la linea llegue hasta el final del lienzo*/
                            pintura_uso/*Y por ultimo usa el tipo de linea que haya cumplido la condición inicial*/
            )

            canvas.drawLine(0F,
                            i * pixeles,
                            width.toFloat(),
                            i*pixeles,
                            pintura_uso
            )
        }
    }

   override fun onTouchEvent(event: MotionEvent): Boolean{
        return when(event.action){
            MotionEvent.ACTION_DOWN -> {//Esto es una accion de evento que indica que se toco la pantalla
                handleTouchEvent(event.x,event.y)
                true
            }
            else -> false
        }
    }


    //FUNCIÓN PARA DETECTAR SI HUBO UNA ACCIÓN TACTIL
    private fun handleTouchEvent(x:Float,y:Float){//Recibe como parametros las cordenadas de los puntos tocados
        val message = "Coordenada X: $x, Coordenada Y: $y, Pixeles: $pixeles, Fila: $filaSeleccionada "
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        filaSeleccionada = (y/pixeles).toInt()
        columnaSeleccionada = (x/pixeles).toInt()

        invalidate()
    }



}