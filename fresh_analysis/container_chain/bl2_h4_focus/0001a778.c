// File offset entry: 0x1a778

uint FUN_ffff0000b801a778(ulong param_1,uint param_2,uint param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  
  uVar4 = param_3 - param_2;
  if (param_3 < param_2 || uVar4 == 0) {
    uVar6 = 0;
  }
  else {
    if (uVar4 < 2) {
      uVar6 = 0;
      uVar5 = param_2;
    }
    else {
      uVar2 = uVar4 & 0xfffffffe;
      uVar7 = 0;
      uVar6 = 0;
      uVar5 = uVar2 + param_2;
      uVar8 = param_2;
      uVar3 = uVar2;
      do {
        uVar1 = uVar8 + 1;
        uVar7 = 1 << (ulong)(uVar8 & 0x1f) | uVar7;
        uVar3 = uVar3 - 2;
        uVar8 = uVar8 + 2;
        uVar6 = 1 << (ulong)(uVar1 & 0x1f) | uVar6;
      } while (uVar3 != 0);
      uVar6 = uVar6 | uVar7;
      if (uVar4 == uVar2) goto LAB_ffff0000b801a7fc;
    }
    do {
      uVar4 = uVar5 + 1;
      uVar6 = 1 << (ulong)(uVar5 & 0x1f) | uVar6;
      uVar5 = uVar4;
    } while (param_3 != uVar4);
  }
LAB_ffff0000b801a7fc:
  uVar4 = (uint)(param_1 >> 0xd);
  uVar8 = (uint)(param_1 >> 2);
  uVar5 = (uint)param_1;
  return (uVar6 & (uVar4 & 6 | uVar5 >> 0x1a & 1 | (uint)(param_1 >> 0x19) & 0x18 |
                   uVar5 >> 0x13 & 0x60 | (uVar8 & 3) << 7 | uVar4 & 0x600 | (uVar5 >> 8 & 3) << 0xb
                   | uVar5 >> 0xe & 0x2000 | (uVar5 & 0x3000) << 2 | uVar8 & 0x30000 |
                   uVar5 << 0xc & 0xc0000 | (uVar5 & 3) << 0x14 | uVar5 << 0xc & 0xc00000 |
                   (uVar5 & 0x300000) << 4 | (uVar5 & 0x10000) << 10 | uVar5 >> 3 & 0x18000000 |
                   (uVar5 & 0x30) << 0x19 | (uVar5 & 0x20000) << 0xe)) >> (ulong)(param_2 & 0x1f);
}


