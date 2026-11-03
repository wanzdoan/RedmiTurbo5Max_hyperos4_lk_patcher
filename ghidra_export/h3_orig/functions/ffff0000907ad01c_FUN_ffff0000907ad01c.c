// ffff0000907ad01c  size=32  FUN_ffff0000907ad01c

void FUN_ffff0000907ad01c(char *param_1,char *param_2,long param_3)

{
  char cVar1;
  
  do {
    if (param_3 == 0) {
      return;
    }
    cVar1 = *param_2;
    param_3 = param_3 + -1;
    *param_1 = cVar1;
    param_1 = param_1 + 1;
    param_2 = param_2 + 1;
  } while (cVar1 != '\0');
  return;
}


