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

    /// This combinator is used whenever arbitrary look ahead is needed. Since
    /// it pretends that it hasn't consumed any input when `self` fails, the
    /// ('<|>') combinator will try its second alternative even when the first
    /// parser failed while consuming input.
    ///
    /// The `attempt` combinator can for example be used to distinguish
    /// identifiers and reserved words. Both reserved words and identifiers are
    /// a sequence of letters. Whenever we expect a certain reserved word where
    /// we can also expect an identifier we have to use the `attempt`
    /// combinator. Suppose we write:
    ///
    ///     let letExpr = StringParser.string("let")
    ///     let identifier = letter.many1
    ///
    ///     let expr = letExpr <|> identifier <?> "expression"
    ///
    /// If the user writes \"lexical\", the parser fails with: _unexpected 'x',
    /// expecting 't' in "let"_. Indeed, since the ('<|>') combinator only tries
    /// alternatives when the first alternative hasn't consumed input, the
    /// `identifier` parser is never tried (because the prefix "le" of the
    /// `string("let")` parser is already consumed). The right behaviour can be
    /// obtained by adding the `attempt` combinator:
    ///
    ///     let letExpr = StringParser.string("let")
    ///     let identifier = StringParser.letter.many1
    ///
    ///     let expr = letExpr.attempt <|> identifier <?> "expression"
    ///
    /// - returns: A parser that pretends that it hasn't consumed any input when
    ///   `self` fails.
    var attempt get() : Parsec <Stream, UserState, Result>

    /// A combinator that parses without consuming any input.
    ///
    /// If `self` fails and consumes some input, so does `lookAhead`. Combine
    /// with `attempt` if this is undesirable.
    ///
    /// - returns: A parser that parses without consuming any input.
    var lookAhead get() : Parsec <Stream, UserState, Result>

    /// This combinator applies `self` _zero_ or more times. It returns an
    /// accumulation of the returned values of `self` that were passed to the
    /// `accumulator` function.
    ///
    /// - parameter accumulator: An accumulator function that process the value
    ///   returned by `self`. The first argument is the value returned by `self`
    ///   and the second argument is the previous processed values returned by
    ///   this accumulator function. It returns the result of processing the
    ///   passed value and the accumulated values.
    /// - returns: The processed values of the accumulator function.
    fun manyAccumulator(
        accumulator: (Result, [Result]) -> [Result]
    ) : GenericParser <Stream, UserState, [Result]>

    /// A parser that always fails without consuming any input.
    static var empty get() : Parsec <Stream, UserState, Result>

    /// The parser returned by `p.labels(message)` behaves as parser `p`, but
    /// whenever the parser `p` fails _without consuming any input_, it replaces
    /// expected error messages with the expected error message `message`.
    ///
    /// This is normally used at the end of a set alternatives where we want to
    /// return an error message in terms of a higher level construct rather than
    /// returning all possible characters. For example, if the `expr` parser
    /// from the `attempt` example would fail, the error message is: '...:
    /// expecting expression'. Without the `GenericParser.labels()` combinator,
    /// the message would be like '...: expecting "let" or "letter"', which is
    /// less friendly.
    ///
    /// This method has the synonym infix operator `<?>`.
    ///
    /// - parameter message: The new error message.
    /// - returns: A parser with a replaced error message.
    fun labels(vararg message: String) : Parsec <Stream, UserState, Result>

    /// Return a parser that always fails with an unexpected error message
    /// without consuming any input.
    ///
    /// The parsers 'fail', '\<?\>' and `unexpected` are the three parsers used
    /// to generate error messages. Of these, only '<?>' is commonly used. For
    /// an example of the use of `unexpected`, see the definition of
    /// `GenericParser.noOccurence`.
    ///
    /// - parameter message: The error message.
    /// - returns: A parser that always fails with an unexpected error message
    ///   without consuming any input.
    /// - SeeAlso: `GenericParser.noOccurence`, `GenericParser.fail(message:
    ///   String)` and `<?>`
    companion object {
        @JvmStatic
        fun unexpected(message: String) : Parsec <Stream, UserState, Result>
    }

    /// Return a parser that always fails with the supplied message.
    ///
    /// - parameter message: The failure message.
    /// - returns: A parser that always fail.
    companion object {
        @JvmStatic
        fun fail(message: String) : Parsec <Stream, UserState, Result>
    }

    /// Return the current source position.
    ///
    /// - returns: The current source position.
    /// - SeeAlso 'SourcePosition'.
    companion object {
        @JvmStatic
        var sourcePosition : GenericParser <Stream, UserState, SourcePosition>
    }
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