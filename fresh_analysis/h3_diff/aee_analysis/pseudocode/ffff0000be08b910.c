
undefined8 FUN_ffff0000be08b910(void)

{
  byte bVar1;
  ulong uVar2;
  bool bVar3;
  int iVar4;
  ulong uVar5;
  long lVar6;
  uint uVar7;
  uint uVar8;
  byte *pbVar9;
  ulong uVar10;
  byte *pbVar11;
  
  DAT_ffff0000be1b7408 = DAT_ffff0000be1b7160;
  DAT_ffff0000be1b7410 = DAT_ffff0000be1b7170;
  if (DAT_ffff0000be1b7170 != 0) {
    do {
      iVar4 = FUN_ffff0000be089718(0x13);
      if (iVar4 != 0) break;
      if (DAT_ffff0000be1b7410 == 0) {
        FUN_ffff0000be05fd98(s___s__end_of_cert__not_found__ffff0000be0c1571,&DAT_ffff0000be0bb811);
        return 0x700e;
      }
      bVar1 = DAT_ffff0000be1b7408[1];
      uVar8 = (uint)bVar1;
      if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
        iVar4 = (uVar8 & 0x7f) + 1;
        uVar7 = uVar8 & 0x7f;
        uVar8 = 0;
        pbVar9 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar7 = uVar7 - 1;
          uVar8 = (uint)*pbVar9 | uVar8 << 8;
          pbVar9 = pbVar9 + 1;
        } while (uVar7 != 0);
      }
      else {
        iVar4 = 1;
      }
      if ((*DAT_ffff0000be1b7408 & 0x20) != 0) {
        uVar8 = 0;
      }
      uVar5 = (ulong)(iVar4 + uVar8 + 1);
      uVar2 = DAT_ffff0000be1b7410 - uVar5;
      if (DAT_ffff0000be1b7410 < uVar5) {
        DAT_ffff0000be1b7410 = 0;
        return 0x101028;
      }
      pbVar9 = DAT_ffff0000be1b7408 + uVar5;
      if (*DAT_ffff0000be1b7408 != 3) {
        DAT_ffff0000be1b7408 = pbVar9;
        DAT_ffff0000be1b7410 = uVar2;
        return 0x700f;
      }
      bVar1 = DAT_ffff0000be1b7408[1];
      if (((char)bVar1 < '\0') && (uVar10 = (ulong)bVar1 & 0x7f, (int)uVar10 != 0)) {
        uVar8 = 0;
        lVar6 = uVar10 + 1;
        pbVar11 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar7 = (int)uVar10 - 1;
          uVar10 = (ulong)uVar7;
          uVar8 = (uint)*pbVar11 | uVar8 << 8;
          pbVar11 = pbVar11 + 1;
        } while (uVar7 != 0);
      }
      else {
        uVar8 = (uint)bVar1;
        lVar6 = 1;
      }
      DAT_ffff0000be1b7130 = uVar8 - 1;
      if (DAT_ffff0000be1b7130 < 0x21) {
        pbVar11 = DAT_ffff0000be1b7408 + lVar6 + 2;
        DAT_ffff0000be1b7408 = pbVar9;
        DAT_ffff0000be1b7410 = uVar2;
        FUN_ffff0000be087428(&DAT_ffff0000be1b7110,pbVar11);
        DAT_ffff0000be1b7408 = (byte *)0x0;
        DAT_ffff0000be1b7410 = 0;
        return 0;
      }
      bVar3 = DAT_ffff0000be1b7410 != uVar5;
      DAT_ffff0000be1b7408 = pbVar9;
      DAT_ffff0000be1b7410 = uVar2;
    } while (bVar3);
  }
  if (DAT_ffff0000be1b6ad0 != 0) {
    FUN_ffff0000be060ae0(PTR_DAT_ffff0000be0f5728 + 0x130,0,0x20);
    FUN_ffff0000be05fd98(s___s__FE_without_cust_wrapped__ffff0000be0b1778,&DAT_ffff0000be0bb811);
  }
  DAT_ffff0000be1b7410 = 0;
  DAT_ffff0000be1b7408 = (byte *)0x0;
  return 0;
}


