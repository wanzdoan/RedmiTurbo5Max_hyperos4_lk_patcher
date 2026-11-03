// ffff00009076cffc  size=272  FUN_ffff00009076cffc

long FUN_ffff00009076cffc(uint *param_1,uint param_2,uint *param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  uint uVar5;
  long lVar6;
  
  uVar3 = FUN_ffff00009076c7d4();
  if (-1 < (int)uVar3) {
    uVar1 = (param_1[3] & 0xff00ff00) >> 8 | (param_1[3] & 0xff00ff) << 8;
    uVar1 = (uVar1 >> 0x10 | uVar1 << 0x10) + param_2;
    uVar2 = uVar3 - uVar1;
    if (uVar1 <= uVar3 && uVar2 != 0) {
      uVar3 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
      uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
      uVar5 = uVar2;
      if (uVar3 == 0x2ff20112) {
        if (((int)param_2 < 0) &&
           (param_2 = -param_2,
           uVar3 = (param_1[8] & 0xff00ff00) >> 8 | (param_1[8] & 0xff00ff) << 8,
           param_2 <= (uVar3 >> 0x10 | uVar3 << 0x10))) {
          if (param_2 <= uVar2) {
            uVar5 = param_2;
          }
          goto LAB_ffff00009076d0e0;
        }
      }
      else {
        if (uVar3 != 0xd00dfeed) {
          uVar3 = 0xfffffff3;
          goto LAB_ffff00009076d0ac;
        }
        if (-1 < (int)param_2) {
          uVar3 = (param_1[5] & 0xff00ff00) >> 8 | (param_1[5] & 0xff00ff) << 8;
          if (0x10 < (uVar3 >> 0x10 | uVar3 << 0x10)) {
            uVar3 = (param_1[8] & 0xff00ff00) >> 8 | (param_1[8] & 0xff00ff) << 8;
            uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
            uVar5 = uVar3 - param_2;
            if (uVar3 < param_2 || uVar5 == 0) goto LAB_ffff00009076d0a8;
            if (uVar2 <= uVar5) {
              uVar5 = uVar2;
            }
          }
LAB_ffff00009076d0e0:
          lVar6 = (long)param_1 + (ulong)uVar1;
          lVar4 = FUN_ffff00009077e7f0(lVar6,0,uVar5);
          if (lVar4 != 0) {
            if (param_3 == (uint *)0x0) {
              return lVar6;
            }
            uVar3 = (int)lVar4 - (int)lVar6;
            goto LAB_ffff00009076d0b4;
          }
          uVar3 = 0xfffffff8;
          goto LAB_ffff00009076d0ac;
        }
      }
    }
LAB_ffff00009076d0a8:
    uVar3 = 0xfffffffc;
  }
LAB_ffff00009076d0ac:
  lVar6 = 0;
  if (param_3 == (uint *)0x0) {
    return 0;
  }
LAB_ffff00009076d0b4:
  *param_3 = uVar3;
  return lVar6;
}


