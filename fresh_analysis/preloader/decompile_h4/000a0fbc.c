// File offset entry: 0xa0fbc

char * FUN_020a0ebc(char *param_1,char *param_2)

{
  bool bVar1;
  int iVar2;
  int iVar3;
  char *pcVar4;
  char *pcVar5;
  char cVar6;
  char cVar7;
  
  if (param_1 == (char *)0x0) {
    if (pcRam0000000002122230 == (char *)0x0) {
      param_1 = (char *)0x0;
    }
    else {
      cVar7 = *pcRam0000000002122230;
      while (cVar7 != '\0') {
        pcRam0000000002122230 = pcRam0000000002122230 + 1;
        iRam0000000002122228 = iRam0000000002122228 + -1;
        cVar7 = *pcRam0000000002122230;
      }
      if (iRam0000000002122228 == 0) {
        param_1 = (char *)0x0;
        pcRam0000000002122230 = (char *)0x0;
        iRam0000000002122228 = 0;
      }
      else {
        param_1 = pcRam0000000002122230 + 1;
        iRam0000000002122228 = iRam0000000002122228 + -1;
        pcRam0000000002122230 = param_1;
      }
    }
  }
  else {
    pcRam0000000002122230 = param_1;
    if (*param_1 == '\0') {
      iRam0000000002122228 = 0;
    }
    else {
      iRam0000000002122228 = 0;
      pcVar4 = param_1;
      do {
        iVar2 = iRam0000000002122228;
        pcVar4 = pcVar4 + 1;
        iRam0000000002122228 = iVar2 + 1;
      } while (*pcVar4 != '\0');
      if (iRam0000000002122228 != 0) {
        pcVar4 = param_1;
        iVar3 = 0;
        do {
          cVar7 = *param_2;
          if (cVar7 != '\0') {
            cVar6 = *pcVar4;
            pcVar5 = param_2;
            do {
              pcVar5 = pcVar5 + 1;
              if (cVar6 == cVar7) {
                cVar6 = '\0';
                *pcVar4 = '\0';
              }
              cVar7 = *pcVar5;
            } while (cVar7 != '\0');
          }
          bVar1 = iVar2 != iVar3;
          pcVar4 = pcVar4 + 1;
          iVar3 = iVar3 + 1;
        } while (bVar1);
      }
    }
  }
  return param_1;
}


