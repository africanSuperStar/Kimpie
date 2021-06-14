package ai.hinton.parsec

//==============================================================================
/// `Parsec` is a parser with stream type `Stream`, user state type `UserState`
/// and return type `Result`.
sealed interface Parsec <Stream, UserState, Result>
{
    /// Return a parser containing the result of mapping transform over `self`.
    ///
    /// This method has the synonym infix operator `<^>`.
    ///
    /// - parameter transform: A mapping function.
    /// - returns: A new parser with the mapped content.
    fun <T> map (
        transform: (Result) -> T
    ) : GenericParser <Stream, UserState, T>

    /// Return a parser by applying the function contained in the supplied
    /// parser to self.
    ///
    /// This method has the synonym infix operator `<*>`.
    ///
    /// - parameter parser: The parser containing the function to apply to self.
    /// - returns: A parser with the applied function.
    fun <T> apply (
        parser: GenericParser <Stream, UserState, (Result) -> T>
    ) : GenericParser <Stream, UserState, T>

    /// This combinator implements choice. The parser `p.alternative(q)` first
    /// applies `p`. If it succeeds, the value of `p` is returned. If `p` fails
    /// _without consuming any input_, parser `q` is tried. The parser is called
    /// _predictive_ since `q` is only tried when parser `p` didn't consume any
    /// input (i.e.. the look ahead is 1). This non-backtracking behaviour
    /// allows for both an efficient implementation of the parser combinators
    /// and the generation of good error messages.
    ///
    /// This method has the synonym infix operator `<|>`.
    ///
    /// - parameter altParser: The alternative parser to try if `self` fails.
    /// - returns: A parser that will first try `self`. If it consumed no input,
    ///   it will try `altParser`.
    fun alternative(altParser: Parsec <Stream, UserState, Result>) : Parsec <Stream, UserState, Result>

    /// Return a parser containing the result of mapping transform over `self`.
    ///
    /// This method has the synonym infix operator `>>-` (bind).
    ///
    /// - parameter transform: A mapping function returning a parser.
    /// - returns: A new parser with the mapped content.
    fun <T> flatMap (
        transform: (
            Result
        ) -> GenericParser <Stream, UserState, T>
    ) : GenericParser <Stream, UserState, T>

}

//==============================================================================
// Extension containing useful methods to run a parser.
/// A `Stream` instance is responsible for maintaining the position of the
/// parser's stream.
public interface Stream <E> : Collection <E> where E: CharArray { }

fun String.arrayOf(elements: CharArray) : Stream <CharArray>
{
    return arrayOf(elements)
}