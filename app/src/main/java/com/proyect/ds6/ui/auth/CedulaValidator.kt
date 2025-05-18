package com.proyect.ds6.ui.auth

/**
 * Clase para validar el formato de cédula panameña
 */
object CedulaValidator {
    /**
     * Valida una cédula panameña según el formato establecido
     * @param cedula Cédula a validar
     * @return Resultado de la validación con información sobre si es válida y su estructura
     */
    fun validateCedula(cedula: String): CedulaValidationResult {
        // Expresión regular para validar el formato de cédula panameña
        val re = Regex("""^P$|^(?:PE|E|N|[23456789]|[23456789](?:A|P)?|1[0123]?|1[0123]?(?:A|P)?)$|^(?:PE|E|N|[23456789]|[23456789](?:AV|PI)?|1[0123]?|1[0123]?(?:AV|PI)?)-?$|^(?:PE|E|N|[23456789](?:AV|PI)?|1[0123]?(?:AV|PI)?)-(?:\d{1,4})-?$|^(PE|E|N|[23456789](?:AV|PI)?|1[0123]?(?:AV|PI)?)-(\d{1,4})-(\d{1,6})$""", RegexOption.IGNORE_CASE)
        
        val matched = re.find(cedula)
        
        var isComplete = false
        val cedulaParts = mutableListOf<String>()
        
        if (matched != null) {
            val groups = matched.groupValues.drop(1) // Eliminar el primer grupo que es la coincidencia completa
            
            if (groups.isNotEmpty() && groups[0].isNotEmpty()) {
                isComplete = true
                
                // Procesar las partes de la cédula
                val part1 = groups[0]
                val part2 = groups.getOrNull(1) ?: ""
                val part3 = groups.getOrNull(2) ?: ""
                
                if (part1.matches(Regex("""^PE|E|N$""", RegexOption.IGNORE_CASE))) {
                    cedulaParts.add("0")
                    cedulaParts.add(part1)
                } else {
                    // Verificar si parte1 tiene letras después de dígitos (ej: 8AV)
                    val tmpMatch = Regex("""(\d+)(\w+)""").find(part1)
                    
                    if (tmpMatch != null) {
                        cedulaParts.add(tmpMatch.groupValues[1]) // Número
                        cedulaParts.add(tmpMatch.groupValues[2]) // Letras
                    } else if (part1.matches(Regex("""^(1[0123]?|[23456789])?$"""))) {
                        cedulaParts.add(part1)
                        cedulaParts.add("")
                    } else {
                        cedulaParts.add(part1)
                    }
                }
                
                // Agregar las otras partes
                if (part2.isNotEmpty()) cedulaParts.add(part2) 
                if (part3.isNotEmpty()) cedulaParts.add(part3)
            }
        }
        
        // Crear y retornar el resultado
        return CedulaValidationResult(
            isValid = cedula.isEmpty() || re.matches(cedula),
            inputString = cedula,
            isComplete = isComplete,
            cedula = if (isComplete) cedulaParts else null
        )
    }
    
    /**
     * Clase de datos que contiene el resultado de la validación de cédula
     */
    data class CedulaValidationResult(
        val isValid: Boolean,
        val inputString: String,
        val isComplete: Boolean,
        val cedula: List<String>?
    )
}
