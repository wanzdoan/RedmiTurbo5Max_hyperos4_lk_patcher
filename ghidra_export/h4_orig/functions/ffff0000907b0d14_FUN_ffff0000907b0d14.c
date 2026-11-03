// ffff0000907b0d14  size=432  FUN_ffff0000907b0d14

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b0d14(void)

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
  
  _DAT_ffff00009090cf48 = _DAT_ffff00009090cc68;
  _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
  if (_DAT_ffff00009090cc78 != 0) {
    do {
      iVar4 = FUN_ffff0000907af524(0x10);
      if (iVar4 != 0) {
        _DAT_ffff00009090cf48 = (byte *)0x0;
        _DAT_ffff00009090cf50 = 0;
        return 0x7014;
      }
      if (_DAT_ffff00009090cf50 == 0) {
        FUN_ffff00009077e11c(s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
        return 0x700e;
      }
      pbVar5 = _DAT_ffff00009090cf48 + 1;
      bVar1 = *pbVar5;
      uVar6 = (uint)bVar1;
      if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
        iVar4 = (uVar6 & 0x7f) + 1;
        uVar9 = uVar6 & 0x7f;
        uVar6 = 0;
        pbVar11 = _DAT_ffff00009090cf48 + 2;
        do {
          uVar9 = uVar9 - 1;
          uVar6 = (uint)*pbVar11 | uVar6 << 8;
          pbVar11 = pbVar11 + 1;
        } while (uVar9 != 0);
      }
      else {
        iVar4 = 1;
      }
      if ((*_DAT_ffff00009090cf48 & 0x20) != 0) {
        uVar6 = 0;
      }
      uVar7 = (ulong)(iVar4 + uVar6 + 1);
      uVar2 = _DAT_ffff00009090cf50 - uVar7;
      if (_DAT_ffff00009090cf50 < uVar7) {
        _DAT_ffff00009090cf50 = 0;
        return 0x101028;
      }
      pbVar11 = _DAT_ffff00009090cf48 + uVar7;
      if (*_DAT_ffff00009090cf48 != 2) {
        _DAT_ffff00009090cf48 = pbVar11;
        _DAT_ffff00009090cf50 = uVar2;
        return 0x700f;
      }
      bVar1 = *pbVar5;
      if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
        _DAT_ffff00009090ca98 = 0;
        lVar10 = uVar12 + 1;
        pbVar8 = _DAT_ffff00009090cf48 + 2;
        do {
          uVar6 = (int)uVar12 - 1;
          uVar12 = (ulong)uVar6;
          _DAT_ffff00009090ca98 = (uint)*pbVar8 | _DAT_ffff00009090ca98 << 8;
          pbVar8 = pbVar8 + 1;
        } while (uVar6 != 0);
      }
      else {
        _DAT_ffff00009090ca98 = (uint)bVar1;
        lVar10 = 1;
      }
      pbVar5 = pbVar5 + lVar10;
      _DAT_ffff00009090cf48 = pbVar11;
      if ((*pbVar5 == 0) && ((char)pbVar5[1] < '\0')) {
        _DAT_ffff00009090ca98 = _DAT_ffff00009090ca98 - 1;
        if (_DAT_ffff00009090ca98 < 5) {
          pbVar5 = pbVar5 + 1;
LAB_ffff0000907b0e60:
          _DAT_ffff00009090cf50 = uVar2;
          FUN_ffff0000907ad234(&DAT_ffff00009090ca94,pbVar5,_DAT_ffff00009090ca98);
          _DAT_ffff00009090cf48 = (byte *)0x0;
          _DAT_ffff00009090cf50 = 0;
          return 0;
        }
      }
      else if (_DAT_ffff00009090ca98 < 5) goto LAB_ffff0000907b0e60;
      bVar3 = _DAT_ffff00009090cf50 != uVar7;
      _DAT_ffff00009090cf50 = uVar2;
    } while (bVar3);
  }
  _DAT_ffff00009090cf50 = 0;
  _DAT_ffff00009090cf48 = (byte *)0x0;
  return 0x7014;
}


