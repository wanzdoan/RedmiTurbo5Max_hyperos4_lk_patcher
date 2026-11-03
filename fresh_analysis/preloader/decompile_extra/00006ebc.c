// File offset entry: 0x6ebc

long FUN_02006dbc(long param_1)

{
  char *pcVar1;
  char cVar2;
  char cVar3;
  long lVar4;
  int iVar5;
  int iVar6;
  long lVar7;
  
  if (cRam000000000210e034 != '\x01') {
    return 0;
  }
  if ((lRam000000000210e068 == 0) || (iRam0000000000104b80 != -1)) {
    lVar4 = 0;
LAB_02006e3c:
    do {
      do {
        iVar5 = (int)lVar4;
        if (*(char *)(lRam000000000210e048 + iVar5) == '\0') goto LAB_02006e14;
        lVar4 = (long)iVar5 + 1;
        do {
          if (0x3ff < lVar4 + -1) goto LAB_02006e14;
          pcVar1 = (char *)(lRam000000000210e048 + lVar4);
          lVar4 = lVar4 + 1;
        } while (*pcVar1 != '\0');
      } while (param_1 == 0);
      lVar7 = 0;
      do {
        cVar2 = *(char *)(param_1 + lVar7);
        cVar3 = *(char *)(lRam000000000210e048 + iVar5 + lVar7);
        iVar6 = (int)lVar7;
        if (cVar2 != cVar3) {
          if (((iVar5 + iVar6 + 1 < 0) || (cVar2 != '\0')) || (cVar3 != '=')) goto LAB_02006e3c;
          goto LAB_02006ec0;
        }
        lVar7 = lVar7 + 1;
      } while (cVar2 != '=');
    } while (iVar5 + (int)lVar7 < 0);
LAB_02006ec0:
    func_0x02007048(0x20c160c);
    FUN_0208ada0();
    lVar4 = lRam000000000210e048 + (ulong)(iVar5 + iVar6 + 1);
  }
  else {
    FUN_0208ada0(0x20c57d3,0x20ccfd2);
LAB_02006e14:
    lVar4 = 0;
  }
  return lVar4;
}


