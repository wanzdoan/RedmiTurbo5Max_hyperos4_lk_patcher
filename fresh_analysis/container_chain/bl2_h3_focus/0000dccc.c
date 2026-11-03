// File offset entry: 0xdccc

ulong FUN_ffff0000b800dccc(void)

{
  ulong *puVar1;
  ulong uVar2;
  ulong *puVar3;
  ulong *puVar4;
  ulong *puVar5;
  ulong *puVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  
  FUN_ffff0000b800ad90(&LAB_ffff0000b80acce8,0xffffffff);
  puVar3 = DAT_ffff0000b8114508 + 1;
  puVar4 = puVar3 + (ulong)(DAT_ffff0000b8114548 - 1U) * 3;
  if (puVar4 < puVar3) {
    uVar11 = 0xffffffffffffffff;
  }
  else {
    puVar6 = (ulong *)((long)DAT_ffff0000b8114508 + (ulong)(DAT_ffff0000b8114548 - 1U) * 0x18 + 9);
    puVar5 = DAT_ffff0000b8114508 + 4;
    if (puVar6 <= puVar5) {
      puVar6 = puVar5;
    }
    puVar6 = puVar6 + -4;
    puVar1 = DAT_ffff0000b8114508;
    if (puVar6 != DAT_ffff0000b8114508) {
      puVar1 = (ulong *)((long)DAT_ffff0000b8114508 + 1);
    }
    uVar7 = (ulong)((long)puVar6 - (long)puVar1) / 0x18;
    if (puVar6 != DAT_ffff0000b8114508) {
      uVar7 = uVar7 + 1;
    }
    if (uVar7 == 0) {
      uVar12 = 0xffffffffffffffff;
    }
    else {
      uVar8 = uVar7 + 1 & 0x3ffffffffffffffe;
      uVar9 = 0xffffffffffffffff;
      uVar12 = uVar8;
      uVar10 = 0xffffffffffffffff;
      do {
        uVar11 = puVar5[-3];
        if (uVar9 <= puVar5[-3]) {
          uVar11 = uVar9;
        }
        uVar2 = *puVar5;
        if (uVar10 <= *puVar5) {
          uVar2 = uVar10;
        }
        uVar12 = uVar12 - 2;
        puVar5 = puVar5 + 6;
        uVar9 = uVar11;
        uVar10 = uVar2;
      } while (uVar12 != 0);
      if (uVar2 <= uVar11) {
        uVar11 = uVar2;
      }
      puVar3 = puVar3 + uVar8 * 3;
      uVar12 = uVar11;
      if (uVar7 + 1 == uVar8) goto LAB_ffff0000b800ddc8;
    }
    do {
      puVar6 = puVar3 + 3;
      uVar11 = *puVar3;
      if (uVar12 <= *puVar3) {
        uVar11 = uVar12;
      }
      puVar3 = puVar6;
      uVar12 = uVar11;
    } while (puVar6 <= puVar4);
  }
LAB_ffff0000b800ddc8:
  if (DAT_ffff0000b8115158 != 0) {
    puVar3 = DAT_ffff0000b8114508 + 0x183;
    uVar7 = uVar11;
    do {
      puVar4 = puVar3 + 0x13;
      uVar11 = *puVar3;
      if (uVar7 <= *puVar3 - 1) {
        uVar11 = uVar7;
      }
      puVar3 = puVar4;
      uVar7 = uVar11;
    } while (puVar4 <= DAT_ffff0000b8114508 + 0xaf0);
  }
  FUN_ffff0000b800ae28(&LAB_ffff0000b80acce8);
  return uVar11;
}


