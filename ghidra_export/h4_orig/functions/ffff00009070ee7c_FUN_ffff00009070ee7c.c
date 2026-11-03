// ffff00009070ee7c  size=1048  FUN_ffff00009070ee7c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070ee7c(int *param_1,undefined4 *param_2,uint param_3)

{
  undefined4 *puVar1;
  undefined4 uVar2;
  long lVar3;
  ulong uVar4;
  int iVar5;
  long lVar6;
  ulong uVar7;
  
  *(undefined4 **)(param_1 + 4) = param_2;
  param_1[2] = param_3;
LAB_ffff00009070ef20:
  do {
    if (param_3 == 0) break;
    switch(param_1[1]) {
    case 0:
      if (param_3 < 0x1c) goto code_r0xffff00009070ef14;
      FUN_ffff00009077ee50(0xffff00009083dfe0,0,0x50);
      FUN_ffff00009077eb20(0xffff00009083dfe0,param_2,0x1c);
      _DAT_ffff00009083e020 = 0;
      _DAT_ffff00009083e008 = 0;
      param_3 = param_3 - _DAT_ffff00009083dfe8;
      param_2 = (undefined4 *)((long)param_2 + (ulong)_DAT_ffff00009083dfe8);
      goto LAB_ffff00009070f104;
    case 1:
      if (_DAT_ffff00009083dff4 <= _DAT_ffff00009083e008) {
        param_1[2] = 0;
        param_1[0] = 0;
        param_1[1] = 1;
        return;
      }
      if (_DAT_ffff00009083e020 + param_3 < (uint)_DAT_ffff00009083dfea) {
        _DAT_ffff00009083e020 = param_3;
        FUN_ffff00009077eb20(0xffff00009083e024,param_2,param_3);
        param_1[2] = 0;
        *(undefined4 **)(param_1 + 4) = param_2;
        param_1[0] = 0;
        param_1[1] = 1;
        return;
      }
      if (_DAT_ffff00009083e020 == 0) {
        FUN_ffff00009077eb20(&DAT_ffff00009083dffc,param_2,0xc);
        uVar7 = (ulong)_DAT_ffff00009083dfea;
      }
      else {
        uVar7 = (ulong)(_DAT_ffff00009083dfea - _DAT_ffff00009083e020);
        FUN_ffff00009077eb20(&DAT_ffff00009083dffc,0xffff00009083e024);
        uVar4 = uVar7;
        if (0xc < (ulong)_DAT_ffff00009083dfea) {
          uVar4 = (uVar7 - _DAT_ffff00009083dfea) + 0xc;
        }
        FUN_ffff00009077eb20(&DAT_ffff00009083dffc + _DAT_ffff00009083e020,param_2,uVar4);
        _DAT_ffff00009083e020 = 0;
      }
      _DAT_ffff00009083e010 = (ulong)_DAT_ffff00009083dfec * (ulong)_DAT_ffff00009083e000;
      if (3 < _DAT_ffff00009083dffc - 0xcac1) {
        iVar5 = -0x6a;
        goto LAB_ffff00009070f21c;
      }
      param_3 = param_3 - (int)uVar7;
      iVar5 = 2;
      switch((uint)_DAT_ffff00009083dffc) {
      case 0xcac2:
        iVar5 = 4;
        break;
      case 0xcac3:
        _DAT_ffff00009083e018 = _DAT_ffff00009083e018 + _DAT_ffff00009083e010;
        iVar5 = 1;
        _DAT_ffff00009083e010 = 0;
        break;
      case 0xcac4:
        iVar5 = 5;
      }
      param_2 = (undefined4 *)((long)param_2 + uVar7);
      *param_1 = 0;
      param_1[1] = iVar5;
      param_1[2] = param_3;
      *(undefined4 **)(param_1 + 4) = param_2;
      _DAT_ffff00009083e008 = _DAT_ffff00009083e008 + 1;
      goto code_r0xffff00009070ef14;
    case 2:
      uVar7 = (ulong)param_3;
      if (uVar7 < _DAT_ffff00009083e010) {
        if ((param_3 & 0xfff) != 0) {
          if (param_3 < 0x1000) {
            param_1[0] = 100;
            param_1[1] = 2;
            return;
          }
          uVar7 = (ulong)(param_3 & 0xfffff000);
        }
        uVar4 = FUN_ffff00009077c144
                          (**(undefined8 **)(param_1 + 10),param_2,_DAT_ffff00009083e018,uVar7);
        if (uVar4 != uVar7) {
LAB_ffff00009070f218:
          iVar5 = -0x67;
LAB_ffff00009070f21c:
          *param_1 = iVar5;
          return;
        }
        if (*param_1 != 0) {
          return;
        }
        param_2 = (undefined4 *)((long)param_2 + uVar7);
        _DAT_ffff00009083e010 = _DAT_ffff00009083e010 - uVar7;
        _DAT_ffff00009083e018 = _DAT_ffff00009083e018 + uVar7;
        iVar5 = 2;
        param_3 = param_3 & 0xfff;
      }
      else {
        uVar7 = FUN_ffff00009077c144(**(undefined8 **)(param_1 + 10),param_2,_DAT_ffff00009083e018);
        if (uVar7 != _DAT_ffff00009083e010) goto LAB_ffff00009070f218;
        if (*param_1 != 0) {
          return;
        }
        param_2 = (undefined4 *)((long)param_2 + uVar7);
        param_3 = param_3 - (int)uVar7;
        _DAT_ffff00009083e018 = _DAT_ffff00009083e018 + uVar7;
        iVar5 = 1;
        _DAT_ffff00009083e010 = 0;
      }
      break;
    default:
      goto code_r0xffff00009070ef14;
    case 4:
      uVar2 = *param_2;
      lVar3 = FUN_ffff00009070f2c8();
      lVar6 = 0;
      do {
        puVar1 = (undefined4 *)(lVar3 + lVar6);
        lVar6 = lVar6 + 8;
        *puVar1 = uVar2;
        puVar1[1] = uVar2;
      } while (lVar6 != 0x200000);
      param_2 = param_2 + 1;
      param_3 = param_3 - 4;
      uVar7 = _DAT_ffff00009083e010;
      if (0x1fffff < _DAT_ffff00009083e010) {
        uVar7 = 0x200000;
      }
      while (uVar7 != 0) {
        uVar4 = FUN_ffff00009077c144
                          (**(undefined8 **)(param_1 + 10),lVar3,_DAT_ffff00009083e018,uVar7);
        if (uVar4 != uVar7) goto LAB_ffff00009070f218;
        if (*param_1 != 0) {
          return;
        }
        _DAT_ffff00009083e010 = _DAT_ffff00009083e010 - uVar7;
        _DAT_ffff00009083e018 = _DAT_ffff00009083e018 + uVar7;
        uVar7 = _DAT_ffff00009083e010;
        if (0x1fffff < _DAT_ffff00009083e010) {
          uVar7 = 0x200000;
        }
      }
LAB_ffff00009070f104:
      iVar5 = 1;
      break;
    case 5:
      param_2 = param_2 + 1;
      iVar5 = 1;
      _DAT_ffff00009083e010 = 0;
      param_3 = param_3 - 4;
    }
    *param_1 = 0;
    param_1[1] = iVar5;
    param_1[2] = param_3;
    *(undefined4 **)(param_1 + 4) = param_2;
    if (param_3 == 0) {
      return;
    }
  } while( true );
code_r0xffff00009070ef14:
  if (*param_1 != 0) {
    return;
  }
  if (param_3 == 0) {
    return;
  }
  goto LAB_ffff00009070ef20;
}


