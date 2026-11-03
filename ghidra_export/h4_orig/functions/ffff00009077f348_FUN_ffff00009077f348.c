// ffff00009077f348  size=16  FUN_ffff00009077f348

int FUN_ffff00009077f348(char *param_1,char *param_2,long param_3)

{
  char cVar1;
  char cVar2;
  
  if (param_3 != 0) {
    do {
      param_3 = param_3 + -1;
      cVar1 = *param_1;
      cVar2 = cVar1 - *param_2;
      param_1 = param_1 + 1;
      param_2 = param_2 + 1;
    } while ((cVar2 == '\0' && cVar1 != '\0') && param_3 != 0);
    return (int)cVar2;
  }
  return 0;
}


