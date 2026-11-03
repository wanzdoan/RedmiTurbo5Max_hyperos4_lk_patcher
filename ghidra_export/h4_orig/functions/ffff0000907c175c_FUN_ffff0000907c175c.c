// ffff0000907c175c  size=116  FUN_ffff0000907c175c

char * FUN_ffff0000907c175c(char *param_1,char *param_2)

{
  char cVar1;
  char *pcVar2;
  char *pcVar3;
  long lVar4;
  char *pcVar5;
  
  cVar1 = *param_1;
  if (cVar1 != '\0') {
    lVar4 = 0;
    pcVar5 = param_1;
    do {
      pcVar5 = pcVar5 + 1;
      if (cVar1 == *param_2) {
        cVar1 = param_2[1];
        pcVar2 = param_2 + 2;
        pcVar3 = pcVar5;
        while( true ) {
          if (cVar1 == '\0') {
            return param_1 + lVar4;
          }
          if (*pcVar3 != cVar1) break;
          cVar1 = *pcVar2;
          pcVar2 = pcVar2 + 1;
          pcVar3 = pcVar3 + 1;
        }
      }
      lVar4 = lVar4 + 1;
      cVar1 = param_1[lVar4];
    } while (cVar1 != '\0');
  }
  return (char *)0x0;
}


