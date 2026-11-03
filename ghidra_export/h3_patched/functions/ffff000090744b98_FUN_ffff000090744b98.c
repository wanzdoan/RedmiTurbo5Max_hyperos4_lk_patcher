// ffff000090744b98  size=1232  FUN_ffff000090744b98

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090744b98(uint param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  uint uVar5;
  ulong uVar6;
  
  if ((int)_DAT_ffff0000908774f0 <= (int)_DAT_ffff0000908774e0) {
    _DAT_ffff0000908774e0 = 0;
    _DAT_ffff000090877500 = _DAT_ffff000090877500 + 1;
    if (_DAT_ffff0000908774e8 <= _DAT_ffff000090877500) {
      if (_DAT_ffff0000908774f8 == 0) {
        lVar4 = 0;
        lVar3 = _DAT_ffff000090877520;
      }
      else {
        uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff0000908774f8 + 0x10) *
                              *(int *)(_DAT_ffff0000908774f8 + 0x14) * 0x10);
        lVar4 = (_DAT_ffff0000908774e8 + -1) * uVar6;
        lVar3 = _DAT_ffff000090877520 + uVar6;
      }
      FUN_ffff00009077e858(_DAT_ffff000090877520,lVar3,lVar4);
      if (_DAT_ffff0000908774f8 == 0) {
        uVar6 = 0;
      }
      else {
        uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff0000908774f8 + 0x10) *
                              *(int *)(_DAT_ffff0000908774f8 + 0x14) * 0x10);
      }
      FUN_ffff00009077eb88
                ((_DAT_ffff000090877520 - uVar6) +
                 CONCAT44(DAT_ffff0000908774e8_4,_DAT_ffff0000908774e8) * uVar6,0);
      _DAT_ffff000090877500 = _DAT_ffff000090877500 + -1;
    }
  }
  switch(param_1 & 0xff) {
  case 8:
    if ((int)_DAT_ffff0000908774e0 < 1) {
      if (_DAT_ffff000090877500 < 2) {
        _DAT_ffff000090877500 = 1;
      }
      _DAT_ffff000090877500 = _DAT_ffff000090877500 + -1;
      _DAT_ffff0000908774e0 = _DAT_ffff0000908774f0;
    }
    _DAT_ffff0000908774e0 = _DAT_ffff0000908774e0 - 1;
    FUN_ffff000090745068(_DAT_ffff0000908774e0 * 8,_DAT_ffff000090877500 << 4,0x20);
    return;
  case 9:
    _DAT_ffff0000908774e0 = _DAT_ffff0000908774e0 & 0xfffffff0 | 8;
    if ((int)_DAT_ffff0000908774f0 <= (int)_DAT_ffff0000908774e0) {
      _DAT_ffff0000908774e0 = 0;
      _DAT_ffff000090877500 = _DAT_ffff000090877500 + 1;
      if (_DAT_ffff0000908774e8 <= _DAT_ffff000090877500) {
        if (_DAT_ffff0000908774f8 == 0) {
          lVar4 = 0;
          lVar3 = _DAT_ffff000090877520;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff0000908774f8 + 0x10) *
                                *(int *)(_DAT_ffff0000908774f8 + 0x14) * 0x10);
          lVar4 = (CONCAT44(DAT_ffff0000908774e8_4,_DAT_ffff0000908774e8) + -1) * uVar6;
          lVar3 = _DAT_ffff000090877520 + uVar6;
        }
        FUN_ffff00009077e858(_DAT_ffff000090877520,lVar3,lVar4);
        if (_DAT_ffff0000908774f8 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff0000908774f8 + 0x10) *
                                *(int *)(_DAT_ffff0000908774f8 + 0x14) * 0x10);
        }
        FUN_ffff00009077eb88
                  ((_DAT_ffff000090877520 - uVar6) +
                   CONCAT44(DAT_ffff0000908774e8_4,_DAT_ffff0000908774e8) * uVar6,0);
        _DAT_ffff000090877500 = _DAT_ffff000090877500 + -1;
      }
    }
    break;
  case 10:
    if ((_DAT_ffff0000908774e0 != 0) || ((DAT_ffff0000908774dc & 1) == 0)) {
      _DAT_ffff0000908774e0 = 0;
      _DAT_ffff000090877500 = _DAT_ffff000090877500 + 1;
      if (_DAT_ffff0000908774e8 <= _DAT_ffff000090877500) {
        if (_DAT_ffff0000908774f8 == 0) {
          lVar4 = 0;
          lVar3 = _DAT_ffff000090877520;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff0000908774f8 + 0x10) *
                                *(int *)(_DAT_ffff0000908774f8 + 0x14) * 0x10);
          lVar4 = (CONCAT44(DAT_ffff0000908774e8_4,_DAT_ffff0000908774e8) + -1) * uVar6;
          lVar3 = _DAT_ffff000090877520 + uVar6;
        }
        FUN_ffff00009077e858(_DAT_ffff000090877520,lVar3,lVar4);
        if (_DAT_ffff0000908774f8 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff0000908774f8 + 0x10) *
                                *(int *)(_DAT_ffff0000908774f8 + 0x14) * 0x10);
        }
        FUN_ffff00009077eb88
                  ((_DAT_ffff000090877520 - uVar6) +
                   CONCAT44(DAT_ffff0000908774e8_4,_DAT_ffff0000908774e8) * uVar6,0);
        _DAT_ffff000090877500 = _DAT_ffff000090877500 + -1;
      }
    }
    DAT_ffff0000908774dc = 0;
    break;
  default:
    iVar1 = FUN_ffff00009077f080(&DAT_ffff0000907df27e,&DAT_ffff0000907d947d,2);
    if (iVar1 == 0) {
      iVar2 = _DAT_ffff0000908774e0 << 3;
      iVar1 = (_DAT_ffff0000908774e8 - _DAT_ffff000090877500) * 0x10;
    }
    else {
      iVar1 = FUN_ffff00009077f080(&DAT_ffff0000907df27e,&DAT_ffff0000907f65b6,3);
      if (iVar1 == 0) {
        uVar5 = _DAT_ffff0000908774f0 + ~_DAT_ffff0000908774e0;
        iVar2 = _DAT_ffff0000908774e8 - _DAT_ffff000090877500;
      }
      else {
        iVar1 = FUN_ffff00009077f080(&DAT_ffff0000907df27e,&DAT_ffff0000907f2e0e,3);
        uVar5 = _DAT_ffff0000908774e0;
        iVar2 = _DAT_ffff000090877500;
        if (iVar1 == 0) {
          iVar1 = (_DAT_ffff000090877500 + (_DAT_ffff0000908774f0 - _DAT_ffff0000908774e8)) * 0x10;
          iVar2 = (_DAT_ffff0000908774e8 - _DAT_ffff0000908774e0) * 8 + -0x50;
          goto LAB_ffff000090744f30;
        }
      }
      iVar1 = uVar5 << 3;
      iVar2 = iVar2 << 4;
    }
