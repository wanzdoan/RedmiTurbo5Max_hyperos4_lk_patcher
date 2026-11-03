
long FUN_ffff0000be061040(char *param_1,long param_2)

{
  char *pcVar1;
  char *pcVar2;
  char *pcVar3;
  
  pcVar3 = param_1;
  if (param_2 != 0) {
    pcVar1 = param_1 + param_2;
    pcVar2 = param_1;
    do {
      pcVar3 = pcVar2;
      if (*pcVar2 == '\0') break;
      param_2 = param_2 + -1;
      pcVar2 = pcVar2 + 1;
      pcVar3 = pcVar1;
    } while (param_2 != 0);
  }
  return (long)pcVar3 - (long)param_1;
}


