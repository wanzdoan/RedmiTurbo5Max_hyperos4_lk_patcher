// ffff0000907afa00  size=412  FUN_ffff0000907afa00

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907afa00(undefined8 param_1,uint param_2,uint *param_3)

{
  byte bVar1;
  byte *pbVar2;
  bool bVar3;
  int iVar4;
  long lVar5;
  uint uVar6;
  ulong uVar7;
  uint uVar8;
  byte *pbVar9;
  byte *pbVar10;
  
  pbVar10 = _DAT_ffff00009090cc68;
  _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
  while( true ) {
    _DAT_ffff00009090cf48 = pbVar10;
    if ((_DAT_ffff00009090cf50 == 0) ||
       (iVar4 = FUN_ffff0000907af524(4), pbVar2 = _DAT_ffff00009090cf48, iVar4 != 0)) {
      _DAT_ffff00009090cf48 = (byte *)0x0;
      _DAT_ffff00009090cf50 = 0;
      return 0x7014;
    }
    if (_DAT_ffff00009090cf50 == 0) {
      FUN_ffff00009077e11c(s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
      return 0x700e;
    }
    bVar1 = _DAT_ffff00009090cf48[1];
    uVar6 = (uint)bVar1;
    if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
      iVar4 = (uVar6 & 0x7f) + 1;
      uVar8 = uVar6 & 0x7f;
      uVar6 = 0;
      pbVar10 = _DAT_ffff00009090cf48 + 2;
      do {
        uVar8 = uVar8 - 1;
        uVar6 = (uint)*pbVar10 | uVar6 << 8;
        pbVar10 = pbVar10 + 1;
      } while (uVar8 != 0);
    }
    else {
      iVar4 = 1;
    }
    if ((*_DAT_ffff00009090cf48 & 0x20) != 0) {
      uVar6 = 0;
    }
    uVar7 = (ulong)(iVar4 + uVar6 + 1);
    bVar3 = _DAT_ffff00009090cf50 < uVar7;
    _DAT_ffff00009090cf50 = _DAT_ffff00009090cf50 - uVar7;
    if (bVar3) break;
    pbVar10 = _DAT_ffff00009090cf48 + uVar7;
    if (*_DAT_ffff00009090cf48 != 3) {
      _DAT_ffff00009090cf48 = pbVar10;
      return 0x700f;
    }
    if (param_2 != 0) {
      bVar1 = _DAT_ffff00009090cf48[1];
      if (((char)bVar1 < '\0') && (uVar7 = (ulong)bVar1 & 0x7f, (int)uVar7 != 0)) {
        uVar6 = 0;
        lVar5 = uVar7 + 1;
        pbVar9 = _DAT_ffff00009090cf48 + 2;
        do {
          uVar8 = (int)uVar7 - 1;
          uVar7 = (ulong)uVar8;
          uVar6 = (uint)*pbVar9 | uVar6 << 8;
          pbVar9 = pbVar9 + 1;
        } while (uVar8 != 0);
      }
      else {
        uVar6 = (uint)bVar1;
        lVar5 = 1;
      }
      _DAT_ffff00009090cf48 = pbVar10;
      *param_3 = uVar6 - 1;
      pbVar10 = _DAT_ffff00009090cf48;
      if (uVar6 - 1 <= param_2) {
        FUN_ffff0000907ad234(param_1,pbVar2 + lVar5 + 2);
        _DAT_ffff00009090cf50 = 0;
        _DAT_ffff00009090cf48 = (byte *)0x0;
        return 0;
      }
    }
  }
  _DAT_ffff00009090cf50 = 0;
  return 0x101028;
}


