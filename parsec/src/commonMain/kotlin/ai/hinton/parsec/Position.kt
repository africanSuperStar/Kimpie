package ai.hinton.parsec

// Textual source positions.
//==============================================================================

// ==============================================================================
/// SourcePosition represents source positions. It contains the name of the
/// source (i.e. file name), a line number and a column number. The upper left
/// is 1, 1. It implements the `Comparable` protocols. The comparison is made
/// using line and column number.
data class SourcePosition(val name: String, var line: Int, private var column: Int) : Comparable<SourcePosition> {

    /// A textual representation of `self`.
    override fun toString(): String {

        val lineMsg   = "line"
        val columnMsg = "column"

        var desc = "(" + lineMsg + " ${line}, " + columnMsg + " ${column})"

        if (name.isNotEmpty())
        {
            desc = "\"${name}\" " + desc
        }

        return desc
    }

    /// Comparison based on the line and column number.
    override fun compareTo(other: SourcePosition): Int {

        if (this.line < other.line)
        {
            return -1
        }
        else if (this.line == other.line)
        {
            if (this.column < this.column) { return 0 }
        }

        return 1
    }

}

