// ffff00009076dcd4  size=168  FUN_ffff00009076dcd4

long FUN_ffff00009076dcd4(long param_1,undefined4 param_2,undefined8 param_3,undefined8 param_4)

{
  uint uVar1;
  undefined8 uVar2;
  long lVar3;
  int local_34;
  
  uVar2 = FUN_ffff00009077ef88(param_3);
  local_34 = 0;
  lVar3 = FUN_ffff00009076d948(param_1,param_2,param_3,uVar2,param_4,&local_34);
  if (lVar3 != 0) {
    uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
    if ((((uVar1 >> 0x10 | uVar1 << 0x10) < 0x10) && ((local_34 + 4U & 7) != 0)) &&
       (uVar1 = (*(uint *)(lVar3 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 4) & 0xff00ff) << 8,
       7 < (uVar1 >> 0x10 | uVar1 << 0x10))) {
      lVar3 = lVar3 + 0x10;
    }
    else {
      lVar3 = lVar3 + 0xc;
    }
  }
  return lVar3;
}


