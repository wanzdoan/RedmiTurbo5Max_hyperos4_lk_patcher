
undefined8 FUN_ffff0000be08af08(void)

{
  byte bVar1;
  ulong uVar2;
  bool bVar3;
  int iVar4;
  byte *pbVar5;
  uint uVar6;
  ulong uVar7;
  byte *pbVar8;
  uint uVar9;
  long lVar10;
  byte *pbVar11;
  ulong uVar12;
  
  DAT_ffff0000be1b7408 = DAT_ffff0000be1b7160;
  DAT_ffff0000be1b7410 = DAT_ffff0000be1b7170;
  if (DAT_ffff0000be1b7170 != 0) {
    do {
      iVar4 = FUN_ffff0000be089718(0x10);
      if (iVar4 != 0) {
        DAT_ffff0000be1b7408 = (byte *)0x0;
        DAT_ffff0000be1b7410 = 0;
        return 0x7014;
      }
      if (DAT_ffff0000be1b7410 == 0) {
        FUN_ffff0000be05fd98(s___s__end_of_cert__not_found__ffff0000be0c1571,&DAT_ffff0000be0bb811);
        return 0x700e;
      }
      pbVar5 = DAT_ffff0000be1b7408 + 1;
      bVar1 = *pbVar5;
      uVar6 = (uint)bVar1;
      if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
        iVar4 = (uVar6 & 0x7f) + 1;
        uVar9 = uVar6 & 0x7f;
        uVar6 = 0;
        pbVar11 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar9 = uVar9 - 1;
          uVar6 = (uint)*pbVar11 | uVar6 << 8;
          pbVar11 = pbVar11 + 1;
        } while (uVar9 != 0);
      }
      else {
        iVar4 = 1;
      }
      if ((*DAT_ffff0000be1b7408 & 0x20) != 0) {
        uVar6 = 0;
      }
      uVar7 = (ulong)(iVar4 + uVar6 + 1);
      uVar2 = DAT_ffff0000be1b7410 - uVar7;
      if (DAT_ffff0000be1b7410 < uVar7) {
        DAT_ffff0000be1b7410 = 0;
        return 0x101028;
      }
      pbVar11 = DAT_ffff0000be1b7408 + uVar7;
      if (*DAT_ffff0000be1b7408 != 2) {
        DAT_ffff0000be1b7408 = pbVar11;
        DAT_ffff0000be1b7410 = uVar2;
        return 0x700f;
      }
      bVar1 = *pbVar5;
      if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
        DAT_ffff0000be1b6f90 = 0;
        lVar10 = uVar12 + 1;
        pbVar8 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar6 = (int)uVar12 - 1;
          uVar12 = (ulong)uVar6;
          DAT_ffff0000be1b6f90 = (uint)*pbVar8 | DAT_ffff0000be1b6f90 << 8;
          pbVar8 = pbVar8 + 1;
        } while (uVar6 != 0);
      }
      else {
        DAT_ffff0000be1b6f90 = (uint)bVar1;
        lVar10 = 1;
      }
      pbVar5 = pbVar5 + lVar10;
      DAT_ffff0000be1b7408 = pbVar11;
      if ((*pbVar5 == 0) && ((char)pbVar5[1] < '\0')) {
        DAT_ffff0000be1b6f90 = DAT_ffff0000be1b6f90 - 1;
        if (DAT_ffff0000be1b6f90 < 5) {
          pbVar5 = pbVar5 + 1;
LAB_ffff0000be08b054:
          DAT_ffff0000be1b7410 = uVar2;
          FUN_ffff0000be087428(0xffff0000be1b6f8c,pbVar5,DAT_ffff0000be1b6f90);
          DAT_ffff0000be1b7408 = (byte *)0x0;
          DAT_ffff0000be1b7410 = 0;
          return 0;
        }
      }
      else if (DAT_ffff0000be1b6f90 < 5) goto LAB_ffff0000be08b054;
      bVar3 = DAT_ffff0000be1b7410 != uVar7;
      DAT_ffff0000be1b7410 = uVar2;
    } while (bVar3);
  }
  DAT_ffff0000be1b7410 = 0;
  DAT_ffff0000be1b7408 = (byte *)0x0;
  return 0x7014;
}


