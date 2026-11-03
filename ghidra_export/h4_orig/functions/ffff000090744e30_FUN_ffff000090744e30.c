// ffff000090744e30  size=1232  FUN_ffff000090744e30

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090744e30(uint param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  uint uVar5;
  ulong uVar6;
  
  if ((int)_DAT_ffff000090877588 <= (int)_DAT_ffff000090877578) {
    _DAT_ffff000090877578 = 0;
    _DAT_ffff000090877598 = _DAT_ffff000090877598 + 1;
    if (_DAT_ffff000090877580 <= _DAT_ffff000090877598) {
      if (_DAT_ffff000090877590 == 0) {
        lVar4 = 0;
        lVar3 = _DAT_ffff0000908775b8;
      }
      else {
        uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff000090877590 + 0x10) *
                              *(int *)(_DAT_ffff000090877590 + 0x14) * 0x10);
        lVar4 = (_DAT_ffff000090877580 + -1) * uVar6;
        lVar3 = _DAT_ffff0000908775b8 + uVar6;
      }
      FUN_ffff00009077eb20(_DAT_ffff0000908775b8,lVar3,lVar4);
      if (_DAT_ffff000090877590 == 0) {
        uVar6 = 0;
      }
      else {
        uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff000090877590 + 0x10) *
                              *(int *)(_DAT_ffff000090877590 + 0x14) * 0x10);
      }
      FUN_ffff00009077ee50
                ((_DAT_ffff0000908775b8 - uVar6) +
                 CONCAT44(DAT_ffff000090877580_4,_DAT_ffff000090877580) * uVar6,0);
      _DAT_ffff000090877598 = _DAT_ffff000090877598 + -1;
    }
  }
  switch(param_1 & 0xff) {
  case 8:
    if ((int)_DAT_ffff000090877578 < 1) {
      if (_DAT_ffff000090877598 < 2) {
        _DAT_ffff000090877598 = 1;
      }
      _DAT_ffff000090877598 = _DAT_ffff000090877598 + -1;
      _DAT_ffff000090877578 = _DAT_ffff000090877588;
    }
    _DAT_ffff000090877578 = _DAT_ffff000090877578 - 1;
    FUN_ffff000090745300(_DAT_ffff000090877578 * 8,_DAT_ffff000090877598 << 4,0x20);
    return;
  case 9:
    _DAT_ffff000090877578 = _DAT_ffff000090877578 & 0xfffffff0 | 8;
    if ((int)_DAT_ffff000090877588 <= (int)_DAT_ffff000090877578) {
      _DAT_ffff000090877578 = 0;
      _DAT_ffff000090877598 = _DAT_ffff000090877598 + 1;
      if (_DAT_ffff000090877580 <= _DAT_ffff000090877598) {
        if (_DAT_ffff000090877590 == 0) {
          lVar4 = 0;
          lVar3 = _DAT_ffff0000908775b8;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff000090877590 + 0x10) *
                                *(int *)(_DAT_ffff000090877590 + 0x14) * 0x10);
          lVar4 = (CONCAT44(DAT_ffff000090877580_4,_DAT_ffff000090877580) + -1) * uVar6;
          lVar3 = _DAT_ffff0000908775b8 + uVar6;
        }
        FUN_ffff00009077eb20(_DAT_ffff0000908775b8,lVar3,lVar4);
        if (_DAT_ffff000090877590 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff000090877590 + 0x10) *
                                *(int *)(_DAT_ffff000090877590 + 0x14) * 0x10);
        }
        FUN_ffff00009077ee50
                  ((_DAT_ffff0000908775b8 - uVar6) +
                   CONCAT44(DAT_ffff000090877580_4,_DAT_ffff000090877580) * uVar6,0);
        _DAT_ffff000090877598 = _DAT_ffff000090877598 + -1;
      }
    }
    break;
  case 10:
    if ((_DAT_ffff000090877578 != 0) || ((DAT_ffff000090877574 & 1) == 0)) {
      _DAT_ffff000090877578 = 0;
      _DAT_ffff000090877598 = _DAT_ffff000090877598 + 1;
      if (_DAT_ffff000090877580 <= _DAT_ffff000090877598) {
        if (_DAT_ffff000090877590 == 0) {
          lVar4 = 0;
          lVar3 = _DAT_ffff0000908775b8;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff000090877590 + 0x10) *
                                *(int *)(_DAT_ffff000090877590 + 0x14) * 0x10);
          lVar4 = (CONCAT44(DAT_ffff000090877580_4,_DAT_ffff000090877580) + -1) * uVar6;
          lVar3 = _DAT_ffff0000908775b8 + uVar6;
        }
        FUN_ffff00009077eb20(_DAT_ffff0000908775b8,lVar3,lVar4);
        if (_DAT_ffff000090877590 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff000090877590 + 0x10) *
                                *(int *)(_DAT_ffff000090877590 + 0x14) * 0x10);
        }
        FUN_ffff00009077ee50
                  ((_DAT_ffff0000908775b8 - uVar6) +
                   CONCAT44(DAT_ffff000090877580_4,_DAT_ffff000090877580) * uVar6,0);
        _DAT_ffff000090877598 = _DAT_ffff000090877598 + -1;
      }
    }
    DAT_ffff000090877574 = 0;
    break;
  default:
    iVar1 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907d9514,2);
    if (iVar1 == 0) {
      iVar2 = _DAT_ffff000090877578 << 3;
      iVar1 = (_DAT_ffff000090877580 - _DAT_ffff000090877598) * 0x10;
    }
    else {
      iVar1 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907f6757,3);
      if (iVar1 == 0) {
        uVar5 = _DAT_ffff000090877588 + ~_DAT_ffff000090877578;
        iVar2 = _DAT_ffff000090877580 - _DAT_ffff000090877598;
      }
      else {
        iVar1 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907f2fb1,3);
        uVar5 = _DAT_ffff000090877578;
        iVar2 = _DAT_ffff000090877598;
        if (iVar1 == 0) {
          iVar1 = (_DAT_ffff000090877598 + (_DAT_ffff000090877588 - _DAT_ffff000090877580)) * 0x10;
          iVar2 = (_DAT_ffff000090877580 - _DAT_ffff000090877578) * 8 + -0x50;
          goto LAB_ffff0000907451c8;
        }
      }
      iVar1 = uVar5 << 3;
      iVar2 = iVar2 << 4;
    }
