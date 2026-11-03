// ffff00009077f1a4  size=80  FUN_ffff00009077f1a4

char * FUN_ffff00009077f1a4(char *param_1,char *param_2)

{
  char cVar1;
  char cVar2;
  char *pcVar3;
  
  cVar1 = *param_1;
  if (cVar1 != '\0') {
    cVar2 = *param_2;
    pcVar3 = param_2;
    do {
      while (cVar2 != '\0') {
        if (cVar1 == cVar2) {
          return param_1;
        }
        cVar2 = pcVar3[1];
        pcVar3 = pcVar3 + 1;
      }
      param_1 = param_1 + 1;
      cVar1 = *param_1;
      cVar2 = *param_2;
      pcVar3 = param_2;
    } while (cVar1 != '\0');
  }
  return (char *)0x0;
}


