// ffff00009076deb0  size=236  FUN_ffff00009076deb0

long FUN_ffff00009076deb0(long param_1,int param_2,long *param_3,uint *param_4)

{
  uint uVar1;
  long lVar2;
  long lVar3;
  uint local_24;
  
  uVar1 = FUN_ffff00009076cee8();
  if ((int)uVar1 < 0) {
    if (param_4 != (uint *)0x0) {
      *param_4 = uVar1;
      return 0;
    }
LAB_ffff00009076df80:
    lVar3 = 0;
  }
  else {
    uVar1 = (*(uint *)(param_1 + 8) & 0xff00ff00) >> 8 | (*(uint *)(param_1 + 8) & 0xff00ff) << 8;
    lVar3 = param_1 + (ulong)(uVar1 >> 0x10 | uVar1 << 0x10) + (long)param_2;
    if (param_4 != (uint *)0x0) {
      uVar1 = (*(uint *)(lVar3 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 4) & 0xff00ff) << 8;
      *param_4 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
    if (param_3 != (long *)0x0) {
      local_24 = 0;
      uVar1 = (*(uint *)(lVar3 + 8) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 8) & 0xff00ff) << 8;
      lVar2 = FUN_ffff00009076d2c4(param_1,uVar1 >> 0x10 | uVar1 << 0x10,&local_24);
      *param_3 = lVar2;
      if (lVar2 == 0) {
        if (param_4 != (uint *)0x0) {
          *param_4 = local_24;
        }
        goto LAB_ffff00009076df80;
      }
    }
    uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
    if ((((uVar1 >> 0x10 | uVar1 << 0x10) < 0x10) && ((param_2 + 4U & 7) != 0)) &&
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


