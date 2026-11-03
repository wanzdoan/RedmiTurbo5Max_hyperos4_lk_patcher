// ffff0000907b13bc  size=448  FUN_ffff0000907b13bc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907b13bc(void)

{
  long lVar1;
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
  
  lVar1 = DAT_ffff000090839ab0;
  _DAT_ffff00009090cf48 = _DAT_ffff00009090cc68;
  _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
  if (_DAT_ffff00009090cc78 == 0) {
    iVar13 = 0;
  }
  else {
    iVar13 = 0x101028;
    do {
      iVar5 = FUN_ffff0000907af524(0x11);
      uVar3 = _DAT_ffff00009090cf50;
      pbVar2 = _DAT_ffff00009090cf48;
      if (iVar5 != 0) {
        bVar6 = 1;
        goto LAB_ffff0000907b153c;
      }
      if (_DAT_ffff00009090cf50 == 0) {
        FUN_ffff00009077e11c(s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
        return 0x700e;
      }
      bVar6 = _DAT_ffff00009090cf48[1];
      uVar10 = (uint)bVar6;
      if (((char)bVar6 < '\0') && ((bVar6 & 0x7f) != 0)) {
        iVar5 = (uVar10 & 0x7f) + 1;
        uVar9 = uVar10 & 0x7f;
        uVar10 = 0;
        pbVar11 = _DAT_ffff00009090cf48 + 2;
        do {
          uVar9 = uVar9 - 1;
          uVar10 = (uint)*pbVar11 | uVar10 << 8;
          pbVar11 = pbVar11 + 1;
        } while (uVar9 != 0);
      }
      else {
        iVar5 = 1;
      }
      if ((*_DAT_ffff00009090cf48 & 0x20) != 0) {
        uVar10 = 0;
      }
      uVar7 = (ulong)(iVar5 + uVar10 + 1);
      bVar4 = _DAT_ffff00009090cf50 < uVar7;
      _DAT_ffff00009090cf50 = _DAT_ffff00009090cf50 - uVar7;
      if (bVar4) {
        _DAT_ffff00009090cf50 = 0;
        return 0x101028;
      }
      if (*_DAT_ffff00009090cf48 != 3) {
        _DAT_ffff00009090cf48 = _DAT_ffff00009090cf48 + uVar7;
        return 0x700f;
      }
      bVar6 = _DAT_ffff00009090cf48[1];
      if (((char)bVar6 < '\0') && (uVar12 = (ulong)bVar6 & 0x7f, (int)uVar12 != 0)) {
        uVar10 = 0;
        lVar8 = uVar12 + 1;
        pbVar11 = _DAT_ffff00009090cf48 + 2;
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
      _DAT_ffff00009090cf48 = _DAT_ffff00009090cf48 + uVar7;
      *(uint *)(lVar1 + 0xec) = uVar10 - 1;
      if (uVar10 - 1 < 0x21) {
        FUN_ffff0000907ad234(lVar1 + 0xcc,pbVar2 + lVar8 + 2);
        bVar6 = 0;
        iVar5 = 0;
        goto LAB_ffff0000907b153c;
      }
    } while (uVar3 != uVar7);
  }
  bVar6 = 1;
  iVar5 = iVar13;
LAB_ffff0000907b153c:
  iVar13 = 0x7014;
  if (!(bool)(bVar6 & _DAT_ffff00009090c5d8 != 0)) {
    iVar13 = iVar5;
  }
  _DAT_ffff00009090cf50 = 0;
  _DAT_ffff00009090cf48 = (byte *)0x0;
  return iVar13;
}