LAB_ffff0000907451c8:
    FUN_ffff000090745300(iVar1,iVar2,param_1);
    _DAT_ffff000090877578 = _DAT_ffff000090877578 + 1;
    if ((int)_DAT_ffff000090877588 <= (int)_DAT_ffff000090877578) {
      _DAT_ffff000090877578 = 0;
      _DAT_ffff000090877598 = _DAT_ffff000090877598 + 1;
      if (_DAT_ffff000090877580 <= _DAT_ffff000090877598) {
        if (_DAT_ffff000090877590 == 0) {
          lVar4 = 0;
          lVar3 = _DAT_ffff0000908775b8;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff000090877590 + 0x10) *
                                *(int *)(_DAT_ffff000090877590 + 0x14) * 0x10);
          lVar4 = (CONCAT44(DAT_ffff000090877580_4,_DAT_ffff000090877580) + -1) * uVar6;
          lVar3 = _DAT_ffff0000908775b8 + uVar6;
        }
        FUN_ffff00009077eb20(_DAT_ffff0000908775b8,lVar3,lVar4);
        if (_DAT_ffff000090877590 == 0) {
          uVar6 = 0;
        }
        else {
          uVar6 = (ulong)(uint)(*(int *)(_DAT_ffff000090877590 + 0x10) *
                                *(int *)(_DAT_ffff000090877590 + 0x14) * 0x10);
        }
        FUN_ffff00009077ee50
                  ((_DAT_ffff0000908775b8 - uVar6) +
                   CONCAT44(DAT_ffff000090877580_4,_DAT_ffff000090877580) * uVar6,0);
        _DAT_ffff000090877598 = _DAT_ffff000090877598 + -1;
      }
      DAT_ffff000090877574 = 1;
    }
    break;
  case 0xd:
    _DAT_ffff000090877578 = 0;
  }
  return;
}


