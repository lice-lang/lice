module LiceHaskell
  ( liceEval
  , licePretty
  , liceParse
  ) where

import Data.Char
import Data.List.Split (splitOn)
import Data.Maybe
import Control.Monad
import Control.Applicative

data AST = I32 Int
         | Sym String
         | Str String
         | Nul
         | Err String
         | Lst [AST]
         | Par (AST, AST)
         | Boo Bool
         | Tmp String
         | Nod AST [AST]
         deriving (Eq, Show)
--

alpha = ['a'..'z'] ++ ['A'..'Z']

-----------------------------------------------------
--------------- my parser combinator ----------------
-----------------------------------------------------

newtype Parser val = Parser { parse :: String -> [(val, String)]  }

runParser :: Parser a -> String -> Maybe a
runParser m s = case parse m s of
  [(res, [])] -> Just res
  _           -> Nothing
--

instance Functor Parser where
  fmap f (Parser ps) = Parser $ \p -> [(f a, b) | (a, b) <- ps p]
--

instance Applicative Parser where
  pure = return
  (Parser p1) <*> (Parser p2) = Parser $ \p ->
    [ (f a, s2) | (f, s1) <- p1 p, (a, s2) <- p2 s1 ]
--

instance Monad Parser where
  return a = Parser $ \s -> [(a, s)]
  p >>= f  = Parser $ concatMap (\(a, s1) -> parse (f a) s1) . parse p
--

instance MonadPlus Parser where
  mzero     = Parser $ const []
  mplus p q = Parser $ \s -> parse p s ++ parse q s
--

instance Alternative Parser where
  empty   = mzero
  p <|> q = Parser $ \s -> case parse p s of
    [] -> parse q s
    rs -> rs
--

item :: Parser Char
item = Parser $ \s -> case s of
  [     ] -> []
  (h : t) -> [(h, t)]
--

satisfy :: (Char -> Bool) -> Parser Char
satisfy p = item >>= \c -> if p c then return c else empty

chainl1 :: Parser a -> Parser (a -> a -> a) -> Parser a
chainl1 p op = do
  a <- p
  rest a
  where rest a = (do
          f <- op
          b <- p
          rest $ f a b)
          <|> return a
--

chainl :: Parser a -> Parser (a -> a -> a) -> a -> Parser a
chainl p op = (chainl1 p op <|>) . return

oneOf ls = satisfy (`elem` ls)
noneOf ls = satisfy $ not . (`elem` ls)
char = satisfy . (==)
nat = read <$> some digit :: Parser Int
digit = satisfy isDigit
reserved = token . string
spaces = many $ oneOf " \n\r\t,"

true = do
  reserved "true"
  return $ Boo True
--

false = do
  reserved "false"
  return $ Boo False
--

null' = do
  reserved "null"
  return Nul
--

string [      ] = return []
string (c : cs) = do
  char c
  string cs
  return $ c : cs
--

token p = do
  a <- p
  spaces
  return a
--

number = do
  s <- string "-" <|> return []
  cs <- some digit
  return $ read $ s ++ cs
--

parens m = do
  reserved "("
  n <- m
  reserved ")"
  return n
--

int :: Parser AST
int = do
  n <- number
  spaces
  return $ I32 n
--

sym :: Parser AST
sym = do
  n <- some $ noneOf "() ,"
  spaces
  return $ Sym n
--

node = do
  nodes <- parens $ many expr
  return $ case nodes of
    []      -> Nul
    (h : t) -> Nod h t
--

expr = true <|> false <|> int <|> null' <|> sym <|> node

-- optimize :: AST -> AST
-- optimize n@(I32 _) = n
-- optimize s@(Sym _) = s
-- optimize otherwise = otherwise
-- -- optimize (Add a b) = case (optimize a, optimize b) of
-- --   (Imm x, Imm y) -> Imm $ x + y
-- --   (  x  ,   y  ) -> Add x y
-- --

preludeFunctions :: [(String, [AST] -> AST)]
preludeFunctions =
  [ ("+", I32 . sum . (toI <$>))
  , ("*", I32 . product . (toI <$>))
  , ("-", I32 . foldr1 (-) . (toI <$>))
  , ("/", I32 . foldr1 div . (toI <$>))
  , ("^", \[I32 a, I32 b] -> I32 $ a ^ b)
  , (">", \[I32 a, I32 b] -> Boo $ a > b)
  , ("<", \[I32 a, I32 b] -> Boo $ a < b)
  , ("!", \[Boo b] -> Boo $ not b)
  , ("list", Lst)
  , ("size", \[Lst ls] -> I32 $ length ls)
  , ("reverse", \[Lst ls] -> Lst $ reverse ls)
  , ("..", \[I32 a, I32 b] -> Lst $ I32 <$> [a .. b])
  , ("==", \[I32 a, I32 b] -> Boo $ a == b)
  , (">=", \[I32 a, I32 b] -> Boo $ a >= b)
  , ("<=", \[I32 a, I32 b] -> Boo $ a <= b)
  , ("!=", \[I32 a, I32 b] -> Boo $ a /= b)
  , ("/=", \[I32 a, I32 b] -> Boo $ a /= b)
  , ("exit", const $ error "Exited Lice REPL.")
  , ("str-con", Str . join . (toS <$>))
  , ("if", \l -> case l of
        [Boo c, a, b] -> if c then a else b
        [Boo c, a]    -> if c then a else Nul)
  ]

  where toI (I32 i) = i
        toS (I32 i) = show i
        toS (Sym s) = s
        toS (Boo b) = show b
        toS  Nul    = "null"
        toS (Str s) = s
--

liceParse :: String -> Maybe AST
liceParse = runParser expr

licePretty :: String -> Maybe String
licePretty s = licePretty' <$> liceParse s

liceEval :: String -> Maybe String
liceEval s = licePretty' . liceEval' <$> liceParse s

liceEval' :: AST -> AST
liceEval' (Nod f l) = case f of
  s@(Sym _)   -> funcInvoke s l
  n@(Nod _ _) -> liceEval' n `funcInvoke` l
  others      -> others
liceEval' others     = others
--

funcInvoke (Sym s) l = case lookup s preludeFunctions of
  (Just f) -> f $ liceEval' <$> l
  Nothing  -> Err "Function not found"
--

licePretty' :: AST -> String
licePretty' (Boo True)  = "true"
licePretty' (Boo False) = "false"
licePretty'  Nul        = "null"
licePretty' (Sym s)     = s
licePretty' (Str s)     = "\"" ++ s ++ "\""
licePretty' (I32 n)     = show n
licePretty' (Err s)     = "{ error:" ++ s ++ " }"
licePretty' (Lst l)     = tail $ join ((' ' :) . licePretty' <$> l)
licePretty' (Nod f p)   = "(" ++ licePretty' f ++ join ((' ' :) . licePretty' <$> p) ++ ")"

main :: IO ()
main = do
  putStrLn "Welcome to lice.hs."
  putStrLn "This is a Haskell implementation of a subset of Lice language."
  putStrLn "see: https://github.com/lice-lang/lice"
  putStrLn "剑未佩妥，出门已是江湖。千帆过尽，归来仍是少年。\n"
  forever $ do
    putStr "|> "
    code <- getLine
    putStrLn $ case liceEval code of
      (Just res) -> res
      Nothing    -> "Hugh?"
--
