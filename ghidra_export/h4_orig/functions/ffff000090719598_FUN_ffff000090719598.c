// ffff000090719598  size=376  FUN_ffff000090719598

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090719598(undefined8 param_1,undefined8 param_2,ulong param_3)

{
  int iVar1;
  uint uVar2;
  ulong uVar3;
  long lVar4;
  uint uVar5;
  
  if ((param_3 != 0) && ((DAT_ffff000090843c70 & 1) == 0)) {
    if ((_DAT_ffff000090843c78 == 0) && (_DAT_ffff000090843c80 != -1)) {
      if (_DAT_ffff000090843c80 != 0x5000) {
        lVar4 = (long)_DAT_ffff000090843c80;
        uVar3 = 0x5000 - lVar4;
        if (lVar4 + param_3 < 0x5001) {
          uVar3 = param_3;
        }
        FUN_ffff00009077eb20(lVar4 + -0xffff6f7bc37c,param_2,uVar3);
        _DAT_ffff000090843c80 = _DAT_ffff000090843c80 + (int)uVar3;
      }
    }
    else if ((((*(uint *)(_DAT_ffff000090843c68 + 0x10) >> 9 & 1) != 0) &&
             ((*(uint *)(_DAT_ffff000090843c68 + 0x10) >> 4 & 1) == 0)) &&
            (_DAT_ffff000090843c78 != 0)) {
      if (_DAT_ffff000090843c80 < 1) {
        uVar5 = *(uint *)(_DAT_ffff000090843c68 + 0x58);
        lVar4 = _DAT_ffff000090843c68;
      }
      else {
        iVar1 = *(uint *)(_DAT_ffff000090843c68 + 0x58) + *(uint *)(_DAT_ffff000090843c68 + 0x54);
        if (*(uint *)(_DAT_ffff000090843c68 + 0x1c) < (uint)(iVar1 + _DAT_ffff000090843c80)) {
          _DAT_ffff000090843c80 = *(uint *)(_DAT_ffff000090843c68 + 0x1c) - iVar1;
        }
        FUN_ffff00009077eb20
                  (_DAT_ffff000090843c78 + (ulong)*(uint *)(_DAT_ffff000090843c68 + 0x54) +
                   (ulong)*(uint *)(_DAT_ffff000090843c68 + 0x58),0xffff000090843c84,
                   (long)_DAT_ffff000090843c80);
        lVar4 = _DAT_ffff000090843c68;
        uVar5 = *(int *)(_DAT_ffff000090843c68 + 0x58) + _DAT_ffff000090843c80;
        *(uint *)(_DAT_ffff000090843c68 + 0x58) = uVar5;
        _DAT_ffff000090843c80 = -1;
      }
      uVar2 = uVar5 + *(uint *)(lVar4 + 0x54);
      if ((ulong)*(uint *)(lVar4 + 0x1c) < uVar2 + param_3) {
        param_3 = (ulong)(*(uint *)(lVar4 + 0x1c) - uVar2);
        *(uint *)(lVar4 + 0x10) = *(uint *)(lVar4 + 0x10) | 0x10;
      }
      FUN_ffff00009077eb20
                (_DAT_ffff000090843c78 + (ulong)*(uint *)(lVar4 + 0x54) + (ulong)uVar5,param_2,
                 param_3);
      *(int *)(_DAT_ffff000090843c68 + 0x58) = *(int *)(_DAT_ffff000090843c68 + 0x58) + (int)param_3
      ;
    }
  }
  return;
}


