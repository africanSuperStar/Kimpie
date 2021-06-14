package ai.hinton.parsec

// The primitive parser combinators.
//==============================================================================

//==============================================================================
/// `GenericParser` is a generic implementation of the `Parsec`.
///
/// - requires: StreamType.Iterator has to be a value type.
public sealed class GenericParser <Stream, UserState, Result> : Parsec <Stream, UserState, Result>
{

}