LAB_ffff000090744f30:
    FUN_ffff000090745068(iVar1,iVar2,param_1);
    _DAT_ffff0000908774e0 = _DAT_ffff0000908774e0 + 1;
    if ((int)_DAT_ffff0000908774f0 <= (int)_DAT_ffff0000908774e0) {
      _DAT_ffff0000908774e0 = 0;
      _DAT_ffff000090877500 = _DAT_ffff000090877500 + 1;
      if (_DAT_ffff0000908774e8 <= _DAT_ffff000090877500) {
        if (_DAT_ffff0000908774f8 == 0) {
          lVar4 = 0;
          lVar3 = _DAT_ffff000090877520;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff0000908774f8 + 0x10) *
                                *(int *)(_DAT_ffff0000908774f8 + 0x14) * 0x10);
          lVar4 = (CONCAT44(DAT_ffff0000908774e8_4,_DAT_ffff0000908774e8) + -1) * uVar6;
          lVar3 = _DAT_ffff000090877520 + uVar6;
        }
        FUN_ffff00009077e858(_DAT_ffff000090877520,lVar3,lVar4);
        if (_DAT_ffff0000908774f8 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff0000908774f8 + 0x10) *
                                *(int *)(_DAT_ffff0000908774f8 + 0x14) * 0x10);
        }
        FUN_ffff00009077eb88
                  ((_DAT_ffff000090877520 - uVar6) +
                   CONCAT44(DAT_ffff0000908774e8_4,_DAT_ffff0000908774e8) * uVar6,0);
        _DAT_ffff000090877500 = _DAT_ffff000090877500 + -1;
      }
      DAT_ffff0000908774dc = 1;
    }
    break;
  case 0xd:
    _DAT_ffff0000908774e0 = 0;
  }
  return;
}


