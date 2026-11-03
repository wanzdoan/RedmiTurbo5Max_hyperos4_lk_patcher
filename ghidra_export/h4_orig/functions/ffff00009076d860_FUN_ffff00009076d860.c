// ffff00009076d860  size=452  FUN_ffff00009076d860

int FUN_ffff00009076d860(long param_1,char *param_2,int param_3)

{
  char *pcVar1;
  uint uVar2;
  int iVar3;
  char *pcVar4;
  char *pcVar5;
  undefined8 uVar6;
  long lVar7;
  char *pcVar8;
  char *pcVar9;
  int local_48;
  int local_44;
  
  iVar3 = FUN_ffff00009076ca9c();
  if (iVar3 < 0) {
    return iVar3;
  }
  if (0 < param_3) {
    pcVar1 = param_2 + param_3;
    if (*param_2 == '/') {
      iVar3 = 0;
      pcVar8 = param_2;
LAB_ffff00009076d8b0:
      do {
        pcVar5 = pcVar8;
        pcVar9 = pcVar8;
        if (pcVar1 <= pcVar8) {
          return iVar3;
        }
        while (*pcVar5 == '/') {
          pcVar5 = pcVar5 + 1;
          pcVar9 = pcVar9 + 1;
          if (pcVar5 == pcVar1) {
            return iVar3;
          }
        }
        pcVar4 = (char *)FUN_ffff00009077eab8(pcVar5,0x2f,(long)pcVar1 - (long)pcVar9);
        pcVar8 = pcVar1;
        if (pcVar4 != (char *)0x0) {
          pcVar8 = pcVar4;
        }
        iVar3 = FUN_ffff00009076d71c(param_1,iVar3,pcVar5,(int)pcVar8 - (int)pcVar9);
      } while (-1 < iVar3);
      return iVar3;
    }
    pcVar5 = (char *)FUN_ffff00009077eab8(param_2,0x2f);
    pcVar8 = pcVar1;
    if (pcVar5 != (char *)0x0) {
      pcVar8 = pcVar5;
    }
    local_48 = 0;
    uVar6 = FUN_ffff00009077f250(s__aliases_ffff0000907deb6c);
    iVar3 = FUN_ffff00009076d860(param_1,s__aliases_ffff0000907deb6c,uVar6);
    if (-1 < iVar3) {
      local_44 = 0;
      lVar7 = FUN_ffff00009076dc10
                        (param_1,iVar3,param_2,(int)pcVar8 - (int)param_2,&local_48,&local_44);
      if (lVar7 != 0) {
        uVar2 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
                (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
        if ((((uVar2 >> 0x10 | uVar2 << 0x10) < 0x10) && ((local_44 + 4U & 7) != 0)) &&
           (uVar2 = (*(uint *)(lVar7 + 4) & 0xff00ff00) >> 8 |
                    (*(uint *)(lVar7 + 4) & 0xff00ff) << 8, 7 < (uVar2 >> 0x10 | uVar2 << 0x10))) {
          pcVar5 = (char *)(lVar7 + 0x10);
        }
        else {
          pcVar5 = (char *)(lVar7 + 0xc);
        }
        if (((0 < local_48) && (pcVar5[local_48 - 1] == '\0')) && (*pcVar5 == '/')) {
          uVar6 = FUN_ffff00009077f250(pcVar5);
          iVar3 = FUN_ffff00009076d860(param_1,pcVar5,uVar6);
          goto LAB_ffff00009076d8b0;
        }
      }
    }
  }
  return -5;
}


