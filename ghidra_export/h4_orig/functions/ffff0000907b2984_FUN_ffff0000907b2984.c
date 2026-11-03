// ffff0000907b2984  size=212  FUN_ffff0000907b2984

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907b2984(long param_1,int param_2)

{
  byte bVar1;
  undefined4 uVar2;
  long lVar3;
  byte *pbVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  
  if (param_1 == 0) {
    uVar2 = 0x100006;
  }
  else {
    uVar2 = 0x101014;
    if ((param_2 == 0x20) && (_DAT_ffff00009090cba8 == 0x40)) {
      lVar3 = 0;
      pbVar4 = &DAT_ffff00009090cb69;
      do {
        bVar1 = pbVar4[-1];
        uVar5 = bVar1 - 0x30;
        if (9 < uVar5) {
          uVar6 = (uint)bVar1;
          uVar5 = uVar6 - 0x57;
          if (5 < bVar1 - 0x61) {
            uVar5 = uVar6;
          }
          if (uVar6 - 0x41 < 6) {
            uVar5 = uVar6 - 0x37;
          }
        }
        bVar1 = *pbVar4;
        uVar7 = (uint)bVar1;
        uVar6 = uVar7 - 0x30;
        if (9 < uVar7 - 0x30) {
          if (uVar7 - 0x41 < 6) {
            uVar6 = uVar7 - 0x37;
          }
          else {
            uVar6 = bVar1 - 0x57;
            if (5 < bVar1 - 0x61) {
              uVar6 = uVar7;
            }
          }
        }
        pbVar4 = pbVar4 + 2;
        *(char *)(param_1 + lVar3) = (char)uVar6 + (char)uVar5 * '\x10';
        lVar3 = lVar3 + 1;
      } while (lVar3 != 0x20);
      uVar2 = 0;
    }
  }
  return uVar2;
}


