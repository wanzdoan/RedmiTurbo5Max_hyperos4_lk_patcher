
undefined8 FUN_ffff0000907b18dc(void)

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
  
  DAT_ffff00009090cf48 = DAT_ffff00009090cc68;
  DAT_ffff00009090cf50 = DAT_ffff00009090cc78;
  if (DAT_ffff00009090cc78 != 0) {
    do {
      iVar4 = FUN_ffff0000907af524(0x14);
      if (iVar4 != 0) {
        DAT_ffff00009090cf48 = (byte *)0x0;
        DAT_ffff00009090cf50 = 0;
        return 0;
      }
      if (DAT_ffff00009090cf50 == 0) {
        FUN_ffff00009077e11c(s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
        return 0x700e;
      }
      bVar1 = DAT_ffff00009090cf48[1];
      uVar8 = (uint)bVar1;
      if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
        iVar4 = (uVar8 & 0x7f) + 1;
        uVar7 = uVar8 & 0x7f;
        uVar8 = 0;
        pbVar9 = DAT_ffff00009090cf48 + 2;
        do {
          uVar7 = uVar7 - 1;
          uVar8 = (uint)*pbVar9 | uVar8 << 8;
          pbVar9 = pbVar9 + 1;
        } while (uVar7 != 0);
      }
      else {
        iVar4 = 1;
      }
      if ((*DAT_ffff00009090cf48 & 0x20) != 0) {
        uVar8 = 0;
      }
      uVar5 = (ulong)(iVar4 + uVar8 + 1);
      uVar2 = DAT_ffff00009090cf50 - uVar5;
      if (DAT_ffff00009090cf50 < uVar5) {
        DAT_ffff00009090cf50 = 0;
        return 0x101028;
      }
      pbVar9 = DAT_ffff00009090cf48 + uVar5;
      if (*DAT_ffff00009090cf48 != 3) {
        DAT_ffff00009090cf48 = pbVar9;
        DAT_ffff00009090cf50 = uVar2;
        return 0x700f;
      }
      bVar1 = DAT_ffff00009090cf48[1];
      if (((char)bVar1 < '\0') && (uVar10 = (ulong)bVar1 & 0x7f, (int)uVar10 != 0)) {
        uVar8 = 0;
        lVar6 = uVar10 + 1;
        pbVar11 = DAT_ffff00009090cf48 + 2;
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
      DAT_ffff00009090cc4c = uVar8 - 1;
      if (DAT_ffff00009090cc4c < 0x11) {
        pbVar11 = DAT_ffff00009090cf48 + lVar6 + 2;
        DAT_ffff00009090cf48 = pbVar9;
        DAT_ffff00009090cf50 = uVar2;
        FUN_ffff0000907ad234(&DAT_ffff00009090cc3c,pbVar11);
        DAT_ffff00009090cf48 = (byte *)0x0;
        DAT_ffff00009090cf50 = 0;
        return 0;
      }
      bVar3 = DAT_ffff00009090cf50 != uVar5;
      DAT_ffff00009090cf48 = pbVar9;
      DAT_ffff00009090cf50 = uVar2;
    } while (bVar3);
  }
  DAT_ffff00009090cf50 = 0;
  DAT_ffff00009090cf48 = (byte *)0x0;
  return 0;
}


