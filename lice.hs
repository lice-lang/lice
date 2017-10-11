{-# LANGUAGE LambdaCase #-}

module LispLovesMe where

import Data.List
import Data.Maybe
import Text.ParserCombinators.ReadP

data AST = I32 Int
         | Sym String
         | Nul
         | Err
         | Lst [AST]
         | Boo Bool
         | Nod AST [AST]
         deriving (Eq, Show)
--

expr = symbols
   +++ numbers
   +++ booleans
   +++ nulls
   +++ nodes

nodes = do
   char '('
   whiteSpace
   fist <- expr
   exprs <- many expr
   char ')'
   whiteSpace
   return $ Nod fist exprs

nulls = (const Nul <$> string "null" <* whiteSpace) +++ nul
  where
    nul = do
        char '('
        whiteSpace
        char ')'
        whiteSpace
        return Nul

booleans = Boo <$> choice [ const True <$> string "true", const False <$> string "false" ] <* whiteSpace
numbers = I32 . read <$> munch1 (`elem` ['0'..'9']) <* whiteSpace

symbols = do
  fist <- satisfy (`notElem` (" ,\n\t\r()" ++ [ '0' .. '9' ]))
  tal <- munch (`notElem` " ,\n\t\r()") <* whiteSpace
  let sym = fist : tal
  if sym `notElem` [ "true", "false", "null" ]
    then return $ Sym sym
    else pfail

whiteSpace = many $ satisfy (`elem`",\r\n\t ")

eval :: AST -> AST
eval (Nod (Sym fist) param) =
    if err
       then Err
       else fromMaybe Err $ ($ ps') <$> lookup fist preludeFunctions
  where
    ps' = fmap eval param
    err = any (\case Err -> True; _ -> False) ps'
eval (Nod _ _) = Err
eval x = x

preludeFunctions :: [(String, [AST] -> AST)]
preludeFunctions =
  [ ("+", checkErr (op (+)))
  , ("*", checkErr (op (*)))
  , ("-", checkErr (op (-)))
  , ("/", checkErr (op div))
  , ("^", \ps -> if length ps == 2 then checkErr (op (^)) ps else Err)
  , (">", \ps -> if length ps == 2 then checkErr (op2bb (>)) ps else Err)
  , ("<", \ps -> if length ps == 2 then checkErr (op2bb (<)) ps else Err)
  , ("!", \ps -> if length ps == 1
                   then case eval $ head ps of
                     Boo b -> Boo $ not b
                     _     -> Err
                   else Err)
  , ("list", checkErr Lst)
  , ("size", checkErr size)
  , ("reverse", checkErr reverse')
  , ("..", checkErr range)
  , ("==", \ps -> if length ps == 2 then checkErr (op2bb (==)) ps else Err)
  , (">=", \ps -> if length ps == 2 then checkErr (op2bb (>=)) ps else Err)
  , ("<=", \ps -> if length ps == 2 then checkErr (op2bb (<=)) ps else Err)
  , ("!=", \ps -> if length ps == 2 then checkErr (op2bb (/=)) ps else Err)
  , ("if", if')
  ]
  where
    checkErr f ps = if err then Err else f ps'
      where
        ps' = fmap eval ps
        err = any (\case Err -> True; _ -> False) ps'
    op f [] = Err
    op f ps = if any (\case I32 x -> False; _ -> True ) prs
                then Err
                else I32 $ foldl1 f ps'
      where
        prs = eval <$> ps
        ps' = (\case I32 v -> v) <$> prs
    op2bb f [I32 a, I32 b] = Boo $ f a b
    op2bb _ _              = Err
    reverse' [Lst x] = Lst $ reverse x
    reverse' _       = Err
    range [I32 a, I32 b] = Lst $ I32 <$> [a..b]
    range _              = Err
    if' (p : a : b) = case p' of
        Boo x -> if x then eval a else case b of
          [ ] -> Nul
          [x] -> eval x
          _   -> Err
        _     -> Err
      where p' = eval p
    if' _             = Err
    size [Lst ls] = I32 $ length ls
    size _        = Err
--
pretty :: AST -> String
pretty (I32 xs)    = show xs
pretty (Nod a b)   = "(" ++ unwords (pretty <$> (a:b)) ++ ")"
pretty (Sym s)     = s
pretty Nul         = "null"
pretty (Boo True)  = "true"
pretty (Boo False) = "false"

lispPretty :: String -> Maybe String
lispPretty s = case filter ((== "") . snd) $ readP_to_S expr $ trimH s of
  [] -> Nothing
  xs -> Just $ pretty $ fst $ last xs

lispEval :: String -> Maybe AST
lispEval s = case filter ((== "") . snd) $ readP_to_S expr $ trimH s of
  [] -> Nothing
  xs -> Just $ eval $ fst $ last xs

trimH (x : xs) | x `elem` ",\r\n\t " = trimH xs
               | otherwise           = x : xs
