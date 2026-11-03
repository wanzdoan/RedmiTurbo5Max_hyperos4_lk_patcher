
undefined8 FUN_ffff0000b8027d24(char *param_1,char *param_2)

{
  long lVar1;
  bool bVar2;
  int iVar3;
  int iVar4;
  undefined8 uVar5;
  long lVar6;
  char *pcVar7;
  char *pcVar8;
  char *pcVar9;
  char cVar10;
  char *pcVar11;
  long lVar12;
  char *pcVar13;
  char *pcVar14;
  
  pcVar7 = DAT_ffff0000b8124f48;
  uVar5 = 0xffffffff;
  if ((param_1 != (char *)0x0) && (param_2 != (char *)0x0)) {
    pcVar14 = DAT_ffff0000b8124f48;
    if (DAT_ffff0000b811cf38 != 0) {
      pcVar14 = pcVar7;
      if (*DAT_ffff0000b8124f48 != '\0') {
        pcVar9 = DAT_ffff0000b8124f48;
        pcVar11 = DAT_ffff0000b8124f48;
        do {
          do {
            pcVar8 = pcVar9;
            pcVar9 = pcVar8 + 1;
          } while (*pcVar9 != '\0');
          lVar12 = (long)((int)pcVar11 - (int)DAT_ffff0000b8124f48);
          pcVar13 = param_1;
          iVar3 = ((int)DAT_ffff0000b8124f48 + -1) - (int)pcVar11;
          do {
            iVar4 = iVar3;
            cVar10 = *pcVar13;
            if (cVar10 != DAT_ffff0000b8124f48[lVar12]) {
              if ((cVar10 != '\0') || (DAT_ffff0000b8124f48[lVar12] != '='))
              goto LAB_ffff0000b8027d84;
              break;
            }
            pcVar13 = pcVar13 + 1;
            lVar12 = lVar12 + 1;
            iVar3 = iVar4 + -1;
          } while (cVar10 != '=');
          if (-1 < -iVar4) {
            if (iVar4 != 0) {
              if (pcVar8[2] == '\0') {
                if (DAT_ffff0000b8124f48 < pcVar11) {
                  pcVar11 = pcVar11 + -1;
                }
                else {
                  *pcVar11 = '\0';
                }
              }
              else {
                pcVar11 = pcVar11 + -1;
                lVar12 = 2;
                cVar10 = pcVar8[2];
                do {
                  pcVar11 = pcVar11 + 1;
                  *pcVar11 = cVar10;
                  pcVar8 = pcVar9 + lVar12;
                  lVar12 = lVar12 + 1;
                  bVar2 = cVar10 != '\0';
                  cVar10 = *pcVar8;
                } while (bVar2 || *pcVar8 != '\0');
              }
              pcVar11[1] = '\0';
            }
            break;
          }
LAB_ffff0000b8027d84:
          pcVar11 = pcVar8 + 2;
          pcVar9 = pcVar11;
        } while (*pcVar11 != '\0');
      }
      for (; (*pcVar14 != '\0' || (pcVar14[1] != '\0')); pcVar14 = pcVar14 + 1) {
      }
      if (pcVar7 < pcVar14) {
        pcVar14 = pcVar14 + 1;
      }
    }
    iVar3 = FUN_ffff0000b804d3cc(param_1);
    iVar4 = FUN_ffff0000b804d3cc(param_2);
    if ((long)(pcVar7 + (0x3fec - (long)pcVar14)) < (long)(iVar4 + iVar3 + 3)) {
      uVar5 = 0xffffffff;
    }
    else {
      cVar10 = *param_1;
      *pcVar14 = cVar10;
      while (cVar10 != '\0') {
        param_1 = param_1 + 1;
        cVar10 = *param_1;
        pcVar14 = pcVar14 + 1;
        *pcVar14 = cVar10;
      }
      pcVar7 = pcVar7 + (0x3feb - (long)pcVar14);
      *pcVar14 = '=';
      pcVar14 = pcVar14 + 1;
      do {
        cVar10 = *param_2;
        pcVar7 = pcVar7 + -1;
        pcVar9 = pcVar14 + 1;
        *pcVar14 = cVar10;
        param_2 = param_2 + 1;
        pcVar14 = pcVar9;
      } while (cVar10 != '\0');
      *pcVar9 = '\0';
      FUN_ffff0000b804d070(pcVar9,0,pcVar7);
      lVar12 = FUN_ffff0000b804a99c(&DAT_ffff0000b808aacf);
      if (lVar12 != 0) {
        FUN_ffff0000b804cd40(&DAT_ffff0000b811cf40,s_ENV_v1_ffff0000b8087ed3,7);
        FUN_ffff0000b804cd40(&DAT_ffff0000b8120f34,s_ENV_v1_ffff0000b8087ed3,7);
        iVar3 = 0;
        iVar4 = 0;
        lVar6 = 0;
        do {
          lVar1 = lVar6 + 2;
          iVar3 = iVar3 + (uint)(byte)(&DAT_ffff0000b811cf48)[lVar6];
          iVar4 = iVar4 + (uint)(byte)(&DAT_ffff0000b811cf49)[lVar6];
          lVar6 = lVar1;
        } while (lVar1 != 0x3fec);
        DAT_ffff0000b8120f3c = iVar4 + iVar3;
        lVar6 = FUN_ffff0000b804aab8(lVar12,&DAT_ffff0000b811cf40,0x20000,0x4000);
        FUN_ffff0000b804a920(lVar12);
        if (lVar6 == 0x4000) {
          DAT_ffff0000b811cf38 = 1;
          return 0;
        }
      }
      FUN_ffff0000b804d070(&DAT_ffff0000b811cf40,0,0x4000);
      uVar5 = 0xfffffffb;
    }
  }
  return uVar5;
}


