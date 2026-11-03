// ffff0000907af738  size=412  FUN_ffff0000907af738

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907af738(undefined8 param_1,uint param_2,uint *param_3)

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
  
  pbVar10 = _DAT_ffff00009090cba8;
  _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
  while( true ) {
    _DAT_ffff00009090ce88 = pbVar10;
    if ((_DAT_ffff00009090ce90 == 0) ||
       (iVar4 = FUN_ffff0000907af25c(4), pbVar2 = _DAT_ffff00009090ce88, iVar4 != 0)) {
      _DAT_ffff00009090ce88 = (byte *)0x0;
      _DAT_ffff00009090ce90 = 0;
      return 0x7014;
    }
    if (_DAT_ffff00009090ce90 == 0) {
      FUN_ffff00009077de54(s___s__end_of_cert__not_found__ffff0000907fab80,&DAT_ffff0000907f20fe);
      return 0x700e;
    }
    bVar1 = _DAT_ffff00009090ce88[1];
    uVar6 = (uint)bVar1;
    if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
      iVar4 = (uVar6 & 0x7f) + 1;
      uVar8 = uVar6 & 0x7f;
      uVar6 = 0;
      pbVar10 = _DAT_ffff00009090ce88 + 2;
      do {
        uVar8 = uVar8 - 1;
        uVar6 = (uint)*pbVar10 | uVar6 << 8;
        pbVar10 = pbVar10 + 1;
      } while (uVar8 != 0);
    }
    else {
      iVar4 = 1;
    }
    if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
      uVar6 = 0;
    }
    uVar7 = (ulong)(iVar4 + uVar6 + 1);
    bVar3 = _DAT_ffff00009090ce90 < uVar7;
    _DAT_ffff00009090ce90 = _DAT_ffff00009090ce90 - uVar7;
    if (bVar3) break;
    pbVar10 = _DAT_ffff00009090ce88 + uVar7;
    if (*_DAT_ffff00009090ce88 != 3) {
      _DAT_ffff00009090ce88 = pbVar10;
      return 0x700f;
    }
    if (param_2 != 0) {
      bVar1 = _DAT_ffff00009090ce88[1];
      if (((char)bVar1 < '\0') && (uVar7 = (ulong)bVar1 & 0x7f, (int)uVar7 != 0)) {
        uVar6 = 0;
        lVar5 = uVar7 + 1;
        pbVar9 = _DAT_ffff00009090ce88 + 2;
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
      _DAT_ffff00009090ce88 = pbVar10;
      *param_3 = uVar6 - 1;
      pbVar10 = _DAT_ffff00009090ce88;
      if (uVar6 - 1 <= param_2) {
        FUN_ffff0000907acf74(param_1,pbVar2 + lVar5 + 2);
        _DAT_ffff00009090ce90 = 0;
        _DAT_ffff00009090ce88 = (byte *)0x0;
        return 0;
      }
    }
  }
  _DAT_ffff00009090ce90 = 0;
  return 0x101028;
}


