// ffff0000907c0b68  size=96  FUN_ffff0000907c0b68

long FUN_ffff0000907c0b68(long param_1)

{
  long lVar1;
  long lVar2;
  
  lVar1 = thunk_FUN_ffff00009077ef88();
  if (-1 < lVar1 + -2) {
    do {
      if (*(char *)(param_1 + lVar1 + -2) == '/') {
        return param_1 + lVar1 + -1;
      }
      lVar2 = lVar1 + -2;
      lVar1 = lVar1 + -1;
    } while (0 < lVar2);
  }
  return param_1;
}


