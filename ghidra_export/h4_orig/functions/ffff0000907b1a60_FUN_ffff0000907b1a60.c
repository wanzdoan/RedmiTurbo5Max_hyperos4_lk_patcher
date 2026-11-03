// ffff0000907b1a60  size=388  FUN_ffff0000907b1a60

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b1a60(void)

{
  byte bVar1;
  ulong uVar2;
  bool bVar3;
  int iVar4;
  uint uVar5;
  ulong uVar6;
  long lVar7;
  uint uVar8;
  byte *pbVar9;
  ulong uVar10;
  byte *pbVar11;
  
  _DAT_ffff00009090cf48 = _DAT_ffff00009090cc68;
  _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
  if (_DAT_ffff00009090cc78 != 0) {
    do {
      iVar4 = FUN_ffff0000907af524(0x15);
      if (iVar4 != 0) {
        _DAT_ffff00009090cf48 = (byte *)0x0;
        _DAT_ffff00009090cf50 = 0;
        return 0;
      }
      if (_DAT_ffff00009090cf50 == 0) {
        FUN_ffff00009077e11c(s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
        return 0x700e;
      }
      bVar1 = _DAT_ffff00009090cf48[1];
      uVar5 = (uint)bVar1;
      if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
        iVar4 = (uVar5 & 0x7f) + 1;
        uVar8 = uVar5 & 0x7f;
        uVar5 = 0;
        pbVar9 = _DAT_ffff00009090cf48 + 2;
        do {
          uVar8 = uVar8 - 1;
          uVar5 = (uint)*pbVar9 | uVar5 << 8;
          pbVar9 = pbVar9 + 1;
        } while (uVar8 != 0);
      }
      else {
        iVar4 = 1;
      }
      if ((*_DAT_ffff00009090cf48 & 0x20) != 0) {
        uVar5 = 0;
      }
      uVar6 = (ulong)(iVar4 + uVar5 + 1);
      uVar2 = _DAT_ffff00009090cf50 - uVar6;
      if (_DAT_ffff00009090cf50 < uVar6) {
        _DAT_ffff00009090cf50 = 0;
        return 0x101028;
      }
      pbVar9 = _DAT_ffff00009090cf48 + uVar6;
      if (*_DAT_ffff00009090cf48 != 3) {
        _DAT_ffff00009090cf48 = pbVar9;
        _DAT_ffff00009090cf50 = uVar2;
        return 0x700f;
      }
      bVar1 = _DAT_ffff00009090cf48[1];
      if (((char)bVar1 < '\0') && (uVar10 = (ulong)bVar1 & 0x7f, (int)uVar10 != 0)) {
        _DAT_ffff00009090cc60 = 0;
        lVar7 = uVar10 + 1;
        pbVar11 = _DAT_ffff00009090cf48 + 2;
        do {
          uVar5 = (int)uVar10 - 1;
          uVar10 = (ulong)uVar5;
          _DAT_ffff00009090cc60 = (uint)*pbVar11 | _DAT_ffff00009090cc60 << 8;
          pbVar11 = pbVar11 + 1;
        } while (uVar5 != 0);
      }
      else {
        _DAT_ffff00009090cc60 = (uint)bVar1;
        lVar7 = 1;
      }
      _DAT_ffff00009090cc60 = _DAT_ffff00009090cc60 - 1;
      if (_DAT_ffff00009090cc60 < 0x11) {
        pbVar11 = _DAT_ffff00009090cf48 + lVar7 + 2;
        _DAT_ffff00009090cf48 = pbVar9;
        _DAT_ffff00009090cf50 = uVar2;
        FUN_ffff0000907ad234(0xffff00009090cc50,pbVar11);
        _DAT_ffff00009090cf48 = (byte *)0x0;
        _DAT_ffff00009090cf50 = 0;
        return 0;
      }
      bVar3 = _DAT_ffff00009090cf50 != uVar6;
      _DAT_ffff00009090cf48 = pbVar9;
      _DAT_ffff00009090cf50 = uVar2;
    } while (bVar3);
  }
  _DAT_ffff00009090cf50 = 0;
  _DAT_ffff00009090cf48 = (byte *)0x0;
  return 0;
}


