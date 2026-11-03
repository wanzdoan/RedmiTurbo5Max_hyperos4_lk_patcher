
int FUN_ffff0000be08b5b0(void)

{
  undefined *puVar1;
  byte *pbVar2;
  ulong uVar3;
  bool bVar4;
  int iVar5;
  byte bVar6;
  ulong uVar7;
  long lVar8;
  uint uVar9;
  uint uVar10;
  byte *pbVar11;
  ulong uVar12;
  int iVar13;
  
  puVar1 = PTR_DAT_ffff0000be0f5728;
  DAT_ffff0000be1b7408 = DAT_ffff0000be1b7160;
  DAT_ffff0000be1b7410 = DAT_ffff0000be1b7170;
  if (DAT_ffff0000be1b7170 == 0) {
    iVar13 = 0;
  }
  else {
    iVar13 = 0x101028;
    do {
      iVar5 = FUN_ffff0000be089718(0x11);
      uVar3 = DAT_ffff0000be1b7410;
      pbVar2 = DAT_ffff0000be1b7408;
      if (iVar5 != 0) {
        bVar6 = 1;
        goto LAB_ffff0000be08b730;
      }
      if (DAT_ffff0000be1b7410 == 0) {
        FUN_ffff0000be05fd98(s___s__end_of_cert__not_found__ffff0000be0c1571,&DAT_ffff0000be0bb811);
        return 0x700e;
      }
      bVar6 = DAT_ffff0000be1b7408[1];
      uVar10 = (uint)bVar6;
      if (((char)bVar6 < '\0') && ((bVar6 & 0x7f) != 0)) {
        iVar5 = (uVar10 & 0x7f) + 1;
        uVar9 = uVar10 & 0x7f;
        uVar10 = 0;
        pbVar11 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar9 = uVar9 - 1;
          uVar10 = (uint)*pbVar11 | uVar10 << 8;
          pbVar11 = pbVar11 + 1;
        } while (uVar9 != 0);
      }
      else {
        iVar5 = 1;
      }
      if ((*DAT_ffff0000be1b7408 & 0x20) != 0) {
        uVar10 = 0;
      }
      uVar7 = (ulong)(iVar5 + uVar10 + 1);
      bVar4 = DAT_ffff0000be1b7410 < uVar7;
      DAT_ffff0000be1b7410 = DAT_ffff0000be1b7410 - uVar7;
      if (bVar4) {
        DAT_ffff0000be1b7410 = 0;
        return 0x101028;
      }
      if (*DAT_ffff0000be1b7408 != 3) {
        DAT_ffff0000be1b7408 = DAT_ffff0000be1b7408 + uVar7;
        return 0x700f;
      }
      bVar6 = DAT_ffff0000be1b7408[1];
      if (((char)bVar6 < '\0') && (uVar12 = (ulong)bVar6 & 0x7f, (int)uVar12 != 0)) {
        uVar10 = 0;
        lVar8 = uVar12 + 1;
        pbVar11 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar9 = (int)uVar12 - 1;
          uVar12 = (ulong)uVar9;
          uVar10 = (uint)*pbVar11 | uVar10 << 8;
          pbVar11 = pbVar11 + 1;
        } while (uVar9 != 0);
      }
      else {
        uVar10 = (uint)bVar6;
        lVar8 = 1;
      }
      DAT_ffff0000be1b7408 = DAT_ffff0000be1b7408 + uVar7;
      *(uint *)(puVar1 + 0xec) = uVar10 - 1;
      if (uVar10 - 1 < 0x21) {
        FUN_ffff0000be087428(puVar1 + 0xcc,pbVar2 + lVar8 + 2);
        bVar6 = 0;
        iVar5 = 0;
        goto LAB_ffff0000be08b730;
      }
    } while (uVar3 != uVar7);
  }
  bVar6 = 1;
  iVar5 = iVar13;
LAB_ffff0000be08b730:
  iVar13 = 0x7014;
  if (!(bool)(bVar6 & DAT_ffff0000be1b6ad0 != 0)) {
    iVar13 = iVar5;
  }
  DAT_ffff0000be1b7410 = 0;
  DAT_ffff0000be1b7408 = (byte *)0x0;
  return iVar13;
}


