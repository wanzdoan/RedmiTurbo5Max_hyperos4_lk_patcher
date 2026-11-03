
uint FUN_ffff0000b80472b4(long param_1,ulong param_2,uint *param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint *puVar6;
  long lVar7;
  char *pcVar8;
  uint uVar9;
  
  *param_3 = 0xfffffff8;
  puVar6 = (uint *)FUN_ffff0000b804720c(param_1,param_2,4);
  if (puVar6 != (uint *)0x0) {
    uVar3 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
    uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
    *param_3 = 0xfffffff5;
    if (8 < uVar3 - 1) {
      return 9;
    }
    uVar1 = (int)param_2 + 4;
    uVar9 = uVar1;
    switch(uVar3) {
    case 1:
      do {
        pcVar8 = (char *)FUN_ffff0000b804720c(param_1,uVar9,1);
        if (pcVar8 == (char *)0x0) {
          return 9;
        }
        uVar9 = uVar9 + 1;
      } while (*pcVar8 != '\0');
      break;
    case 2:
    case 4:
    case 9:
      break;
    case 3:
      puVar6 = (uint *)FUN_ffff0000b804720c(param_1,uVar1,4);
      if (puVar6 == (uint *)0x0) {
        return 9;
      }
      uVar9 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      uVar4 = uVar9 >> 0x10 | uVar9 << 0x10;
      uVar2 = uVar4 + uVar1;
      if (0x7ffffffe < uVar2) {
        return 9;
      }
      if (CARRY4(uVar4,uVar1)) {
        return 9;
      }
      uVar9 = uVar2 + 8;
      uVar5 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
      if ((((uVar5 >> 0x10 | uVar5 << 0x10) < 0x10) && (7 < uVar4)) && ((uVar1 & 7) != 0)) {
        uVar9 = uVar2 + 0xc;
      }
      break;
    default:
      goto code_r0xffff0000b80473f4;
    }
    lVar7 = FUN_ffff0000b804720c(param_1,param_2 & 0xffffffff,uVar9 - (int)param_2);
    if (lVar7 != 0) {
      *param_3 = uVar9 + 3 & 0xfffffffc;
      return uVar3;
    }
  }
code_r0xffff0000b80473f4:
  return 9;
}


