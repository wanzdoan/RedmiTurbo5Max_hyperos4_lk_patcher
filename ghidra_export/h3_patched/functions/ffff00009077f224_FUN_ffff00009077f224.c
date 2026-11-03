// ffff00009077f224  size=88  FUN_ffff00009077f224

long FUN_ffff00009077f224(char *param_1,char *param_2)

{
  long lVar1;
  char cVar2;
  char *pcVar3;
  char cVar4;
  
  cVar2 = *param_1;
  if (cVar2 != '\0') {
    lVar1 = 0;
    while (pcVar3 = param_2, cVar4 = *param_2, *param_2 != '\0') {
      while (pcVar3 = pcVar3 + 1, cVar2 != cVar4) {
        cVar4 = *pcVar3;
        if (cVar4 == '\0') {
          return lVar1;
        }
      }
      param_1 = param_1 + 1;
      cVar2 = *param_1;
      lVar1 = lVar1 + 1;
      if (cVar2 == '\0') {
        return lVar1;
      }
    }
  }
  return 0;
}


