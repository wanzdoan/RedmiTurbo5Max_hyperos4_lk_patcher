// ffff00009076de08  size=320  FUN_ffff00009076de08

int FUN_ffff00009076de08(long param_1,undefined4 param_2,undefined8 param_3,undefined8 param_4)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  undefined8 uVar6;
  long lVar7;
  int iVar8;
  ulong uVar9;
  int local_48;
  int local_44;
  
  local_48 = 0;
  uVar6 = FUN_ffff00009077ef88(param_3);
  local_44 = 0;
  lVar7 = FUN_ffff00009076d948(param_1,param_2,param_3,uVar6,&local_48,&local_44);
  if (lVar7 != 0) {
    uVar3 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
    if ((((uVar3 >> 0x10 | uVar3 << 0x10) < 0x10) && ((local_44 + 4U & 7) != 0)) &&
       (uVar3 = (*(uint *)(lVar7 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar7 + 4) & 0xff00ff) << 8,
       7 < (uVar3 >> 0x10 | uVar3 << 0x10))) {
      uVar9 = lVar7 + 0x10;
    }
    else {
      uVar9 = lVar7 + 0xc;
    }
    iVar4 = FUN_ffff00009077ef88(param_4);
    if (0 < local_48) {
      iVar8 = 0;
      uVar1 = uVar9 + (long)local_48;
      do {
        iVar5 = FUN_ffff00009077f16c(uVar9,uVar1 - uVar9);
        local_48 = iVar5 + 1;
        uVar2 = uVar9 + (long)local_48;
        if (uVar1 < uVar2) {
          return -0xf;
        }
        if ((iVar5 == iVar4) && (iVar5 = FUN_ffff00009077e820(uVar9,param_4), iVar5 == 0)) {
          return iVar8;
        }
        iVar8 = iVar8 + 1;
        uVar9 = uVar2;
      } while (uVar2 < uVar1);
    }
    local_48 = -1;
  }
  return local_48;
}


